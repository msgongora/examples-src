from newrelic_api import Labels, Servers
from datetime import datetime, timedelta
import re
import json
import sys
#import math

APIKEY='3231231dasdasda'
Servers = Servers(api_key=APIKEY)
Labels  = Labels(api_key=APIKEY)

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
def getServerName(id):
    """
    list(filter_name=None, filter_ids=None, filter_labels=None, page=None)
    """
    return Servers.show(id)['server']['name']
"-------------------------------------------"
def getNetOutBytessec(id, tfrom, tto):
    met = Servers.metric_data(id=id, 
                 names=['System/Network/All/Transmitted/bytes/sec'],
                 values=['per_second'],
                 from_dt=tfrom,  #'2017-02-09T02:00:00+00:00'
                 to_dt=tto,      #'2017-02-09T04:00:00+00:00'
                 summarize=True)
    return int(round(met['metric_data']['metrics'][0]['timeslices'][0]['values']['per_second']*8/float(1024*1024),0))
"-------------------------------------------"
'''def sortCluster(list):
    sorted(se, key=lambda server: re.findall('\d+', server.name))'''

def svcCmp(serverA, serverB):
    #nNameA = re.findall('\d+', serverA.name)
    nNameA = int(re.compile(r'(wms|origin|edge)(\d+)').search(serverA.name).group(2))
    nNameB = int(re.compile(r'(wms|origin|edge)(\d+)').search(serverB.name).group(2))
    if nNameA < nNameB:
        return -1
    elif nNameA == nNameB:
        if re.compile('origin').search(serverA.name):
            return -1
        else:
            return 1
    else:
        return 1    
"-------------------------------------------"
class Server(object):
    name = ""
    id = 0
    neto = 0
    type = ""
    clust = -1
    dc = ""

    def __init__(self, id, name, neto):
        self.name = name
        self.id = id
        self.neto = neto
        self.__whichType()
        self.__whichDC()
        self.__whichCluster()
    
    def __whichCluster(self):
        tmpclust = self.getDictCluster()
        if self.dc == "ISPrime":
            res = int(re.findall('\d+', self.name)[0])
            if self.type == "Edge":
                res = next(k for k, v in tmpclust.items() if res in v)
        else:
            res = 4
        self.clust = res
         
    @staticmethod    
    def getDictCluster():
        edgLen    =4
        clustLen  = 8
        clustCnt  = 1
        clust = {}
        for c in range(1, clustLen):
            ce = 1
            orig=c
            if c > 3:
                orig=c+1 
            while ce <= edgLen:
                edge = c*ce+(c-1)*(edgLen-ce)
                ce+=1
                if orig in clust:
                    clust[orig].append(edge)
                else:
                    clust[orig] = [edge]

        return clust
    
    def __whichType(self):
        self.type = "Origin" if re.compile('server-origin').search(self.name) else "Edge"

    def __whichDC(self):
        isprime = re.compile('server-(cams|rls)')
        if isprime.search(self.name):
            self.dc = "ISPrime"
        else:
            self.dc = "C2"
            self.name = self.name.replace('.domain.org','')


"-------------------------------------------"
#https://github.com/ambitioninc/newrelic-api
lab = json.dumps(Labels.list())
lab = json_loads_byteified(lab)

for label in lab['labels']:
    if label['name'] == "Fms":
        serversId = label['server_health_status']['green']
#print serversId
ocnt=1
ecnt=1
fmsClust={}
fmsList = []
if len(sys.argv) < 3:
    print "Usage: newrelic.py 2017-02-09T20:00 2017-02-09T21:00"
    sys.exit(1)

tfrom='2017-02-09T02:00:00+00:00'
tto='2017-02-09T04:00:00+00:00'

fromtmp = datetime.strptime(sys.argv[1], "%Y-%m-%dT%H:%M")
totmp = datetime.strptime(sys.argv[2], "%Y-%m-%dT%H:%M")
print fromtmp
print totmp
fromtmp += timedelta(hours=4)
totmp += timedelta(hours=4)
# print mytime.strftime("%Y-%m-%dT%H:%M")
tfrom = fromtmp.strftime("%Y-%m-%dT%H:%M") + ':00+00:00'
tto = totmp.strftime("%Y-%m-%dT%H:%M") + ':00+00:00'
# s = '2017-02-09T21:00'
print tfrom + " " + tto



for id in serversId:
    name = getServerName(id)
    neto = getNetOutBytessec(id, tfrom, tto)
    currSer = Server(id, name, neto)
    currClu = currSer.clust
    fmsList.append(currSer)
    
    if currClu in fmsClust.keys():
        fmsClust[currClu].append(currSer)
    else:
        fmsClust[currClu] = [currSer]

for cl, se in fmsClust.items():
    #print ss
    print "Cluster {} \n  |\n   `-".format(cl),
    clustCnt = 1 
    se.sort(svcCmp)
    for s in se:
        if clustCnt == 1:
            print "Orig {0:14}".format(s.name),
        else:
            print "{0:>5} Edge {1:14}".format('',s.name),
        
        print "Id-> {0:<8} Network_Out-> {1} Mbits/sec".format(s.id, s.neto)
        clustCnt+=1
           

"""
met = json.dumps(Servers.metric_data(id='40604254', 
                 names=['System/Network/All/Transmitted/bytes/sec'],
                 values=['per_second'],
                 from_dt='2017-02-09T02:00:00+00:00',
                 to_dt='2017-02-09T04:00:00+00:00',
                 summarize=True))

met = json_loads_byteified(met)
"""

#print getServerName(id='406044344')
#print getNetOutBytessec(id='40604254'), "Mbits/sec"
#[0]['values']['per_second']
#for metric in met['metric_data']['metrics'][0]['timeslices']:
#    print metric['values']['per_second']
