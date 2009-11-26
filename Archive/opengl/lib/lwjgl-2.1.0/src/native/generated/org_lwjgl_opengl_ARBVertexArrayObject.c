/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glBindVertexArrayPROC) (GLuint array);
typedef void (APIENTRY *glDeleteVertexArraysPROC) (GLsizei n, const GLuint * arrays);
typedef void (APIENTRY *glGenVertexArraysPROC) (GLsizei n, GLuint * arrays);
typedef GLboolean (APIENTRY *glIsVertexArrayPROC) (GLuint array);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBVertexArrayObject_nglBindVertexArray(JNIEnv *env, jclass clazz, jint array, jlong function_pointer) {
	glBindVertexArrayPROC glBindVertexArray = (glBindVertexArrayPROC)((intptr_t)function_pointer);
	glBindVertexArray(array);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBVertexArrayObject_nglDeleteVertexArrays(JNIEnv *env, jclass clazz, jint n, jobject arrays, jint arrays_position, jlong function_pointer) {
	const GLuint *arrays_address = ((const GLuint *)(*env)->GetDirectBufferAddress(env, arrays)) + arrays_position;
	glDeleteVertexArraysPROC glDeleteVertexArrays = (glDeleteVertexArraysPROC)((intptr_t)function_pointer);
	glDeleteVertexArrays(n, arrays_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBVertexArrayObject_nglGenVertexArrays(JNIEnv *env, jclass clazz, jint n, jobject arrays, jint arrays_position, jlong function_pointer) {
	GLuint *arrays_address = ((GLuint *)(*env)->GetDirectBufferAddress(env, arrays)) + arrays_position;
	glGenVertexArraysPROC glGenVertexArrays = (glGenVertexArraysPROC)((intptr_t)function_pointer);
	glGenVertexArrays(n, arrays_address);
}

JNIEXPORT jboolean JNICALL Java_org_lwjgl_opengl_ARBVertexArrayObject_nglIsVertexArray(JNIEnv *env, jclass clazz, jint array, jlong function_pointer) {
	glIsVertexArrayPROC glIsVertexArray = (glIsVertexArrayPROC)((intptr_t)function_pointer);
	GLboolean __result = glIsVertexArray(array);
	return __result;
}

