/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glBindBufferRangeEXTPROC) (GLenum target, GLuint index, GLuint buffer, GLintptr offset, GLsizeiptr size);
typedef void (APIENTRY *glBindBufferOffsetEXTPROC) (GLenum target, GLuint index, GLuint buffer, GLintptr offset);
typedef void (APIENTRY *glBindBufferBaseEXTPROC) (GLenum target, GLuint index, GLuint buffer);
typedef void (APIENTRY *glBeginTransformFeedbackEXTPROC) (GLenum primitiveMode);
typedef void (APIENTRY *glEndTransformFeedbackEXTPROC) ();
typedef void (APIENTRY *glTransformFeedbackVaryingsEXTPROC) (GLuint program, GLsizei count, const GLchar * varyings, GLenum bufferMode);
typedef void (APIENTRY *glGetTransformFeedbackVaryingEXTPROC) (GLuint program, GLuint index, GLsizei bufSize, GLsizei * length, GLsizei * size, GLenum * type, GLchar * name);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTTransformFeedback_nglBindBufferRangeEXT(JNIEnv *env, jclass clazz, jint target, jint index, jint buffer, jlong offset, jlong size, jlong function_pointer) {
	glBindBufferRangeEXTPROC glBindBufferRangeEXT = (glBindBufferRangeEXTPROC)((intptr_t)function_pointer);
	glBindBufferRangeEXT(target, index, buffer, offset, size);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTTransformFeedback_nglBindBufferOffsetEXT(JNIEnv *env, jclass clazz, jint target, jint index, jint buffer, jlong offset, jlong function_pointer) {
	glBindBufferOffsetEXTPROC glBindBufferOffsetEXT = (glBindBufferOffsetEXTPROC)((intptr_t)function_pointer);
	glBindBufferOffsetEXT(target, index, buffer, offset);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTTransformFeedback_nglBindBufferBaseEXT(JNIEnv *env, jclass clazz, jint target, jint index, jint buffer, jlong function_pointer) {
	glBindBufferBaseEXTPROC glBindBufferBaseEXT = (glBindBufferBaseEXTPROC)((intptr_t)function_pointer);
	glBindBufferBaseEXT(target, index, buffer);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTTransformFeedback_nglBeginTransformFeedbackEXT(JNIEnv *env, jclass clazz, jint primitiveMode, jlong function_pointer) {
	glBeginTransformFeedbackEXTPROC glBeginTransformFeedbackEXT = (glBeginTransformFeedbackEXTPROC)((intptr_t)function_pointer);
	glBeginTransformFeedbackEXT(primitiveMode);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTTransformFeedback_nglEndTransformFeedbackEXT(JNIEnv *env, jclass clazz, jlong function_pointer) {
	glEndTransformFeedbackEXTPROC glEndTransformFeedbackEXT = (glEndTransformFeedbackEXTPROC)((intptr_t)function_pointer);
	glEndTransformFeedbackEXT();
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTTransformFeedback_nglTransformFeedbackVaryingsEXT(JNIEnv *env, jclass clazz, jint program, jint count, jobject varyings, jint varyings_position, jint bufferMode, jlong function_pointer) {
	const GLchar *varyings_address = ((const GLchar *)(*env)->GetDirectBufferAddress(env, varyings)) + varyings_position;
	glTransformFeedbackVaryingsEXTPROC glTransformFeedbackVaryingsEXT = (glTransformFeedbackVaryingsEXTPROC)((intptr_t)function_pointer);
	glTransformFeedbackVaryingsEXT(program, count, varyings_address, bufferMode);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTTransformFeedback_nglGetTransformFeedbackVaryingEXT(JNIEnv *env, jclass clazz, jint program, jint index, jint bufSize, jobject length, jint length_position, jobject size, jint size_position, jobject type, jint type_position, jobject name, jint name_position, jlong function_pointer) {
	GLsizei *length_address = ((GLsizei *)safeGetBufferAddress(env, length)) + length_position;
	GLsizei *size_address = ((GLsizei *)safeGetBufferAddress(env, size)) + size_position;
	GLenum *type_address = ((GLenum *)safeGetBufferAddress(env, type)) + type_position;
	GLchar *name_address = ((GLchar *)(*env)->GetDirectBufferAddress(env, name)) + name_position;
	glGetTransformFeedbackVaryingEXTPROC glGetTransformFeedbackVaryingEXT = (glGetTransformFeedbackVaryingEXTPROC)((intptr_t)function_pointer);
	glGetTransformFeedbackVaryingEXT(program, index, bufSize, length_address, size_address, type_address, name_address);
}

