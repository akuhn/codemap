/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.LWJGLException;
import org.lwjgl.BufferChecks;
import org.lwjgl.NondirectBufferWrapper;
import java.nio.*;

public final class NVParameterBufferObject {
	/**
	 *Accepted by the &lt;pname&gt; parameter of GetProgramivARB: 
	 */
	public static final int GL_MAX_PROGRAM_PARAMETER_BUFFER_BINDINGS_NV = 0x8da0;
	public static final int GL_MAX_PROGRAM_PARAMETER_BUFFER_SIZE_NV = 0x8da1;
	/**
	 * Accepted by the &lt;target&gt; parameter of ProgramBufferParametersfvNV,
	 * ProgramBufferParametersIivNV, and ProgramBufferParametersIuivNV,
	 * BindBufferRangeNV, BindBufferOffsetNV, BindBufferBaseNV, and BindBuffer
	 * and the &lt;value&gt; parameter of GetIntegerIndexedvEXT:
	 */
	public static final int GL_VERTEX_PROGRAM_PARAMETER_BUFFER_NV = 0x8da2;
	public static final int GL_GEOMETRY_PROGRAM_PARAMETER_BUFFER_NV = 0x8da3;
	public static final int GL_FRAGMENT_PROGRAM_PARAMETER_BUFFER_NV = 0x8da4;

	private NVParameterBufferObject() {
	}


	public static void glProgramBufferParametersNV(int target, int buffer, int index, FloatBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.NV_parameter_buffer_object_glProgramBufferParametersfvNV_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		params = NondirectBufferWrapper.wrapDirect(params);
		nglProgramBufferParametersfvNV(target, buffer, index, (params.remaining()) >> 2, params, params.position(), function_pointer);
	}
	private static native void nglProgramBufferParametersfvNV(int target, int buffer, int index, int count, FloatBuffer params, int params_position, long function_pointer);

	public static void glProgramBufferParametersINV(int target, int buffer, int index, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.NV_parameter_buffer_object_glProgramBufferParametersIivNV_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		params = NondirectBufferWrapper.wrapDirect(params);
		nglProgramBufferParametersIivNV(target, buffer, index, (params.remaining()) >> 2, params, params.position(), function_pointer);
	}
	private static native void nglProgramBufferParametersIivNV(int target, int buffer, int index, int count, IntBuffer params, int params_position, long function_pointer);

	public static void glProgramBufferParametersIuNV(int target, int buffer, int index, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.NV_parameter_buffer_object_glProgramBufferParametersIuivNV_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		params = NondirectBufferWrapper.wrapDirect(params);
		nglProgramBufferParametersIuivNV(target, buffer, index, (params.remaining()) >> 2, params, params.position(), function_pointer);
	}
	private static native void nglProgramBufferParametersIuivNV(int target, int buffer, int index, int count, IntBuffer params, int params_position, long function_pointer);
}
