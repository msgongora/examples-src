#!/bin/bash - 
#===============================================================================
#
#          FILE:  cli_util-msg.sh
# 
#         USAGE:  ./bash_utils.sh 
# 
#   DESCRIPTION:  Utiles para la consola de sistemas basados en debian
# 
#       OPTIONS:  ---
#  REQUIREMENTS:  ---
#          BUGS:  ---
#         NOTES:  ---
#        AUTHOR: Ing. Marcel Sanchez Gongora (Lekram), mrsanchez@uci.cu
#       COMPANY: CENIA,UCI
#       CREATED: 08/30/2011 12:45:03 PM CDT
#      REVISION:  ---
#===============================================================================

[[ -f /usr/bin/aptitude ]] && PM="/usr/bin/aptitude" || PM="/usr/bin/apt-get"
Depend () { # Conocer dependencias de un paquete
    if [ -f "$1" ]; then
        dpkg --info "$1" | grep --color "^.*Depends"
    else
        apt-cache show $1 | grep --color "^Depends"
    fi
}
Recomend() { # Conocer recomendados y sugeridos de un paquete
    if [ -f "$1" ]; then
        dpkg --info "$1" | egrep --color "^.*(Recommends|Suggests)"
    else
        apt-cache show $1 | egrep --color "^.*(Recommends|Suggests)"
    fi
}
Search() { # Hacer varios tipos de b'usquedas de paquetes
    (
    USAGE="Modo de uso: busca [opciones | paquete]\n
    -l <patrón>\t \040\040: paquetes instalados según el patrón\n
    -f <fichero>\t \040\040: paquetes que contienen el fichero"
    temp=`getopt -o l:f: -- "$@"`
    if [ $? != 0 ]; then echo -e $USAGE; return; fi
    eval set -- "$temp"
    while true ; do
        case "$1" in
            -l) regex="$2" ; shift 2 ;;
            -f) file="$2" ; shift 2 ;;
            --) shift ; break ;;
        esac
    done
    if [ "x$regex" != "x" ]; then
        dpkg -l |egrep --color "$regex"
        return
    fi
    if [ "x$file" != "x" ]; then
        sudo apt-file find $file
        return
    fi
    if [ -z "$1" ]; then echo -e $USAGE; return; fi
    apt-cache search $1
    )
}
Inform () { # Conocer descripci'on de un paquete
    if [ -f "$1" ]; then
        dpkg --info "$1"
    else
        apt-cache show $1
    fi
}
Clean() { # Borrar la cache
    sudo apt-get clean 
    if [ -x "`sudo which pbuilder`" ]; then
        mount | egrep -q "/home/marcel/devel/repository|/usr/lib/pbuilder"
        [[ $? -ne 0 ]] && sudo pbuilder clean
   fi
}
Install () { # Instalar paquetes
    if [ -f "$1" ]; then
        sudo LANG=C dpkg -i "$@"
    else
        sudo LANG=C $PM install $@
    fi
}
Version () { # Conocer las versiones que existen de un paquete
    if [ -f "$1" ]; then
        dpkg --info "$1" | grep --color "^.*Version" | sed 's/Version: //g'
    else
        apt-cache policy $@ | tail -n +4
        #		apt-cache show $1 | grep "^Version" | sed 's/Version: //g'
    fi
}
Repo_add() { 
    local tmp="include"
    if [ "$1" = "sid" ] || [ "$1" = "experimental" ]; then
        local dist=$1
        shift
    else
        echo "Modo de uso: repoadd <distro> <paquete>|<changes>"
        return 1 
    fi
    
    if [ -n "$1" ]; then
        [[ "${1##*.}" = "deb" ]] && tmp="${tmp}deb"
    fi
    while [ $# -gt 0 ]; do
        reprepro --ignore=wrongdistribution -Vb ~/devel/repository $tmp $dist $1
        shift
    done
}
Repo_del() {
    if [ "$1" = "sid" ] || [ "$1" = "experimental" ]; then
        local dist=$1
        shift
    else
        echo "Modo de uso: repodel <distro> <paquete>|<dsc>"
        return 1 
    fi
    if [ "x$1" = "xdeb" ] || [ "x$1" = "xdsc" ]; then 
        local opt="-T$1"
        shift
    fi
    while [ $# -gt 0 ]; do
        for i in `reprepro -b ~/devel/repository/  listfilter $dist '$Source (==$1)'|\
          awk '{print $2}'|sort|uniq`; do
            reprepro $opt -Vb ~/devel/repository --listdir ~/devel/repository/pool remove $dist $i
        done
        shift
    done
}
Repo_list() {
    if [ $# -gt 0 ]; then
        reprepro -b ~/devel/repository/ listfilter sid 'Package' | egrep --color "$1"
        reprepro -b ~/devel/repository/ listfilter experimental 'Package' | egrep --color "$1"
    else
        reprepro -b ~/devel/repository/ listfilter sid 'Package'
        reprepro -b ~/devel/repository/ listfilter experimental 'Package'
    fi
}
OpenWithGvim() { 
#    if [  "$#" -gt 1 ]; then
        $HOME/.config/rox.sourceforge.net/SendTo/functions/Open_with_Gvim "$@"
#    else
#        exec gvim -p "$@"
#    fi

}

Package_source() {
    if [ $# -gt 0 ]; then
        if [ -d "$1" ]; then
            dpkg-source -b "$1"
        else
            echo "Error: $1 must be a directory"
        fi
    else
        local pwd1=`pwd`; cd ..; local pwd2=`pwd`
        local dir=`echo "$pwd1" | sed "s#$pwd2/##"`
        dpkg-source -b "$dir"
    fi
}
Port() {
    if [ $# -gt 0 ]; then
        sudo netstat -aplnt | grep --color $1
    else
        sudo netstat -aplnt
    fi
}
VideoInfo() {
    mplayer -vo null -ao null -frames 0 -identify "$@" 2>/dev/null |
		sed -ne '/^ID_/ {
		                  s/[]()|&;<>`'"'"'\\!$" []/\\&/g;p
	  	                }'
    
}
if [ -n "`echo $1|grep [0-9]`" ]; then
   MODE=$1
   shift
else
    echo "\$CLI_UTIL [0|1|2] etc..."
    exit 1
fi


case $MODE in
    0) "$@"
        ;;

    1)  SERVICE=$1
        OPT=$2
        operation(){
	        sudo service $1 $2
        }
        case $OPT in
	        0) operation $SERVICE stop;;
	        1) operation $SERVICE start;;
	        2) operation $SERVICE restart;;
	        *) echo "Usage: $SERVICE {0|1|2} (to stop|start|restart)"
        esac
        ;;
    *)
        ;;

esac    # --- end of case ---
