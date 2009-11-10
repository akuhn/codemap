/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.LWJGLException;
import org.lwjgl.BufferChecks;
import org.lwjgl.NondirectBufferWrapper;
import java.nio.*;

public final class NVConditionalRender {
	/**
	 *  Accepted by the &lt;mode&gt; parameter of BeginConditionalRenderNV:
	 */
	public static final int GL_QUERY_WAIT_NV = 0x8e13;
	public static final int GL_QUERY_NO_WAIT_NV = 0x8e14;
	public static final int GL_QUERY_BY_REGION_WAIT_NV = 0x8e15;
	public static final int GL_QUERY_BY_REGION_NO_WAIT_NV = 0x8e16;

	private NVConditionalRender() {
	}


	public static void glBeginConditionalRenderNV(int id, int mode) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.NV_conditional_render_glBeginConditionalRenderNV_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglBeginConditionalRenderNV(id, mode, function_pointer);
	}
	private static native void nglBeginConditionalRenderNV(int id, int mode, long function_pointer);

	public static void glEndConditionalRenderNV() {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.NV_conditional_render_glEndConditionalRenderNV_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglEndConditionalRenderNV(function_pointer);
	}
	private static native void nglEndConditionalRenderNV(long function_pointer);
}
