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

static void
savePassToGnomeKeyRing (gchar *pServer, gchar *pUser, gchar *pPass)
{
	GnomeKeyringResult lvRes;
	gchar *lvProt = "smb";
	gchar *lvDom = "1";
	gchar lvDescript[50];
	g_sprintf (lvDescript, "%s://%s@%s", lvProt, pUser, pServer);
    g_set_application_name("XScaner");
    lvRes = gnome_keyring_store_password_sync (MY_KEYRING_NETWORK_PASSWORD,	/* The password type */
                                      GNOME_KEYRING_SESSION,          		/* Where to save it */
                                      lvDescript,       					/* Password description, displayed to user */
                                      pPass,                 				/* The password itself */
                                      /* These are the attributes */
                                      "user", pUser, 
                                      "server", pServer,
                                      "protocol", lvProt,
                                      "domain", lvDom,
                                      NULL); /* Always end with NULL */

//    res = gnome_keyring_set_network_password_sync(NULL,
//    									"marcel",
//    									"1",
//    									"10.7.16.162",
//    									NULL,
//    									"smb",
//    									NULL,
//    									0,
//    									"newpass",
//    									&id);
//    g_print ("set network password: res: %d id: %d\n", res, id);
    
    if (lvRes != GNOME_KEYRING_RESULT_OK)
    	g_print ("Unable to save password.");
}

static void
delPassFromGnomeKeyRing(gchar *pServer, gchar *pUser)
{
	GnomeKeyringResult lvRes;
	g_set_application_name("XScaner");
	lvRes = gnome_keyring_delete_password_sync (MY_KEYRING_NETWORK_PASSWORD, 
									"user", pUser,
									"server", pServer,
									NULL); 
	
	if (lvRes != GNOME_KEYRING_RESULT_OK)
		g_print ("Unable to remove password.");
}
static gchar *
getPassFromGnomeKeyRing(gchar *pServer, gchar *pUser)
{
	gchar *lvPass;
	GnomeKeyringResult lvRes;
	lvRes = gnome_keyring_find_password_sync(MY_KEYRING_NETWORK_PASSWORD,
									&lvPass,
									"user", pUser,
									"server", pServer,
									NULL); 
	if (lvRes == GNOME_KEYRING_RESULT_OK)
		g_print("%s", lvPass);
	else
		g_print("Unable to find password.");
}
static gboolean
isAvailableGnomeKeyRing()
{
	if (gnome_keyring_is_available()) {
		return TRUE;
	} else {
		printf("Unable to establish comunicattion with the GNOME-keyring daemon.");
		return FALSE;
	}
}

int main ()
{
//	savePassToGnomeKeyRing("10.7.16.162", "marcel", "newpass");
	delPassFromGnomeKeyRing("10.7.16.162", "marcel");
	return 0;
}