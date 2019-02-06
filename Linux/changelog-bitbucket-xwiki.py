#!/usr/bin/env python2

import re
import json
import sys 
import urllib2
import git
import subprocess
import re
from datetime import datetime
from collections import OrderedDict


URL_PR_ALL="https://__YOUR__BITBUCKET__/rest/api/1.0/projects/__PROJECT_ID__/repos/__REPO__/pull-requests?state=MERGED&withProperties=false&start={}&limit={}&withAttributes=false&at=refs/heads/{}"

URL_PR_SIN="https://__YOUR__BITBUCKET__/rest/api/1.0/projects/__PROJECT_ID__/repos/__REPO__/pull-requests/{}"

URL_PR_DET="https://__YOUR__BITBUCKET__/projects/__PROJECT_ID__/repos/__REPO__/pull-requests/{}/overview"

"-------------------------------------------"
def addHeader(request, field, value):
    request.add_header(field, value)
    return request
"-------------------------------------------"
def json_loads_byteified(json_text):
    return _byteify(
        json.loads(json_text, object_hook=_byteify),
        ignore_dicts=True
    )
"-------------------------------------------"
def _byteify(data, ignore_dicts = False):
    if isinstance(data, unicode):
        return data.encode('utf-8')
    if isinstance(data, list):
        return [ _byteify(item, ignore_dicts=True) for item in data ]
    if isinstance(data, dict) and not ignore_dicts:
        return {
            _byteify(key, ignore_dicts=True): _byteify(value, ignore_dicts=True)
            for key, value in data.iteritems()
        }
    return data
"-------------------------------------------"
def getResponse(url):
    request = urllib2.Request(url)
    addHeader(request, "Authorization", "Bearer dG9wc2VjcmV0X2Zha2VfcGFzc3dvcmQ=")
    addHeader(request, "Cache-Control", "no-cache")
    addHeader(request, "Content-Type", "application/json")
    res = urllib2.urlopen(request)
    return res.read();
"-------------------------------------------" 

def get_prs(branchBefore, branchCurrent):
    cmd1 = ['git', '--git-dir=/Users/marcel/devel/repo-src/.git', 'show', '--format="%ct"', '--merges', branchBefore]

    lines1 = subprocess.check_output(cmd1, stderr=subprocess.STDOUT).split('\n')
    date1 = lines1[0]

    cmd2 =['git', '--git-dir=/Users/marcel/devel/repo-src/.git', 'log', '-g', branchCurrent, '--format="%ct"']
    lines2 = subprocess.check_output(cmd2, stderr=subprocess.STDOUT).split('\n')

    date2 = lines2[0]

    cmd = ['git', '--git-dir=/Users/marcel/devel/repo-src/.git', 'log', '--merges','--since={}'.format(date1), '--until={}'.format(date2), branchCurrent,
            '--format="%ci -#- %s -#- %h -#- %an"']
    process = subprocess.Popen(cmd, shell=False, stdout=subprocess.PIPE)
    output, errrors = process.communicate()
    prs = []
    current_pr = {}
    for line in output.split("\n"):
        if line and not line.isspace():
            # print line
            regex = re.match(".*Merge pull request #(\d+).*to {}".format(branchCurrent), line)
            if regex:
                current_pr['id'] = regex.group(1)
                r = json.loads(getResponse(URL_PR_SIN.format(current_pr['id'])))
                current_pr['title'] = r['title'].encode('utf-8')
                current_pr['commit'] = r['fromRef']['latestCommit']
                current_pr['author'] = r['author']['user']['displayName'].encode('utf-8')
                prs.append(dict(current_pr))
    return prs
"-------------------------------------------" 


limit         = 99
start         = 0
size          = 0
flag          = True
branchBefore  = 'origin/release/18.4'
branchCurrent = 'release/18.5'

while flag:
    r = json.loads(getResponse(URL_PR_ALL.format(start,limit, branchCurrent)))
    # print r['isLastPage']
    flag = not r['isLastPage']
    size += r['size']
    if flag:
        start += limit
values = r['values']
xwiki = "* {} [[view details>>{}]] - **[{}]**"

# strnew = "date: {} - title: {} - commit: {}"
# get prev release latest commit date: `preDate'  git show --format="%ct" origin/release/18.2 --merges
#get  develop merged PR after `preDate' git log origin/develop --format="%ci %s -- %h" --merges|grep -E " to release/18.3"
# first branch commit git --git-dir=$HOME/devel/repo-src/.git log -g release/18.3 --pretty=format:"%ct"|tail -n1
# git --git-dir=$HOME/devel/repo-src/.git log origin/develop  --format="%ci %s -- %h" --merges --since=1529590985 --until=1530559154|grep "pull request"

for current in values:
    print xwiki.format(current['title'], URL_PR_DET.format(current['id']), current['author']['user']['displayName'])

prs = get_prs(branchBefore, branchCurrent)
prs.sort(key = lambda c: c['id'])

for pr in prs:
    print xwiki.format(pr['title'], URL_PR_DET.format(pr['id']), pr['author'])
