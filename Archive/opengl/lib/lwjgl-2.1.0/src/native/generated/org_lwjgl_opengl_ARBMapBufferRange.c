/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef GLvoid * (APIENTRY *glMapBufferRangePROC) (GLenum target, GLintptr offset, GLsizeiptr length, GLbitfield access);
typedef void (APIENTRY *glFlushMappedBufferRangePROC) (GLenum target, GLintptr offset, GLsizeiptr length);

JNIEXPORT jobject JNICALL Java_org_lwjgl_opengl_ARBMapBufferRange_nglMapBufferRange(JNIEnv *env, jclass clazz, jint target, jlong offset, jlong length, jint access, jlong result_size, jobject old_buffer, jlong function_pointer) {
	glMapBufferRangePROC glMapBufferRange = (glMapBufferRangePROC)((intptr_t)function_pointer);
	GLvoid * __result = glMapBufferRange(target, offset, length, access);
	return safeNewBufferCached(env, __result, result_size, old_buffer);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBMapBufferRange_nglFlushMappedBufferRange(JNIEnv *env, jclass clazz, jint target, jlong offset, jlong length, jlong function_pointer) {
	glFlushMappedBufferRangePROC glFlushMappedBufferRange = (glFlushMappedBufferRangePROC)((intptr_t)function_pointer);
	glFlushMappedBufferRange(target, offset, length);
}

