/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glClientAttribDefaultEXTPROC) (GLbitfield mask);
typedef void (APIENTRY *glPushClientAttribDefaultEXTPROC) (GLbitfield mask);
typedef void (APIENTRY *glMatrixLoadfEXTPROC) (GLenum matrixMode, const GLfloat * m);
typedef void (APIENTRY *glMatrixLoaddEXTPROC) (GLenum matrixMode, const GLdouble * m);
typedef void (APIENTRY *glMatrixMultfEXTPROC) (GLenum matrixMode, const GLfloat * m);
typedef void (APIENTRY *glMatrixMultdEXTPROC) (GLenum matrixMode, const GLdouble * m);
typedef void (APIENTRY *glMatrixLoadIdentityEXTPROC) (GLenum matrixMode);
typedef void (APIENTRY *glMatrixRotatefEXTPROC) (GLenum matrixMode, GLfloat angle, GLfloat x, GLfloat y, GLfloat z);
typedef void (APIENTRY *glMatrixRotatedEXTPROC) (GLenum matrixMode, GLdouble angle, GLdouble x, GLdouble y, GLdouble z);
typedef void (APIENTRY *glMatrixScalefEXTPROC) (GLenum matrixMode, GLfloat x, GLfloat y, GLfloat z);
typedef void (APIENTRY *glMatrixScaledEXTPROC) (GLenum matrixMode, GLdouble x, GLdouble y, GLdouble z);
typedef void (APIENTRY *glMatrixTranslatefEXTPROC) (GLenum matrixMode, GLfloat x, GLfloat y, GLfloat z);
typedef void (APIENTRY *glMatrixTranslatedEXTPROC) (GLenum matrixMode, GLdouble x, GLdouble y, GLdouble z);
typedef void (APIENTRY *glMatrixOrthoEXTPROC) (GLenum matrixMode, GLdouble l, GLdouble r, GLdouble b, GLdouble t, GLdouble n, GLdouble f);
typedef void (APIENTRY *glMatrixFrustumEXTPROC) (GLenum matrixMode, GLdouble l, GLdouble r, GLdouble b, GLdouble t, GLdouble n, GLdouble f);
typedef void (APIENTRY *glMatrixPushEXTPROC) (GLenum matrixMode);
typedef void (APIENTRY *glMatrixPopEXTPROC) (GLenum matrixMode);
typedef void (APIENTRY *glTextureParameteriEXTPROC) (GLuint texture, GLenum target, GLenum pname, GLint param);
typedef void (APIENTRY *glTextureParameterivEXTPROC) (GLuint texture, GLenum target, GLenum pname, const GLint * param);
typedef void (APIENTRY *glTextureParameterfEXTPROC) (GLuint texture, GLenum target, GLenum pname, GLfloat param);
typedef void (APIENTRY *glTextureParameterfvEXTPROC) (GLuint texture, GLenum target, GLenum pname, const GLfloat * param);
typedef void (APIENTRY *glTextureImage1DEXTPROC) (GLuint texture, GLenum target, GLint level, GLint internalformat, GLsizei width, GLint border, GLenum format, GLenum type, const GLvoid * pixels);
typedef void (APIENTRY *glTextureImage2DEXTPROC) (GLuint texture, GLenum target, GLint level, GLint internalformat, GLsizei width, GLsizei height, GLint border, GLenum format, GLenum type, const GLvoid * pixels);
typedef void (APIENTRY *glTextureSubImage1DEXTPROC) (GLuint texture, GLenum target, GLint level, GLint xoffset, GLsizei width, GLenum format, GLenum type, const GLvoid * pixels);
typedef void (APIENTRY *glTextureSubImage2DEXTPROC) (GLuint texture, GLenum target, GLint level, GLint xoffset, GLint yoffset, GLsizei width, GLsizei height, GLenum format, GLenum type, const GLvoid * pixels);
typedef void (APIENTRY *glCopyTextureImage1DEXTPROC) (GLuint texture, GLenum target, GLint level, GLenum internalformat, GLint x, GLint y, GLsizei width, GLint border);
typedef void (APIENTRY *glCopyTextureImage2DEXTPROC) (GLuint texture, GLenum target, GLint level, GLenum internalformat, GLint x, GLint y, GLsizei width, GLsizei height, GLint border);
typedef void (APIENTRY *glCopyTextureSubImage1DEXTPROC) (GLuint texture, GLenum target, GLint level, GLint xoffset, GLint x, GLint y, GLsizei width);
typedef void (APIENTRY *glCopyTextureSubImage2DEXTPROC) (GLuint texture, GLenum target, GLint level, GLint xoffset, GLint yoffset, GLint x, GLint y, GLsizei width, GLsizei height);
typedef void (APIENTRY *glGetTextureImageEXTPROC) (GLuint texture, GLenum target, GLint level, GLenum format, GLenum type, GLvoid * pixels);
typedef void (APIENTRY *glGetTextureParameterfvEXTPROC) (GLuint texture, GLenum target, GLenum pname, GLfloat * params);
typedef void (APIENTRY *glGetTextureParameterivEXTPROC) (GLuint texture, GLenum target, GLenum pname, GLint * params);
typedef void (APIENTRY *glGetTextureLevelParameterfvEXTPROC) (GLuint texture, GLenum target, GLint level, GLenum pname, GLfloat * params);
typedef void (APIENTRY *glGetTextureLevelParameterivEXTPROC) (GLuint texture, GLenum target, GLint level, GLenum pname, GLint * params);
typedef void (APIENTRY *glTextureImage3DEXTPROC) (GLuint texture, GLenum target, GLint level, GLint internalformat, GLsizei width, GLsizei height, GLsizei depth, GLint border, GLenum format, GLenum type, const GLvoid * pixels);
typedef void (APIENTRY *glTextureSubImage3DEXTPROC) (GLuint texture, GLenum target, GLint level, GLint xoffset, GLint yoffset, GLint zoffset, GLsizei width, GLsizei height, GLsizei depth, GLenum format, GLenum type, const GLvoid * pixels);
typedef void (APIENTRY *glCopyTextureSubImage3DEXTPROC) (GLuint texture, GLenum target, GLint level, GLint xoffset, GLint yoffset, GLint zoffset, GLint x, GLint y, GLsizei width, GLsizei height);
typedef void (APIENTRY *glBindMultiTextureEXTPROC) (GLenum texunit, GLenum target, GLuint texture);
typedef void (APIENTRY *glMultiTexCoordPointerEXTPROC) (GLenum texunit, GLint size, GLenum type, GLsizei stride, const GLvoid * pointer);
typedef void (APIENTRY *glMultiTexEnvfEXTPROC) (GLenum texunit, GLenum target, GLenum pname, GLfloat param);
typedef void (APIENTRY *glMultiTexEnvfvEXTPROC) (GLenum texunit, GLenum target, GLenum pname, const GLfloat * params);
typedef void (APIENTRY *glMultiTexEnviEXTPROC) (GLenum texunit, GLenum target, GLenum pname, GLint param);
typedef void (APIENTRY *glMultiTexEnvivEXTPROC) (GLenum texunit, GLenum target, GLenum pname, const GLint * params);
typedef void (APIENTRY *glMultiTexGendEXTPROC) (GLenum texunit, GLenum coord, GLenum pname, GLdouble param);
typedef void (APIENTRY *glMultiTexGendvEXTPROC) (GLenum texunit, GLenum coord, GLenum pname, const GLdouble * params);
typedef void (APIENTRY *glMultiTexGenfEXTPROC) (GLenum texunit, GLenum coord, GLenum pname, GLfloat param);
typedef void (APIENTRY *glMultiTexGenfvEXTPROC) (GLenum texunit, GLenum coord, GLenum pname, const GLfloat * params);
typedef void (APIENTRY *glMultiTexGeniEXTPROC) (GLenum texunit, GLenum coord, GLenum pname, GLint param);
typedef void (APIENTRY *glMultiTexGenivEXTPROC) (GLenum texunit, GLenum coord, GLenum pname, const GLint * params);
typedef void (APIENTRY *glGetMultiTexEnvfvEXTPROC) (GLenum texunit, GLenum target, GLenum pname, GLfloat * params);
typedef void (APIENTRY *glGetMultiTexEnvivEXTPROC) (GLenum texunit, GLenum target, GLenum pname, GLint * params);
typedef void (APIENTRY *glGetMultiTexGendvEXTPROC) (GLenum texunit, GLenum coord, GLenum pname, GLdouble * params);
typedef void (APIENTRY *glGetMultiTexGenfvEXTPROC) (GLenum texunit, GLenum coord, GLenum pname, GLfloat * params);
typedef void (APIENTRY *glGetMultiTexGenivEXTPROC) (GLenum texunit, GLenum coord, GLenum pname, GLint * params);
typedef void (APIENTRY *glMultiTexParameteriEXTPROC) (GLenum texunit, GLenum target, GLenum pname, GLint param);
typedef void (APIENTRY *glMultiTexParameterivEXTPROC) (GLenum texunit, GLenum target, GLenum pname, const GLint * param);
typedef void (APIENTRY *glMultiTexParameterfEXTPROC) (GLenum texunit, GLenum target, GLenum pname, GLfloat param);
typedef void (APIENTRY *glMultiTexParameterfvEXTPROC) (GLenum texunit, GLenum target, GLenum pname, const GLfloat * param);
typedef void (APIENTRY *glMultiTexImage1DEXTPROC) (GLenum texunit, GLenum target, GLint level, GLint internalformat, GLsizei width, GLint border, GLenum format, GLenum type, const GLvoid * pixels);
typedef void (APIENTRY *glMultiTexImage2DEXTPROC) (GLenum texunit, GLenum target, GLint level, GLint internalformat, GLsizei width, GLsizei height, GLint border, GLenum format, GLenum type, const GLvoid * pixels);
typedef void (APIENTRY *glMultiTexSubImage1DEXTPROC) (GLenum texunit, GLenum target, GLint level, GLint xoffset, GLsizei width, GLenum format, GLenum type, const GLvoid * pixels);
typedef void (APIENTRY *glMultiTexSubImage2DEXTPROC) (GLenum texunit, GLenum target, GLint level, GLint xoffset, GLint yoffset, GLsizei width, GLsizei height, GLenum format, GLenum type, const GLvoid * pixels);
typedef void (APIENTRY *glCopyMultiTexImage1DEXTPROC) (GLenum texunit, GLenum target, GLint level, GLenum internalformat, GLint x, GLint y, GLsizei width, GLint border);
typedef void (APIENTRY *glCopyMultiTexImage2DEXTPROC) (GLenum texunit, GLenum target, GLint level, GLenum internalformat, GLint x, GLint y, GLsizei width, GLsizei height, GLint border);
typedef void (APIENTRY *glCopyMultiTexSubImage1DEXTPROC) (GLenum texunit, GLenum target, GLint level, GLint xoffset, GLint x, GLint y, GLsizei width);
typedef void (APIENTRY *glCopyMultiTexSubImage2DEXTPROC) (GLenum texunit, GLenum target, GLint level, GLint xoffset, GLint yoffset, GLint x, GLint y, GLsizei width, GLsizei height);
typedef void (APIENTRY *glGetMultiTexImageEXTPROC) (GLenum texunit, GLenum target, GLint level, GLenum format, GLenum type, GLvoid * pixels);
typedef void (APIENTRY *glGetMultiTexParameterfvEXTPROC) (GLenum texunit, GLenum target, GLenum pname, GLfloat * params);
typedef void (APIENTRY *glGetMultiTexParameterivEXTPROC) (GLenum texunit, GLenum target, GLenum pname, GLint * params);
typedef void (APIENTRY *glGetMultiTexLevelParameterfvEXTPROC) (GLenum texunit, GLenum target, GLint level, GLenum pname, GLfloat * params);
typedef void (APIENTRY *glGetMultiTexLevelParameterivEXTPROC) (GLenum texunit, GLenum target, GLint level, GLenum pname, GLint * params);
typedef void (APIENTRY *glMultiTexImage3DEXTPROC) (GLenum texunit, GLenum target, GLint level, GLint internalformat, GLsizei width, GLsizei height, GLsizei depth, GLint border, GLenum format, GLenum type, const GLvoid * pixels);
typedef void (APIENTRY *glMultiTexSubImage3DEXTPROC) (GLenum texunit, GLenum target, GLint level, GLint xoffset, GLint yoffset, GLint zoffset, GLsizei width, GLsizei height, GLsizei depth, GLenum format, GLenum type, const GLvoid * pixels);
typedef void (APIENTRY *glCopyMultiTexSubImage3DEXTPROC) (GLenum texunit, GLenum target, GLint level, GLint xoffset, GLint yoffset, GLint zoffset, GLint x, GLint y, GLsizei width, GLsizei height);
typedef void (APIENTRY *glEnableClientStateIndexedEXTPROC) (GLenum array, GLuint index);
typedef void (APIENTRY *glDisableClientStateIndexedEXTPROC) (GLenum array, GLuint index);
typedef void (APIENTRY *glGetFloatIndexedvEXTPROC) (GLenum pname, GLuint index, GLfloat * params);
typedef void (APIENTRY *glGetDoubleIndexedvEXTPROC) (GLenum pname, GLuint index, GLdouble * params);
typedef void (APIENTRY *glGetPointerIndexedvEXTPROC) (GLenum pname, GLuint index, GLvoid ** params);
typedef void (APIENTRY *glEnableIndexedEXTPROC) (GLenum cap, GLuint index);
typedef void (APIENTRY *glDisableIndexedEXTPROC) (GLenum cap, GLuint index);
typedef GLboolean (APIENTRY *glIsEnabledIndexedEXTPROC) (GLenum cap, GLuint index);
typedef void (APIENTRY *glGetIntegerIndexedvEXTPROC) (GLenum pname, GLuint index, GLint * params);
typedef void (APIENTRY *glGetBooleanIndexedvEXTPROC) (GLenum pname, GLuint index, GLboolean * params);
typedef void (APIENTRY *glNamedProgramStringEXTPROC) (GLuint program, GLenum target, GLenum format, GLsizei len, const GLvoid * string);
typedef void (APIENTRY *glNamedProgramLocalParameter4dEXTPROC) (GLuint program, GLenum target, GLuint index, GLdouble x, GLdouble y, GLdouble z, GLdouble w);
typedef void (APIENTRY *glNamedProgramLocalParameter4dvEXTPROC) (GLuint program, GLenum target, GLuint index, const GLdouble * params);
typedef void (APIENTRY *glNamedProgramLocalParameter4fEXTPROC) (GLuint program, GLenum target, GLuint index, GLfloat x, GLfloat y, GLfloat z, GLfloat w);
typedef void (APIENTRY *glNamedProgramLocalParameter4fvEXTPROC) (GLuint program, GLenum target, GLuint index, const GLfloat * params);
typedef void (APIENTRY *glGetNamedProgramLocalParameterdvEXTPROC) (GLuint program, GLenum target, GLuint index, GLdouble * params);
typedef void (APIENTRY *glGetNamedProgramLocalParameterfvEXTPROC) (GLuint program, GLenum target, GLuint index, GLfloat * params);
typedef void (APIENTRY *glGetNamedProgramivEXTPROC) (GLuint program, GLenum target, GLenum pname, GLint * params);
typedef void (APIENTRY *glGetNamedProgramStringEXTPROC) (GLuint program, GLenum target, GLenum pname, GLvoid * string);
typedef void (APIENTRY *glCompressedTextureImage3DEXTPROC) (GLuint texture, GLenum target, GLint level, GLenum internalformat, GLsizei width, GLsizei height, GLsizei depth, GLint border, GLsizei imageSize, const GLvoid * data);
typedef void (APIENTRY *glCompressedTextureImage2DEXTPROC) (GLuint texture, GLenum target, GLint level, GLenum internalformat, GLsizei width, GLsizei height, GLint border, GLsizei imageSize, const GLvoid * data);
typedef void (APIENTRY *glCompressedTextureImage1DEXTPROC) (GLuint texture, GLenum target, GLint level, GLenum internalformat, GLsizei width, GLint border, GLsizei imageSize, const GLvoid * data);
typedef void (APIENTRY *glCompressedTextureSubImage3DEXTPROC) (GLuint texture, GLenum target, GLint level, GLint xoffset, GLint yoffset, GLint zoffset, GLsizei width, GLsizei height, GLsizei depth, GLenum format, GLsizei imageSize, const GLvoid * data);
typedef void (APIENTRY *glCompressedTextureSubImage2DEXTPROC) (GLuint texture, GLenum target, GLint level, GLint xoffset, GLint yoffset, GLsizei width, GLsizei height, GLenum format, GLsizei imageSize, const GLvoid * data);
typedef void (APIENTRY *glCompressedTextureSubImage1DEXTPROC) (GLuint texture, GLenum target, GLint level, GLint xoffset, GLsizei width, GLenum format, GLsizei imageSize, const GLvoid * data);
typedef void (APIENTRY *glGetCompressedTextureImageEXTPROC) (GLuint texture, GLenum target, GLint level, GLvoid * img);
typedef void (APIENTRY *glCompressedMultiTexImage3DEXTPROC) (GLenum texunit, GLenum target, GLint level, GLenum internalformat, GLsizei width, GLsizei height, GLsizei depth, GLint border, GLsizei imageSize, const GLvoid * data);
typedef void (APIENTRY *glCompressedMultiTexImage2DEXTPROC) (GLenum texunit, GLenum target, GLint level, GLenum internalformat, GLsizei width, GLsizei height, GLint border, GLsizei imageSize, const GLvoid * data);
typedef void (APIENTRY *glCompressedMultiTexImage1DEXTPROC) (GLenum texunit, GLenum target, GLint level, GLenum internalformat, GLsizei width, GLint border, GLsizei imageSize, const GLvoid * data);
typedef void (APIENTRY *glCompressedMultiTexSubImage3DEXTPROC) (GLenum texunit, GLenum target, GLint level, GLint xoffset, GLint yoffset, GLint zoffset, GLsizei width, GLsizei height, GLsizei depth, GLenum format, GLsizei imageSize, const GLvoid * data);
typedef void (APIENTRY *glCompressedMultiTexSubImage2DEXTPROC) (GLenum texunit, GLenum target, GLint level, GLint xoffset, GLint yoffset, GLsizei width, GLsizei height, GLenum format, GLsizei imageSize, const GLvoid * data);
typedef void (APIENTRY *glCompressedMultiTexSubImage1DEXTPROC) (GLenum texunit, GLenum target, GLint level, GLint xoffset, GLsizei width, GLenum format, GLsizei imageSize, const GLvoid * data);
typedef void (APIENTRY *glGetCompressedMultiTexImageEXTPROC) (GLenum texunit, GLenum target, GLint level, GLvoid * img);
typedef void (APIENTRY *glGetCompressedTexImagePROC) (GLenum target, GLint lod, GLvoid * img);
typedef void (APIENTRY *glMatrixLoadTransposefEXTPROC) (GLenum matrixMode, const GLfloat * m);
typedef void (APIENTRY *glMatrixLoadTransposedEXTPROC) (GLenum matrixMode, const GLdouble * m);
typedef void (APIENTRY *glMatrixMultTransposefEXTPROC) (GLenum matrixMode, const GLfloat * m);
typedef void (APIENTRY *glMatrixMultTransposedEXTPROC) (GLenum matrixMode, const GLdouble * m);
typedef void (APIENTRY *glNamedBufferDataEXTPROC) (GLuint buffer, GLsizeiptr size, const GLvoid * data, GLenum usage);
typedef void (APIENTRY *glNamedBufferSubDataEXTPROC) (GLuint buffer, GLintptr offset, GLsizeiptr size, const GLvoid * data);
typedef GLvoid * (APIENTRY *glMapNamedBufferEXTPROC) (GLuint buffer, GLenum access);
typedef GLboolean (APIENTRY *glUnmapNamedBufferEXTPROC) (GLuint buffer);
typedef void (APIENTRY *glGetNamedBufferParameterivEXTPROC) (GLuint buffer, GLenum pname, GLint * params);
typedef void (APIENTRY *glGetNamedBufferPointervEXTPROC) (GLuint buffer, GLenum pname, GLvoid ** params);
typedef void (APIENTRY *glGetNamedBufferSubDataEXTPROC) (GLuint buffer, GLintptr offset, GLsizeiptr size, GLvoid * data);
typedef void (APIENTRY *glProgramUniform1fEXTPROC) (GLuint program, GLint location, GLfloat v0);
typedef void (APIENTRY *glProgramUniform2fEXTPROC) (GLuint program, GLint location, GLfloat v0, GLfloat v1);
typedef void (APIENTRY *glProgramUniform3fEXTPROC) (GLuint program, GLint location, GLfloat v0, GLfloat v1, GLfloat v2);
typedef void (APIENTRY *glProgramUniform4fEXTPROC) (GLuint program, GLint location, GLfloat v0, GLfloat v1, GLfloat v2, GLfloat v3);
typedef void (APIENTRY *glProgramUniform1iEXTPROC) (GLuint program, GLint location, GLint v0);
typedef void (APIENTRY *glProgramUniform2iEXTPROC) (GLuint program, GLint location, GLint v0, GLint v1);
typedef void (APIENTRY *glProgramUniform3iEXTPROC) (GLuint program, GLint location, GLint v0, GLint v1, GLint v2);
typedef void (APIENTRY *glProgramUniform4iEXTPROC) (GLuint program, GLint location, GLint v0, GLint v1, GLint v2, GLint v3);
typedef void (APIENTRY *glProgramUniform1fvEXTPROC) (GLuint program, GLint location, GLsizei count, const GLfloat * value);
typedef void (APIENTRY *glProgramUniform2fvEXTPROC) (GLuint program, GLint location, GLsizei count, const GLfloat * value);
typedef void (APIENTRY *glProgramUniform3fvEXTPROC) (GLuint program, GLint location, GLsizei count, const GLfloat * value);
typedef void (APIENTRY *glProgramUniform4fvEXTPROC) (GLuint program, GLint location, GLsizei count, const GLfloat * value);
typedef void (APIENTRY *glProgramUniform1ivEXTPROC) (GLuint program, GLint location, GLsizei count, const GLint * value);
typedef void (APIENTRY *glProgramUniform2ivEXTPROC) (GLuint program, GLint location, GLsizei count, const GLint * value);
typedef void (APIENTRY *glProgramUniform3ivEXTPROC) (GLuint program, GLint location, GLsizei count, const GLint * value);
typedef void (APIENTRY *glProgramUniform4ivEXTPROC) (GLuint program, GLint location, GLsizei count, const GLint * value);
typedef void (APIENTRY *glProgramUniformMatrix2fvEXTPROC) (GLuint program, GLint location, GLsizei count, GLboolean transpose, const GLfloat * value);
typedef void (APIENTRY *glProgramUniformMatrix3fvEXTPROC) (GLuint program, GLint location, GLsizei count, GLboolean transpose, const GLfloat * value);
typedef void (APIENTRY *glProgramUniformMatrix4fvEXTPROC) (GLuint program, GLint location, GLsizei count, GLboolean transpose, const GLfloat * value);
typedef void (APIENTRY *glProgramUniformMatrix2x3fvEXTPROC) (GLuint program, GLint location, GLsizei count, GLboolean transpose, const GLfloat * value);
typedef void (APIENTRY *glProgramUniformMatrix3x2fvEXTPROC) (GLuint program, GLint location, GLsizei count, GLboolean transpose, const GLfloat * value);
typedef void (APIENTRY *glProgramUniformMatrix2x4fvEXTPROC) (GLuint program, GLint location, GLsizei count, GLboolean transpose, const GLfloat * value);
typedef void (APIENTRY *glProgramUniformMatrix4x2fvEXTPROC) (GLuint program, GLint location, GLsizei count, GLboolean transpose, const GLfloat * value);
typedef void (APIENTRY *glProgramUniformMatrix3x4fvEXTPROC) (GLuint program, GLint location, GLsizei count, GLboolean transpose, const GLfloat * value);
typedef void (APIENTRY *glProgramUniformMatrix4x3fvEXTPROC) (GLuint program, GLint location, GLsizei count, GLboolean transpose, const GLfloat * value);
typedef void (APIENTRY *glTextureBufferEXTPROC) (GLuint texture, GLenum target, GLenum internalformat, GLuint buffer);
typedef void (APIENTRY *glMultiTexBufferEXTPROC) (GLenum texunit, GLenum target, GLenum internalformat, GLuint buffer);
typedef void (APIENTRY *glTextureParameterIivEXTPROC) (GLuint texture, GLenum target, GLenum pname, const GLint * params);
typedef void (APIENTRY *glTextureParameterIuivEXTPROC) (GLuint texture, GLenum target, GLenum pname, const GLuint * params);
typedef void (APIENTRY *glGetTextureParameterIivEXTPROC) (GLuint texture, GLenum target, GLenum pname, GLint * params);
typedef void (APIENTRY *glGetTextureParameterIuivEXTPROC) (GLuint texture, GLenum target, GLenum pname, GLuint * params);
typedef void (APIENTRY *glMultiTexParameterIivEXTPROC) (GLenum texunit, GLenum target, GLenum pname, const GLint * params);
typedef void (APIENTRY *glMultiTexParameterIuivEXTPROC) (GLenum texunit, GLenum target, GLenum pname, const GLuint * params);
typedef void (APIENTRY *glGetMultiTexParameterIivEXTPROC) (GLenum texunit, GLenum target, GLenum pname, GLint * params);
typedef void (APIENTRY *glGetMultiTexParameterIuivEXTPROC) (GLenum texunit, GLenum target, GLenum pname, GLuint * params);
typedef void (APIENTRY *glProgramUniform1uiEXTPROC) (GLuint program, GLint location, GLuint v0);
typedef void (APIENTRY *glProgramUniform2uiEXTPROC) (GLuint program, GLint location, GLuint v0, GLuint v1);
typedef void (APIENTRY *glProgramUniform3uiEXTPROC) (GLuint program, GLint location, GLuint v0, GLuint v1, GLuint v2);
typedef void (APIENTRY *glProgramUniform4uiEXTPROC) (GLuint program, GLint location, GLuint v0, GLuint v1, GLuint v2, GLuint v3);
typedef void (APIENTRY *glProgramUniform1uivEXTPROC) (GLuint program, GLint location, GLsizei count, const GLuint * value);
typedef void (APIENTRY *glProgramUniform2uivEXTPROC) (GLuint program, GLint location, GLsizei count, const GLuint * value);
typedef void (APIENTRY *glProgramUniform3uivEXTPROC) (GLuint program, GLint location, GLsizei count, const GLuint * value);
typedef void (APIENTRY *glProgramUniform4uivEXTPROC) (GLuint program, GLint location, GLsizei count, const GLuint * value);
typedef void (APIENTRY *glNamedProgramLocalParameters4fvEXTPROC) (GLuint program, GLenum target, GLuint index, GLsizei count, const GLfloat * params);
typedef void (APIENTRY *glNamedProgramLocalParameterI4iEXTPROC) (GLuint program, GLenum target, GLuint index, GLint x, GLint y, GLint z, GLint w);
typedef void (APIENTRY *glNamedProgramLocalParameterI4ivEXTPROC) (GLuint program, GLenum target, GLuint index, const GLint * params);
typedef void (APIENTRY *glNamedProgramLocalParametersI4ivEXTPROC) (GLuint program, GLenum target, GLuint index, GLsizei count, const GLint * params);
typedef void (APIENTRY *glNamedProgramLocalParameterI4uiEXTPROC) (GLuint program, GLenum target, GLuint index, GLuint x, GLuint y, GLuint z, GLuint w);
typedef void (APIENTRY *glNamedProgramLocalParameterI4uivEXTPROC) (GLuint program, GLenum target, GLuint index, const GLuint * params);
typedef void (APIENTRY *glNamedProgramLocalParametersI4uivEXTPROC) (GLuint program, GLenum target, GLuint index, GLsizei count, const GLuint * params);
typedef void (APIENTRY *glGetNamedProgramLocalParameterIivEXTPROC) (GLuint program, GLenum target, GLuint index, GLint * params);
typedef void (APIENTRY *glGetNamedProgramLocalParameterIuivEXTPROC) (GLuint program, GLenum target, GLuint index, GLuint * params);
typedef void (APIENTRY *glNamedRenderbufferStorageEXTPROC) (GLuint renderbuffer, GLenum internalformat, GLsizei width, GLsizei height);
typedef void (APIENTRY *glGetNamedRenderbufferParameterivEXTPROC) (GLuint renderbuffer, GLenum pname, GLint * params);
typedef void (APIENTRY *glNamedRenderbufferStorageMultisampleEXTPROC) (GLuint renderbuffer, GLsizei samples, GLenum internalformat, GLsizei width, GLsizei height);
typedef void (APIENTRY *glNamedRenderbufferStorageMultisampleCoverageEXTPROC) (GLuint renderbuffer, GLsizei coverageSamples, GLsizei colorSamples, GLenum internalformat, GLsizei width, GLsizei height);
typedef GLenum (APIENTRY *glCheckNamedFramebufferStatusEXTPROC) (GLuint framebuffer, GLenum target);
typedef void (APIENTRY *glNamedFramebufferTexture1DEXTPROC) (GLuint framebuffer, GLenum attachment, GLenum textarget, GLuint texture, GLint level);
typedef void (APIENTRY *glNamedFramebufferTexture2DEXTPROC) (GLuint framebuffer, GLenum attachment, GLenum textarget, GLuint texture, GLint level);
typedef void (APIENTRY *glNamedFramebufferTexture3DEXTPROC) (GLuint framebuffer, GLenum attachment, GLenum textarget, GLuint texture, GLint level, GLint zoffset);
typedef void (APIENTRY *glNamedFramebufferRenderbufferEXTPROC) (GLuint framebuffer, GLenum attachment, GLenum renderbuffertarget, GLuint renderbuffer);
typedef void (APIENTRY *glGetNamedFramebufferAttachmentParameterivEXTPROC) (GLuint framebuffer, GLenum attachment, GLenum pname, GLint * params);
typedef void (APIENTRY *glGenerateTextureMipmapEXTPROC) (GLuint texture, GLenum target);
typedef void (APIENTRY *glGenerateMultiTexMipmapEXTPROC) (GLenum texunit, GLenum target);
typedef void (APIENTRY *glFramebufferDrawBufferEXTPROC) (GLuint framebuffer, GLenum mode);
typedef void (APIENTRY *glFramebufferDrawBuffersEXTPROC) (GLuint framebuffer, GLsizei n, const GLenum * bufs);
typedef void (APIENTRY *glFramebufferReadBufferEXTPROC) (GLuint framebuffer, GLenum mode);
typedef void (APIENTRY *glGetFramebufferParameterivEXTPROC) (GLuint framebuffer, GLenum pname, GLint * param);
typedef void (APIENTRY *glNamedFramebufferTextureEXTPROC) (GLuint framebuffer, GLenum attachment, GLuint texture, GLint level);
typedef void (APIENTRY *glNamedFramebufferTextureLayerEXTPROC) (GLuint framebuffer, GLenum attachment, GLuint texture, GLint level, GLint layer);
typedef void (APIENTRY *glNamedFramebufferTextureFaceEXTPROC) (GLuint framebuffer, GLenum attachment, GLuint texture, GLint level, GLenum face);
typedef void (APIENTRY *glTextureRenderbufferEXTPROC) (GLuint texture, GLenum target, GLuint renderbuffer);
typedef void (APIENTRY *glMultiTexRenderbufferEXTPROC) (GLenum texunit, GLenum target, GLuint renderbuffer);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglClientAttribDefaultEXT(JNIEnv *env, jclass clazz, jint mask, jlong function_pointer) {
	glClientAttribDefaultEXTPROC glClientAttribDefaultEXT = (glClientAttribDefaultEXTPROC)((intptr_t)function_pointer);
	glClientAttribDefaultEXT(mask);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglPushClientAttribDefaultEXT(JNIEnv *env, jclass clazz, jint mask, jlong function_pointer) {
	glPushClientAttribDefaultEXTPROC glPushClientAttribDefaultEXT = (glPushClientAttribDefaultEXTPROC)((intptr_t)function_pointer);
	glPushClientAttribDefaultEXT(mask);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglMatrixLoadfEXT(JNIEnv *env, jclass clazz, jint matrixMode, jobject m, jint m_position, jlong function_pointer) {
	const GLfloat *m_address = ((const GLfloat *)(*env)->GetDirectBufferAddress(env, m)) + m_position;
	glMatrixLoadfEXTPROC glMatrixLoadfEXT = (glMatrixLoadfEXTPROC)((intptr_t)function_pointer);
	glMatrixLoadfEXT(matrixMode, m_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglMatrixLoaddEXT(JNIEnv *env, jclass clazz, jint matrixMode, jobject m, jint m_position, jlong function_pointer) {
	const GLdouble *m_address = ((const GLdouble *)(*env)->GetDirectBufferAddress(env, m)) + m_position;
	glMatrixLoaddEXTPROC glMatrixLoaddEXT = (glMatrixLoaddEXTPROC)((intptr_t)function_pointer);
	glMatrixLoaddEXT(matrixMode, m_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglMatrixMultfEXT(JNIEnv *env, jclass clazz, jint matrixMode, jobject m, jint m_position, jlong function_pointer) {
	const GLfloat *m_address = ((const GLfloat *)(*env)->GetDirectBufferAddress(env, m)) + m_position;
	glMatrixMultfEXTPROC glMatrixMultfEXT = (glMatrixMultfEXTPROC)((intptr_t)function_pointer);
	glMatrixMultfEXT(matrixMode, m_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglMatrixMultdEXT(JNIEnv *env, jclass clazz, jint matrixMode, jobject m, jint m_position, jlong function_pointer) {
	const GLdouble *m_address = ((const GLdouble *)(*env)->GetDirectBufferAddress(env, m)) + m_position;
	glMatrixMultdEXTPROC glMatrixMultdEXT = (glMatrixMultdEXTPROC)((intptr_t)function_pointer);
	glMatrixMultdEXT(matrixMode, m_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglMatrixLoadIdentityEXT(JNIEnv *env, jclass clazz, jint matrixMode, jlong function_pointer) {
	glMatrixLoadIdentityEXTPROC glMatrixLoadIdentityEXT = (glMatrixLoadIdentityEXTPROC)((intptr_t)function_pointer);
	glMatrixLoadIdentityEXT(matrixMode);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglMatrixRotatefEXT(JNIEnv *env, jclass clazz, jint matrixMode, jfloat angle, jfloat x, jfloat y, jfloat z, jlong function_pointer) {
	glMatrixRotatefEXTPROC glMatrixRotatefEXT = (glMatrixRotatefEXTPROC)((intptr_t)function_pointer);
	glMatrixRotatefEXT(matrixMode, angle, x, y, z);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglMatrixRotatedEXT(JNIEnv *env, jclass clazz, jint matrixMode, jdouble angle, jdouble x, jdouble y, jdouble z, jlong function_pointer) {
	glMatrixRotatedEXTPROC glMatrixRotatedEXT = (glMatrixRotatedEXTPROC)((intptr_t)function_pointer);
	glMatrixRotatedEXT(matrixMode, angle, x, y, z);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglMatrixScalefEXT(JNIEnv *env, jclass clazz, jint matrixMode, jfloat x, jfloat y, jfloat z, jlong function_pointer) {
	glMatrixScalefEXTPROC glMatrixScalefEXT = (glMatrixScalefEXTPROC)((intptr_t)function_pointer);
	glMatrixScalefEXT(matrixMode, x, y, z);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglMatrixScaledEXT(JNIEnv *env, jclass clazz, jint matrixMode, jdouble x, jdouble y, jdouble z, jlong function_pointer) {
	glMatrixScaledEXTPROC glMatrixScaledEXT = (glMatrixScaledEXTPROC)((intptr_t)function_pointer);
	glMatrixScaledEXT(matrixMode, x, y, z);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglMatrixTranslatefEXT(JNIEnv *env, jclass clazz, jint matrixMode, jfloat x, jfloat y, jfloat z, jlong function_pointer) {
	glMatrixTranslatefEXTPROC glMatrixTranslatefEXT = (glMatrixTranslatefEXTPROC)((intptr_t)function_pointer);
	glMatrixTranslatefEXT(matrixMode, x, y, z);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglMatrixTranslatedEXT(JNIEnv *env, jclass clazz, jint matrixMode, jdouble x, jdouble y, jdouble z, jlong function_pointer) {
	glMatrixTranslatedEXTPROC glMatrixTranslatedEXT = (glMatrixTranslatedEXTPROC)((intptr_t)function_pointer);
	glMatrixTranslatedEXT(matrixMode, x, y, z);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglMatrixOrthoEXT(JNIEnv *env, jclass clazz, jint matrixMode, jdouble l, jdouble r, jdouble b, jdouble t, jdouble n, jdouble f, jlong function_pointer) {
	glMatrixOrthoEXTPROC glMatrixOrthoEXT = (glMatrixOrthoEXTPROC)((intptr_t)function_pointer);
	glMatrixOrthoEXT(matrixMode, l, r, b, t, n, f);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglMatrixFrustumEXT(JNIEnv *env, jclass clazz, jint matrixMode, jdouble l, jdouble r, jdouble b, jdouble t, jdouble n, jdouble f, jlong function_pointer) {
	glMatrixFrustumEXTPROC glMatrixFrustumEXT = (glMatrixFrustumEXTPROC)((intptr_t)function_pointer);
	glMatrixFrustumEXT(matrixMode, l, r, b, t, n, f);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglMatrixPushEXT(JNIEnv *env, jclass clazz, jint matrixMode, jlong function_pointer) {
	glMatrixPushEXTPROC glMatrixPushEXT = (glMatrixPushEXTPROC)((intptr_t)function_pointer);
	glMatrixPushEXT(matrixMode);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglMatrixPopEXT(JNIEnv *env, jclass clazz, jint matrixMode, jlong function_pointer) {
	glMatrixPopEXTPROC glMatrixPopEXT = (glMatrixPopEXTPROC)((intptr_t)function_pointer);
	glMatrixPopEXT(matrixMode);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglTextureParameteriEXT(JNIEnv *env, jclass clazz, jint texture, jint target, jint pname, jint param, jlong function_pointer) {
	glTextureParameteriEXTPROC glTextureParameteriEXT = (glTextureParameteriEXTPROC)((intptr_t)function_pointer);
	glTextureParameteriEXT(texture, target, pname, param);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglTextureParameterivEXT(JNIEnv *env, jclass clazz, jint texture, jint target, jint pname, jobject param, jint param_position, jlong function_pointer) {
	const GLint *param_address = ((const GLint *)(*env)->GetDirectBufferAddress(env, param)) + param_position;
	glTextureParameterivEXTPROC glTextureParameterivEXT = (glTextureParameterivEXTPROC)((intptr_t)function_pointer);
	glTextureParameterivEXT(texture, target, pname, param_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglTextureParameterfEXT(JNIEnv *env, jclass clazz, jint texture, jint target, jint pname, jfloat param, jlong function_pointer) {
	glTextureParameterfEXTPROC glTextureParameterfEXT = (glTextureParameterfEXTPROC)((intptr_t)function_pointer);
	glTextureParameterfEXT(texture, target, pname, param);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglTextureParameterfvEXT(JNIEnv *env, jclass clazz, jint texture, jint target, jint pname, jobject param, jint param_position, jlong function_pointer) {
	const GLfloat *param_address = ((const GLfloat *)(*env)->GetDirectBufferAddress(env, param)) + param_position;
	glTextureParameterfvEXTPROC glTextureParameterfvEXT = (glTextureParameterfvEXTPROC)((intptr_t)function_pointer);
	glTextureParameterfvEXT(texture, target, pname, param_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglTextureImage1DEXT(JNIEnv *env, jclass clazz, jint texture, jint target, jint level, jint internalformat, jint width, jint border, jint format, jint type, jobject pixels, jint pixels_position, jlong function_pointer) {
	const GLvoid *pixels_address = ((const GLvoid *)(((char *)safeGetBufferAddress(env, pixels)) + pixels_position));
	glTextureImage1DEXTPROC glTextureImage1DEXT = (glTextureImage1DEXTPROC)((intptr_t)function_pointer);
	glTextureImage1DEXT(texture, target, level, internalformat, width, border, format, type, pixels_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglTextureImage1DEXTBO(JNIEnv *env, jclass clazz, jint texture, jint target, jint level, jint internalformat, jint width, jint border, jint format, jint type, jlong pixels_buffer_offset, jlong function_pointer) {
	const GLvoid *pixels_address = ((const GLvoid *)offsetToPointer(pixels_buffer_offset));
	glTextureImage1DEXTPROC glTextureImage1DEXT = (glTextureImage1DEXTPROC)((intptr_t)function_pointer);
	glTextureImage1DEXT(texture, target, level, internalformat, width, border, format, type, pixels_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglTextureImage2DEXT(JNIEnv *env, jclass clazz, jint texture, jint target, jint level, jint internalformat, jint width, jint height, jint border, jint format, jint type, jobject pixels, jint pixels_position, jlong function_pointer) {
	const GLvoid *pixels_address = ((const GLvoid *)(((char *)safeGetBufferAddress(env, pixels)) + pixels_position));
	glTextureImage2DEXTPROC glTextureImage2DEXT = (glTextureImage2DEXTPROC)((intptr_t)function_pointer);
	glTextureImage2DEXT(texture, target, level, internalformat, width, height, border, format, type, pixels_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglTextureImage2DEXTBO(JNIEnv *env, jclass clazz, jint texture, jint target, jint level, jint internalformat, jint width, jint height, jint border, jint format, jint type, jlong pixels_buffer_offset, jlong function_pointer) {
	const GLvoid *pixels_address = ((const GLvoid *)offsetToPointer(pixels_buffer_offset));
	glTextureImage2DEXTPROC glTextureImage2DEXT = (glTextureImage2DEXTPROC)((intptr_t)function_pointer);
	glTextureImage2DEXT(texture, target, level, internalformat, width, height, border, format, type, pixels_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglTextureSubImage1DEXT(JNIEnv *env, jclass clazz, jint texture, jint target, jint level, jint xoffset, jint width, jint format, jint type, jobject pixels, jint pixels_position, jlong function_pointer) {
	const GLvoid *pixels_address = ((const GLvoid *)(((char *)(*env)->GetDirectBufferAddress(env, pixels)) + pixels_position));
	glTextureSubImage1DEXTPROC glTextureSubImage1DEXT = (glTextureSubImage1DEXTPROC)((intptr_t)function_pointer);
	glTextureSubImage1DEXT(texture, target, level, xoffset, width, format, type, pixels_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglTextureSubImage1DEXTBO(JNIEnv *env, jclass clazz, jint texture, jint target, jint level, jint xoffset, jint width, jint format, jint type, jlong pixels_buffer_offset, jlong function_pointer) {
	const GLvoid *pixels_address = ((const GLvoid *)offsetToPointer(pixels_buffer_offset));
	glTextureSubImage1DEXTPROC glTextureSubImage1DEXT = (glTextureSubImage1DEXTPROC)((intptr_t)function_pointer);
	glTextureSubImage1DEXT(texture, target, level, xoffset, width, format, type, pixels_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglTextureSubImage2DEXT(JNIEnv *env, jclass clazz, jint texture, jint target, jint level, jint xoffset, jint yoffset, jint width, jint height, jint format, jint type, jobject pixels, jint pixels_position, jlong function_pointer) {
	const GLvoid *pixels_address = ((const GLvoid *)(((char *)(*env)->GetDirectBufferAddress(env, pixels)) + pixels_position));
	glTextureSubImage2DEXTPROC glTextureSubImage2DEXT = (glTextureSubImage2DEXTPROC)((intptr_t)function_pointer);
	glTextureSubImage2DEXT(texture, target, level, xoffset, yoffset, width, height, format, type, pixels_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglTextureSubImage2DEXTBO(JNIEnv *env, jclass clazz, jint texture, jint target, jint level, jint xoffset, jint yoffset, jint width, jint height, jint format, jint type, jlong pixels_buffer_offset, jlong function_pointer) {
	const GLvoid *pixels_address = ((const GLvoid *)offsetToPointer(pixels_buffer_offset));
	glTextureSubImage2DEXTPROC glTextureSubImage2DEXT = (glTextureSubImage2DEXTPROC)((intptr_t)function_pointer);
	glTextureSubImage2DEXT(texture, target, level, xoffset, yoffset, width, height, format, type, pixels_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglCopyTextureImage1DEXT(JNIEnv *env, jclass clazz, jint texture, jint target, jint level, jint internalformat, jint x, jint y, jint width, jint border, jlong function_pointer) {
	glCopyTextureImage1DEXTPROC glCopyTextureImage1DEXT = (glCopyTextureImage1DEXTPROC)((intptr_t)function_pointer);
	glCopyTextureImage1DEXT(texture, target, level, internalformat, x, y, width, border);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglCopyTextureImage2DEXT(JNIEnv *env, jclass clazz, jint texture, jint target, jint level, jint internalformat, jint x, jint y, jint width, jint height, jint border, jlong function_pointer) {
	glCopyTextureImage2DEXTPROC glCopyTextureImage2DEXT = (glCopyTextureImage2DEXTPROC)((intptr_t)function_pointer);
	glCopyTextureImage2DEXT(texture, target, level, internalformat, x, y, width, height, border);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglCopyTextureSubImage1DEXT(JNIEnv *env, jclass clazz, jint texture, jint target, jint level, jint xoffset, jint x, jint y, jint width, jlong function_pointer) {
	glCopyTextureSubImage1DEXTPROC glCopyTextureSubImage1DEXT = (glCopyTextureSubImage1DEXTPROC)((intptr_t)function_pointer);
	glCopyTextureSubImage1DEXT(texture, target, level, xoffset, x, y, width);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglCopyTextureSubImage2DEXT(JNIEnv *env, jclass clazz, jint texture, jint target, jint level, jint xoffset, jint yoffset, jint x, jint y, jint width, jint height, jlong function_pointer) {
	glCopyTextureSubImage2DEXTPROC glCopyTextureSubImage2DEXT = (glCopyTextureSubImage2DEXTPROC)((intptr_t)function_pointer);
	glCopyTextureSubImage2DEXT(texture, target, level, xoffset, yoffset, x, y, width, height);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglGetTextureImageEXT(JNIEnv *env, jclass clazz, jint texture, jint target, jint level, jint format, jint type, jobject pixels, jint pixels_position, jlong function_pointer) {
	GLvoid *pixels_address = ((GLvoid *)(((char *)(*env)->GetDirectBufferAddress(env, pixels)) + pixels_position));
	glGetTextureImageEXTPROC glGetTextureImageEXT = (glGetTextureImageEXTPROC)((intptr_t)function_pointer);
	glGetTextureImageEXT(texture, target, level, format, type, pixels_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglGetTextureImageEXTBO(JNIEnv *env, jclass clazz, jint texture, jint target, jint level, jint format, jint type, jlong pixels_buffer_offset, jlong function_pointer) {
	GLvoid *pixels_address = ((GLvoid *)offsetToPointer(pixels_buffer_offset));
	glGetTextureImageEXTPROC glGetTextureImageEXT = (glGetTextureImageEXTPROC)((intptr_t)function_pointer);
	glGetTextureImageEXT(texture, target, level, format, type, pixels_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglGetTextureParameterfvEXT(JNIEnv *env, jclass clazz, jint texture, jint target, jint pname, jobject params, jint params_position, jlong function_pointer) {
	GLfloat *params_address = ((GLfloat *)(*env)->GetDirectBufferAddress(env, params)) + params_position;
	glGetTextureParameterfvEXTPROC glGetTextureParameterfvEXT = (glGetTextureParameterfvEXTPROC)((intptr_t)function_pointer);
	glGetTextureParameterfvEXT(texture, target, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglGetTextureParameterivEXT(JNIEnv *env, jclass clazz, jint texture, jint target, jint pname, jobject params, jint params_position, jlong function_pointer) {
	GLint *params_address = ((GLint *)(*env)->GetDirectBufferAddress(env, params)) + params_position;
	glGetTextureParameterivEXTPROC glGetTextureParameterivEXT = (glGetTextureParameterivEXTPROC)((intptr_t)function_pointer);
	glGetTextureParameterivEXT(texture, target, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglGetTextureLevelParameterfvEXT(JNIEnv *env, jclass clazz, jint texture, jint target, jint level, jint pname, jobject params, jint params_position, jlong function_pointer) {
	GLfloat *params_address = ((GLfloat *)(*env)->GetDirectBufferAddress(env, params)) + params_position;
	glGetTextureLevelParameterfvEXTPROC glGetTextureLevelParameterfvEXT = (glGetTextureLevelParameterfvEXTPROC)((intptr_t)function_pointer);
	glGetTextureLevelParameterfvEXT(texture, target, level, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglGetTextureLevelParameterivEXT(JNIEnv *env, jclass clazz, jint texture, jint target, jint level, jint pname, jobject params, jint params_position, jlong function_pointer) {
	GLint *params_address = ((GLint *)(*env)->GetDirectBufferAddress(env, params)) + params_position;
	glGetTextureLevelParameterivEXTPROC glGetTextureLevelParameterivEXT = (glGetTextureLevelParameterivEXTPROC)((intptr_t)function_pointer);
	glGetTextureLevelParameterivEXT(texture, target, level, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglTextureImage3DEXT(JNIEnv *env, jclass clazz, jint texture, jint target, jint level, jint internalformat, jint width, jint height, jint depth, jint border, jint format, jint type, jobject pixels, jint pixels_position, jlong function_pointer) {
	const GLvoid *pixels_address = ((const GLvoid *)(((char *)safeGetBufferAddress(env, pixels)) + pixels_position));
	glTextureImage3DEXTPROC glTextureImage3DEXT = (glTextureImage3DEXTPROC)((intptr_t)function_pointer);
	glTextureImage3DEXT(texture, target, level, internalformat, width, height, depth, border, format, type, pixels_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglTextureImage3DEXTBO(JNIEnv *env, jclass clazz, jint texture, jint target, jint level, jint internalformat, jint width, jint height, jint depth, jint border, jint format, jint type, jlong pixels_buffer_offset, jlong function_pointer) {
	const GLvoid *pixels_address = ((const GLvoid *)offsetToPointer(pixels_buffer_offset));
	glTextureImage3DEXTPROC glTextureImage3DEXT = (glTextureImage3DEXTPROC)((intptr_t)function_pointer);
	glTextureImage3DEXT(texture, target, level, internalformat, width, height, depth, border, format, type, pixels_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglTextureSubImage3DEXT(JNIEnv *env, jclass clazz, jint texture, jint target, jint level, jint xoffset, jint yoffset, jint zoffset, jint width, jint height, jint depth, jint format, jint type, jobject pixels, jint pixels_position, jlong function_pointer) {
	const GLvoid *pixels_address = ((const GLvoid *)(((char *)(*env)->GetDirectBufferAddress(env, pixels)) + pixels_position));
	glTextureSubImage3DEXTPROC glTextureSubImage3DEXT = (glTextureSubImage3DEXTPROC)((intptr_t)function_pointer);
	glTextureSubImage3DEXT(texture, target, level, xoffset, yoffset, zoffset, width, height, depth, format, type, pixels_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglTextureSubImage3DEXTBO(JNIEnv *env, jclass clazz, jint texture, jint target, jint level, jint xoffset, jint yoffset, jint zoffset, jint width, jint height, jint depth, jint format, jint type, jlong pixels_buffer_offset, jlong function_pointer) {
	const GLvoid *pixels_address = ((const GLvoid *)offsetToPointer(pixels_buffer_offset));
	glTextureSubImage3DEXTPROC glTextureSubImage3DEXT = (glTextureSubImage3DEXTPROC)((intptr_t)function_pointer);
	glTextureSubImage3DEXT(texture, target, level, xoffset, yoffset, zoffset, width, height, depth, format, type, pixels_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglCopyTextureSubImage3DEXT(JNIEnv *env, jclass clazz, jint texture, jint target, jint level, jint xoffset, jint yoffset, jint zoffset, jint x, jint y, jint width, jint height, jlong function_pointer) {
	glCopyTextureSubImage3DEXTPROC glCopyTextureSubImage3DEXT = (glCopyTextureSubImage3DEXTPROC)((intptr_t)function_pointer);
	glCopyTextureSubImage3DEXT(texture, target, level, xoffset, yoffset, zoffset, x, y, width, height);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglBindMultiTextureEXT(JNIEnv *env, jclass clazz, jint texunit, jint target, jint texture, jlong function_pointer) {
	glBindMultiTextureEXTPROC glBindMultiTextureEXT = (glBindMultiTextureEXTPROC)((intptr_t)function_pointer);
	glBindMultiTextureEXT(texunit, target, texture);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglMultiTexCoordPointerEXT(JNIEnv *env, jclass clazz, jint texunit, jint size, jint type, jint stride, jobject pointer, jint pointer_position, jlong function_pointer) {
	const GLvoid *pointer_address = ((const GLvoid *)(((char *)(*env)->GetDirectBufferAddress(env, pointer)) + pointer_position));
	glMultiTexCoordPointerEXTPROC glMultiTexCoordPointerEXT = (glMultiTexCoordPointerEXTPROC)((intptr_t)function_pointer);
	glMultiTexCoordPointerEXT(texunit, size, type, stride, pointer_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglMultiTexCoordPointerEXTBO(JNIEnv *env, jclass clazz, jint texunit, jint size, jint type, jint stride, jlong pointer_buffer_offset, jlong function_pointer) {
	const GLvoid *pointer_address = ((const GLvoid *)offsetToPointer(pointer_buffer_offset));
	glMultiTexCoordPointerEXTPROC glMultiTexCoordPointerEXT = (glMultiTexCoordPointerEXTPROC)((intptr_t)function_pointer);
	glMultiTexCoordPointerEXT(texunit, size, type, stride, pointer_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglMultiTexEnvfEXT(JNIEnv *env, jclass clazz, jint texunit, jint target, jint pname, jfloat param, jlong function_pointer) {
	glMultiTexEnvfEXTPROC glMultiTexEnvfEXT = (glMultiTexEnvfEXTPROC)((intptr_t)function_pointer);
	glMultiTexEnvfEXT(texunit, target, pname, param);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglMultiTexEnvfvEXT(JNIEnv *env, jclass clazz, jint texunit, jint target, jint pname, jobject params, jint params_position, jlong function_pointer) {
	const GLfloat *params_address = ((const GLfloat *)(*env)->GetDirectBufferAddress(env, params)) + params_position;
	glMultiTexEnvfvEXTPROC glMultiTexEnvfvEXT = (glMultiTexEnvfvEXTPROC)((intptr_t)function_pointer);
	glMultiTexEnvfvEXT(texunit, target, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglMultiTexEnviEXT(JNIEnv *env, jclass clazz, jint texunit, jint target, jint pname, jint param, jlong function_pointer) {
	glMultiTexEnviEXTPROC glMultiTexEnviEXT = (glMultiTexEnviEXTPROC)((intptr_t)function_pointer);
	glMultiTexEnviEXT(texunit, target, pname, param);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglMultiTexEnvivEXT(JNIEnv *env, jclass clazz, jint texunit, jint target, jint pname, jobject params, jint params_position, jlong function_pointer) {
	const GLint *params_address = ((const GLint *)(*env)->GetDirectBufferAddress(env, params)) + params_position;
	glMultiTexEnvivEXTPROC glMultiTexEnvivEXT = (glMultiTexEnvivEXTPROC)((intptr_t)function_pointer);
	glMultiTexEnvivEXT(texunit, target, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglMultiTexGendEXT(JNIEnv *env, jclass clazz, jint texunit, jint coord, jint pname, jdouble param, jlong function_pointer) {
	glMultiTexGendEXTPROC glMultiTexGendEXT = (glMultiTexGendEXTPROC)((intptr_t)function_pointer);
	glMultiTexGendEXT(texunit, coord, pname, param);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglMultiTexGendvEXT(JNIEnv *env, jclass clazz, jint texunit, jint coord, jint pname, jobject params, jint params_position, jlong function_pointer) {
	const GLdouble *params_address = ((const GLdouble *)(*env)->GetDirectBufferAddress(env, params)) + params_position;
	glMultiTexGendvEXTPROC glMultiTexGendvEXT = (glMultiTexGendvEXTPROC)((intptr_t)function_pointer);
	glMultiTexGendvEXT(texunit, coord, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglMultiTexGenfEXT(JNIEnv *env, jclass clazz, jint texunit, jint coord, jint pname, jfloat param, jlong function_pointer) {
	glMultiTexGenfEXTPROC glMultiTexGenfEXT = (glMultiTexGenfEXTPROC)((intptr_t)function_pointer);
	glMultiTexGenfEXT(texunit, coord, pname, param);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglMultiTexGenfvEXT(JNIEnv *env, jclass clazz, jint texunit, jint coord, jint pname, jobject params, jint params_position, jlong function_pointer) {
	const GLfloat *params_address = ((const GLfloat *)(*env)->GetDirectBufferAddress(env, params)) + params_position;
	glMultiTexGenfvEXTPROC glMultiTexGenfvEXT = (glMultiTexGenfvEXTPROC)((intptr_t)function_pointer);
	glMultiTexGenfvEXT(texunit, coord, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglMultiTexGeniEXT(JNIEnv *env, jclass clazz, jint texunit, jint coord, jint pname, jint param, jlong function_pointer) {
	glMultiTexGeniEXTPROC glMultiTexGeniEXT = (glMultiTexGeniEXTPROC)((intptr_t)function_pointer);
	glMultiTexGeniEXT(texunit, coord, pname, param);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglMultiTexGenivEXT(JNIEnv *env, jclass clazz, jint texunit, jint coord, jint pname, jobject params, jint params_position, jlong function_pointer) {
	const GLint *params_address = ((const GLint *)(*env)->GetDirectBufferAddress(env, params)) + params_position;
	glMultiTexGenivEXTPROC glMultiTexGenivEXT = (glMultiTexGenivEXTPROC)((intptr_t)function_pointer);
	glMultiTexGenivEXT(texunit, coord, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglGetMultiTexEnvfvEXT(JNIEnv *env, jclass clazz, jint texunit, jint target, jint pname, jobject params, jint params_position, jlong function_pointer) {
	GLfloat *params_address = ((GLfloat *)(*env)->GetDirectBufferAddress(env, params)) + params_position;
	glGetMultiTexEnvfvEXTPROC glGetMultiTexEnvfvEXT = (glGetMultiTexEnvfvEXTPROC)((intptr_t)function_pointer);
	glGetMultiTexEnvfvEXT(texunit, target, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglGetMultiTexEnvivEXT(JNIEnv *env, jclass clazz, jint texunit, jint target, jint pname, jobject params, jint params_position, jlong function_pointer) {
	GLint *params_address = ((GLint *)(*env)->GetDirectBufferAddress(env, params)) + params_position;
	glGetMultiTexEnvivEXTPROC glGetMultiTexEnvivEXT = (glGetMultiTexEnvivEXTPROC)((intptr_t)function_pointer);
	glGetMultiTexEnvivEXT(texunit, target, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglGetMultiTexGendvEXT(JNIEnv *env, jclass clazz, jint texunit, jint coord, jint pname, jobject params, jint params_position, jlong function_pointer) {
	GLdouble *params_address = ((GLdouble *)(*env)->GetDirectBufferAddress(env, params)) + params_position;
	glGetMultiTexGendvEXTPROC glGetMultiTexGendvEXT = (glGetMultiTexGendvEXTPROC)((intptr_t)function_pointer);
	glGetMultiTexGendvEXT(texunit, coord, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglGetMultiTexGenfvEXT(JNIEnv *env, jclass clazz, jint texunit, jint coord, jint pname, jobject params, jint params_position, jlong function_pointer) {
	GLfloat *params_address = ((GLfloat *)(*env)->GetDirectBufferAddress(env, params)) + params_position;
	glGetMultiTexGenfvEXTPROC glGetMultiTexGenfvEXT = (glGetMultiTexGenfvEXTPROC)((intptr_t)function_pointer);
	glGetMultiTexGenfvEXT(texunit, coord, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglGetMultiTexGenivEXT(JNIEnv *env, jclass clazz, jint texunit, jint coord, jint pname, jobject params, jint params_position, jlong function_pointer) {
	GLint *params_address = ((GLint *)(*env)->GetDirectBufferAddress(env, params)) + params_position;
	glGetMultiTexGenivEXTPROC glGetMultiTexGenivEXT = (glGetMultiTexGenivEXTPROC)((intptr_t)function_pointer);
	glGetMultiTexGenivEXT(texunit, coord, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglMultiTexParameteriEXT(JNIEnv *env, jclass clazz, jint texunit, jint target, jint pname, jint param, jlong function_pointer) {
	glMultiTexParameteriEXTPROC glMultiTexParameteriEXT = (glMultiTexParameteriEXTPROC)((intptr_t)function_pointer);
	glMultiTexParameteriEXT(texunit, target, pname, param);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglMultiTexParameterivEXT(JNIEnv *env, jclass clazz, jint texunit, jint target, jint pname, jobject param, jint param_position, jlong function_pointer) {
	const GLint *param_address = ((const GLint *)(*env)->GetDirectBufferAddress(env, param)) + param_position;
	glMultiTexParameterivEXTPROC glMultiTexParameterivEXT = (glMultiTexParameterivEXTPROC)((intptr_t)function_pointer);
	glMultiTexParameterivEXT(texunit, target, pname, param_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglMultiTexParameterfEXT(JNIEnv *env, jclass clazz, jint texunit, jint target, jint pname, jfloat param, jlong function_pointer) {
	glMultiTexParameterfEXTPROC glMultiTexParameterfEXT = (glMultiTexParameterfEXTPROC)((intptr_t)function_pointer);
	glMultiTexParameterfEXT(texunit, target, pname, param);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglMultiTexParameterfvEXT(JNIEnv *env, jclass clazz, jint texunit, jint target, jint pname, jobject param, jint param_position, jlong function_pointer) {
	const GLfloat *param_address = ((const GLfloat *)(*env)->GetDirectBufferAddress(env, param)) + param_position;
	glMultiTexParameterfvEXTPROC glMultiTexParameterfvEXT = (glMultiTexParameterfvEXTPROC)((intptr_t)function_pointer);
	glMultiTexParameterfvEXT(texunit, target, pname, param_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglMultiTexImage1DEXT(JNIEnv *env, jclass clazz, jint texunit, jint target, jint level, jint internalformat, jint width, jint border, jint format, jint type, jobject pixels, jint pixels_position, jlong function_pointer) {
	const GLvoid *pixels_address = ((const GLvoid *)(((char *)safeGetBufferAddress(env, pixels)) + pixels_position));
	glMultiTexImage1DEXTPROC glMultiTexImage1DEXT = (glMultiTexImage1DEXTPROC)((intptr_t)function_pointer);
	glMultiTexImage1DEXT(texunit, target, level, internalformat, width, border, format, type, pixels_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglMultiTexImage1DEXTBO(JNIEnv *env, jclass clazz, jint texunit, jint target, jint level, jint internalformat, jint width, jint border, jint format, jint type, jlong pixels_buffer_offset, jlong function_pointer) {
	const GLvoid *pixels_address = ((const GLvoid *)offsetToPointer(pixels_buffer_offset));
	glMultiTexImage1DEXTPROC glMultiTexImage1DEXT = (glMultiTexImage1DEXTPROC)((intptr_t)function_pointer);
	glMultiTexImage1DEXT(texunit, target, level, internalformat, width, border, format, type, pixels_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglMultiTexImage2DEXT(JNIEnv *env, jclass clazz, jint texunit, jint target, jint level, jint internalformat, jint width, jint height, jint border, jint format, jint type, jobject pixels, jint pixels_position, jlong function_pointer) {
	const GLvoid *pixels_address = ((const GLvoid *)(((char *)safeGetBufferAddress(env, pixels)) + pixels_position));
	glMultiTexImage2DEXTPROC glMultiTexImage2DEXT = (glMultiTexImage2DEXTPROC)((intptr_t)function_pointer);
	glMultiTexImage2DEXT(texunit, target, level, internalformat, width, height, border, format, type, pixels_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglMultiTexImage2DEXTBO(JNIEnv *env, jclass clazz, jint texunit, jint target, jint level, jint internalformat, jint width, jint height, jint border, jint format, jint type, jlong pixels_buffer_offset, jlong function_pointer) {
	const GLvoid *pixels_address = ((const GLvoid *)offsetToPointer(pixels_buffer_offset));
	glMultiTexImage2DEXTPROC glMultiTexImage2DEXT = (glMultiTexImage2DEXTPROC)((intptr_t)function_pointer);
	glMultiTexImage2DEXT(texunit, target, level, internalformat, width, height, border, format, type, pixels_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglMultiTexSubImage1DEXT(JNIEnv *env, jclass clazz, jint texunit, jint target, jint level, jint xoffset, jint width, jint format, jint type, jobject pixels, jint pixels_position, jlong function_pointer) {
	const GLvoid *pixels_address = ((const GLvoid *)(((char *)(*env)->GetDirectBufferAddress(env, pixels)) + pixels_position));
	glMultiTexSubImage1DEXTPROC glMultiTexSubImage1DEXT = (glMultiTexSubImage1DEXTPROC)((intptr_t)function_pointer);
	glMultiTexSubImage1DEXT(texunit, target, level, xoffset, width, format, type, pixels_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglMultiTexSubImage1DEXTBO(JNIEnv *env, jclass clazz, jint texunit, jint target, jint level, jint xoffset, jint width, jint format, jint type, jlong pixels_buffer_offset, jlong function_pointer) {
	const GLvoid *pixels_address = ((const GLvoid *)offsetToPointer(pixels_buffer_offset));
	glMultiTexSubImage1DEXTPROC glMultiTexSubImage1DEXT = (glMultiTexSubImage1DEXTPROC)((intptr_t)function_pointer);
	glMultiTexSubImage1DEXT(texunit, target, level, xoffset, width, format, type, pixels_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglMultiTexSubImage2DEXT(JNIEnv *env, jclass clazz, jint texunit, jint target, jint level, jint xoffset, jint yoffset, jint width, jint height, jint format, jint type, jobject pixels, jint pixels_position, jlong function_pointer) {
	const GLvoid *pixels_address = ((const GLvoid *)(((char *)(*env)->GetDirectBufferAddress(env, pixels)) + pixels_position));
	glMultiTexSubImage2DEXTPROC glMultiTexSubImage2DEXT = (glMultiTexSubImage2DEXTPROC)((intptr_t)function_pointer);
	glMultiTexSubImage2DEXT(texunit, target, level, xoffset, yoffset, width, height, format, type, pixels_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglMultiTexSubImage2DEXTBO(JNIEnv *env, jclass clazz, jint texunit, jint target, jint level, jint xoffset, jint yoffset, jint width, jint height, jint format, jint type, jlong pixels_buffer_offset, jlong function_pointer) {
	const GLvoid *pixels_address = ((const GLvoid *)offsetToPointer(pixels_buffer_offset));
	glMultiTexSubImage2DEXTPROC glMultiTexSubImage2DEXT = (glMultiTexSubImage2DEXTPROC)((intptr_t)function_pointer);
	glMultiTexSubImage2DEXT(texunit, target, level, xoffset, yoffset, width, height, format, type, pixels_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglCopyMultiTexImage1DEXT(JNIEnv *env, jclass clazz, jint texunit, jint target, jint level, jint internalformat, jint x, jint y, jint width, jint border, jlong function_pointer) {
	glCopyMultiTexImage1DEXTPROC glCopyMultiTexImage1DEXT = (glCopyMultiTexImage1DEXTPROC)((intptr_t)function_pointer);
	glCopyMultiTexImage1DEXT(texunit, target, level, internalformat, x, y, width, border);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglCopyMultiTexImage2DEXT(JNIEnv *env, jclass clazz, jint texunit, jint target, jint level, jint internalformat, jint x, jint y, jint width, jint height, jint border, jlong function_pointer) {
	glCopyMultiTexImage2DEXTPROC glCopyMultiTexImage2DEXT = (glCopyMultiTexImage2DEXTPROC)((intptr_t)function_pointer);
	glCopyMultiTexImage2DEXT(texunit, target, level, internalformat, x, y, width, height, border);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglCopyMultiTexSubImage1DEXT(JNIEnv *env, jclass clazz, jint texunit, jint target, jint level, jint xoffset, jint x, jint y, jint width, jlong function_pointer) {
	glCopyMultiTexSubImage1DEXTPROC glCopyMultiTexSubImage1DEXT = (glCopyMultiTexSubImage1DEXTPROC)((intptr_t)function_pointer);
	glCopyMultiTexSubImage1DEXT(texunit, target, level, xoffset, x, y, width);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglCopyMultiTexSubImage2DEXT(JNIEnv *env, jclass clazz, jint texunit, jint target, jint level, jint xoffset, jint yoffset, jint x, jint y, jint width, jint height, jlong function_pointer) {
	glCopyMultiTexSubImage2DEXTPROC glCopyMultiTexSubImage2DEXT = (glCopyMultiTexSubImage2DEXTPROC)((intptr_t)function_pointer);
	glCopyMultiTexSubImage2DEXT(texunit, target, level, xoffset, yoffset, x, y, width, height);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglGetMultiTexImageEXT(JNIEnv *env, jclass clazz, jint texunit, jint target, jint level, jint format, jint type, jobject pixels, jint pixels_position, jlong function_pointer) {
	GLvoid *pixels_address = ((GLvoid *)(((char *)(*env)->GetDirectBufferAddress(env, pixels)) + pixels_position));
	glGetMultiTexImageEXTPROC glGetMultiTexImageEXT = (glGetMultiTexImageEXTPROC)((intptr_t)function_pointer);
	glGetMultiTexImageEXT(texunit, target, level, format, type, pixels_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglGetMultiTexImageEXTBO(JNIEnv *env, jclass clazz, jint texunit, jint target, jint level, jint format, jint type, jlong pixels_buffer_offset, jlong function_pointer) {
	GLvoid *pixels_address = ((GLvoid *)offsetToPointer(pixels_buffer_offset));
	glGetMultiTexImageEXTPROC glGetMultiTexImageEXT = (glGetMultiTexImageEXTPROC)((intptr_t)function_pointer);
	glGetMultiTexImageEXT(texunit, target, level, format, type, pixels_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglGetMultiTexParameterfvEXT(JNIEnv *env, jclass clazz, jint texunit, jint target, jint pname, jobject params, jint params_position, jlong function_pointer) {
	GLfloat *params_address = ((GLfloat *)(*env)->GetDirectBufferAddress(env, params)) + params_position;
	glGetMultiTexParameterfvEXTPROC glGetMultiTexParameterfvEXT = (glGetMultiTexParameterfvEXTPROC)((intptr_t)function_pointer);
	glGetMultiTexParameterfvEXT(texunit, target, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglGetMultiTexParameterivEXT(JNIEnv *env, jclass clazz, jint texunit, jint target, jint pname, jobject params, jint params_position, jlong function_pointer) {
	GLint *params_address = ((GLint *)(*env)->GetDirectBufferAddress(env, params)) + params_position;
	glGetMultiTexParameterivEXTPROC glGetMultiTexParameterivEXT = (glGetMultiTexParameterivEXTPROC)((intptr_t)function_pointer);
	glGetMultiTexParameterivEXT(texunit, target, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglGetMultiTexLevelParameterfvEXT(JNIEnv *env, jclass clazz, jint texunit, jint target, jint level, jint pname, jobject params, jint params_position, jlong function_pointer) {
	GLfloat *params_address = ((GLfloat *)(*env)->GetDirectBufferAddress(env, params)) + params_position;
	glGetMultiTexLevelParameterfvEXTPROC glGetMultiTexLevelParameterfvEXT = (glGetMultiTexLevelParameterfvEXTPROC)((intptr_t)function_pointer);
	glGetMultiTexLevelParameterfvEXT(texunit, target, level, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglGetMultiTexLevelParameterivEXT(JNIEnv *env, jclass clazz, jint texunit, jint target, jint level, jint pname, jobject params, jint params_position, jlong function_pointer) {
	GLint *params_address = ((GLint *)(*env)->GetDirectBufferAddress(env, params)) + params_position;
	glGetMultiTexLevelParameterivEXTPROC glGetMultiTexLevelParameterivEXT = (glGetMultiTexLevelParameterivEXTPROC)((intptr_t)function_pointer);
	glGetMultiTexLevelParameterivEXT(texunit, target, level, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglMultiTexImage3DEXT(JNIEnv *env, jclass clazz, jint texunit, jint target, jint level, jint internalformat, jint width, jint height, jint depth, jint border, jint format, jint type, jobject pixels, jint pixels_position, jlong function_pointer) {
	const GLvoid *pixels_address = ((const GLvoid *)(((char *)safeGetBufferAddress(env, pixels)) + pixels_position));
	glMultiTexImage3DEXTPROC glMultiTexImage3DEXT = (glMultiTexImage3DEXTPROC)((intptr_t)function_pointer);
	glMultiTexImage3DEXT(texunit, target, level, internalformat, width, height, depth, border, format, type, pixels_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglMultiTexImage3DEXTBO(JNIEnv *env, jclass clazz, jint texunit, jint target, jint level, jint internalformat, jint width, jint height, jint depth, jint border, jint format, jint type, jlong pixels_buffer_offset, jlong function_pointer) {
	const GLvoid *pixels_address = ((const GLvoid *)offsetToPointer(pixels_buffer_offset));
	glMultiTexImage3DEXTPROC glMultiTexImage3DEXT = (glMultiTexImage3DEXTPROC)((intptr_t)function_pointer);
	glMultiTexImage3DEXT(texunit, target, level, internalformat, width, height, depth, border, format, type, pixels_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglMultiTexSubImage3DEXT(JNIEnv *env, jclass clazz, jint texunit, jint target, jint level, jint xoffset, jint yoffset, jint zoffset, jint width, jint height, jint depth, jint format, jint type, jobject pixels, jint pixels_position, jlong function_pointer) {
	const GLvoid *pixels_address = ((const GLvoid *)(((char *)(*env)->GetDirectBufferAddress(env, pixels)) + pixels_position));
	glMultiTexSubImage3DEXTPROC glMultiTexSubImage3DEXT = (glMultiTexSubImage3DEXTPROC)((intptr_t)function_pointer);
	glMultiTexSubImage3DEXT(texunit, target, level, xoffset, yoffset, zoffset, width, height, depth, format, type, pixels_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglMultiTexSubImage3DEXTBO(JNIEnv *env, jclass clazz, jint texunit, jint target, jint level, jint xoffset, jint yoffset, jint zoffset, jint width, jint height, jint depth, jint format, jint type, jlong pixels_buffer_offset, jlong function_pointer) {
	const GLvoid *pixels_address = ((const GLvoid *)offsetToPointer(pixels_buffer_offset));
	glMultiTexSubImage3DEXTPROC glMultiTexSubImage3DEXT = (glMultiTexSubImage3DEXTPROC)((intptr_t)function_pointer);
	glMultiTexSubImage3DEXT(texunit, target, level, xoffset, yoffset, zoffset, width, height, depth, format, type, pixels_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglCopyMultiTexSubImage3DEXT(JNIEnv *env, jclass clazz, jint texunit, jint target, jint level, jint xoffset, jint yoffset, jint zoffset, jint x, jint y, jint width, jint height, jlong function_pointer) {
	glCopyMultiTexSubImage3DEXTPROC glCopyMultiTexSubImage3DEXT = (glCopyMultiTexSubImage3DEXTPROC)((intptr_t)function_pointer);
	glCopyMultiTexSubImage3DEXT(texunit, target, level, xoffset, yoffset, zoffset, x, y, width, height);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglEnableClientStateIndexedEXT(JNIEnv *env, jclass clazz, jint array, jint index, jlong function_pointer) {
	glEnableClientStateIndexedEXTPROC glEnableClientStateIndexedEXT = (glEnableClientStateIndexedEXTPROC)((intptr_t)function_pointer);
	glEnableClientStateIndexedEXT(array, index);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglDisableClientStateIndexedEXT(JNIEnv *env, jclass clazz, jint array, jint index, jlong function_pointer) {
	glDisableClientStateIndexedEXTPROC glDisableClientStateIndexedEXT = (glDisableClientStateIndexedEXTPROC)((intptr_t)function_pointer);
	glDisableClientStateIndexedEXT(array, index);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglGetFloatIndexedvEXT(JNIEnv *env, jclass clazz, jint pname, jint index, jobject params, jint params_position, jlong function_pointer) {
	GLfloat *params_address = ((GLfloat *)(*env)->GetDirectBufferAddress(env, params)) + params_position;
	glGetFloatIndexedvEXTPROC glGetFloatIndexedvEXT = (glGetFloatIndexedvEXTPROC)((intptr_t)function_pointer);
	glGetFloatIndexedvEXT(pname, index, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglGetDoubleIndexedvEXT(JNIEnv *env, jclass clazz, jint pname, jint index, jobject params, jint params_position, jlong function_pointer) {
	GLdouble *params_address = ((GLdouble *)(*env)->GetDirectBufferAddress(env, params)) + params_position;
	glGetDoubleIndexedvEXTPROC glGetDoubleIndexedvEXT = (glGetDoubleIndexedvEXTPROC)((intptr_t)function_pointer);
	glGetDoubleIndexedvEXT(pname, index, params_address);
}

JNIEXPORT jobject JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglGetPointerIndexedvEXT(JNIEnv *env, jclass clazz, jint pname, jint index, jlong result_size, jlong function_pointer) {
	glGetPointerIndexedvEXTPROC glGetPointerIndexedvEXT = (glGetPointerIndexedvEXTPROC)((intptr_t)function_pointer);
	GLvoid * __result;
	glGetPointerIndexedvEXT(pname, index, &__result);
	return safeNewBuffer(env, __result, result_size);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglEnableIndexedEXT(JNIEnv *env, jclass clazz, jint cap, jint index, jlong function_pointer) {
	glEnableIndexedEXTPROC glEnableIndexedEXT = (glEnableIndexedEXTPROC)((intptr_t)function_pointer);
	glEnableIndexedEXT(cap, index);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglDisableIndexedEXT(JNIEnv *env, jclass clazz, jint cap, jint index, jlong function_pointer) {
	glDisableIndexedEXTPROC glDisableIndexedEXT = (glDisableIndexedEXTPROC)((intptr_t)function_pointer);
	glDisableIndexedEXT(cap, index);
}

JNIEXPORT jboolean JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglIsEnabledIndexedEXT(JNIEnv *env, jclass clazz, jint cap, jint index, jlong function_pointer) {
	glIsEnabledIndexedEXTPROC glIsEnabledIndexedEXT = (glIsEnabledIndexedEXTPROC)((intptr_t)function_pointer);
	GLboolean __result = glIsEnabledIndexedEXT(cap, index);
	return __result;
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglGetIntegerIndexedvEXT(JNIEnv *env, jclass clazz, jint pname, jint index, jobject params, jint params_position, jlong function_pointer) {
	GLint *params_address = ((GLint *)(*env)->GetDirectBufferAddress(env, params)) + params_position;
	glGetIntegerIndexedvEXTPROC glGetIntegerIndexedvEXT = (glGetIntegerIndexedvEXTPROC)((intptr_t)function_pointer);
	glGetIntegerIndexedvEXT(pname, index, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglGetBooleanIndexedvEXT(JNIEnv *env, jclass clazz, jint pname, jint index, jobject params, jint params_position, jlong function_pointer) {
	GLboolean *params_address = ((GLboolean *)(*env)->GetDirectBufferAddress(env, params)) + params_position;
	glGetBooleanIndexedvEXTPROC glGetBooleanIndexedvEXT = (glGetBooleanIndexedvEXTPROC)((intptr_t)function_pointer);
	glGetBooleanIndexedvEXT(pname, index, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglNamedProgramStringEXT(JNIEnv *env, jclass clazz, jint program, jint target, jint format, jint len, jobject string, jint string_position, jlong function_pointer) {
	const GLvoid *string_address = ((const GLvoid *)(((char *)(*env)->GetDirectBufferAddress(env, string)) + string_position));
	glNamedProgramStringEXTPROC glNamedProgramStringEXT = (glNamedProgramStringEXTPROC)((intptr_t)function_pointer);
	glNamedProgramStringEXT(program, target, format, len, string_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglNamedProgramLocalParameter4dEXT(JNIEnv *env, jclass clazz, jint program, jint target, jint index, jdouble x, jdouble y, jdouble z, jdouble w, jlong function_pointer) {
	glNamedProgramLocalParameter4dEXTPROC glNamedProgramLocalParameter4dEXT = (glNamedProgramLocalParameter4dEXTPROC)((intptr_t)function_pointer);
	glNamedProgramLocalParameter4dEXT(program, target, index, x, y, z, w);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglNamedProgramLocalParameter4dvEXT(JNIEnv *env, jclass clazz, jint program, jint target, jint index, jobject params, jint params_position, jlong function_pointer) {
	const GLdouble *params_address = ((const GLdouble *)(*env)->GetDirectBufferAddress(env, params)) + params_position;
	glNamedProgramLocalParameter4dvEXTPROC glNamedProgramLocalParameter4dvEXT = (glNamedProgramLocalParameter4dvEXTPROC)((intptr_t)function_pointer);
	glNamedProgramLocalParameter4dvEXT(program, target, index, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglNamedProgramLocalParameter4fEXT(JNIEnv *env, jclass clazz, jint program, jint target, jint index, jfloat x, jfloat y, jfloat z, jfloat w, jlong function_pointer) {
	glNamedProgramLocalParameter4fEXTPROC glNamedProgramLocalParameter4fEXT = (glNamedProgramLocalParameter4fEXTPROC)((intptr_t)function_pointer);
	glNamedProgramLocalParameter4fEXT(program, target, index, x, y, z, w);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglNamedProgramLocalParameter4fvEXT(JNIEnv *env, jclass clazz, jint program, jint target, jint index, jobject params, jint params_position, jlong function_pointer) {
	const GLfloat *params_address = ((const GLfloat *)(*env)->GetDirectBufferAddress(env, params)) + params_position;
	glNamedProgramLocalParameter4fvEXTPROC glNamedProgramLocalParameter4fvEXT = (glNamedProgramLocalParameter4fvEXTPROC)((intptr_t)function_pointer);
	glNamedProgramLocalParameter4fvEXT(program, target, index, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglGetNamedProgramLocalParameterdvEXT(JNIEnv *env, jclass clazz, jint program, jint target, jint index, jobject params, jint params_position, jlong function_pointer) {
	GLdouble *params_address = ((GLdouble *)(*env)->GetDirectBufferAddress(env, params)) + params_position;
	glGetNamedProgramLocalParameterdvEXTPROC glGetNamedProgramLocalParameterdvEXT = (glGetNamedProgramLocalParameterdvEXTPROC)((intptr_t)function_pointer);
	glGetNamedProgramLocalParameterdvEXT(program, target, index, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglGetNamedProgramLocalParameterfvEXT(JNIEnv *env, jclass clazz, jint program, jint target, jint index, jobject params, jint params_position, jlong function_pointer) {
	GLfloat *params_address = ((GLfloat *)(*env)->GetDirectBufferAddress(env, params)) + params_position;
	glGetNamedProgramLocalParameterfvEXTPROC glGetNamedProgramLocalParameterfvEXT = (glGetNamedProgramLocalParameterfvEXTPROC)((intptr_t)function_pointer);
	glGetNamedProgramLocalParameterfvEXT(program, target, index, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglGetNamedProgramivEXT(JNIEnv *env, jclass clazz, jint program, jint target, jint pname, jobject params, jint params_position, jlong function_pointer) {
	GLint *params_address = ((GLint *)(*env)->GetDirectBufferAddress(env, params)) + params_position;
	glGetNamedProgramivEXTPROC glGetNamedProgramivEXT = (glGetNamedProgramivEXTPROC)((intptr_t)function_pointer);
	glGetNamedProgramivEXT(program, target, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglGetNamedProgramStringEXT(JNIEnv *env, jclass clazz, jint program, jint target, jint pname, jobject string, jint string_position, jlong function_pointer) {
	GLvoid *string_address = ((GLvoid *)(((char *)(*env)->GetDirectBufferAddress(env, string)) + string_position));
	glGetNamedProgramStringEXTPROC glGetNamedProgramStringEXT = (glGetNamedProgramStringEXTPROC)((intptr_t)function_pointer);
	glGetNamedProgramStringEXT(program, target, pname, string_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglCompressedTextureImage3DEXT(JNIEnv *env, jclass clazz, jint texture, jint target, jint level, jint internalformat, jint width, jint height, jint depth, jint border, jint imageSize, jobject data, jint data_position, jlong function_pointer) {
	const GLvoid *data_address = ((const GLvoid *)(((char *)(*env)->GetDirectBufferAddress(env, data)) + data_position));
	glCompressedTextureImage3DEXTPROC glCompressedTextureImage3DEXT = (glCompressedTextureImage3DEXTPROC)((intptr_t)function_pointer);
	glCompressedTextureImage3DEXT(texture, target, level, internalformat, width, height, depth, border, imageSize, data_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglCompressedTextureImage3DEXTBO(JNIEnv *env, jclass clazz, jint texture, jint target, jint level, jint internalformat, jint width, jint height, jint depth, jint border, jint imageSize, jlong data_buffer_offset, jlong function_pointer) {
	const GLvoid *data_address = ((const GLvoid *)offsetToPointer(data_buffer_offset));
	glCompressedTextureImage3DEXTPROC glCompressedTextureImage3DEXT = (glCompressedTextureImage3DEXTPROC)((intptr_t)function_pointer);
	glCompressedTextureImage3DEXT(texture, target, level, internalformat, width, height, depth, border, imageSize, data_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglCompressedTextureImage2DEXT(JNIEnv *env, jclass clazz, jint texture, jint target, jint level, jint internalformat, jint width, jint height, jint border, jint imageSize, jobject data, jint data_position, jlong function_pointer) {
	const GLvoid *data_address = ((const GLvoid *)(((char *)(*env)->GetDirectBufferAddress(env, data)) + data_position));
	glCompressedTextureImage2DEXTPROC glCompressedTextureImage2DEXT = (glCompressedTextureImage2DEXTPROC)((intptr_t)function_pointer);
	glCompressedTextureImage2DEXT(texture, target, level, internalformat, width, height, border, imageSize, data_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglCompressedTextureImage2DEXTBO(JNIEnv *env, jclass clazz, jint texture, jint target, jint level, jint internalformat, jint width, jint height, jint border, jint imageSize, jlong data_buffer_offset, jlong function_pointer) {
	const GLvoid *data_address = ((const GLvoid *)offsetToPointer(data_buffer_offset));
	glCompressedTextureImage2DEXTPROC glCompressedTextureImage2DEXT = (glCompressedTextureImage2DEXTPROC)((intptr_t)function_pointer);
	glCompressedTextureImage2DEXT(texture, target, level, internalformat, width, height, border, imageSize, data_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglCompressedTextureImage1DEXT(JNIEnv *env, jclass clazz, jint texture, jint target, jint level, jint internalformat, jint width, jint border, jint imageSize, jobject data, jint data_position, jlong function_pointer) {
	const GLvoid *data_address = ((const GLvoid *)(((char *)(*env)->GetDirectBufferAddress(env, data)) + data_position));
	glCompressedTextureImage1DEXTPROC glCompressedTextureImage1DEXT = (glCompressedTextureImage1DEXTPROC)((intptr_t)function_pointer);
	glCompressedTextureImage1DEXT(texture, target, level, internalformat, width, border, imageSize, data_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglCompressedTextureImage1DEXTBO(JNIEnv *env, jclass clazz, jint texture, jint target, jint level, jint internalformat, jint width, jint border, jint imageSize, jlong data_buffer_offset, jlong function_pointer) {
	const GLvoid *data_address = ((const GLvoid *)offsetToPointer(data_buffer_offset));
	glCompressedTextureImage1DEXTPROC glCompressedTextureImage1DEXT = (glCompressedTextureImage1DEXTPROC)((intptr_t)function_pointer);
	glCompressedTextureImage1DEXT(texture, target, level, internalformat, width, border, imageSize, data_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglCompressedTextureSubImage3DEXT(JNIEnv *env, jclass clazz, jint texture, jint target, jint level, jint xoffset, jint yoffset, jint zoffset, jint width, jint height, jint depth, jint format, jint imageSize, jobject data, jint data_position, jlong function_pointer) {
	const GLvoid *data_address = ((const GLvoid *)(((char *)(*env)->GetDirectBufferAddress(env, data)) + data_position));
	glCompressedTextureSubImage3DEXTPROC glCompressedTextureSubImage3DEXT = (glCompressedTextureSubImage3DEXTPROC)((intptr_t)function_pointer);
	glCompressedTextureSubImage3DEXT(texture, target, level, xoffset, yoffset, zoffset, width, height, depth, format, imageSize, data_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglCompressedTextureSubImage3DEXTBO(JNIEnv *env, jclass clazz, jint texture, jint target, jint level, jint xoffset, jint yoffset, jint zoffset, jint width, jint height, jint depth, jint format, jint imageSize, jlong data_buffer_offset, jlong function_pointer) {
	const GLvoid *data_address = ((const GLvoid *)offsetToPointer(data_buffer_offset));
	glCompressedTextureSubImage3DEXTPROC glCompressedTextureSubImage3DEXT = (glCompressedTextureSubImage3DEXTPROC)((intptr_t)function_pointer);
	glCompressedTextureSubImage3DEXT(texture, target, level, xoffset, yoffset, zoffset, width, height, depth, format, imageSize, data_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglCompressedTextureSubImage2DEXT(JNIEnv *env, jclass clazz, jint texture, jint target, jint level, jint xoffset, jint yoffset, jint width, jint height, jint format, jint imageSize, jobject data, jint data_position, jlong function_pointer) {
	const GLvoid *data_address = ((const GLvoid *)(((char *)(*env)->GetDirectBufferAddress(env, data)) + data_position));
	glCompressedTextureSubImage2DEXTPROC glCompressedTextureSubImage2DEXT = (glCompressedTextureSubImage2DEXTPROC)((intptr_t)function_pointer);
	glCompressedTextureSubImage2DEXT(texture, target, level, xoffset, yoffset, width, height, format, imageSize, data_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglCompressedTextureSubImage2DEXTBO(JNIEnv *env, jclass clazz, jint texture, jint target, jint level, jint xoffset, jint yoffset, jint width, jint height, jint format, jint imageSize, jlong data_buffer_offset, jlong function_pointer) {
	const GLvoid *data_address = ((const GLvoid *)offsetToPointer(data_buffer_offset));
	glCompressedTextureSubImage2DEXTPROC glCompressedTextureSubImage2DEXT = (glCompressedTextureSubImage2DEXTPROC)((intptr_t)function_pointer);
	glCompressedTextureSubImage2DEXT(texture, target, level, xoffset, yoffset, width, height, format, imageSize, data_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglCompressedTextureSubImage1DEXT(JNIEnv *env, jclass clazz, jint texture, jint target, jint level, jint xoffset, jint width, jint format, jint imageSize, jobject data, jint data_position, jlong function_pointer) {
	const GLvoid *data_address = ((const GLvoid *)(((char *)(*env)->GetDirectBufferAddress(env, data)) + data_position));
	glCompressedTextureSubImage1DEXTPROC glCompressedTextureSubImage1DEXT = (glCompressedTextureSubImage1DEXTPROC)((intptr_t)function_pointer);
	glCompressedTextureSubImage1DEXT(texture, target, level, xoffset, width, format, imageSize, data_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglCompressedTextureSubImage1DEXTBO(JNIEnv *env, jclass clazz, jint texture, jint target, jint level, jint xoffset, jint width, jint format, jint imageSize, jlong data_buffer_offset, jlong function_pointer) {
	const GLvoid *data_address = ((const GLvoid *)offsetToPointer(data_buffer_offset));
	glCompressedTextureSubImage1DEXTPROC glCompressedTextureSubImage1DEXT = (glCompressedTextureSubImage1DEXTPROC)((intptr_t)function_pointer);
	glCompressedTextureSubImage1DEXT(texture, target, level, xoffset, width, format, imageSize, data_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglGetCompressedTextureImageEXT(JNIEnv *env, jclass clazz, jint texture, jint target, jint level, jobject img, jint img_position, jlong function_pointer) {
	GLvoid *img_address = ((GLvoid *)(((char *)(*env)->GetDirectBufferAddress(env, img)) + img_position));
	glGetCompressedTextureImageEXTPROC glGetCompressedTextureImageEXT = (glGetCompressedTextureImageEXTPROC)((intptr_t)function_pointer);
	glGetCompressedTextureImageEXT(texture, target, level, img_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglGetCompressedTextureImageEXTBO(JNIEnv *env, jclass clazz, jint texture, jint target, jint level, jlong img_buffer_offset, jlong function_pointer) {
	GLvoid *img_address = ((GLvoid *)offsetToPointer(img_buffer_offset));
	glGetCompressedTextureImageEXTPROC glGetCompressedTextureImageEXT = (glGetCompressedTextureImageEXTPROC)((intptr_t)function_pointer);
	glGetCompressedTextureImageEXT(texture, target, level, img_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglCompressedMultiTexImage3DEXT(JNIEnv *env, jclass clazz, jint texunit, jint target, jint level, jint internalformat, jint width, jint height, jint depth, jint border, jint imageSize, jobject data, jint data_position, jlong function_pointer) {
	const GLvoid *data_address = ((const GLvoid *)(((char *)(*env)->GetDirectBufferAddress(env, data)) + data_position));
	glCompressedMultiTexImage3DEXTPROC glCompressedMultiTexImage3DEXT = (glCompressedMultiTexImage3DEXTPROC)((intptr_t)function_pointer);
	glCompressedMultiTexImage3DEXT(texunit, target, level, internalformat, width, height, depth, border, imageSize, data_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglCompressedMultiTexImage3DEXTBO(JNIEnv *env, jclass clazz, jint texunit, jint target, jint level, jint internalformat, jint width, jint height, jint depth, jint border, jint imageSize, jlong data_buffer_offset, jlong function_pointer) {
	const GLvoid *data_address = ((const GLvoid *)offsetToPointer(data_buffer_offset));
	glCompressedMultiTexImage3DEXTPROC glCompressedMultiTexImage3DEXT = (glCompressedMultiTexImage3DEXTPROC)((intptr_t)function_pointer);
	glCompressedMultiTexImage3DEXT(texunit, target, level, internalformat, width, height, depth, border, imageSize, data_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglCompressedMultiTexImage2DEXT(JNIEnv *env, jclass clazz, jint texunit, jint target, jint level, jint internalformat, jint width, jint height, jint border, jint imageSize, jobject data, jint data_position, jlong function_pointer) {
	const GLvoid *data_address = ((const GLvoid *)(((char *)(*env)->GetDirectBufferAddress(env, data)) + data_position));
	glCompressedMultiTexImage2DEXTPROC glCompressedMultiTexImage2DEXT = (glCompressedMultiTexImage2DEXTPROC)((intptr_t)function_pointer);
	glCompressedMultiTexImage2DEXT(texunit, target, level, internalformat, width, height, border, imageSize, data_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglCompressedMultiTexImage2DEXTBO(JNIEnv *env, jclass clazz, jint texunit, jint target, jint level, jint internalformat, jint width, jint height, jint border, jint imageSize, jlong data_buffer_offset, jlong function_pointer) {
	const GLvoid *data_address = ((const GLvoid *)offsetToPointer(data_buffer_offset));
	glCompressedMultiTexImage2DEXTPROC glCompressedMultiTexImage2DEXT = (glCompressedMultiTexImage2DEXTPROC)((intptr_t)function_pointer);
	glCompressedMultiTexImage2DEXT(texunit, target, level, internalformat, width, height, border, imageSize, data_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglCompressedMultiTexImage1DEXT(JNIEnv *env, jclass clazz, jint texunit, jint target, jint level, jint internalformat, jint width, jint border, jint imageSize, jobject data, jint data_position, jlong function_pointer) {
	const GLvoid *data_address = ((const GLvoid *)(((char *)(*env)->GetDirectBufferAddress(env, data)) + data_position));
	glCompressedMultiTexImage1DEXTPROC glCompressedMultiTexImage1DEXT = (glCompressedMultiTexImage1DEXTPROC)((intptr_t)function_pointer);
	glCompressedMultiTexImage1DEXT(texunit, target, level, internalformat, width, border, imageSize, data_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglCompressedMultiTexImage1DEXTBO(JNIEnv *env, jclass clazz, jint texunit, jint target, jint level, jint internalformat, jint width, jint border, jint imageSize, jlong data_buffer_offset, jlong function_pointer) {
	const GLvoid *data_address = ((const GLvoid *)offsetToPointer(data_buffer_offset));
	glCompressedMultiTexImage1DEXTPROC glCompressedMultiTexImage1DEXT = (glCompressedMultiTexImage1DEXTPROC)((intptr_t)function_pointer);
	glCompressedMultiTexImage1DEXT(texunit, target, level, internalformat, width, border, imageSize, data_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglCompressedMultiTexSubImage3DEXT(JNIEnv *env, jclass clazz, jint texunit, jint target, jint level, jint xoffset, jint yoffset, jint zoffset, jint width, jint height, jint depth, jint format, jint imageSize, jobject data, jint data_position, jlong function_pointer) {
	const GLvoid *data_address = ((const GLvoid *)(((char *)(*env)->GetDirectBufferAddress(env, data)) + data_position));
	glCompressedMultiTexSubImage3DEXTPROC glCompressedMultiTexSubImage3DEXT = (glCompressedMultiTexSubImage3DEXTPROC)((intptr_t)function_pointer);
	glCompressedMultiTexSubImage3DEXT(texunit, target, level, xoffset, yoffset, zoffset, width, height, depth, format, imageSize, data_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglCompressedMultiTexSubImage3DEXTBO(JNIEnv *env, jclass clazz, jint texunit, jint target, jint level, jint xoffset, jint yoffset, jint zoffset, jint width, jint height, jint depth, jint format, jint imageSize, jlong data_buffer_offset, jlong function_pointer) {
	const GLvoid *data_address = ((const GLvoid *)offsetToPointer(data_buffer_offset));
	glCompressedMultiTexSubImage3DEXTPROC glCompressedMultiTexSubImage3DEXT = (glCompressedMultiTexSubImage3DEXTPROC)((intptr_t)function_pointer);
	glCompressedMultiTexSubImage3DEXT(texunit, target, level, xoffset, yoffset, zoffset, width, height, depth, format, imageSize, data_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglCompressedMultiTexSubImage2DEXT(JNIEnv *env, jclass clazz, jint texunit, jint target, jint level, jint xoffset, jint yoffset, jint width, jint height, jint format, jint imageSize, jobject data, jint data_position, jlong function_pointer) {
	const GLvoid *data_address = ((const GLvoid *)(((char *)(*env)->GetDirectBufferAddress(env, data)) + data_position));
	glCompressedMultiTexSubImage2DEXTPROC glCompressedMultiTexSubImage2DEXT = (glCompressedMultiTexSubImage2DEXTPROC)((intptr_t)function_pointer);
	glCompressedMultiTexSubImage2DEXT(texunit, target, level, xoffset, yoffset, width, height, format, imageSize, data_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglCompressedMultiTexSubImage2DEXTBO(JNIEnv *env, jclass clazz, jint texunit, jint target, jint level, jint xoffset, jint yoffset, jint width, jint height, jint format, jint imageSize, jlong data_buffer_offset, jlong function_pointer) {
	const GLvoid *data_address = ((const GLvoid *)offsetToPointer(data_buffer_offset));
	glCompressedMultiTexSubImage2DEXTPROC glCompressedMultiTexSubImage2DEXT = (glCompressedMultiTexSubImage2DEXTPROC)((intptr_t)function_pointer);
	glCompressedMultiTexSubImage2DEXT(texunit, target, level, xoffset, yoffset, width, height, format, imageSize, data_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglCompressedMultiTexSubImage1DEXT(JNIEnv *env, jclass clazz, jint texunit, jint target, jint level, jint xoffset, jint width, jint format, jint imageSize, jobject data, jint data_position, jlong function_pointer) {
	const GLvoid *data_address = ((const GLvoid *)(((char *)(*env)->GetDirectBufferAddress(env, data)) + data_position));
	glCompressedMultiTexSubImage1DEXTPROC glCompressedMultiTexSubImage1DEXT = (glCompressedMultiTexSubImage1DEXTPROC)((intptr_t)function_pointer);
	glCompressedMultiTexSubImage1DEXT(texunit, target, level, xoffset, width, format, imageSize, data_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglCompressedMultiTexSubImage1DEXTBO(JNIEnv *env, jclass clazz, jint texunit, jint target, jint level, jint xoffset, jint width, jint format, jint imageSize, jlong data_buffer_offset, jlong function_pointer) {
	const GLvoid *data_address = ((const GLvoid *)offsetToPointer(data_buffer_offset));
	glCompressedMultiTexSubImage1DEXTPROC glCompressedMultiTexSubImage1DEXT = (glCompressedMultiTexSubImage1DEXTPROC)((intptr_t)function_pointer);
	glCompressedMultiTexSubImage1DEXT(texunit, target, level, xoffset, width, format, imageSize, data_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglGetCompressedMultiTexImageEXT(JNIEnv *env, jclass clazz, jint texunit, jint target, jint level, jobject img, jint img_position, jlong function_pointer) {
	GLvoid *img_address = ((GLvoid *)(((char *)(*env)->GetDirectBufferAddress(env, img)) + img_position));
	glGetCompressedMultiTexImageEXTPROC glGetCompressedMultiTexImageEXT = (glGetCompressedMultiTexImageEXTPROC)((intptr_t)function_pointer);
	glGetCompressedMultiTexImageEXT(texunit, target, level, img_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglGetCompressedMultiTexImageEXTBO(JNIEnv *env, jclass clazz, jint texunit, jint target, jint level, jlong img_buffer_offset, jlong function_pointer) {
	GLvoid *img_address = ((GLvoid *)offsetToPointer(img_buffer_offset));
	glGetCompressedMultiTexImageEXTPROC glGetCompressedMultiTexImageEXT = (glGetCompressedMultiTexImageEXTPROC)((intptr_t)function_pointer);
	glGetCompressedMultiTexImageEXT(texunit, target, level, img_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglGetCompressedTexImage(JNIEnv *env, jclass clazz, jint target, jint lod, jobject img, jint img_position, jlong function_pointer) {
	GLvoid *img_address = ((GLvoid *)(((char *)(*env)->GetDirectBufferAddress(env, img)) + img_position));
	glGetCompressedTexImagePROC glGetCompressedTexImage = (glGetCompressedTexImagePROC)((intptr_t)function_pointer);
	glGetCompressedTexImage(target, lod, img_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglGetCompressedTexImageBO(JNIEnv *env, jclass clazz, jint target, jint lod, jlong img_buffer_offset, jlong function_pointer) {
	GLvoid *img_address = ((GLvoid *)offsetToPointer(img_buffer_offset));
	glGetCompressedTexImagePROC glGetCompressedTexImage = (glGetCompressedTexImagePROC)((intptr_t)function_pointer);
	glGetCompressedTexImage(target, lod, img_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglMatrixLoadTransposefEXT(JNIEnv *env, jclass clazz, jint matrixMode, jobject m, jint m_position, jlong function_pointer) {
	const GLfloat *m_address = ((const GLfloat *)(*env)->GetDirectBufferAddress(env, m)) + m_position;
	glMatrixLoadTransposefEXTPROC glMatrixLoadTransposefEXT = (glMatrixLoadTransposefEXTPROC)((intptr_t)function_pointer);
	glMatrixLoadTransposefEXT(matrixMode, m_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglMatrixLoadTransposedEXT(JNIEnv *env, jclass clazz, jint matrixMode, jobject m, jint m_position, jlong function_pointer) {
	const GLdouble *m_address = ((const GLdouble *)(*env)->GetDirectBufferAddress(env, m)) + m_position;
	glMatrixLoadTransposedEXTPROC glMatrixLoadTransposedEXT = (glMatrixLoadTransposedEXTPROC)((intptr_t)function_pointer);
	glMatrixLoadTransposedEXT(matrixMode, m_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglMatrixMultTransposefEXT(JNIEnv *env, jclass clazz, jint matrixMode, jobject m, jint m_position, jlong function_pointer) {
	const GLfloat *m_address = ((const GLfloat *)(*env)->GetDirectBufferAddress(env, m)) + m_position;
	glMatrixMultTransposefEXTPROC glMatrixMultTransposefEXT = (glMatrixMultTransposefEXTPROC)((intptr_t)function_pointer);
	glMatrixMultTransposefEXT(matrixMode, m_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglMatrixMultTransposedEXT(JNIEnv *env, jclass clazz, jint matrixMode, jobject m, jint m_position, jlong function_pointer) {
	const GLdouble *m_address = ((const GLdouble *)(*env)->GetDirectBufferAddress(env, m)) + m_position;
	glMatrixMultTransposedEXTPROC glMatrixMultTransposedEXT = (glMatrixMultTransposedEXTPROC)((intptr_t)function_pointer);
	glMatrixMultTransposedEXT(matrixMode, m_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglNamedBufferDataEXT(JNIEnv *env, jclass clazz, jint buffer, jlong size, jobject data, jint data_position, jint usage, jlong function_pointer) {
	const GLvoid *data_address = ((const GLvoid *)(((char *)safeGetBufferAddress(env, data)) + data_position));
	glNamedBufferDataEXTPROC glNamedBufferDataEXT = (glNamedBufferDataEXTPROC)((intptr_t)function_pointer);
	glNamedBufferDataEXT(buffer, size, data_address, usage);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglNamedBufferSubDataEXT(JNIEnv *env, jclass clazz, jint buffer, jlong offset, jlong size, jobject data, jint data_position, jlong function_pointer) {
	const GLvoid *data_address = ((const GLvoid *)(((char *)(*env)->GetDirectBufferAddress(env, data)) + data_position));
	glNamedBufferSubDataEXTPROC glNamedBufferSubDataEXT = (glNamedBufferSubDataEXTPROC)((intptr_t)function_pointer);
	glNamedBufferSubDataEXT(buffer, offset, size, data_address);
}

JNIEXPORT jobject JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglMapNamedBufferEXT(JNIEnv *env, jclass clazz, jint buffer, jint access, jlong result_size, jobject old_buffer, jlong function_pointer) {
	glMapNamedBufferEXTPROC glMapNamedBufferEXT = (glMapNamedBufferEXTPROC)((intptr_t)function_pointer);
	GLvoid * __result = glMapNamedBufferEXT(buffer, access);
	return safeNewBufferCached(env, __result, result_size, old_buffer);
}

JNIEXPORT jboolean JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglUnmapNamedBufferEXT(JNIEnv *env, jclass clazz, jint buffer, jlong function_pointer) {
	glUnmapNamedBufferEXTPROC glUnmapNamedBufferEXT = (glUnmapNamedBufferEXTPROC)((intptr_t)function_pointer);
	GLboolean __result = glUnmapNamedBufferEXT(buffer);
	return __result;
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglGetNamedBufferParameterivEXT(JNIEnv *env, jclass clazz, jint buffer, jint pname, jobject params, jint params_position, jlong function_pointer) {
	GLint *params_address = ((GLint *)(*env)->GetDirectBufferAddress(env, params)) + params_position;
	glGetNamedBufferParameterivEXTPROC glGetNamedBufferParameterivEXT = (glGetNamedBufferParameterivEXTPROC)((intptr_t)function_pointer);
	glGetNamedBufferParameterivEXT(buffer, pname, params_address);
}

JNIEXPORT jobject JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglGetNamedBufferPointervEXT(JNIEnv *env, jclass clazz, jint buffer, jint pname, jlong result_size, jlong function_pointer) {
	glGetNamedBufferPointervEXTPROC glGetNamedBufferPointervEXT = (glGetNamedBufferPointervEXTPROC)((intptr_t)function_pointer);
	GLvoid * __result;
	glGetNamedBufferPointervEXT(buffer, pname, &__result);
	return safeNewBuffer(env, __result, result_size);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglGetNamedBufferSubDataEXT(JNIEnv *env, jclass clazz, jint buffer, jlong offset, jlong size, jobject data, jint data_position, jlong function_pointer) {
	GLvoid *data_address = ((GLvoid *)(((char *)(*env)->GetDirectBufferAddress(env, data)) + data_position));
	glGetNamedBufferSubDataEXTPROC glGetNamedBufferSubDataEXT = (glGetNamedBufferSubDataEXTPROC)((intptr_t)function_pointer);
	glGetNamedBufferSubDataEXT(buffer, offset, size, data_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglProgramUniform1fEXT(JNIEnv *env, jclass clazz, jint program, jint location, jfloat v0, jlong function_pointer) {
	glProgramUniform1fEXTPROC glProgramUniform1fEXT = (glProgramUniform1fEXTPROC)((intptr_t)function_pointer);
	glProgramUniform1fEXT(program, location, v0);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglProgramUniform2fEXT(JNIEnv *env, jclass clazz, jint program, jint location, jfloat v0, jfloat v1, jlong function_pointer) {
	glProgramUniform2fEXTPROC glProgramUniform2fEXT = (glProgramUniform2fEXTPROC)((intptr_t)function_pointer);
	glProgramUniform2fEXT(program, location, v0, v1);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglProgramUniform3fEXT(JNIEnv *env, jclass clazz, jint program, jint location, jfloat v0, jfloat v1, jfloat v2, jlong function_pointer) {
	glProgramUniform3fEXTPROC glProgramUniform3fEXT = (glProgramUniform3fEXTPROC)((intptr_t)function_pointer);
	glProgramUniform3fEXT(program, location, v0, v1, v2);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglProgramUniform4fEXT(JNIEnv *env, jclass clazz, jint program, jint location, jfloat v0, jfloat v1, jfloat v2, jfloat v3, jlong function_pointer) {
	glProgramUniform4fEXTPROC glProgramUniform4fEXT = (glProgramUniform4fEXTPROC)((intptr_t)function_pointer);
	glProgramUniform4fEXT(program, location, v0, v1, v2, v3);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglProgramUniform1iEXT(JNIEnv *env, jclass clazz, jint program, jint location, jint v0, jlong function_pointer) {
	glProgramUniform1iEXTPROC glProgramUniform1iEXT = (glProgramUniform1iEXTPROC)((intptr_t)function_pointer);
	glProgramUniform1iEXT(program, location, v0);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglProgramUniform2iEXT(JNIEnv *env, jclass clazz, jint program, jint location, jint v0, jint v1, jlong function_pointer) {
	glProgramUniform2iEXTPROC glProgramUniform2iEXT = (glProgramUniform2iEXTPROC)((intptr_t)function_pointer);
	glProgramUniform2iEXT(program, location, v0, v1);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglProgramUniform3iEXT(JNIEnv *env, jclass clazz, jint program, jint location, jint v0, jint v1, jint v2, jlong function_pointer) {
	glProgramUniform3iEXTPROC glProgramUniform3iEXT = (glProgramUniform3iEXTPROC)((intptr_t)function_pointer);
	glProgramUniform3iEXT(program, location, v0, v1, v2);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglProgramUniform4iEXT(JNIEnv *env, jclass clazz, jint program, jint location, jint v0, jint v1, jint v2, jint v3, jlong function_pointer) {
	glProgramUniform4iEXTPROC glProgramUniform4iEXT = (glProgramUniform4iEXTPROC)((intptr_t)function_pointer);
	glProgramUniform4iEXT(program, location, v0, v1, v2, v3);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglProgramUniform1fvEXT(JNIEnv *env, jclass clazz, jint program, jint location, jint count, jobject value, jint value_position, jlong function_pointer) {
	const GLfloat *value_address = ((const GLfloat *)(*env)->GetDirectBufferAddress(env, value)) + value_position;
	glProgramUniform1fvEXTPROC glProgramUniform1fvEXT = (glProgramUniform1fvEXTPROC)((intptr_t)function_pointer);
	glProgramUniform1fvEXT(program, location, count, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglProgramUniform2fvEXT(JNIEnv *env, jclass clazz, jint program, jint location, jint count, jobject value, jint value_position, jlong function_pointer) {
	const GLfloat *value_address = ((const GLfloat *)(*env)->GetDirectBufferAddress(env, value)) + value_position;
	glProgramUniform2fvEXTPROC glProgramUniform2fvEXT = (glProgramUniform2fvEXTPROC)((intptr_t)function_pointer);
	glProgramUniform2fvEXT(program, location, count, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglProgramUniform3fvEXT(JNIEnv *env, jclass clazz, jint program, jint location, jint count, jobject value, jint value_position, jlong function_pointer) {
	const GLfloat *value_address = ((const GLfloat *)(*env)->GetDirectBufferAddress(env, value)) + value_position;
	glProgramUniform3fvEXTPROC glProgramUniform3fvEXT = (glProgramUniform3fvEXTPROC)((intptr_t)function_pointer);
	glProgramUniform3fvEXT(program, location, count, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglProgramUniform4fvEXT(JNIEnv *env, jclass clazz, jint program, jint location, jint count, jobject value, jint value_position, jlong function_pointer) {
	const GLfloat *value_address = ((const GLfloat *)(*env)->GetDirectBufferAddress(env, value)) + value_position;
	glProgramUniform4fvEXTPROC glProgramUniform4fvEXT = (glProgramUniform4fvEXTPROC)((intptr_t)function_pointer);
	glProgramUniform4fvEXT(program, location, count, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglProgramUniform1ivEXT(JNIEnv *env, jclass clazz, jint program, jint location, jint count, jobject value, jint value_position, jlong function_pointer) {
	const GLint *value_address = ((const GLint *)(*env)->GetDirectBufferAddress(env, value)) + value_position;
	glProgramUniform1ivEXTPROC glProgramUniform1ivEXT = (glProgramUniform1ivEXTPROC)((intptr_t)function_pointer);
	glProgramUniform1ivEXT(program, location, count, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglProgramUniform2ivEXT(JNIEnv *env, jclass clazz, jint program, jint location, jint count, jobject value, jint value_position, jlong function_pointer) {
	const GLint *value_address = ((const GLint *)(*env)->GetDirectBufferAddress(env, value)) + value_position;
	glProgramUniform2ivEXTPROC glProgramUniform2ivEXT = (glProgramUniform2ivEXTPROC)((intptr_t)function_pointer);
	glProgramUniform2ivEXT(program, location, count, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglProgramUniform3ivEXT(JNIEnv *env, jclass clazz, jint program, jint location, jint count, jobject value, jint value_position, jlong function_pointer) {
	const GLint *value_address = ((const GLint *)(*env)->GetDirectBufferAddress(env, value)) + value_position;
	glProgramUniform3ivEXTPROC glProgramUniform3ivEXT = (glProgramUniform3ivEXTPROC)((intptr_t)function_pointer);
	glProgramUniform3ivEXT(program, location, count, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglProgramUniform4ivEXT(JNIEnv *env, jclass clazz, jint program, jint location, jint count, jobject value, jint value_position, jlong function_pointer) {
	const GLint *value_address = ((const GLint *)(*env)->GetDirectBufferAddress(env, value)) + value_position;
	glProgramUniform4ivEXTPROC glProgramUniform4ivEXT = (glProgramUniform4ivEXTPROC)((intptr_t)function_pointer);
	glProgramUniform4ivEXT(program, location, count, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglProgramUniformMatrix2fvEXT(JNIEnv *env, jclass clazz, jint program, jint location, jint count, jboolean transpose, jobject value, jint value_position, jlong function_pointer) {
	const GLfloat *value_address = ((const GLfloat *)(*env)->GetDirectBufferAddress(env, value)) + value_position;
	glProgramUniformMatrix2fvEXTPROC glProgramUniformMatrix2fvEXT = (glProgramUniformMatrix2fvEXTPROC)((intptr_t)function_pointer);
	glProgramUniformMatrix2fvEXT(program, location, count, transpose, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglProgramUniformMatrix3fvEXT(JNIEnv *env, jclass clazz, jint program, jint location, jint count, jboolean transpose, jobject value, jint value_position, jlong function_pointer) {
	const GLfloat *value_address = ((const GLfloat *)(*env)->GetDirectBufferAddress(env, value)) + value_position;
	glProgramUniformMatrix3fvEXTPROC glProgramUniformMatrix3fvEXT = (glProgramUniformMatrix3fvEXTPROC)((intptr_t)function_pointer);
	glProgramUniformMatrix3fvEXT(program, location, count, transpose, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglProgramUniformMatrix4fvEXT(JNIEnv *env, jclass clazz, jint program, jint location, jint count, jboolean transpose, jobject value, jint value_position, jlong function_pointer) {
	const GLfloat *value_address = ((const GLfloat *)(*env)->GetDirectBufferAddress(env, value)) + value_position;
	glProgramUniformMatrix4fvEXTPROC glProgramUniformMatrix4fvEXT = (glProgramUniformMatrix4fvEXTPROC)((intptr_t)function_pointer);
	glProgramUniformMatrix4fvEXT(program, location, count, transpose, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglProgramUniformMatrix2x3fvEXT(JNIEnv *env, jclass clazz, jint program, jint location, jint count, jboolean transpose, jobject value, jint value_position, jlong function_pointer) {
	const GLfloat *value_address = ((const GLfloat *)(*env)->GetDirectBufferAddress(env, value)) + value_position;
	glProgramUniformMatrix2x3fvEXTPROC glProgramUniformMatrix2x3fvEXT = (glProgramUniformMatrix2x3fvEXTPROC)((intptr_t)function_pointer);
	glProgramUniformMatrix2x3fvEXT(program, location, count, transpose, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglProgramUniformMatrix3x2fvEXT(JNIEnv *env, jclass clazz, jint program, jint location, jint count, jboolean transpose, jobject value, jint value_position, jlong function_pointer) {
	const GLfloat *value_address = ((const GLfloat *)(*env)->GetDirectBufferAddress(env, value)) + value_position;
	glProgramUniformMatrix3x2fvEXTPROC glProgramUniformMatrix3x2fvEXT = (glProgramUniformMatrix3x2fvEXTPROC)((intptr_t)function_pointer);
	glProgramUniformMatrix3x2fvEXT(program, location, count, transpose, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglProgramUniformMatrix2x4fvEXT(JNIEnv *env, jclass clazz, jint program, jint location, jint count, jboolean transpose, jobject value, jint value_position, jlong function_pointer) {
	const GLfloat *value_address = ((const GLfloat *)(*env)->GetDirectBufferAddress(env, value)) + value_position;
	glProgramUniformMatrix2x4fvEXTPROC glProgramUniformMatrix2x4fvEXT = (glProgramUniformMatrix2x4fvEXTPROC)((intptr_t)function_pointer);
	glProgramUniformMatrix2x4fvEXT(program, location, count, transpose, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglProgramUniformMatrix4x2fvEXT(JNIEnv *env, jclass clazz, jint program, jint location, jint count, jboolean transpose, jobject value, jint value_position, jlong function_pointer) {
	const GLfloat *value_address = ((const GLfloat *)(*env)->GetDirectBufferAddress(env, value)) + value_position;
	glProgramUniformMatrix4x2fvEXTPROC glProgramUniformMatrix4x2fvEXT = (glProgramUniformMatrix4x2fvEXTPROC)((intptr_t)function_pointer);
	glProgramUniformMatrix4x2fvEXT(program, location, count, transpose, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglProgramUniformMatrix3x4fvEXT(JNIEnv *env, jclass clazz, jint program, jint location, jint count, jboolean transpose, jobject value, jint value_position, jlong function_pointer) {
	const GLfloat *value_address = ((const GLfloat *)(*env)->GetDirectBufferAddress(env, value)) + value_position;
	glProgramUniformMatrix3x4fvEXTPROC glProgramUniformMatrix3x4fvEXT = (glProgramUniformMatrix3x4fvEXTPROC)((intptr_t)function_pointer);
	glProgramUniformMatrix3x4fvEXT(program, location, count, transpose, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglProgramUniformMatrix4x3fvEXT(JNIEnv *env, jclass clazz, jint program, jint location, jint count, jboolean transpose, jobject value, jint value_position, jlong function_pointer) {
	const GLfloat *value_address = ((const GLfloat *)(*env)->GetDirectBufferAddress(env, value)) + value_position;
	glProgramUniformMatrix4x3fvEXTPROC glProgramUniformMatrix4x3fvEXT = (glProgramUniformMatrix4x3fvEXTPROC)((intptr_t)function_pointer);
	glProgramUniformMatrix4x3fvEXT(program, location, count, transpose, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglTextureBufferEXT(JNIEnv *env, jclass clazz, jint texture, jint target, jint internalformat, jint buffer, jlong function_pointer) {
	glTextureBufferEXTPROC glTextureBufferEXT = (glTextureBufferEXTPROC)((intptr_t)function_pointer);
	glTextureBufferEXT(texture, target, internalformat, buffer);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglMultiTexBufferEXT(JNIEnv *env, jclass clazz, jint texunit, jint target, jint internalformat, jint buffer, jlong function_pointer) {
	glMultiTexBufferEXTPROC glMultiTexBufferEXT = (glMultiTexBufferEXTPROC)((intptr_t)function_pointer);
	glMultiTexBufferEXT(texunit, target, internalformat, buffer);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglTextureParameterIivEXT(JNIEnv *env, jclass clazz, jint texture, jint target, jint pname, jobject params, jint params_position, jlong function_pointer) {
	const GLint *params_address = ((const GLint *)(*env)->GetDirectBufferAddress(env, params)) + params_position;
	glTextureParameterIivEXTPROC glTextureParameterIivEXT = (glTextureParameterIivEXTPROC)((intptr_t)function_pointer);
	glTextureParameterIivEXT(texture, target, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglTextureParameterIuivEXT(JNIEnv *env, jclass clazz, jint texture, jint target, jint pname, jobject params, jint params_position, jlong function_pointer) {
	const GLuint *params_address = ((const GLuint *)(*env)->GetDirectBufferAddress(env, params)) + params_position;
	glTextureParameterIuivEXTPROC glTextureParameterIuivEXT = (glTextureParameterIuivEXTPROC)((intptr_t)function_pointer);
	glTextureParameterIuivEXT(texture, target, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglGetTextureParameterIivEXT(JNIEnv *env, jclass clazz, jint texture, jint target, jint pname, jobject params, jint params_position, jlong function_pointer) {
	GLint *params_address = ((GLint *)(*env)->GetDirectBufferAddress(env, params)) + params_position;
	glGetTextureParameterIivEXTPROC glGetTextureParameterIivEXT = (glGetTextureParameterIivEXTPROC)((intptr_t)function_pointer);
	glGetTextureParameterIivEXT(texture, target, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglGetTextureParameterIuivEXT(JNIEnv *env, jclass clazz, jint texture, jint target, jint pname, jobject params, jint params_position, jlong function_pointer) {
	GLuint *params_address = ((GLuint *)(*env)->GetDirectBufferAddress(env, params)) + params_position;
	glGetTextureParameterIuivEXTPROC glGetTextureParameterIuivEXT = (glGetTextureParameterIuivEXTPROC)((intptr_t)function_pointer);
	glGetTextureParameterIuivEXT(texture, target, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglMultiTexParameterIivEXT(JNIEnv *env, jclass clazz, jint texunit, jint target, jint pname, jobject params, jint params_position, jlong function_pointer) {
	const GLint *params_address = ((const GLint *)(*env)->GetDirectBufferAddress(env, params)) + params_position;
	glMultiTexParameterIivEXTPROC glMultiTexParameterIivEXT = (glMultiTexParameterIivEXTPROC)((intptr_t)function_pointer);
	glMultiTexParameterIivEXT(texunit, target, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglMultiTexParameterIuivEXT(JNIEnv *env, jclass clazz, jint texunit, jint target, jint pname, jobject params, jint params_position, jlong function_pointer) {
	const GLuint *params_address = ((const GLuint *)(*env)->GetDirectBufferAddress(env, params)) + params_position;
	glMultiTexParameterIuivEXTPROC glMultiTexParameterIuivEXT = (glMultiTexParameterIuivEXTPROC)((intptr_t)function_pointer);
	glMultiTexParameterIuivEXT(texunit, target, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglGetMultiTexParameterIivEXT(JNIEnv *env, jclass clazz, jint texunit, jint target, jint pname, jobject params, jint params_position, jlong function_pointer) {
	GLint *params_address = ((GLint *)(*env)->GetDirectBufferAddress(env, params)) + params_position;
	glGetMultiTexParameterIivEXTPROC glGetMultiTexParameterIivEXT = (glGetMultiTexParameterIivEXTPROC)((intptr_t)function_pointer);
	glGetMultiTexParameterIivEXT(texunit, target, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglGetMultiTexParameterIuivEXT(JNIEnv *env, jclass clazz, jint texunit, jint target, jint pname, jobject params, jint params_position, jlong function_pointer) {
	GLuint *params_address = ((GLuint *)(*env)->GetDirectBufferAddress(env, params)) + params_position;
	glGetMultiTexParameterIuivEXTPROC glGetMultiTexParameterIuivEXT = (glGetMultiTexParameterIuivEXTPROC)((intptr_t)function_pointer);
	glGetMultiTexParameterIuivEXT(texunit, target, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglProgramUniform1uiEXT(JNIEnv *env, jclass clazz, jint program, jint location, jint v0, jlong function_pointer) {
	glProgramUniform1uiEXTPROC glProgramUniform1uiEXT = (glProgramUniform1uiEXTPROC)((intptr_t)function_pointer);
	glProgramUniform1uiEXT(program, location, v0);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglProgramUniform2uiEXT(JNIEnv *env, jclass clazz, jint program, jint location, jint v0, jint v1, jlong function_pointer) {
	glProgramUniform2uiEXTPROC glProgramUniform2uiEXT = (glProgramUniform2uiEXTPROC)((intptr_t)function_pointer);
	glProgramUniform2uiEXT(program, location, v0, v1);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglProgramUniform3uiEXT(JNIEnv *env, jclass clazz, jint program, jint location, jint v0, jint v1, jint v2, jlong function_pointer) {
	glProgramUniform3uiEXTPROC glProgramUniform3uiEXT = (glProgramUniform3uiEXTPROC)((intptr_t)function_pointer);
	glProgramUniform3uiEXT(program, location, v0, v1, v2);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglProgramUniform4uiEXT(JNIEnv *env, jclass clazz, jint program, jint location, jint v0, jint v1, jint v2, jint v3, jlong function_pointer) {
	glProgramUniform4uiEXTPROC glProgramUniform4uiEXT = (glProgramUniform4uiEXTPROC)((intptr_t)function_pointer);
	glProgramUniform4uiEXT(program, location, v0, v1, v2, v3);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglProgramUniform1uivEXT(JNIEnv *env, jclass clazz, jint program, jint location, jint count, jobject value, jint value_position, jlong function_pointer) {
	const GLuint *value_address = ((const GLuint *)(*env)->GetDirectBufferAddress(env, value)) + value_position;
	glProgramUniform1uivEXTPROC glProgramUniform1uivEXT = (glProgramUniform1uivEXTPROC)((intptr_t)function_pointer);
	glProgramUniform1uivEXT(program, location, count, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglProgramUniform2uivEXT(JNIEnv *env, jclass clazz, jint program, jint location, jint count, jobject value, jint value_position, jlong function_pointer) {
	const GLuint *value_address = ((const GLuint *)(*env)->GetDirectBufferAddress(env, value)) + value_position;
	glProgramUniform2uivEXTPROC glProgramUniform2uivEXT = (glProgramUniform2uivEXTPROC)((intptr_t)function_pointer);
	glProgramUniform2uivEXT(program, location, count, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglProgramUniform3uivEXT(JNIEnv *env, jclass clazz, jint program, jint location, jint count, jobject value, jint value_position, jlong function_pointer) {
	const GLuint *value_address = ((const GLuint *)(*env)->GetDirectBufferAddress(env, value)) + value_position;
	glProgramUniform3uivEXTPROC glProgramUniform3uivEXT = (glProgramUniform3uivEXTPROC)((intptr_t)function_pointer);
	glProgramUniform3uivEXT(program, location, count, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglProgramUniform4uivEXT(JNIEnv *env, jclass clazz, jint program, jint location, jint count, jobject value, jint value_position, jlong function_pointer) {
	const GLuint *value_address = ((const GLuint *)(*env)->GetDirectBufferAddress(env, value)) + value_position;
	glProgramUniform4uivEXTPROC glProgramUniform4uivEXT = (glProgramUniform4uivEXTPROC)((intptr_t)function_pointer);
	glProgramUniform4uivEXT(program, location, count, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglNamedProgramLocalParameters4fvEXT(JNIEnv *env, jclass clazz, jint program, jint target, jint index, jint count, jobject params, jint params_position, jlong function_pointer) {
	const GLfloat *params_address = ((const GLfloat *)(*env)->GetDirectBufferAddress(env, params)) + params_position;
	glNamedProgramLocalParameters4fvEXTPROC glNamedProgramLocalParameters4fvEXT = (glNamedProgramLocalParameters4fvEXTPROC)((intptr_t)function_pointer);
	glNamedProgramLocalParameters4fvEXT(program, target, index, count, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglNamedProgramLocalParameterI4iEXT(JNIEnv *env, jclass clazz, jint program, jint target, jint index, jint x, jint y, jint z, jint w, jlong function_pointer) {
	glNamedProgramLocalParameterI4iEXTPROC glNamedProgramLocalParameterI4iEXT = (glNamedProgramLocalParameterI4iEXTPROC)((intptr_t)function_pointer);
	glNamedProgramLocalParameterI4iEXT(program, target, index, x, y, z, w);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglNamedProgramLocalParameterI4ivEXT(JNIEnv *env, jclass clazz, jint program, jint target, jint index, jobject params, jint params_position, jlong function_pointer) {
	const GLint *params_address = ((const GLint *)(*env)->GetDirectBufferAddress(env, params)) + params_position;
	glNamedProgramLocalParameterI4ivEXTPROC glNamedProgramLocalParameterI4ivEXT = (glNamedProgramLocalParameterI4ivEXTPROC)((intptr_t)function_pointer);
	glNamedProgramLocalParameterI4ivEXT(program, target, index, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglNamedProgramLocalParametersI4ivEXT(JNIEnv *env, jclass clazz, jint program, jint target, jint index, jint count, jobject params, jint params_position, jlong function_pointer) {
	const GLint *params_address = ((const GLint *)(*env)->GetDirectBufferAddress(env, params)) + params_position;
	glNamedProgramLocalParametersI4ivEXTPROC glNamedProgramLocalParametersI4ivEXT = (glNamedProgramLocalParametersI4ivEXTPROC)((intptr_t)function_pointer);
	glNamedProgramLocalParametersI4ivEXT(program, target, index, count, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglNamedProgramLocalParameterI4uiEXT(JNIEnv *env, jclass clazz, jint program, jint target, jint index, jint x, jint y, jint z, jint w, jlong function_pointer) {
	glNamedProgramLocalParameterI4uiEXTPROC glNamedProgramLocalParameterI4uiEXT = (glNamedProgramLocalParameterI4uiEXTPROC)((intptr_t)function_pointer);
	glNamedProgramLocalParameterI4uiEXT(program, target, index, x, y, z, w);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglNamedProgramLocalParameterI4uivEXT(JNIEnv *env, jclass clazz, jint program, jint target, jint index, jobject params, jint params_position, jlong function_pointer) {
	const GLuint *params_address = ((const GLuint *)(*env)->GetDirectBufferAddress(env, params)) + params_position;
	glNamedProgramLocalParameterI4uivEXTPROC glNamedProgramLocalParameterI4uivEXT = (glNamedProgramLocalParameterI4uivEXTPROC)((intptr_t)function_pointer);
	glNamedProgramLocalParameterI4uivEXT(program, target, index, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglNamedProgramLocalParametersI4uivEXT(JNIEnv *env, jclass clazz, jint program, jint target, jint index, jint count, jobject params, jint params_position, jlong function_pointer) {
	const GLuint *params_address = ((const GLuint *)(*env)->GetDirectBufferAddress(env, params)) + params_position;
	glNamedProgramLocalParametersI4uivEXTPROC glNamedProgramLocalParametersI4uivEXT = (glNamedProgramLocalParametersI4uivEXTPROC)((intptr_t)function_pointer);
	glNamedProgramLocalParametersI4uivEXT(program, target, index, count, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglGetNamedProgramLocalParameterIivEXT(JNIEnv *env, jclass clazz, jint program, jint target, jint index, jobject params, jint params_position, jlong function_pointer) {
	GLint *params_address = ((GLint *)(*env)->GetDirectBufferAddress(env, params)) + params_position;
	glGetNamedProgramLocalParameterIivEXTPROC glGetNamedProgramLocalParameterIivEXT = (glGetNamedProgramLocalParameterIivEXTPROC)((intptr_t)function_pointer);
	glGetNamedProgramLocalParameterIivEXT(program, target, index, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglGetNamedProgramLocalParameterIuivEXT(JNIEnv *env, jclass clazz, jint program, jint target, jint index, jobject params, jint params_position, jlong function_pointer) {
	GLuint *params_address = ((GLuint *)(*env)->GetDirectBufferAddress(env, params)) + params_position;
	glGetNamedProgramLocalParameterIuivEXTPROC glGetNamedProgramLocalParameterIuivEXT = (glGetNamedProgramLocalParameterIuivEXTPROC)((intptr_t)function_pointer);
	glGetNamedProgramLocalParameterIuivEXT(program, target, index, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglNamedRenderbufferStorageEXT(JNIEnv *env, jclass clazz, jint renderbuffer, jint internalformat, jint width, jint height, jlong function_pointer) {
	glNamedRenderbufferStorageEXTPROC glNamedRenderbufferStorageEXT = (glNamedRenderbufferStorageEXTPROC)((intptr_t)function_pointer);
	glNamedRenderbufferStorageEXT(renderbuffer, internalformat, width, height);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglGetNamedRenderbufferParameterivEXT(JNIEnv *env, jclass clazz, jint renderbuffer, jint pname, jobject params, jint params_position, jlong function_pointer) {
	GLint *params_address = ((GLint *)(*env)->GetDirectBufferAddress(env, params)) + params_position;
	glGetNamedRenderbufferParameterivEXTPROC glGetNamedRenderbufferParameterivEXT = (glGetNamedRenderbufferParameterivEXTPROC)((intptr_t)function_pointer);
	glGetNamedRenderbufferParameterivEXT(renderbuffer, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglNamedRenderbufferStorageMultisampleEXT(JNIEnv *env, jclass clazz, jint renderbuffer, jint samples, jint internalformat, jint width, jint height, jlong function_pointer) {
	glNamedRenderbufferStorageMultisampleEXTPROC glNamedRenderbufferStorageMultisampleEXT = (glNamedRenderbufferStorageMultisampleEXTPROC)((intptr_t)function_pointer);
	glNamedRenderbufferStorageMultisampleEXT(renderbuffer, samples, internalformat, width, height);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglNamedRenderbufferStorageMultisampleCoverageEXT(JNIEnv *env, jclass clazz, jint renderbuffer, jint coverageSamples, jint colorSamples, jint internalformat, jint width, jint height, jlong function_pointer) {
	glNamedRenderbufferStorageMultisampleCoverageEXTPROC glNamedRenderbufferStorageMultisampleCoverageEXT = (glNamedRenderbufferStorageMultisampleCoverageEXTPROC)((intptr_t)function_pointer);
	glNamedRenderbufferStorageMultisampleCoverageEXT(renderbuffer, coverageSamples, colorSamples, internalformat, width, height);
}

JNIEXPORT jint JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglCheckNamedFramebufferStatusEXT(JNIEnv *env, jclass clazz, jint framebuffer, jint target, jlong function_pointer) {
	glCheckNamedFramebufferStatusEXTPROC glCheckNamedFramebufferStatusEXT = (glCheckNamedFramebufferStatusEXTPROC)((intptr_t)function_pointer);
	GLenum __result = glCheckNamedFramebufferStatusEXT(framebuffer, target);
	return __result;
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglNamedFramebufferTexture1DEXT(JNIEnv *env, jclass clazz, jint framebuffer, jint attachment, jint textarget, jint texture, jint level, jlong function_pointer) {
	glNamedFramebufferTexture1DEXTPROC glNamedFramebufferTexture1DEXT = (glNamedFramebufferTexture1DEXTPROC)((intptr_t)function_pointer);
	glNamedFramebufferTexture1DEXT(framebuffer, attachment, textarget, texture, level);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglNamedFramebufferTexture2DEXT(JNIEnv *env, jclass clazz, jint framebuffer, jint attachment, jint textarget, jint texture, jint level, jlong function_pointer) {
	glNamedFramebufferTexture2DEXTPROC glNamedFramebufferTexture2DEXT = (glNamedFramebufferTexture2DEXTPROC)((intptr_t)function_pointer);
	glNamedFramebufferTexture2DEXT(framebuffer, attachment, textarget, texture, level);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglNamedFramebufferTexture3DEXT(JNIEnv *env, jclass clazz, jint framebuffer, jint attachment, jint textarget, jint texture, jint level, jint zoffset, jlong function_pointer) {
	glNamedFramebufferTexture3DEXTPROC glNamedFramebufferTexture3DEXT = (glNamedFramebufferTexture3DEXTPROC)((intptr_t)function_pointer);
	glNamedFramebufferTexture3DEXT(framebuffer, attachment, textarget, texture, level, zoffset);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglNamedFramebufferRenderbufferEXT(JNIEnv *env, jclass clazz, jint framebuffer, jint attachment, jint renderbuffertarget, jint renderbuffer, jlong function_pointer) {
	glNamedFramebufferRenderbufferEXTPROC glNamedFramebufferRenderbufferEXT = (glNamedFramebufferRenderbufferEXTPROC)((intptr_t)function_pointer);
	glNamedFramebufferRenderbufferEXT(framebuffer, attachment, renderbuffertarget, renderbuffer);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglGetNamedFramebufferAttachmentParameterivEXT(JNIEnv *env, jclass clazz, jint framebuffer, jint attachment, jint pname, jobject params, jint params_position, jlong function_pointer) {
	GLint *params_address = ((GLint *)(*env)->GetDirectBufferAddress(env, params)) + params_position;
	glGetNamedFramebufferAttachmentParameterivEXTPROC glGetNamedFramebufferAttachmentParameterivEXT = (glGetNamedFramebufferAttachmentParameterivEXTPROC)((intptr_t)function_pointer);
	glGetNamedFramebufferAttachmentParameterivEXT(framebuffer, attachment, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglGenerateTextureMipmapEXT(JNIEnv *env, jclass clazz, jint texture, jint target, jlong function_pointer) {
	glGenerateTextureMipmapEXTPROC glGenerateTextureMipmapEXT = (glGenerateTextureMipmapEXTPROC)((intptr_t)function_pointer);
	glGenerateTextureMipmapEXT(texture, target);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglGenerateMultiTexMipmapEXT(JNIEnv *env, jclass clazz, jint texunit, jint target, jlong function_pointer) {
	glGenerateMultiTexMipmapEXTPROC glGenerateMultiTexMipmapEXT = (glGenerateMultiTexMipmapEXTPROC)((intptr_t)function_pointer);
	glGenerateMultiTexMipmapEXT(texunit, target);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglFramebufferDrawBufferEXT(JNIEnv *env, jclass clazz, jint framebuffer, jint mode, jlong function_pointer) {
	glFramebufferDrawBufferEXTPROC glFramebufferDrawBufferEXT = (glFramebufferDrawBufferEXTPROC)((intptr_t)function_pointer);
	glFramebufferDrawBufferEXT(framebuffer, mode);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglFramebufferDrawBuffersEXT(JNIEnv *env, jclass clazz, jint framebuffer, jint n, jobject bufs, jint bufs_position, jlong function_pointer) {
	const GLenum *bufs_address = ((const GLenum *)(*env)->GetDirectBufferAddress(env, bufs)) + bufs_position;
	glFramebufferDrawBuffersEXTPROC glFramebufferDrawBuffersEXT = (glFramebufferDrawBuffersEXTPROC)((intptr_t)function_pointer);
	glFramebufferDrawBuffersEXT(framebuffer, n, bufs_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglFramebufferReadBufferEXT(JNIEnv *env, jclass clazz, jint framebuffer, jint mode, jlong function_pointer) {
	glFramebufferReadBufferEXTPROC glFramebufferReadBufferEXT = (glFramebufferReadBufferEXTPROC)((intptr_t)function_pointer);
	glFramebufferReadBufferEXT(framebuffer, mode);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglGetFramebufferParameterivEXT(JNIEnv *env, jclass clazz, jint framebuffer, jint pname, jobject param, jint param_position, jlong function_pointer) {
	GLint *param_address = ((GLint *)(*env)->GetDirectBufferAddress(env, param)) + param_position;
	glGetFramebufferParameterivEXTPROC glGetFramebufferParameterivEXT = (glGetFramebufferParameterivEXTPROC)((intptr_t)function_pointer);
	glGetFramebufferParameterivEXT(framebuffer, pname, param_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglNamedFramebufferTextureEXT(JNIEnv *env, jclass clazz, jint framebuffer, jint attachment, jint texture, jint level, jlong function_pointer) {
	glNamedFramebufferTextureEXTPROC glNamedFramebufferTextureEXT = (glNamedFramebufferTextureEXTPROC)((intptr_t)function_pointer);
	glNamedFramebufferTextureEXT(framebuffer, attachment, texture, level);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglNamedFramebufferTextureLayerEXT(JNIEnv *env, jclass clazz, jint framebuffer, jint attachment, jint texture, jint level, jint layer, jlong function_pointer) {
	glNamedFramebufferTextureLayerEXTPROC glNamedFramebufferTextureLayerEXT = (glNamedFramebufferTextureLayerEXTPROC)((intptr_t)function_pointer);
	glNamedFramebufferTextureLayerEXT(framebuffer, attachment, texture, level, layer);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglNamedFramebufferTextureFaceEXT(JNIEnv *env, jclass clazz, jint framebuffer, jint attachment, jint texture, jint level, jint face, jlong function_pointer) {
	glNamedFramebufferTextureFaceEXTPROC glNamedFramebufferTextureFaceEXT = (glNamedFramebufferTextureFaceEXTPROC)((intptr_t)function_pointer);
	glNamedFramebufferTextureFaceEXT(framebuffer, attachment, texture, level, face);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglTextureRenderbufferEXT(JNIEnv *env, jclass clazz, jint texture, jint target, jint renderbuffer, jlong function_pointer) {
	glTextureRenderbufferEXTPROC glTextureRenderbufferEXT = (glTextureRenderbufferEXTPROC)((intptr_t)function_pointer);
	glTextureRenderbufferEXT(texture, target, renderbuffer);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTDirectStateAccess_nglMultiTexRenderbufferEXT(JNIEnv *env, jclass clazz, jint texunit, jint target, jint renderbuffer, jlong function_pointer) {
	glMultiTexRenderbufferEXTPROC glMultiTexRenderbufferEXT = (glMultiTexRenderbufferEXTPROC)((intptr_t)function_pointer);
	glMultiTexRenderbufferEXT(texunit, target, renderbuffer);
}

