# Vars
LIBRARY = `pwd`/libkeyring-java.so
SRC = uci_scanner_keyring_KeyRingConnector.c
INCLUDES = -I/usr/lib/jvm/java-6-sun/include -I/usr/lib/jvm/java-6-sun/include/linux `pkg-config --libs gnome-keyring-1 --cflags gnome-keyring-1`


# All Target
all:
	-@echo 'Generando libreria de enlace dinamico'
	gcc -shared $(INCLUDES) -o $(LIBRARY) $(SRC)
# clean Target
clean:
	-@echo 'Eliminando libreria de enlace dinamico'
	-rm $(LIBRARY)