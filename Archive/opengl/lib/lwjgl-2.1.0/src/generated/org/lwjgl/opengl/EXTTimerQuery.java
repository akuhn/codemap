/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.LWJGLException;
import org.lwjgl.BufferChecks;
import org.lwjgl.NondirectBufferWrapper;
import java.nio.*;

public final class EXTTimerQuery {
	/**
	 * Accepted by the &lt;target&gt; parameter of BeginQuery, EndQuery, and
	 * GetQueryiv:
	 */
	public static final int GL_TIME_ELAPSED_EXT = 0x88bf;

	private EXTTimerQuery() {
	}


	public static void glGetQueryObjectEXT(int id, int pname, LongBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_timer_query_glGetQueryObjecti64vEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		LongBuffer params_saved = params;
		params = NondirectBufferWrapper.wrapNoCopyBuffer(params, 1);
		nglGetQueryObjecti64vEXT(id, pname, params, params.position(), function_pointer);
		NondirectBufferWrapper.copy(params, params_saved);
	}
	private static native void nglGetQueryObjecti64vEXT(int id, int pname, LongBuffer params, int params_position, long function_pointer);

	public static void glGetQueryObjectuEXT(int id, int pname, LongBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_timer_query_glGetQueryObjectui64vEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		LongBuffer params_saved = params;
		params = NondirectBufferWrapper.wrapNoCopyBuffer(params, 1);
		nglGetQueryObjectui64vEXT(id, pname, params, params.position(), function_pointer);
		NondirectBufferWrapper.copy(params, params_saved);
	}
	private static native void nglGetQueryObjectui64vEXT(int id, int pname, LongBuffer params, int params_position, long function_pointer);
}
