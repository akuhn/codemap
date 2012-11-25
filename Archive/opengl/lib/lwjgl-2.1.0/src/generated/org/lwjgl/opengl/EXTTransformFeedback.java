/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.LWJGLException;
import org.lwjgl.BufferChecks;
import org.lwjgl.NondirectBufferWrapper;
import java.nio.*;

public final class EXTTransformFeedback {
	/**
	 * Accepted by the &lt;target&gt; parameters of BindBuffer, BufferData,
	 * BufferSubData, MapBuffer, UnmapBuffer, GetBufferSubData,
	 * GetBufferPointerv, BindBufferRangeEXT, BindBufferOffsetEXT and
	 * BindBufferBaseEXT:
	 */
	public static final int GL_TRANSFORM_FEEDBACK_BUFFER_EXT = 0x8c8e;
	/**
	 * Accepted by the &lt;param&gt; parameter of GetIntegerIndexedvEXT and
	 * GetBooleanIndexedvEXT:
	 */
	public static final int GL_TRANSFORM_FEEDBACK_BUFFER_START_EXT = 0x8c84;
	public static final int GL_TRANSFORM_FEEDBACK_BUFFER_SIZE_EXT = 0x8c85;
	/**
	 * Accepted by the &lt;param&gt; parameter of GetIntegerIndexedvEXT and
	 * GetBooleanIndexedvEXT, and by the &lt;pname&gt; parameter of GetBooleanv,
	 * GetDoublev, GetIntegerv, and GetFloatv:
	 */
	public static final int GL_TRANSFORM_FEEDBACK_BUFFER_BINDING_EXT = 0x8c8f;
	/**
	 *Accepted by the &lt;bufferMode&gt; parameter of TransformFeedbackVaryingsEXT: 
	 */
	public static final int GL_INTERLEAVED_ATTRIBS_EXT = 0x8c8c;
	public static final int GL_SEPARATE_ATTRIBS_EXT = 0x8c8d;
	/**
	 * Accepted by the &lt;target&gt; parameter of BeginQuery, EndQuery, and
	 * GetQueryiv:
	 */
	public static final int GL_PRIMITIVES_GENERATED_EXT = 0x8c87;
	public static final int GL_TRANSFORM_FEEDBACK_PRIMITIVES_WRITTEN_EXT = 0x8c88;
	/**
	 * Accepted by the &lt;cap&gt; parameter of Enable, Disable, and IsEnabled, and by
	 * the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv, GetFloatv, and
	 * GetDoublev:
	 */
	public static final int GL_RASTERIZER_DISCARD_EXT = 0x8c89;
	/**
	 * Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetDoublev, GetIntegerv,
	 * and GetFloatv:
	 */
	public static final int GL_MAX_TRANSFORM_FEEDBACK_INTERLEAVED_COMPONENTS_EXT = 0x8c8a;
	public static final int GL_MAX_TRANSFORM_FEEDBACK_SEPARATE_ATTRIBS_EXT = 0x8c8b;
	public static final int GL_MAX_TRANSFORM_FEEDBACK_SEPARATE_COMPONENTS_EXT = 0x8c80;
	/**
	 *Accepted by the &lt;pname&gt; parameter of GetProgramiv: 
	 */
	public static final int GL_TRANSFORM_FEEDBACK_VARYINGS_EXT = 0x8c83;
	public static final int GL_TRANSFORM_FEEDBACK_BUFFER_MODE_EXT = 0x8c7f;
	public static final int GL_TRANSFORM_FEEDBACK_VARYING_MAX_LENGTH_EXT = 0x8c76;

	private EXTTransformFeedback() {
	}


	public static void glBindBufferRangeEXT(int target, int index, int buffer, long offset, long size) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_transform_feedback_glBindBufferRangeEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglBindBufferRangeEXT(target, index, buffer, offset, size, function_pointer);
	}
	private static native void nglBindBufferRangeEXT(int target, int index, int buffer, long offset, long size, long function_pointer);

	public static void glBindBufferOffsetEXT(int target, int index, int buffer, long offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_transform_feedback_glBindBufferOffsetEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglBindBufferOffsetEXT(target, index, buffer, offset, function_pointer);
	}
	private static native void nglBindBufferOffsetEXT(int target, int index, int buffer, long offset, long function_pointer);

	public static void glBindBufferBaseEXT(int target, int index, int buffer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_transform_feedback_glBindBufferBaseEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglBindBufferBaseEXT(target, index, buffer, function_pointer);
	}
	private static native void nglBindBufferBaseEXT(int target, int index, int buffer, long function_pointer);

	public static void glBeginTransformFeedbackEXT(int primitiveMode) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_transform_feedback_glBeginTransformFeedbackEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglBeginTransformFeedbackEXT(primitiveMode, function_pointer);
	}
	private static native void nglBeginTransformFeedbackEXT(int primitiveMode, long function_pointer);

	public static void glEndTransformFeedbackEXT() {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_transform_feedback_glEndTransformFeedbackEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglEndTransformFeedbackEXT(function_pointer);
	}
	private static native void nglEndTransformFeedbackEXT(long function_pointer);

	public static void glTransformFeedbackVaryingsEXT(int program, int count, ByteBuffer varyings, int bufferMode) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_transform_feedback_glTransformFeedbackVaryingsEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		varyings = NondirectBufferWrapper.wrapDirect(varyings);
		BufferChecks.checkNullTerminated(varyings);
		nglTransformFeedbackVaryingsEXT(program, count, varyings, varyings.position(), bufferMode, function_pointer);
	}
	private static native void nglTransformFeedbackVaryingsEXT(int program, int count, ByteBuffer varyings, int varyings_position, int bufferMode, long function_pointer);

	public static void glGetTransformFeedbackVaryingEXT(int program, int index, IntBuffer length, IntBuffer size, IntBuffer type, ByteBuffer name) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_transform_feedback_glGetTransformFeedbackVaryingEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer length_saved = length;
		if (length != null)
			length = NondirectBufferWrapper.wrapNoCopyBuffer(length, 1);
		IntBuffer size_saved = size;
		if (size != null)
			size = NondirectBufferWrapper.wrapNoCopyBuffer(size, 1);
		IntBuffer type_saved = type;
		if (type != null)
			type = NondirectBufferWrapper.wrapNoCopyBuffer(type, 1);
		name = NondirectBufferWrapper.wrapDirect(name);
		nglGetTransformFeedbackVaryingEXT(program, index, (name.remaining()), length, length != null ? length.position() : 0, size, size != null ? size.position() : 0, type, type != null ? type.position() : 0, name, name.position(), function_pointer);
		NondirectBufferWrapper.copy(length, length_saved);
		NondirectBufferWrapper.copy(size, size_saved);
		NondirectBufferWrapper.copy(type, type_saved);
	}
	private static native void nglGetTransformFeedbackVaryingEXT(int program, int index, int bufSize, IntBuffer length, int length_position, IntBuffer size, int size_position, IntBuffer type, int type_position, ByteBuffer name, int name_position, long function_pointer);
}
