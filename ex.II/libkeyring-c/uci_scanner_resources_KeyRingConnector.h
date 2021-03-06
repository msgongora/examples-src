/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class uci_scanner_resources_KeyRingConnector */

#ifndef _Included_uci_scanner_resources_KeyRingConnector
#define _Included_uci_scanner_resources_KeyRingConnector
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     uci_scanner_resources_KeyRingConnector
 * Method:    savePassToGnomeKeyRing
 * Signature: (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_uci_scanner_resources_KeyRingConnector_savePassToGnomeKeyRing
  (JNIEnv *, jobject, jstring, jstring, jstring, jstring);

/*
 * Class:     uci_scanner_resources_KeyRingConnector
 * Method:    delPassFromGnomeKeyRing
 * Signature: (Ljava/lang/String;Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_uci_scanner_resources_KeyRingConnector_delPassFromGnomeKeyRing
  (JNIEnv *, jobject, jstring, jstring);

/*
 * Class:     uci_scanner_resources_KeyRingConnector
 * Method:    getPassFromGnomeKeyRing
 * Signature: (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_uci_scanner_resources_KeyRingConnector_getPassFromGnomeKeyRing
  (JNIEnv *, jobject, jstring, jstring);

/*
 * Class:     uci_scanner_resources_KeyRingConnector
 * Method:    isAvailableGnomeKeyRing
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_uci_scanner_resources_KeyRingConnector_isAvailableGnomeKeyRing
  (JNIEnv *, jobject);

#ifdef __cplusplus
}
#endif
#endif
