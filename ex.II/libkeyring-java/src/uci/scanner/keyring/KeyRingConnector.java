/**
 * Copyright (c) 2009 Marcel Sanchez Gongora <msgongora@gmail.com>
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 */

package uci.scanner.keyring;

import uci.scanner.keyring.util.JarLib;

//import com.sun.org.apache.bcel.internal.generic.LoadClass;

/**
 * Esta clase es una envoltura (wrapper) de la librerias 
 * <a href="http://live.gnome.org/GnomeKeyring">gnome-keyring</a>, 
 * la cual proporciona acceso al Gestor de Claves de GNOME.
 * 
 *  @author Marcel Sanchez Gongora
 *  @version 1.0
 */

public class KeyRingConnector {
	static { 
//		System.loadLibrary("keyring-java");
		JarLib.load(KeyRingConnector.class, "keyring-java");
	}
	/**
	 * Guarda los detalles de la conección en el Gestor de Claves de GNOME.
	 * @param pServer Servidor
	 * @param pUser Usuario
	 * @param pPass Contraseña
	 */
	public native void savePassToGnomeKeyRing (String pServer, String pUser, String pPass, String pDomain);
	/**
	 * Elimina los detalles de la conección del Gestor de Claves de GNOME.
	 * @param pServer Servidor
	 * @param pUser Usuario
	 */
	public native void delPassFromGnomeKeyRing(String pServer, String pUser);
	/**
	 * Devuelve la contraseña del usuario <b>pUser</b> para el servidor <b>pServer</b>
	 * @param pServer Servidor
	 * @param pUser Usuario
	 * @return La contraseña
	 */
	public native String getPassFromGnomeKeyRing(String pServer, String pUser);
	/**
	 * Comprueba si el demonio del Gestor de Claves de GNOME esta corriendo.
	 * @return TRUE si está corriendo, FALSE en caso contrario.
	 */
	public native boolean isAvailableGnomeKeyRing();
//	static {
//		System.out.println("java.library.path:"
//				+ System.getProperty("java.library.path"));
		// Si quisieramos cargar la libreria buscando en el java.library.path
//		 System.loadLibrary("keyring");
		// Para forzar la carga de la libreria
//		System.load("/home/marcel/workspace/ws_visual/libkeyring-java/libs/libkeyring.so");
//		System.load("libs/libkeyring.so");
				
//	}
}
