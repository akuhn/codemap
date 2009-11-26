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

import java.nio.IntBuffer;
import java.nio.ByteBuffer;

public interface NV_transform_feedback {

	/**
	 * Accepted by the &lt;target&gt; parameters of BindBuffer, BufferData,
	 * BufferSubData, MapBuffer, UnmapBuffer, GetBufferSubData,
	 * GetBufferPointerv, BindBufferRangeNV, BindBufferOffsetNV and
	 * BindBufferBaseNV:
	 */
	int GL_TRANSFORM_FEEDBACK_BUFFER_NV = 0x8C8E;

	/**
	 * Accepted by the &lt;param&gt; parameter of GetIntegerIndexedvEXT and
	 * GetBooleanIndexedvEXT:
	 */
	int GL_TRANSFORM_FEEDBACK_BUFFER_START_NV = 0x8C84;
	int GL_TRANSFORM_FEEDBACK_BUFFER_SIZE_NV = 0x8C85;
	int GL_TRANSFORM_FEEDBACK_RECORD_NV = 0x8C86;

	/**
	 * Accepted by the &lt;param&gt; parameter of GetIntegerIndexedvEXT and
	 * GetBooleanIndexedvEXT, and by the &lt;pname&gt; parameter of GetBooleanv,
	 * GetDoublev, GetIntegerv, and GetFloatv:
	 */
	int GL_TRANSFORM_FEEDBACK_BUFFER_BINDING_NV = 0x8C8F;

	/**
	 * Accepted by the &lt;bufferMode&gt; parameter of TransformFeedbackAttribsNV and
	 * TransformFeedbackVaryingsNV:
	 */
	int GL_INTERLEAVED_ATTRIBS_NV = 0x8C8C;
	int GL_SEPARATE_ATTRIBS_NV = 0x8C8D;

	/**
	 * Accepted by the &lt;target&gt; parameter of BeginQuery, EndQuery, and
	 * GetQueryiv:
	 */
	int GL_PRIMITIVES_GENERATED_NV = 0x8C87;
	int GL_TRANSFORM_FEEDBACK_PRIMITIVES_WRITTEN_NV = 0x8C88;

	/**
	 * Accepted by the &lt;cap&gt; parameter of Enable, Disable, and IsEnabled, and by
	 * the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv, GetFloatv, and
	 * GetDoublev:
	 */
	int GL_RASTERIZER_DISCARD_NV = 0x8C89;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetDoublev, GetIntegerv,
	 * and GetFloatv:
	 */
	int GL_MAX_TRANSFORM_FEEDBACK_INTERLEAVED_COMPONENTS_NV = 0x8C8A;
	int GL_MAX_TRANSFORM_FEEDBACK_SEPARATE_ATTRIBS_NV = 0x8C8B;
	int GL_MAX_TRANSFORM_FEEDBACK_SEPARATE_COMPONENTS_NV = 0x8C80;
	int GL_TRANSFORM_FEEDBACK_ATTRIBS_NV = 0x8C7E;

	/** Accepted by the &lt;pname&gt; parameter of GetProgramiv: */
	int GL_ACTIVE_VARYINGS_NV = 0x8C81;
	int GL_ACTIVE_VARYING_MAX_LENGTH_NV = 0x8C82;
	int GL_TRANSFORM_FEEDBACK_VARYINGS_NV = 0x8C83;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetDoublev, GetIntegerv,
	 * GetFloatv, and GetProgramiv:
	 */
	int GL_TRANSFORM_FEEDBACK_BUFFER_MODE_NV = 0x8C7F;

	/** Accepted by the &lt;attribs&gt; parameter of TransformFeedbackAttribsNV: */
	int GL_BACK_PRIMARY_COLOR_NV = 0x8C77;
	int GL_BACK_SECONDARY_COLOR_NV = 0x8C78;
	int GL_TEXTURE_COORD_NV = 0x8C79;
	int GL_CLIP_DISTANCE_NV = 0x8C7A;
	int GL_VERTEX_ID_NV = 0x8C7B;
	int GL_PRIMITIVE_ID_NV = 0x8C7C;
	int GL_GENERIC_ATTRIB_NV = 0x8C7D;
	int GL_LAYER_NV = 0x8DAA;

	void glBindBufferRangeNV(@GLenum int target, @GLuint int index, @GLuint int buffer, @GLintptr long offset, @GLsizeiptr long size);

	void glBindBufferOffsetNV(@GLenum int target, @GLuint int index, @GLuint int buffer, @GLintptr long offset);

	void glBindBufferBaseNV(@GLenum int target, @GLuint int index, @GLuint int buffer);

	void glTransformFeedbackAttribsNV(@AutoSize("attribs") @GLsizei int count, @Const IntBuffer attribs, @GLenum int bufferMode);

	void glTransformFeedbackVaryingsNV(@GLuint int program, @AutoSize("locations") @GLsizei int count, @Const IntBuffer locations, @GLenum int bufferMode);

	void glBeginTransformFeedbackNV(@GLenum int primitiveMode);

	void glEndTransformFeedbackNV();

	int glGetVaryingLocationNV(@GLuint int program, @NullTerminated @Const @GLchar ByteBuffer name);

	void glGetActiveVaryingNV(@GLuint int program, @GLuint int index, @AutoSize("name") @GLsizei int bufSize,
							  @OutParameter @Check("1") @GLsizei IntBuffer length,
							  @OutParameter @Check("1") @GLsizei IntBuffer size,
							  @OutParameter @Check("1") @GLenum IntBuffer type, @GLchar ByteBuffer name);

	void glActiveVaryingNV(@GLuint int program, @NullTerminated @Const @GLchar ByteBuffer name);

	void glGetTransformFeedbackVaryingNV(@GLuint int program, @GLuint int index, @OutParameter @Check("1") IntBuffer location);

}
