/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.LWJGLException;
import org.lwjgl.BufferChecks;
import org.lwjgl.NondirectBufferWrapper;
import java.nio.*;

public final class ARBDrawInstanced {

	private ARBDrawInstanced() {
	}


	public static void glDrawArraysInstancedARB(int mode, int first, int count, int primcount) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.ARB_draw_instanced_glDrawArraysInstancedARB_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglDrawArraysInstancedARB(mode, first, count, primcount, function_pointer);
	}
	private static native void nglDrawArraysInstancedARB(int mode, int first, int count, int primcount, long function_pointer);

	public static void glDrawElementsInstancedARB(int mode, ByteBuffer indices, int primcount) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.ARB_draw_instanced_glDrawElementsInstancedARB_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureElementVBOdisabled(caps);
		indices = NondirectBufferWrapper.wrapDirect(indices);
		nglDrawElementsInstancedARB(mode, (indices.remaining()), GL11.GL_UNSIGNED_BYTE, indices, indices.position(), primcount, function_pointer);
	}
	public static void glDrawElementsInstancedARB(int mode, IntBuffer indices, int primcount) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.ARB_draw_instanced_glDrawElementsInstancedARB_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureElementVBOdisabled(caps);
		indices = NondirectBufferWrapper.wrapDirect(indices);
		nglDrawElementsInstancedARB(mode, (indices.remaining()), GL11.GL_UNSIGNED_INT, indices, indices.position() << 2, primcount, function_pointer);
	}
	public static void glDrawElementsInstancedARB(int mode, ShortBuffer indices, int primcount) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.ARB_draw_instanced_glDrawElementsInstancedARB_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureElementVBOdisabled(caps);
		indices = NondirectBufferWrapper.wrapDirect(indices);
		nglDrawElementsInstancedARB(mode, (indices.remaining()), GL11.GL_UNSIGNED_SHORT, indices, indices.position() << 1, primcount, function_pointer);
	}
	private static native void nglDrawElementsInstancedARB(int mode, int count, int type, Buffer indices, int indices_position, int primcount, long function_pointer);
	public static void glDrawElementsInstancedARB(int mode, int count, int type, long indices_buffer_offset, int primcount) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.ARB_draw_instanced_glDrawElementsInstancedARB_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureElementVBOenabled(caps);
		nglDrawElementsInstancedARBBO(mode, count, type, indices_buffer_offset, primcount, function_pointer);
	}
	private static native void nglDrawElementsInstancedARBBO(int mode, int count, int type, long indices_buffer_offset, int primcount, long function_pointer);
}
