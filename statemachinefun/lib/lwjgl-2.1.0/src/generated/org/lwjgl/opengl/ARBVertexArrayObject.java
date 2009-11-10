/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.LWJGLException;
import org.lwjgl.BufferChecks;
import org.lwjgl.NondirectBufferWrapper;
import java.nio.*;

public final class ARBVertexArrayObject {
	/**
	 * Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv,
	 * GetFloatv, and GetDoublev:
	 */
	public static final int GL_VERTEX_ARRAY_BINDING = 0x85b5;

	private ARBVertexArrayObject() {
	}


	public static void glBindVertexArray(int array) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.ARB_vertex_array_object_glBindVertexArray_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglBindVertexArray(array, function_pointer);
	}
	private static native void nglBindVertexArray(int array, long function_pointer);

	public static void glDeleteVertexArrays(IntBuffer arrays) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.ARB_vertex_array_object_glDeleteVertexArrays_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		arrays = NondirectBufferWrapper.wrapDirect(arrays);
		nglDeleteVertexArrays((arrays.remaining()), arrays, arrays.position(), function_pointer);
	}
	private static native void nglDeleteVertexArrays(int n, IntBuffer arrays, int arrays_position, long function_pointer);

	public static void glGenVertexArrays(IntBuffer arrays) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.ARB_vertex_array_object_glGenVertexArrays_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer arrays_saved = arrays;
		arrays = NondirectBufferWrapper.wrapNoCopyDirect(arrays);
		nglGenVertexArrays((arrays.remaining()), arrays, arrays.position(), function_pointer);
		NondirectBufferWrapper.copy(arrays, arrays_saved);
	}
	private static native void nglGenVertexArrays(int n, IntBuffer arrays, int arrays_position, long function_pointer);

	public static boolean glIsVertexArray(int array) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.ARB_vertex_array_object_glIsVertexArray_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		boolean __result = nglIsVertexArray(array, function_pointer);
		return __result;
	}
	private static native boolean nglIsVertexArray(int array, long function_pointer);
}
