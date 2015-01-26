#include "uci_scanner_resources_KeyRingConnector.h"
#include <jni.h>
#include <glib.h>
#include <stdio.h>
#include <gnome-keyring.h>

static const GnomeKeyringPasswordSchema my_network_password_schema = {
	GNOME_KEYRING_ITEM_NETWORK_PASSWORD,
	{
		{  "user", GNOME_KEYRING_ATTRIBUTE_TYPE_STRING },
		{  "server", GNOME_KEYRING_ATTRIBUTE_TYPE_STRING },
		{  "domain", GNOME_KEYRING_ATTRIBUTE_TYPE_STRING },
		{  "object", GNOME_KEYRING_ATTRIBUTE_TYPE_STRING },
		{  "protocol", GNOME_KEYRING_ATTRIBUTE_TYPE_STRING },
		{  "port", GNOME_KEYRING_ATTRIBUTE_TYPE_UINT32 },
		{  "NULL", 0 },
	}
};

const GnomeKeyringPasswordSchema *MY_KEYRING_NETWORK_PASSWORD = &my_network_password_schema;


JNIEXPORT void JNICALL Java_uci_scanner_resources_KeyRingConnector_savePassToGnomeKeyRing
  (JNIEnv *pEnv, jobject pObj, jstring pServer, jstring pUser, jstring pPass, jstring pDomain) 
{
	const char *lvServer = (*pEnv)->GetStringUTFChars(pEnv, pServer, 0);
	const char *lvUser = (*pEnv)->GetStringUTFChars(pEnv, pUser, 0);
	const char *lvPass = (*pEnv)->GetStringUTFChars(pEnv, pPass, 0);
	const char *lvDomain = (*pEnv)->GetStringUTFChars(pEnv, pDomain, 0);
	GnomeKeyringResult lvRes;
	gchar *lvProt = "smb";
//	gchar *lvDom = "1";
	gchar lvDescript[50];
	g_sprintf (lvDescript, "%s://%s@%s", lvProt, lvUser, lvServer);
	g_set_application_name("XScaner");
	lvRes = gnome_keyring_store_password_sync (MY_KEYRING_NETWORK_PASSWORD,	/* The password type */
											GNOME_KEYRING_SESSION, 			/* Where to save it */
											lvDescript, 					/* Password description, displayed to user */
											lvPass, 							/* The password itself */
											/* These are the attributes */
											"user", lvUser,
											"server", lvServer,
											"protocol", lvProt,
											"domain", lvDomain,
											NULL); 							/* Always end with NULL */
	(*pEnv)->ReleaseStringUTFChars(pEnv, pServer, lvServer);
	(*pEnv)->ReleaseStringUTFChars(pEnv, pUser, lvUser);
	(*pEnv)->ReleaseStringUTFChars(pEnv, pPass, lvPass);
	(*pEnv)->ReleaseStringUTFChars(pEnv, pDomain, lvDomain);
	if (lvRes != GNOME_KEYRING_RESULT_OK)
		g_print ("Unable to save password.");
}

JNIEXPORT void JNICALL Java_uci_scanner_resources_KeyRingConnector_delPassFromGnomeKeyRing
  (JNIEnv *pEnv, jobject pObj, jstring pServer, jstring pUser) 
{
	const char *lvServer = (*pEnv)->GetStringUTFChars(pEnv, pServer, 0);
	const char *lvUser = (*pEnv)->GetStringUTFChars(pEnv, pUser, 0);
	GnomeKeyringResult lvRes;
	g_set_application_name("XScaner");
	lvRes = gnome_keyring_delete_password_sync (MY_KEYRING_NETWORK_PASSWORD, 
											"user", lvUser,
											"server", lvServer,
											NULL); 
	
	(*pEnv)->ReleaseStringUTFChars(pEnv, pServer, lvServer);
	(*pEnv)->ReleaseStringUTFChars(pEnv, pUser, lvUser);
	if (lvRes != GNOME_KEYRING_RESULT_OK)
		g_print ("Unable to remove password.");
	
}

JNIEXPORT jstring JNICALL Java_uci_scanner_resources_KeyRingConnector_getPassFromGnomeKeyRing
  (JNIEnv *pEnv, jobject pObj, jstring pServer, jstring pUser) 
{
	const char *lvServer = (*pEnv)->GetStringUTFChars(pEnv, pServer, 0);
	const char *lvUser = (*pEnv)->GetStringUTFChars(pEnv, pUser, 0);
	gchar *lvPass;
	GnomeKeyringResult lvRes;
	lvRes = gnome_keyring_find_password_sync(MY_KEYRING_NETWORK_PASSWORD,
											&lvPass,
											"user", lvUser,
											"server", lvServer,
											NULL); 
	
	(*pEnv)->ReleaseStringUTFChars(pEnv, pServer, lvServer);
	(*pEnv)->ReleaseStringUTFChars(pEnv, pUser, lvUser);
	if (lvRes == GNOME_KEYRING_RESULT_OK)
		g_print("%s", lvPass);
	else
		g_print("Unable to find password.");	
}

JNIEXPORT jboolean JNICALL Java_uci_scanner_resources_KeyRingConnector_isAvailableGnomeKeyRing
  (JNIEnv *pEnv, jobject pObj) 
{
	if (gnome_keyring_is_available()) {
		return TRUE;
	} else {
		printf("Unable to establish comunicattion with the GNOME-keyring daemon.");
		return FALSE;
	}
}
