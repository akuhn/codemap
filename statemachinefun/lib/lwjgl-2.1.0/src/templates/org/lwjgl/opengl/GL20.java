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
package org.lwjgl.opengl;

import org.lwjgl.util.generator.*;

import java.nio.*;

public interface GL20 {
	// ------------------------------------------------------------------
	// -------------------[ ARB_shading_language_100 ]-------------------
	// ------------------------------------------------------------------

	/**
	 * Accepted by the &lt;name&gt; parameter of GetString:
	 */
	int GL_SHADING_LANGUAGE_VERSION = 0x8B8C;

	// ------------------------------------------------------------------
	// ----------------------[ ARB_shader_objects ]----------------------
	// ------------------------------------------------------------------

	/**
	 * Accepted by the &lt;pname&gt; argument of GetInteger:
	 */
	int GL_CURRENT_PROGRAM = 0x8B8D;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetObjectParameter{fi}vARB:
	 */
	int GL_SHADER_TYPE = 0x8B4F;
	int GL_DELETE_STATUS = 0x8B80;
	int GL_COMPILE_STATUS = 0x8B81;
	int GL_LINK_STATUS = 0x8B82;
	int GL_VALIDATE_STATUS = 0x8B83;
	int GL_INFO_LOG_LENGTH = 0x8B84;
	int GL_ATTACHED_SHADERS = 0x8B85;
	int GL_ACTIVE_UNIFORMS = 0x8B86;
	int GL_ACTIVE_UNIFORM_MAX_LENGTH = 0x8B87;
	int GL_ACTIVE_ATTRIBUTES = 0x8B89;
	int GL_ACTIVE_ATTRIBUTE_MAX_LENGTH = 0x8B8A;
	int GL_SHADER_SOURCE_LENGTH = 0x8B88;

	/**
	 * Returned by the &lt;params&gt; parameter of GetObjectParameter{fi}vARB:
	 */
	int GL_SHADER_OBJECT = 0x8B48;

	/**
	 * Returned by the &lt;type&gt; parameter of GetActiveUniformARB:
	 */
	int GL_FLOAT_VEC2 = 0x8B50;
	int GL_FLOAT_VEC3 = 0x8B51;
	int GL_FLOAT_VEC4 = 0x8B52;
	int GL_INT_VEC2 = 0x8B53;
	int GL_INT_VEC3 = 0x8B54;
	int GL_INT_VEC4 = 0x8B55;
	int GL_BOOL = 0x8B56;
	int GL_BOOL_VEC2 = 0x8B57;
	int GL_BOOL_VEC3 = 0x8B58;
	int GL_BOOL_VEC4 = 0x8B59;
	int GL_FLOAT_MAT2 = 0x8B5A;
	int GL_FLOAT_MAT3 = 0x8B5B;
	int GL_FLOAT_MAT4 = 0x8B5C;
	int GL_SAMPLER_1D = 0x8B5D;
	int GL_SAMPLER_2D = 0x8B5E;
	int GL_SAMPLER_3D = 0x8B5F;
	int GL_SAMPLER_CUBE = 0x8B60;
	int GL_SAMPLER_1D_SHADOW = 0x8B61;
	int GL_SAMPLER_2D_SHADOW = 0x8B62;

	/**
	 * The ARB_shader_objects extension allows multiple, optionally null-terminated, source strings to define a shader program.
	 * <p/>
	 * This method uses just a single string, that should NOT be null-terminated.
	 *
	 * @param shader
	 * @param string
	 */
	void glShaderSource(@GLuint int shader, @Constant("1") @GLsizei int count,
	                    @Indirect
	                    @Check
	                    @Const
	                    @GLchar ByteBuffer string,
	                    @AutoSize("string")
	                    @Indirect
	                    @Const
	                    @GLint int length);

	int glCreateShader(@GLuint int type);

	boolean glIsShader(@GLuint int shader);

	void glCompileShader(@GLuint int shader);

	void glDeleteShader(@GLuint int shader);

	int glCreateProgram();

	boolean glIsProgram(int program);

	void glAttachShader(@GLuint int program, @GLuint int shader);

	void glDetachShader(@GLuint int program, @GLuint int shader);

	void glLinkProgram(@GLuint int program);

	void glUseProgram(@GLuint int program);

	void glValidateProgram(@GLuint int program);

	void glDeleteProgram(@GLuint int program);

	void glUniform1f(int location, float v0);

	void glUniform2f(int location, float v0, float v1);

	void glUniform3f(int location, float v0, float v1, float v2);

	void glUniform4f(int location, float v0, float v1, float v2, float v3);

	void glUniform1i(int location, int v0);

	void glUniform2i(int location, int v0, int v1);

	void glUniform3i(int location, int v0, int v1, int v2);

	void glUniform4i(int location, int v0, int v1, int v2, int v3);

	@StripPostfix("values")
	void glUniform1fv(int location, @AutoSize("values") @GLsizei int count, FloatBuffer values);

	@StripPostfix("values")
	void glUniform2fv(int location, @AutoSize(value = "values", expression = " >> 1") @GLsizei int count, FloatBuffer values);

	@StripPostfix("values")
	void glUniform3fv(int location, @AutoSize(value = "values", expression = " / 3") @GLsizei int count, FloatBuffer values);

	@StripPostfix("values")
	void glUniform4fv(int location, @AutoSize(value = "values", expression = " >> 2") @GLsizei int count, FloatBuffer values);

	@StripPostfix("values")
	void glUniform1iv(int location, @AutoSize("values") @GLsizei int count, IntBuffer values);

	@StripPostfix("values")
	void glUniform2iv(int location, @AutoSize(value = "values", expression = " >> 1") @GLsizei int count, IntBuffer values);

	@StripPostfix("values")
	void glUniform3iv(int location, @AutoSize(value = "values", expression = " / 3") @GLsizei int count, IntBuffer values);

	@StripPostfix("values")
	void glUniform4iv(int location, @AutoSize(value = "values", expression = " >> 2") @GLsizei int count, IntBuffer values);

	@StripPostfix("matrices")
	void glUniformMatrix2fv(int location, @AutoSize(value = "matrices", expression = " >> 2") @GLsizei int count,
	                        boolean transpose, FloatBuffer matrices);

	@StripPostfix("matrices")
	void glUniformMatrix3fv(int location, @AutoSize(value = "matrices", expression = " / (3 * 3)") @GLsizei int count,
	                        boolean transpose, FloatBuffer matrices);

	@StripPostfix("matrices")
	void glUniformMatrix4fv(int location, @AutoSize(value = "matrices", expression = " >> 4") @GLsizei int count,
	                        boolean transpose, FloatBuffer matrices);

	@StripPostfix("params")
	void glGetShaderiv(@GLuint int shader, @GLenum int pname, @OutParameter @Check IntBuffer params);

	@StripPostfix("params")
	void glGetProgramiv(@GLuint int program, @GLenum int pname, @OutParameter @Check IntBuffer params);

	void glGetShaderInfoLog(@GLuint int shader, @AutoSize("infoLog") @GLsizei int maxLength,
			                @OutParameter
	                        @GLsizei
	                        @Check(value = "1", canBeNull = true) IntBuffer length,
	                        @GLchar ByteBuffer infoLog);

	void glGetProgramInfoLog(@GLuint int program, @AutoSize("infoLog") @GLsizei int maxLength,
			                 @OutParameter
	                         @GLsizei
	                         @Check(value = "1", canBeNull = true) IntBuffer length,
	                         @GLchar ByteBuffer infoLog);

	void glGetAttachedShaders(@GLuint int program, @AutoSize("shaders") @GLsizei int maxCount,
			                  @OutParameter
	                          @GLsizei
	                          @Check(value = "1", canBeNull = true) IntBuffer count,
	                          @GLuint IntBuffer shaders);

	/**
	 * Returns the location of the uniform with the specified name. The ByteBuffer should contain the uniform name as a
	 * <b>null-terminated</b> string.
	 *
	 * @param program
	 * @param name
	 *
	 */
	int glGetUniformLocation(@GLuint int program, @NullTerminated @Check("1") @Const @GLchar ByteBuffer name);

	void glGetActiveUniform(@GLuint int program, @GLuint int index, @AutoSize("name") @GLsizei int maxLength,
	                        @Check(value = "1", canBeNull = true)
	                        @OutParameter @GLsizei IntBuffer length,
	                        @Check
	                        @OutParameter @GLsizei IntBuffer size,
	                        @Check
	                        @OutParameter @GLenum IntBuffer type,
	                        @OutParameter @GLchar ByteBuffer name);

	@StripPostfix("params")
	void glGetUniformfv(@GLuint int program, int location, @OutParameter @Check FloatBuffer params);

	@StripPostfix("params")
	void glGetUniformiv(@GLuint int program, int location, @OutParameter @Check IntBuffer params);

	void glGetShaderSource(@GLuint int shader, @OutParameter @AutoSize("source") @GLsizei int maxLength,
	                       @Check(value = "1", canBeNull = true)
	                       @GLsizei IntBuffer length,
	                       @GLchar ByteBuffer source);

	// ------------------------------------------------------------------
	// ----------------------[ ARB_vertex_program ]----------------------
	// ------------------------------------------------------------------

	void glVertexAttrib1s(@GLuint int index, short x);

	void glVertexAttrib1f(@GLuint int index, float x);

	void glVertexAttrib1d(@GLuint int index, double x);

	void glVertexAttrib2s(@GLuint int index, short x, short y);

	void glVertexAttrib2f(@GLuint int index, float x, float y);

	void glVertexAttrib2d(@GLuint int index, double x, double y);

	void glVertexAttrib3s(@GLuint int index, short x, short y, short z);

	void glVertexAttrib3f(@GLuint int index, float x, float y, float z);

	void glVertexAttrib3d(@GLuint int index, double x, double y, double z);

	void glVertexAttrib4s(@GLuint int index, short x, short y, short z, short w);

	void glVertexAttrib4f(@GLuint int index, float x, float y, float z, float w);

	void glVertexAttrib4d(@GLuint int index, double x, double y, double z, double w);

	void glVertexAttrib4Nub(@GLuint int index, @GLubyte byte x, @GLubyte byte y, @GLubyte byte z, @GLubyte byte w);

	void glVertexAttribPointer(@GLuint int index, int size, @AutoType("buffer") @GLenum int type, boolean normalized, @GLsizei int stride,
	                           @CachedReference(index="index",name="glVertexAttribPointer_buffer")
	                           @BufferObject(BufferKind.ArrayVBO)
	                           @Check
	                           @Const
	                           @GLubyte
	                           @GLbyte
	                           @GLshort
	                           @GLushort
	                           @GLint
	                           @GLuint
	                           @GLfloat
	                           @GLdouble Buffer buffer);

	void glEnableVertexAttribArray(@GLuint int index);

	void glDisableVertexAttribArray(@GLuint int index);

	@StripPostfix("params")
	void glGetVertexAttribfv(@GLuint int index, @GLenum int pname, @OutParameter @Check("4") FloatBuffer params);

	@StripPostfix("params")
	void glGetVertexAttribdv(@GLuint int index, @GLenum int pname, @OutParameter @Check("4") DoubleBuffer params);

	@StripPostfix("params")
	void glGetVertexAttribiv(@GLuint int index, @GLenum int pname, @OutParameter @Check("4") IntBuffer params);

	@StripPostfix("pointer")
	void glGetVertexAttribPointerv(@GLuint int index, @GLenum int pname, @Result @GLvoid ByteBuffer pointer);

	// -----------------------------------------------------------------
	// ----------------------[ ARB_vertex_shader ]----------------------
	// -----------------------------------------------------------------

	/**
	 * Accepted by the &lt;shaderType&gt; argument of CreateShader and
	 * returned by the &lt;params&gt; parameter of GetShader{if}v:
	 */
	int GL_VERTEX_SHADER = 0x8B31;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv,
	 * GetFloatv, and GetDoublev:
	 */
	int GL_MAX_VERTEX_UNIFORM_COMPONENTS = 0x8B4A;
	int GL_MAX_VARYING_FLOATS = 0x8B4B;
	int GL_MAX_VERTEX_ATTRIBS = 0x8869;
	int GL_MAX_TEXTURE_IMAGE_UNITS = 0x8872;
	int GL_MAX_VERTEX_TEXTURE_IMAGE_UNITS = 0x8B4C;
	int GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS = 0x8B4D;
	int GL_MAX_TEXTURE_COORDS = 0x8871;

	/**
	 * Accepted by the &lt;cap&gt; parameter of Disable, Enable, and IsEnabled, and
	 * by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv, GetFloatv, and
	 * GetDoublev:
	 */
	int GL_VERTEX_PROGRAM_POINT_SIZE = 0x8642;
	int GL_VERTEX_PROGRAM_TWO_SIDE = 0x8643;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetVertexAttrib{dfi}vARB:
	 */
	int GL_VERTEX_ATTRIB_ARRAY_ENABLED = 0x8622;
	int GL_VERTEX_ATTRIB_ARRAY_SIZE = 0x8623;
	int GL_VERTEX_ATTRIB_ARRAY_STRIDE = 0x8624;
	int GL_VERTEX_ATTRIB_ARRAY_TYPE = 0x8625;
	int GL_VERTEX_ATTRIB_ARRAY_NORMALIZED = 0x886A;
	int GL_CURRENT_VERTEX_ATTRIB = 0x8626;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetVertexAttribPointervARB:
	 */
	int GL_VERTEX_ATTRIB_ARRAY_POINTER = 0x8645;

	void glBindAttribLocation(@GLuint int program, @GLuint int index, @NullTerminated @Const @GLchar ByteBuffer name);

	void glGetActiveAttrib(@GLuint int program, @GLuint int index, @AutoSize("name") @GLsizei int maxLength,
			               @OutParameter
	                       @Check(value = "1", canBeNull = true)
	                       @GLsizei IntBuffer length,
	                       @Check("1") IntBuffer size,
	                       @Check("1")
	                       @GLenum IntBuffer type,
	                       @Const
	                       @GLchar ByteBuffer name);

	int glGetAttribLocation(@GLuint int program, @NullTerminated @Const @GLchar ByteBuffer name);

	// -------------------------------------------------------------------
	// ----------------------[ ARB_fragment_shader ]----------------------
	// -------------------------------------------------------------------

	/**
	 * Accepted by the &lt;shaderType&gt; argument of CreateShader and
	 * returned by the &lt;params&gt; parameter of GetShader{fi}vARB:
	 */
	int GL_FRAGMENT_SHADER = 0x8B30;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv,
	 * GetFloatv, and GetDoublev:
	 */
	int GL_MAX_FRAGMENT_UNIFORM_COMPONENTS = 0x8B49;

	/**
	 * Accepted by the &lt;target&gt; parameter of Hint and the &lt;pname&gt; parameter of
	 * GetBooleanv, GetIntegerv, GetFloatv, and GetDoublev:
	 */
	int GL_FRAGMENT_SHADER_DERIVATIVE_HINT = 0x8B8B;

	// ----------------------------------------------------------------
	// ----------------------[ ARB_draw_buffers ]----------------------
	// ----------------------------------------------------------------

	/**
	 * Accepted by the &lt;pname&gt; parameters of GetIntegerv, GetFloatv,
	 * and GetDoublev:
	 */
	int GL_MAX_DRAW_BUFFERS = 0x8824;
	int GL_DRAW_BUFFER0 = 0x8825;
	int GL_DRAW_BUFFER1 = 0x8826;
	int GL_DRAW_BUFFER2 = 0x8827;
	int GL_DRAW_BUFFER3 = 0x8828;
	int GL_DRAW_BUFFER4 = 0x8829;
	int GL_DRAW_BUFFER5 = 0x882A;
	int GL_DRAW_BUFFER6 = 0x882B;
	int GL_DRAW_BUFFER7 = 0x882C;
	int GL_DRAW_BUFFER8 = 0x882D;
	int GL_DRAW_BUFFER9 = 0x882E;
	int GL_DRAW_BUFFER10 = 0x882F;
	int GL_DRAW_BUFFER11 = 0x8830;
	int GL_DRAW_BUFFER12 = 0x8831;
	int GL_DRAW_BUFFER13 = 0x8832;
	int GL_DRAW_BUFFER14 = 0x8833;
	int GL_DRAW_BUFFER15 = 0x8834;

	void glDrawBuffers(@AutoSize("buffers") @GLsizei int size, @Const @GLenum IntBuffer buffers);

	// ----------------------------------------------------------------
	// ----------------------[ ARB_point_sprite ]----------------------
	// ----------------------------------------------------------------

	/**
	 * Accepted by the &lt;cap&gt; parameter of Enable, Disable, and IsEnabled, by
	 * the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv, GetFloatv, and
	 * GetDoublev, and by the &lt;target&gt; parameter of TexEnvi, TexEnviv,
	 * TexEnvf, TexEnvfv, GetTexEnviv, and GetTexEnvfv:
	 */
	int GL_POINT_SPRITE = 0x8861;

	/**
	 * When the &lt;target&gt; parameter of TexEnvf, TexEnvfv, TexEnvi, TexEnviv,
	 * GetTexEnvfv, or GetTexEnviv is POINT_SPRITE, then the value of
	 * &lt;pname&gt; may be:
	 */
	int GL_COORD_REPLACE = 0x8862;

	/**
	 * Accepted by the &lt;pname&gt; parameter of PointParameter{if}vARB, and the
	 * &lt;pname&gt; of Get:
	 */
	int GL_POINT_SPRITE_COORD_ORIGIN = 0x8CA0;

	/**
	 * Accepted by the &lt;param&gt; parameter of PointParameter{if}vARB:
	 */
	int GL_LOWER_LEFT = 0x8CA1;
	int GL_UPPER_LEFT = 0x8CA2;

	// -----------------------------------------------------------------
	// ----------------------[ Two-Sided Stencil ]----------------------
	// -----------------------------------------------------------------

	int GL_STENCIL_BACK_FUNC = 0x8800;
	int GL_STENCIL_BACK_FAIL = 0x8801;
	int GL_STENCIL_BACK_PASS_DEPTH_FAIL = 0x8802;
	int GL_STENCIL_BACK_PASS_DEPTH_PASS = 0x8803;
	int GL_STENCIL_BACK_REF = 0x8CA3;
	int GL_STENCIL_BACK_VALUE_MASK = 0x8CA4;
	int GL_STENCIL_BACK_WRITEMASK = 0x8CA5;

	void glStencilOpSeparate(@GLenum int face, @GLenum int sfail, @GLenum int dpfail, @GLenum int dppass);

	void glStencilFuncSeparate(@GLenum int face, @GLenum int func, int ref, @GLuint int mask);

	void glStencilMaskSeparate(@GLenum int face, @GLuint int mask);

	// -------------------------------------------------------------
	// ----------------------[ EXT_blend_equation_separate ]----------------------
	// -------------------------------------------------------------

	int GL_BLEND_EQUATION_RGB = 0x8009;
	int GL_BLEND_EQUATION_ALPHA = 0x883D;

	void glBlendEquationSeparate(@GLenum int modeRGB, @GLenum int modeAlpha);
}
