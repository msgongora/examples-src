#!/usr/bin/perl 
#############################################################################
# Copyright (C) 2008-2011 Marcel S. Gongora <mrsanchez@uci.cu>              #
#									    #
# This program is free software; you can redistribute it and/or modify	    #
# it under the terms of the GNU General Public License version 2 as	    #
# published by the Free Software Foundation.				    #
#                                                                           #
# Depend:                                                                   #
# 		perl (>=5.10.0)						    #
# 		libmail-imapclient-perl (>=3.07)                            #   
#		libio-socket-ssl-perl (>=1.02)                              #
#		libnet-dbus-perl (>=0.33.6)				    #
#		notification-daemon			                    #
#		libdigest-sha1-perl                                         #
#		libdigest-hmac-perl                                         #
#		                                                            #
#############################################################################

use strict;
#use warnings;
use Switch;
use Mail::IMAPClient;
use IO::Socket::SSL;
use MIME::Base64;
use Net::DBus qw(:typing);
use Getopt::Std;
use vars qw($fileTemp %opts $server $port $user $sessionBus);
use Env qw(USER);

$fileTemp = "/tmp/jamn.tmp";
$server = 'fakeserver.uci.cu';
$port = '993';
$user = 'mrsanchez';
getopts('sn', \%opts);
if ( !($opts{s}) and !($opts{n}) ) {
	&usage;
}

$sessionBus = Net::DBus->session;

my $nService = $sessionBus->get_service("org.freedesktop.Notifications");
my $nObject = $nService->get_object("/org/freedesktop/Notifications");
my $nId;
my $ssl;
my @folders = ('INBOX','INBOX/1.work','INBOX/2.project');
 
	#name of server
	if( defined($opts{s}) ) {
		print $server;
        exit;
	}
#eval{
	$ssl = IO::Socket::SSL->new( PeerAddr	=> $server,
								PeerPort	=> $port,
								Timeout 	=> 2, 
								);
                                #error("SSL Error: ".$@) unless defined $ssl;
    error("Error 1") unless defined $ssl;
#};

    #$ssl->autoflush(1);

	#numbers of message
	if( defined($opts{n}) ) {
		# TO CHANGE PASSWORD: 
        # perl -MMIME::Base32 -e 'print MIME::Base32::encode("newPassword");'
        # perl -MMIME::Base64 -e 'print encode_base64("newPassword")'
        # COPY OUTPUT AND PASTE BELOW
        
        my $imap = Mail::IMAPClient->new( Server	=> $server.':'.$port,
                                          Socket	=> $ssl,
                                          User		=> $user,
                                          Password	=> decode_base64("TmV3dGxxYm5lOTBvbA=="),
                                          Timeout	=> 2,
                                        );
            # module uses eval, so we use $@ instead of $!
            #error("IMAP Error: ".$@) unless defined $imap;
            error("Error 2") unless defined $imap;
        my @mails;
        my $count = 0;
        foreach (@folders){
            $imap->select($_);
            @mails = $imap->unseen;
            $count += @mails;
        }
		my $aux = 0;
		my $notify = 0;
		my $bool = 0;
        eval{ open (F, $fileTemp) }; #|| open (F, ">$fileTemp");
		if (!$@){
			my $tmp = <F>;
			$aux = $count;
			$aux -= $tmp;
			close F;
		}else{
			$bool = 1;
		}
		if ($bool){
			$notify = $count;
		}elsif ($aux > 0){
			$notify = $aux;
		}
        my $pid = `pgrep -u $USER evolution$| head -n1`;
        if ( $pid !~ /\d+/ ){
            $pid = `pgrep -u $USER thunderbird$| head -n1`;
        }
        if ($notify > 0 and $pid !~ /\d+/ ){
			$nId = $nObject->Notify("Mail-notification-msg", 0, 
									"/home/marcel/script/jamn/jamn.png", 
									'New email', 'You have received '. $aux .
									' new message(s)', 
									[], {}, 5000); 
        # para el notify antiguo 
        # ['Close', 'Close'], {x => dbus_int32(995), y => dbus_int32(745)}, 5000);	
		}
		open (F, ">$fileTemp");
		print F $count;
		close F;
		print $count;
        $imap->logout();
	}

	

#======FUNCTIONS===============================================================

#====Usage message=============================================================
sub usage {
	#option requires an argument
	print "Usage: jamn [-s | -n ]\n";
	exit(1);
}
sub error {
	printf ("%s\n" , @_);
	exit(1);
}
