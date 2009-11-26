/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.LWJGLException;
import org.lwjgl.BufferChecks;
import org.lwjgl.NondirectBufferWrapper;
import java.nio.*;

public final class ARBMapBufferRange {
	/**
	 *Accepted by the &lt;access&gt; parameter of MapBufferRange: 
	 */
	public static final int GL_MAP_READ_BIT = 0x1;
	public static final int GL_MAP_WRITE_BIT = 0x2;
	public static final int GL_MAP_INVALIDATE_RANGE_BIT = 0x4;
	public static final int GL_MAP_INVALIDATE_BUFFER_BIT = 0x8;
	public static final int GL_MAP_FLUSH_EXPLICIT_BIT = 0x10;
	public static final int GL_MAP_UNSYNCHRONIZED_BIT = 0x20;

	private ARBMapBufferRange() {
	}


	public static java.nio.ByteBuffer glMapBufferRange(int target, long offset, long length, int access, long result_size, java.nio.ByteBuffer old_buffer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.ARB_map_buffer_range_glMapBufferRange_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (old_buffer != null)
			BufferChecks.checkDirect(old_buffer);
		java.nio.ByteBuffer __result = nglMapBufferRange(target, offset, length, access, result_size, old_buffer, function_pointer);
		return __result;
	}
	private static native java.nio.ByteBuffer nglMapBufferRange(int target, long offset, long length, int access, long result_size, java.nio.ByteBuffer old_buffer, long function_pointer);

	public static void glFlushMappedBufferRange(int target, long offset, long length) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.ARB_map_buffer_range_glFlushMappedBufferRange_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglFlushMappedBufferRange(target, offset, length, function_pointer);
	}
	private static native void nglFlushMappedBufferRange(int target, long offset, long length, long function_pointer);
}
