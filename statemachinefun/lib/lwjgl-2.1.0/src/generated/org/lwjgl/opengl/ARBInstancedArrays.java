/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.LWJGLException;
import org.lwjgl.BufferChecks;
import org.lwjgl.NondirectBufferWrapper;
import java.nio.*;

public final class ARBInstancedArrays {
	/**
	 * Accepted by the &lt;pname&gt; parameters of GetVertexAttribdv,
	 * GetVertexAttribfv, and GetVertexAttribiv:
	 */
	public static final int GL_VERTEX_ATTRIB_ARRAY_DIVISOR_ARB = 0x88fe;

	private ARBInstancedArrays() {
	}


	public static void glVertexAttribDivisorARB(int index, int divisor) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.ARB_instanced_arrays_glVertexAttribDivisorARB_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttribDivisorARB(index, divisor, function_pointer);
	}
	private static native void nglVertexAttribDivisorARB(int index, int divisor, long function_pointer);
}
