/* 
 * Copyright (c) 2002-2008 LWJGL Project
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are 
 * met:
 * 
 * * Redistributions of source code must retain the above copyright 
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'LWJGL' nor the names of 
 *   its contributors may be used to endorse or promote products derived 
 *   from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR 
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR 
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING 
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
 
/**
 * $Id: org_lwjgl_openal_ALC.c 2279 2006-02-23 19:22:00Z elias_naur $
 *
 * This is the actual JNI implementation of the OpenAL context/device library. 
 * 
 * @author Brian Matzon <brian@matzon.dk>
 * @version $Revision: 2279 $
 */

/* OpenAL includes */
#include "extal.h"

//alc
typedef ALCubyte*   (ALCAPIENTRY *alcGetStringPROC)(ALCdevice *device,ALCenum param);
typedef ALCvoid     (ALCAPIENTRY *alcGetIntegervPROC)(ALCdevice *device,ALCenum param,ALCsizei size,ALCint *data);
typedef ALCdevice*  (ALCAPIENTRY *alcOpenDevicePROC)(ALCubyte *deviceName);
typedef ALCboolean  (ALCAPIENTRY *alcCloseDevicePROC)(ALCdevice *device);
typedef ALCcontext* (ALCAPIENTRY *alcCreateContextPROC)(ALCdevice *device,ALCint *attrList);
typedef ALCenum		(ALCAPIENTRY *alcMakeContextCurrentPROC)(ALCcontext *context);
typedef ALCvoid	    (ALCAPIENTRY *alcProcessContextPROC)(ALCcontext *context);
typedef ALCdevice*  (ALCAPIENTRY *alcGetContextsDevicePROC)(ALCcontext *context);
typedef ALCvoid	    (ALCAPIENTRY *alcSuspendContextPROC)(ALCcontext *context);
typedef ALCvoid     (ALCAPIENTRY *alcDestroyContextPROC)(ALCcontext *context);
typedef ALCenum	    (ALCAPIENTRY *alcGetErrorPROC)(ALCdevice *device);
typedef ALCboolean  (ALCAPIENTRY *alcIsExtensionPresentPROC)(ALCdevice *device,ALCubyte *extName);
//typedef ALCvoid*    (ALCAPIENTRY *alcGetProcAddressPROC)(ALCdevice *device,ALCubyte *funcName);
typedef ALCenum	    (ALCAPIENTRY *alcGetEnumValuePROC)(ALCdevice *device,ALCubyte *enumName);
typedef ALCcontext* (ALCAPIENTRY *alcGetCurrentContextPROC)(ALCvoid);

static alcGetCurrentContextPROC alcGetCurrentContext = NULL;
static alcGetStringPROC alcGetString;
static alcGetIntegervPROC alcGetIntegerv;
static alcOpenDevicePROC alcOpenDevice;
static alcCloseDevicePROC alcCloseDevice;
static alcCreateContextPROC alcCreateContext;
static alcMakeContextCurrentPROC alcMakeContextCurrent;
static alcProcessContextPROC alcProcessContext;
static alcGetContextsDevicePROC alcGetContextsDevice;
static alcSuspendContextPROC alcSuspendContext;
static alcDestroyContextPROC alcDestroyContext;
static alcGetErrorPROC alcGetError;
static alcIsExtensionPresentPROC alcIsExtensionPresent;
//static alcGetProcAddressPROC alcGetProcAddress;
static alcGetEnumValuePROC alcGetEnumValue;

/**
 * This function returns strings related to the context.
 *
 * C Specification:
 * ALubyte * alcGetString(ALCdevice *device, ALenum token);
 */
static jstring JNICALL Java_org_lwjgl_openal_ALC10_nalcGetString (JNIEnv *env, jclass clazz, jlong deviceaddress, jint token) {
	const char* alcString = (const char*) alcGetString((ALCdevice*)((intptr_t)deviceaddress), (ALenum) token);
	int length;
	int i=1;

	if(alcString == NULL) {
		return NULL;
	}

	// Special treatment of enumeration tokens
	// These are encoded using \0 between elements and a finishing \0\0
	switch(token) {
		case 0x1005:	// ALC_DEVICE_SPECIFIER
		case 0x310:		// ALC_CAPTURE_DEVICE_SPECIFIER
		case 0x1013:	// ALC_ALL_DEVICES_SPECIFIER
			while (alcString[i - 1] != '\0' || alcString[i] != '\0') { 
				i++; 
			}
			length = i + 1;
			break;
		default:
			length = strlen(alcString);
	}
	return NewStringNativeWithLength(env, alcString, length);
}

/**
 * This function returns integers related to the context.
 * 
 * C Specification:
 * ALvoid alcGetIntegerv(ALCdevice *device, ALenum token, ALsizei size, ALint *dest);
 */
static void JNICALL Java_org_lwjgl_openal_ALC10_nalcGetIntegerv (JNIEnv *env, jclass clazz, jlong deviceaddress, jint token, jint size, jobject dest, jint offset) {
	ALint* address = NULL;
	if (dest != NULL) {
		address = offset + (ALint*) (*env)->GetDirectBufferAddress(env, dest);
	}
	alcGetIntegerv((ALCdevice*)((intptr_t)deviceaddress), (ALenum) token, (ALsizei) size, address);
}

/**
 * This function opens a device by name.
 * 
 * C Specification:
 * ALCdevice *alcOpenDevice( const ALubyte *tokstr );
 */
static jlong JNICALL Java_org_lwjgl_openal_ALC10_nalcOpenDevice (JNIEnv *env, jclass clazz, jstring tokstr) {
	char * tokenstring;
	ALCdevice* device;

	if(tokstr != NULL) {
		tokenstring = GetStringNativeChars(env, tokstr);
	} else {
		tokenstring = NULL;
	}

	/* get device */
	device = alcOpenDevice((ALubyte *) tokenstring);

	if(tokenstring != NULL) {
		free(tokenstring);
	}

	return (jlong)((intptr_t)device);
}

/**
 * This function closes a device by name.
 * 
 * C Specification:
 * bool alcCloseDevice( ALCdevice *dev );
 */
static jboolean JNICALL Java_org_lwjgl_openal_ALC10_nalcCloseDevice (JNIEnv *env, jclass clazz, jlong deviceaddress) {
	return alcCloseDevice((ALCdevice*)((intptr_t)deviceaddress));
}

/**
 * This function creates a context using a specified device.
 * 
 * C Specification:
 * ALCcontext* alcCreateContext( ALCdevice *dev, ALint* attrlist );
 */
static jlong JNICALL Java_org_lwjgl_openal_ALC10_nalcCreateContext (JNIEnv *env, jclass clazz, jlong deviceaddress, jobject attrlist) {
	ALint* address = NULL;
	ALCcontext* context;

	if (attrlist != NULL) {
		address = (ALint*) safeGetBufferAddress(env, attrlist);
	}
	context = alcCreateContext((ALCdevice*)((intptr_t)deviceaddress), address); 
	
	return (jlong)((intptr_t)context);
}

/**
 * This function makes a specified context the current context.
 *
 * C Specification:
 * ALCboolean alcMakeContextCurrent(ALCcontext *context);
 */
static jint JNICALL Java_org_lwjgl_openal_ALC10_nalcMakeContextCurrent (JNIEnv *env, jclass clazz, jlong contextaddress) {
	ALCcontext* context = (ALCcontext*)((intptr_t)contextaddress);
	return alcMakeContextCurrent(context);
}

/**
 * This function tells a context to begin processing.
 * 
 * C Specification:
 * void alcProcessContext(ALCcontext *context);
 */
static void JNICALL Java_org_lwjgl_openal_ALC10_nalcProcessContext (JNIEnv *env, jclass clazz, jlong contextaddress) {
	alcProcessContext((ALCcontext*)((intptr_t)contextaddress));
}

/**
 * This function retrieves the current context.
 * 
 * C Specification:
 * ALCcontext* alcGetCurrentContext( ALvoid );
 */
static jlong JNICALL Java_org_lwjgl_openal_ALC10_nalcGetCurrentContext (JNIEnv *env, jclass clazz) {
	ALCcontext* context = alcGetCurrentContext();
	return (jlong)((intptr_t)context);
}

/**
 * This function retrieves the specified contexts device
 * 
 * C Specification:
 * ALCdevice* alcGetContextsDevice(ALCcontext *context);
 */
static jlong JNICALL Java_org_lwjgl_openal_ALC10_nalcGetContextsDevice (JNIEnv *env, jclass clazz, jlong contextaddress) {
	ALCdevice* device = alcGetContextsDevice((ALCcontext*)((intptr_t)contextaddress));
	return (jlong)((intptr_t)device);
}

/**
 * This function suspends processing on a specified context.
 *
 * C Specification:
 * void alcSuspendContext(ALCcontext *context);
 */
static void JNICALL Java_org_lwjgl_openal_ALC10_nalcSuspendContext (JNIEnv *env, jclass clazz, jlong contextaddress) {
	alcSuspendContext((ALCcontext*)((intptr_t)contextaddress));
}

/**
 * This function destroys a context.
 * 
 * C Specification:
 * void alcDestroyContext(ALCcontext *context);
 */
static void JNICALL Java_org_lwjgl_openal_ALC10_nalcDestroyContext (JNIEnv *env, jclass clazz, jlong contextaddress) {
	alcDestroyContext((ALCcontext*)((intptr_t)contextaddress));
}

/**
 * This function retrieves the specified devices context error state.
 * 
 * C Specification:
 * ALCenum alcGetError(ALCdevice *device);
 */
static jint JNICALL Java_org_lwjgl_openal_ALC10_nalcGetError (JNIEnv *env, jclass clazz, jlong deviceaddress) {
	return alcGetError((ALCdevice*)((intptr_t)deviceaddress));
}

/**
 * This function queries if a specified context extension is available.
 * 
 * C Specification:
 * ALboolean alcIsExtensionPresent(ALCdevice *device, ALubyte *extName);
 */
static jboolean JNICALL Java_org_lwjgl_openal_ALC10_nalcIsExtensionPresent (JNIEnv *env, jclass clazz, jlong deviceaddress, jstring extName) {
	/* get extension */
	ALubyte* functionname = (ALubyte*) GetStringNativeChars(env, extName);
	
	jboolean result = (jboolean) alcIsExtensionPresent((ALCdevice*)((intptr_t)deviceaddress), functionname);
	
	free(functionname);
	
	return result;
}

/**
 * This function retrieves the enum value for a specified enumeration name.
 *
 * C Specification:
 * ALenum alcGetEnumValue(ALCdevice *device, ALubyte *enumName);
 */
static jint JNICALL Java_org_lwjgl_openal_ALC10_nalcGetEnumValue (JNIEnv *env, jclass clazz, jlong deviceaddress, jstring enumName) {	
	/* get extension */
	ALubyte* enumerationname = (ALubyte*) GetStringNativeChars(env, enumName);
	
	jint result = (jint) alcGetEnumValue((ALCdevice*)((intptr_t)deviceaddress), enumerationname);
	
	free(enumerationname);
	
	return result;
}

/**
 * Loads the context OpenAL functions
 *
 * @return true if all methods were loaded, false if one of the methods could not be loaded
 */
#ifdef __cplusplus
extern "C" {
#endif
JNIEXPORT void JNICALL Java_org_lwjgl_openal_ALC10_initNativeStubs(JNIEnv *env, jclass clazz) {
	JavaMethodAndExtFunction functions[] = {
		{"nalcGetString", "(JI)Ljava/lang/String;", (void*)&Java_org_lwjgl_openal_ALC10_nalcGetString, "alcGetString", (void*)&alcGetString},
		{"nalcGetIntegerv", "(JIILjava/nio/Buffer;I)V", (void*)&Java_org_lwjgl_openal_ALC10_nalcGetIntegerv, "alcGetIntegerv", (void*)&alcGetIntegerv},
		{"nalcOpenDevice", "(Ljava/lang/String;)J", (void*)&Java_org_lwjgl_openal_ALC10_nalcOpenDevice, "alcOpenDevice", (void*)&alcOpenDevice},
		{"nalcCloseDevice", "(J)Z", (void*)&Java_org_lwjgl_openal_ALC10_nalcCloseDevice, "alcCloseDevice", (void*)&alcCloseDevice},
		{"nalcCreateContext", "(JLjava/nio/IntBuffer;)J", (void*)&Java_org_lwjgl_openal_ALC10_nalcCreateContext, "alcCreateContext", (void*)&alcCreateContext},
		{"nalcMakeContextCurrent", "(J)I", (void*)&Java_org_lwjgl_openal_ALC10_nalcMakeContextCurrent, "alcMakeContextCurrent", (void*)&alcMakeContextCurrent},
		{"nalcProcessContext", "(J)V", (void*)&Java_org_lwjgl_openal_ALC10_nalcProcessContext, "alcProcessContext", (void*)&alcProcessContext},
		{"nalcGetCurrentContext", "()J", (void*)&Java_org_lwjgl_openal_ALC10_nalcGetCurrentContext, "alcGetCurrentContext", (void*)&alcGetCurrentContext},
		{"nalcGetContextsDevice", "(J)J", (void*)&Java_org_lwjgl_openal_ALC10_nalcGetContextsDevice, "alcGetContextsDevice", (void*)&alcGetContextsDevice},
		{"nalcSuspendContext", "(J)V", (void*)&Java_org_lwjgl_openal_ALC10_nalcSuspendContext, "alcSuspendContext", (void*)&alcSuspendContext},
		{"nalcDestroyContext", "(J)V", (void*)&Java_org_lwjgl_openal_ALC10_nalcDestroyContext, "alcDestroyContext", (void*)&alcDestroyContext},
		{"nalcGetError", "(J)I", (void*)&Java_org_lwjgl_openal_ALC10_nalcGetError, "alcGetError", (void*)&alcGetError},
		{"nalcIsExtensionPresent", "(JLjava/lang/String;)Z", (void*)&Java_org_lwjgl_openal_ALC10_nalcIsExtensionPresent, "alcIsExtensionPresent", (void*)&alcIsExtensionPresent},
		{"nalcGetEnumValue", "(JLjava/lang/String;)I", (void*)&Java_org_lwjgl_openal_ALC10_nalcGetEnumValue, "alcGetEnumValue", (void*)&alcGetEnumValue}
	};
	int num_functions = NUMFUNCTIONS(functions);
	extal_InitializeClass(env, clazz, num_functions, functions);
}
#ifdef __cplusplus
}
#endif
