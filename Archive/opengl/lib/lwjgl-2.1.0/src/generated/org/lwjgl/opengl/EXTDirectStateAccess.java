/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.LWJGLException;
import org.lwjgl.BufferChecks;
import org.lwjgl.NondirectBufferWrapper;
import java.nio.*;

public final class EXTDirectStateAccess {

	private EXTDirectStateAccess() {
	}


	public static void glClientAttribDefaultEXT(int mask) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glClientAttribDefaultEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglClientAttribDefaultEXT(mask, function_pointer);
	}
	private static native void nglClientAttribDefaultEXT(int mask, long function_pointer);

	public static void glPushClientAttribDefaultEXT(int mask) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glPushClientAttribDefaultEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglPushClientAttribDefaultEXT(mask, function_pointer);
	}
	private static native void nglPushClientAttribDefaultEXT(int mask, long function_pointer);

	public static void glMatrixLoadEXT(int matrixMode, FloatBuffer m) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glMatrixLoadfEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		m = NondirectBufferWrapper.wrapBuffer(m, 16);
		nglMatrixLoadfEXT(matrixMode, m, m.position(), function_pointer);
	}
	private static native void nglMatrixLoadfEXT(int matrixMode, FloatBuffer m, int m_position, long function_pointer);

	public static void glMatrixLoadEXT(int matrixMode, DoubleBuffer m) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glMatrixLoaddEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		m = NondirectBufferWrapper.wrapBuffer(m, 16);
		nglMatrixLoaddEXT(matrixMode, m, m.position(), function_pointer);
	}
	private static native void nglMatrixLoaddEXT(int matrixMode, DoubleBuffer m, int m_position, long function_pointer);

	public static void glMatrixMultEXT(int matrixMode, FloatBuffer m) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glMatrixMultfEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		m = NondirectBufferWrapper.wrapBuffer(m, 16);
		nglMatrixMultfEXT(matrixMode, m, m.position(), function_pointer);
	}
	private static native void nglMatrixMultfEXT(int matrixMode, FloatBuffer m, int m_position, long function_pointer);

	public static void glMatrixMultEXT(int matrixMode, DoubleBuffer m) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glMatrixMultdEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		m = NondirectBufferWrapper.wrapBuffer(m, 16);
		nglMatrixMultdEXT(matrixMode, m, m.position(), function_pointer);
	}
	private static native void nglMatrixMultdEXT(int matrixMode, DoubleBuffer m, int m_position, long function_pointer);

	public static void glMatrixLoadIdentityEXT(int matrixMode) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glMatrixLoadIdentityEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglMatrixLoadIdentityEXT(matrixMode, function_pointer);
	}
	private static native void nglMatrixLoadIdentityEXT(int matrixMode, long function_pointer);

	public static void glMatrixRotatefEXT(int matrixMode, float angle, float x, float y, float z) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glMatrixRotatefEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglMatrixRotatefEXT(matrixMode, angle, x, y, z, function_pointer);
	}
	private static native void nglMatrixRotatefEXT(int matrixMode, float angle, float x, float y, float z, long function_pointer);

	public static void glMatrixRotatedEXT(int matrixMode, double angle, double x, double y, double z) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glMatrixRotatedEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglMatrixRotatedEXT(matrixMode, angle, x, y, z, function_pointer);
	}
	private static native void nglMatrixRotatedEXT(int matrixMode, double angle, double x, double y, double z, long function_pointer);

	public static void glMatrixScalefEXT(int matrixMode, float x, float y, float z) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glMatrixScalefEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglMatrixScalefEXT(matrixMode, x, y, z, function_pointer);
	}
	private static native void nglMatrixScalefEXT(int matrixMode, float x, float y, float z, long function_pointer);

	public static void glMatrixScaledEXT(int matrixMode, double x, double y, double z) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glMatrixScaledEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglMatrixScaledEXT(matrixMode, x, y, z, function_pointer);
	}
	private static native void nglMatrixScaledEXT(int matrixMode, double x, double y, double z, long function_pointer);

	public static void glMatrixTranslatefEXT(int matrixMode, float x, float y, float z) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glMatrixTranslatefEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglMatrixTranslatefEXT(matrixMode, x, y, z, function_pointer);
	}
	private static native void nglMatrixTranslatefEXT(int matrixMode, float x, float y, float z, long function_pointer);

	public static void glMatrixTranslatedEXT(int matrixMode, double x, double y, double z) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glMatrixTranslatedEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglMatrixTranslatedEXT(matrixMode, x, y, z, function_pointer);
	}
	private static native void nglMatrixTranslatedEXT(int matrixMode, double x, double y, double z, long function_pointer);

	public static void glMatrixOrthoEXT(int matrixMode, double l, double r, double b, double t, double n, double f) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glMatrixOrthoEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglMatrixOrthoEXT(matrixMode, l, r, b, t, n, f, function_pointer);
	}
	private static native void nglMatrixOrthoEXT(int matrixMode, double l, double r, double b, double t, double n, double f, long function_pointer);

	public static void glMatrixFrustumEXT(int matrixMode, double l, double r, double b, double t, double n, double f) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glMatrixFrustumEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglMatrixFrustumEXT(matrixMode, l, r, b, t, n, f, function_pointer);
	}
	private static native void nglMatrixFrustumEXT(int matrixMode, double l, double r, double b, double t, double n, double f, long function_pointer);

	public static void glMatrixPushEXT(int matrixMode) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glMatrixPushEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglMatrixPushEXT(matrixMode, function_pointer);
	}
	private static native void nglMatrixPushEXT(int matrixMode, long function_pointer);

	public static void glMatrixPopEXT(int matrixMode) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glMatrixPopEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglMatrixPopEXT(matrixMode, function_pointer);
	}
	private static native void nglMatrixPopEXT(int matrixMode, long function_pointer);

	public static void glTextureParameteriEXT(int texture, int target, int pname, int param) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glTextureParameteriEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglTextureParameteriEXT(texture, target, pname, param, function_pointer);
	}
	private static native void nglTextureParameteriEXT(int texture, int target, int pname, int param, long function_pointer);

	public static void glTextureParameterEXT(int texture, int target, int pname, IntBuffer param) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glTextureParameterivEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		param = NondirectBufferWrapper.wrapBuffer(param, 4);
		nglTextureParameterivEXT(texture, target, pname, param, param.position(), function_pointer);
	}
	private static native void nglTextureParameterivEXT(int texture, int target, int pname, IntBuffer param, int param_position, long function_pointer);

	public static void glTextureParameterfEXT(int texture, int target, int pname, float param) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glTextureParameterfEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglTextureParameterfEXT(texture, target, pname, param, function_pointer);
	}
	private static native void nglTextureParameterfEXT(int texture, int target, int pname, float param, long function_pointer);

	public static void glTextureParameterEXT(int texture, int target, int pname, FloatBuffer param) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glTextureParameterfvEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		param = NondirectBufferWrapper.wrapBuffer(param, 4);
		nglTextureParameterfvEXT(texture, target, pname, param, param.position(), function_pointer);
	}
	private static native void nglTextureParameterfvEXT(int texture, int target, int pname, FloatBuffer param, int param_position, long function_pointer);

	public static void glTextureImage1DEXT(int texture, int target, int level, int internalformat, int width, int border, int format, int type, ByteBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glTextureImage1DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		if (pixels != null)
			pixels = NondirectBufferWrapper.wrapBuffer(pixels, GLChecks.calculateTexImage1DStorage(pixels, format, type, width));
		nglTextureImage1DEXT(texture, target, level, internalformat, width, border, format, type, pixels, pixels != null ? pixels.position() : 0, function_pointer);
	}
	public static void glTextureImage1DEXT(int texture, int target, int level, int internalformat, int width, int border, int format, int type, DoubleBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glTextureImage1DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		if (pixels != null)
			pixels = NondirectBufferWrapper.wrapBuffer(pixels, GLChecks.calculateTexImage1DStorage(pixels, format, type, width));
		nglTextureImage1DEXT(texture, target, level, internalformat, width, border, format, type, pixels, pixels != null ? pixels.position() << 3 : 0, function_pointer);
	}
	public static void glTextureImage1DEXT(int texture, int target, int level, int internalformat, int width, int border, int format, int type, FloatBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glTextureImage1DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		if (pixels != null)
			pixels = NondirectBufferWrapper.wrapBuffer(pixels, GLChecks.calculateTexImage1DStorage(pixels, format, type, width));
		nglTextureImage1DEXT(texture, target, level, internalformat, width, border, format, type, pixels, pixels != null ? pixels.position() << 2 : 0, function_pointer);
	}
	public static void glTextureImage1DEXT(int texture, int target, int level, int internalformat, int width, int border, int format, int type, IntBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glTextureImage1DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		if (pixels != null)
			pixels = NondirectBufferWrapper.wrapBuffer(pixels, GLChecks.calculateTexImage1DStorage(pixels, format, type, width));
		nglTextureImage1DEXT(texture, target, level, internalformat, width, border, format, type, pixels, pixels != null ? pixels.position() << 2 : 0, function_pointer);
	}
	public static void glTextureImage1DEXT(int texture, int target, int level, int internalformat, int width, int border, int format, int type, ShortBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glTextureImage1DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		if (pixels != null)
			pixels = NondirectBufferWrapper.wrapBuffer(pixels, GLChecks.calculateTexImage1DStorage(pixels, format, type, width));
		nglTextureImage1DEXT(texture, target, level, internalformat, width, border, format, type, pixels, pixels != null ? pixels.position() << 1 : 0, function_pointer);
	}
	private static native void nglTextureImage1DEXT(int texture, int target, int level, int internalformat, int width, int border, int format, int type, Buffer pixels, int pixels_position, long function_pointer);
	public static void glTextureImage1DEXT(int texture, int target, int level, int internalformat, int width, int border, int format, int type, long pixels_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glTextureImage1DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOenabled(caps);
		nglTextureImage1DEXTBO(texture, target, level, internalformat, width, border, format, type, pixels_buffer_offset, function_pointer);
	}
	private static native void nglTextureImage1DEXTBO(int texture, int target, int level, int internalformat, int width, int border, int format, int type, long pixels_buffer_offset, long function_pointer);

	public static void glTextureImage2DEXT(int texture, int target, int level, int internalformat, int width, int height, int border, int format, int type, ByteBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glTextureImage2DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		if (pixels != null)
			pixels = NondirectBufferWrapper.wrapBuffer(pixels, GLChecks.calculateTexImage2DStorage(pixels, format, type, width, height));
		nglTextureImage2DEXT(texture, target, level, internalformat, width, height, border, format, type, pixels, pixels != null ? pixels.position() : 0, function_pointer);
	}
	public static void glTextureImage2DEXT(int texture, int target, int level, int internalformat, int width, int height, int border, int format, int type, DoubleBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glTextureImage2DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		if (pixels != null)
			pixels = NondirectBufferWrapper.wrapBuffer(pixels, GLChecks.calculateTexImage2DStorage(pixels, format, type, width, height));
		nglTextureImage2DEXT(texture, target, level, internalformat, width, height, border, format, type, pixels, pixels != null ? pixels.position() << 3 : 0, function_pointer);
	}
	public static void glTextureImage2DEXT(int texture, int target, int level, int internalformat, int width, int height, int border, int format, int type, FloatBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glTextureImage2DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		if (pixels != null)
			pixels = NondirectBufferWrapper.wrapBuffer(pixels, GLChecks.calculateTexImage2DStorage(pixels, format, type, width, height));
		nglTextureImage2DEXT(texture, target, level, internalformat, width, height, border, format, type, pixels, pixels != null ? pixels.position() << 2 : 0, function_pointer);
	}
	public static void glTextureImage2DEXT(int texture, int target, int level, int internalformat, int width, int height, int border, int format, int type, IntBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glTextureImage2DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		if (pixels != null)
			pixels = NondirectBufferWrapper.wrapBuffer(pixels, GLChecks.calculateTexImage2DStorage(pixels, format, type, width, height));
		nglTextureImage2DEXT(texture, target, level, internalformat, width, height, border, format, type, pixels, pixels != null ? pixels.position() << 2 : 0, function_pointer);
	}
	public static void glTextureImage2DEXT(int texture, int target, int level, int internalformat, int width, int height, int border, int format, int type, ShortBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glTextureImage2DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		if (pixels != null)
			pixels = NondirectBufferWrapper.wrapBuffer(pixels, GLChecks.calculateTexImage2DStorage(pixels, format, type, width, height));
		nglTextureImage2DEXT(texture, target, level, internalformat, width, height, border, format, type, pixels, pixels != null ? pixels.position() << 1 : 0, function_pointer);
	}
	private static native void nglTextureImage2DEXT(int texture, int target, int level, int internalformat, int width, int height, int border, int format, int type, Buffer pixels, int pixels_position, long function_pointer);
	public static void glTextureImage2DEXT(int texture, int target, int level, int internalformat, int width, int height, int border, int format, int type, long pixels_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glTextureImage2DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOenabled(caps);
		nglTextureImage2DEXTBO(texture, target, level, internalformat, width, height, border, format, type, pixels_buffer_offset, function_pointer);
	}
	private static native void nglTextureImage2DEXTBO(int texture, int target, int level, int internalformat, int width, int height, int border, int format, int type, long pixels_buffer_offset, long function_pointer);

	public static void glTextureSubImage1DEXT(int texture, int target, int level, int xoffset, int width, int format, int type, ByteBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glTextureSubImage1DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		pixels = NondirectBufferWrapper.wrapBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, 1, 1));
		nglTextureSubImage1DEXT(texture, target, level, xoffset, width, format, type, pixels, pixels.position(), function_pointer);
	}
	public static void glTextureSubImage1DEXT(int texture, int target, int level, int xoffset, int width, int format, int type, DoubleBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glTextureSubImage1DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		pixels = NondirectBufferWrapper.wrapBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, 1, 1));
		nglTextureSubImage1DEXT(texture, target, level, xoffset, width, format, type, pixels, pixels.position() << 3, function_pointer);
	}
	public static void glTextureSubImage1DEXT(int texture, int target, int level, int xoffset, int width, int format, int type, FloatBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glTextureSubImage1DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		pixels = NondirectBufferWrapper.wrapBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, 1, 1));
		nglTextureSubImage1DEXT(texture, target, level, xoffset, width, format, type, pixels, pixels.position() << 2, function_pointer);
	}
	public static void glTextureSubImage1DEXT(int texture, int target, int level, int xoffset, int width, int format, int type, IntBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glTextureSubImage1DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		pixels = NondirectBufferWrapper.wrapBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, 1, 1));
		nglTextureSubImage1DEXT(texture, target, level, xoffset, width, format, type, pixels, pixels.position() << 2, function_pointer);
	}
	public static void glTextureSubImage1DEXT(int texture, int target, int level, int xoffset, int width, int format, int type, ShortBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glTextureSubImage1DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		pixels = NondirectBufferWrapper.wrapBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, 1, 1));
		nglTextureSubImage1DEXT(texture, target, level, xoffset, width, format, type, pixels, pixels.position() << 1, function_pointer);
	}
	private static native void nglTextureSubImage1DEXT(int texture, int target, int level, int xoffset, int width, int format, int type, Buffer pixels, int pixels_position, long function_pointer);
	public static void glTextureSubImage1DEXT(int texture, int target, int level, int xoffset, int width, int format, int type, long pixels_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glTextureSubImage1DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOenabled(caps);
		nglTextureSubImage1DEXTBO(texture, target, level, xoffset, width, format, type, pixels_buffer_offset, function_pointer);
	}
	private static native void nglTextureSubImage1DEXTBO(int texture, int target, int level, int xoffset, int width, int format, int type, long pixels_buffer_offset, long function_pointer);

	public static void glTextureSubImage2DEXT(int texture, int target, int level, int xoffset, int yoffset, int width, int height, int format, int type, ByteBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glTextureSubImage2DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		pixels = NondirectBufferWrapper.wrapBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, 1));
		nglTextureSubImage2DEXT(texture, target, level, xoffset, yoffset, width, height, format, type, pixels, pixels.position(), function_pointer);
	}
	public static void glTextureSubImage2DEXT(int texture, int target, int level, int xoffset, int yoffset, int width, int height, int format, int type, DoubleBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glTextureSubImage2DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		pixels = NondirectBufferWrapper.wrapBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, 1));
		nglTextureSubImage2DEXT(texture, target, level, xoffset, yoffset, width, height, format, type, pixels, pixels.position() << 3, function_pointer);
	}
	public static void glTextureSubImage2DEXT(int texture, int target, int level, int xoffset, int yoffset, int width, int height, int format, int type, FloatBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glTextureSubImage2DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		pixels = NondirectBufferWrapper.wrapBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, 1));
		nglTextureSubImage2DEXT(texture, target, level, xoffset, yoffset, width, height, format, type, pixels, pixels.position() << 2, function_pointer);
	}
	public static void glTextureSubImage2DEXT(int texture, int target, int level, int xoffset, int yoffset, int width, int height, int format, int type, IntBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glTextureSubImage2DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		pixels = NondirectBufferWrapper.wrapBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, 1));
		nglTextureSubImage2DEXT(texture, target, level, xoffset, yoffset, width, height, format, type, pixels, pixels.position() << 2, function_pointer);
	}
	public static void glTextureSubImage2DEXT(int texture, int target, int level, int xoffset, int yoffset, int width, int height, int format, int type, ShortBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glTextureSubImage2DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		pixels = NondirectBufferWrapper.wrapBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, 1));
		nglTextureSubImage2DEXT(texture, target, level, xoffset, yoffset, width, height, format, type, pixels, pixels.position() << 1, function_pointer);
	}
	private static native void nglTextureSubImage2DEXT(int texture, int target, int level, int xoffset, int yoffset, int width, int height, int format, int type, Buffer pixels, int pixels_position, long function_pointer);
	public static void glTextureSubImage2DEXT(int texture, int target, int level, int xoffset, int yoffset, int width, int height, int format, int type, long pixels_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glTextureSubImage2DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOenabled(caps);
		nglTextureSubImage2DEXTBO(texture, target, level, xoffset, yoffset, width, height, format, type, pixels_buffer_offset, function_pointer);
	}
	private static native void nglTextureSubImage2DEXTBO(int texture, int target, int level, int xoffset, int yoffset, int width, int height, int format, int type, long pixels_buffer_offset, long function_pointer);

	public static void glCopyTextureImage1DEXT(int texture, int target, int level, int internalformat, int x, int y, int width, int border) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glCopyTextureImage1DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglCopyTextureImage1DEXT(texture, target, level, internalformat, x, y, width, border, function_pointer);
	}
	private static native void nglCopyTextureImage1DEXT(int texture, int target, int level, int internalformat, int x, int y, int width, int border, long function_pointer);

	public static void glCopyTextureImage2DEXT(int texture, int target, int level, int internalformat, int x, int y, int width, int height, int border) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glCopyTextureImage2DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglCopyTextureImage2DEXT(texture, target, level, internalformat, x, y, width, height, border, function_pointer);
	}
	private static native void nglCopyTextureImage2DEXT(int texture, int target, int level, int internalformat, int x, int y, int width, int height, int border, long function_pointer);

	public static void glCopyTextureSubImage1DEXT(int texture, int target, int level, int xoffset, int x, int y, int width) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glCopyTextureSubImage1DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglCopyTextureSubImage1DEXT(texture, target, level, xoffset, x, y, width, function_pointer);
	}
	private static native void nglCopyTextureSubImage1DEXT(int texture, int target, int level, int xoffset, int x, int y, int width, long function_pointer);

	public static void glCopyTextureSubImage2DEXT(int texture, int target, int level, int xoffset, int yoffset, int x, int y, int width, int height) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glCopyTextureSubImage2DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglCopyTextureSubImage2DEXT(texture, target, level, xoffset, yoffset, x, y, width, height, function_pointer);
	}
	private static native void nglCopyTextureSubImage2DEXT(int texture, int target, int level, int xoffset, int yoffset, int x, int y, int width, int height, long function_pointer);

	public static void glGetTextureImageEXT(int texture, int target, int level, int format, int type, ByteBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glGetTextureImageEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		ByteBuffer pixels_saved = pixels;
		pixels = NondirectBufferWrapper.wrapNoCopyBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, 1, 1, 1));
		nglGetTextureImageEXT(texture, target, level, format, type, pixels, pixels.position(), function_pointer);
		NondirectBufferWrapper.copy(pixels, pixels_saved);
	}
	public static void glGetTextureImageEXT(int texture, int target, int level, int format, int type, DoubleBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glGetTextureImageEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		DoubleBuffer pixels_saved = pixels;
		pixels = NondirectBufferWrapper.wrapNoCopyBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, 1, 1, 1));
		nglGetTextureImageEXT(texture, target, level, format, type, pixels, pixels.position() << 3, function_pointer);
		NondirectBufferWrapper.copy(pixels, pixels_saved);
	}
	public static void glGetTextureImageEXT(int texture, int target, int level, int format, int type, FloatBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glGetTextureImageEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		FloatBuffer pixels_saved = pixels;
		pixels = NondirectBufferWrapper.wrapNoCopyBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, 1, 1, 1));
		nglGetTextureImageEXT(texture, target, level, format, type, pixels, pixels.position() << 2, function_pointer);
		NondirectBufferWrapper.copy(pixels, pixels_saved);
	}
	public static void glGetTextureImageEXT(int texture, int target, int level, int format, int type, IntBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glGetTextureImageEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		IntBuffer pixels_saved = pixels;
		pixels = NondirectBufferWrapper.wrapNoCopyBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, 1, 1, 1));
		nglGetTextureImageEXT(texture, target, level, format, type, pixels, pixels.position() << 2, function_pointer);
		NondirectBufferWrapper.copy(pixels, pixels_saved);
	}
	public static void glGetTextureImageEXT(int texture, int target, int level, int format, int type, ShortBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glGetTextureImageEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		ShortBuffer pixels_saved = pixels;
		pixels = NondirectBufferWrapper.wrapNoCopyBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, 1, 1, 1));
		nglGetTextureImageEXT(texture, target, level, format, type, pixels, pixels.position() << 1, function_pointer);
		NondirectBufferWrapper.copy(pixels, pixels_saved);
	}
	private static native void nglGetTextureImageEXT(int texture, int target, int level, int format, int type, Buffer pixels, int pixels_position, long function_pointer);
	public static void glGetTextureImageEXT(int texture, int target, int level, int format, int type, long pixels_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glGetTextureImageEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOenabled(caps);
		nglGetTextureImageEXTBO(texture, target, level, format, type, pixels_buffer_offset, function_pointer);
	}
	private static native void nglGetTextureImageEXTBO(int texture, int target, int level, int format, int type, long pixels_buffer_offset, long function_pointer);

	public static void glGetTextureParameterEXT(int texture, int target, int pname, FloatBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glGetTextureParameterfvEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		FloatBuffer params_saved = params;
		params = NondirectBufferWrapper.wrapNoCopyBuffer(params, 4);
		nglGetTextureParameterfvEXT(texture, target, pname, params, params.position(), function_pointer);
		NondirectBufferWrapper.copy(params, params_saved);
	}
	private static native void nglGetTextureParameterfvEXT(int texture, int target, int pname, FloatBuffer params, int params_position, long function_pointer);

	public static void glGetTextureParameterEXT(int texture, int target, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glGetTextureParameterivEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer params_saved = params;
		params = NondirectBufferWrapper.wrapNoCopyBuffer(params, 4);
		nglGetTextureParameterivEXT(texture, target, pname, params, params.position(), function_pointer);
		NondirectBufferWrapper.copy(params, params_saved);
	}
	private static native void nglGetTextureParameterivEXT(int texture, int target, int pname, IntBuffer params, int params_position, long function_pointer);

	public static void glGetTextureLevelParameterEXT(int texture, int target, int level, int pname, FloatBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glGetTextureLevelParameterfvEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		FloatBuffer params_saved = params;
		params = NondirectBufferWrapper.wrapNoCopyBuffer(params, 4);
		nglGetTextureLevelParameterfvEXT(texture, target, level, pname, params, params.position(), function_pointer);
		NondirectBufferWrapper.copy(params, params_saved);
	}
	private static native void nglGetTextureLevelParameterfvEXT(int texture, int target, int level, int pname, FloatBuffer params, int params_position, long function_pointer);

	public static void glGetTextureLevelParameterEXT(int texture, int target, int level, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glGetTextureLevelParameterivEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer params_saved = params;
		params = NondirectBufferWrapper.wrapNoCopyBuffer(params, 4);
		nglGetTextureLevelParameterivEXT(texture, target, level, pname, params, params.position(), function_pointer);
		NondirectBufferWrapper.copy(params, params_saved);
	}
	private static native void nglGetTextureLevelParameterivEXT(int texture, int target, int level, int pname, IntBuffer params, int params_position, long function_pointer);

	public static void glTextureImage3DEXT(int texture, int target, int level, int internalformat, int width, int height, int depth, int border, int format, int type, ByteBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glTextureImage3DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		if (pixels != null)
			pixels = NondirectBufferWrapper.wrapBuffer(pixels, GLChecks.calculateTexImage3DStorage(pixels, format, type, width, height, depth));
		nglTextureImage3DEXT(texture, target, level, internalformat, width, height, depth, border, format, type, pixels, pixels != null ? pixels.position() : 0, function_pointer);
	}
	public static void glTextureImage3DEXT(int texture, int target, int level, int internalformat, int width, int height, int depth, int border, int format, int type, DoubleBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glTextureImage3DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		if (pixels != null)
			pixels = NondirectBufferWrapper.wrapBuffer(pixels, GLChecks.calculateTexImage3DStorage(pixels, format, type, width, height, depth));
		nglTextureImage3DEXT(texture, target, level, internalformat, width, height, depth, border, format, type, pixels, pixels != null ? pixels.position() << 3 : 0, function_pointer);
	}
	public static void glTextureImage3DEXT(int texture, int target, int level, int internalformat, int width, int height, int depth, int border, int format, int type, FloatBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glTextureImage3DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		if (pixels != null)
			pixels = NondirectBufferWrapper.wrapBuffer(pixels, GLChecks.calculateTexImage3DStorage(pixels, format, type, width, height, depth));
		nglTextureImage3DEXT(texture, target, level, internalformat, width, height, depth, border, format, type, pixels, pixels != null ? pixels.position() << 2 : 0, function_pointer);
	}
	public static void glTextureImage3DEXT(int texture, int target, int level, int internalformat, int width, int height, int depth, int border, int format, int type, IntBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glTextureImage3DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		if (pixels != null)
			pixels = NondirectBufferWrapper.wrapBuffer(pixels, GLChecks.calculateTexImage3DStorage(pixels, format, type, width, height, depth));
		nglTextureImage3DEXT(texture, target, level, internalformat, width, height, depth, border, format, type, pixels, pixels != null ? pixels.position() << 2 : 0, function_pointer);
	}
	public static void glTextureImage3DEXT(int texture, int target, int level, int internalformat, int width, int height, int depth, int border, int format, int type, ShortBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glTextureImage3DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		if (pixels != null)
			pixels = NondirectBufferWrapper.wrapBuffer(pixels, GLChecks.calculateTexImage3DStorage(pixels, format, type, width, height, depth));
		nglTextureImage3DEXT(texture, target, level, internalformat, width, height, depth, border, format, type, pixels, pixels != null ? pixels.position() << 1 : 0, function_pointer);
	}
	private static native void nglTextureImage3DEXT(int texture, int target, int level, int internalformat, int width, int height, int depth, int border, int format, int type, Buffer pixels, int pixels_position, long function_pointer);
	public static void glTextureImage3DEXT(int texture, int target, int level, int internalformat, int width, int height, int depth, int border, int format, int type, long pixels_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glTextureImage3DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOenabled(caps);
		nglTextureImage3DEXTBO(texture, target, level, internalformat, width, height, depth, border, format, type, pixels_buffer_offset, function_pointer);
	}
	private static native void nglTextureImage3DEXTBO(int texture, int target, int level, int internalformat, int width, int height, int depth, int border, int format, int type, long pixels_buffer_offset, long function_pointer);

	public static void glTextureSubImage3DEXT(int texture, int target, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, ByteBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glTextureSubImage3DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		pixels = NondirectBufferWrapper.wrapBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, depth));
		nglTextureSubImage3DEXT(texture, target, level, xoffset, yoffset, zoffset, width, height, depth, format, type, pixels, pixels.position(), function_pointer);
	}
	public static void glTextureSubImage3DEXT(int texture, int target, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, DoubleBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glTextureSubImage3DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		pixels = NondirectBufferWrapper.wrapBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, depth));
		nglTextureSubImage3DEXT(texture, target, level, xoffset, yoffset, zoffset, width, height, depth, format, type, pixels, pixels.position() << 3, function_pointer);
	}
	public static void glTextureSubImage3DEXT(int texture, int target, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, FloatBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glTextureSubImage3DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		pixels = NondirectBufferWrapper.wrapBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, depth));
		nglTextureSubImage3DEXT(texture, target, level, xoffset, yoffset, zoffset, width, height, depth, format, type, pixels, pixels.position() << 2, function_pointer);
	}
	public static void glTextureSubImage3DEXT(int texture, int target, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, IntBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glTextureSubImage3DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		pixels = NondirectBufferWrapper.wrapBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, depth));
		nglTextureSubImage3DEXT(texture, target, level, xoffset, yoffset, zoffset, width, height, depth, format, type, pixels, pixels.position() << 2, function_pointer);
	}
	public static void glTextureSubImage3DEXT(int texture, int target, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, ShortBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glTextureSubImage3DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		pixels = NondirectBufferWrapper.wrapBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, depth));
		nglTextureSubImage3DEXT(texture, target, level, xoffset, yoffset, zoffset, width, height, depth, format, type, pixels, pixels.position() << 1, function_pointer);
	}
	private static native void nglTextureSubImage3DEXT(int texture, int target, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, Buffer pixels, int pixels_position, long function_pointer);
	public static void glTextureSubImage3DEXT(int texture, int target, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, long pixels_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glTextureSubImage3DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOenabled(caps);
		nglTextureSubImage3DEXTBO(texture, target, level, xoffset, yoffset, zoffset, width, height, depth, format, type, pixels_buffer_offset, function_pointer);
	}
	private static native void nglTextureSubImage3DEXTBO(int texture, int target, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, long pixels_buffer_offset, long function_pointer);

	public static void glCopyTextureSubImage3DEXT(int texture, int target, int level, int xoffset, int yoffset, int zoffset, int x, int y, int width, int height) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glCopyTextureSubImage3DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglCopyTextureSubImage3DEXT(texture, target, level, xoffset, yoffset, zoffset, x, y, width, height, function_pointer);
	}
	private static native void nglCopyTextureSubImage3DEXT(int texture, int target, int level, int xoffset, int yoffset, int zoffset, int x, int y, int width, int height, long function_pointer);

	public static void glBindMultiTextureEXT(int texunit, int target, int texture) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glBindMultiTextureEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglBindMultiTextureEXT(texunit, target, texture, function_pointer);
	}
	private static native void nglBindMultiTextureEXT(int texunit, int target, int texture, long function_pointer);

	public static void glMultiTexCoordPointerEXT(int texunit, int size, int stride, DoubleBuffer pointer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glMultiTexCoordPointerEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOdisabled(caps);
		pointer = NondirectBufferWrapper.wrapDirect(pointer);
		nglMultiTexCoordPointerEXT(texunit, size, GL11.GL_DOUBLE, stride, pointer, pointer.position() << 3, function_pointer);
	}
	public static void glMultiTexCoordPointerEXT(int texunit, int size, int stride, FloatBuffer pointer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glMultiTexCoordPointerEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOdisabled(caps);
		pointer = NondirectBufferWrapper.wrapDirect(pointer);
		nglMultiTexCoordPointerEXT(texunit, size, GL11.GL_FLOAT, stride, pointer, pointer.position() << 2, function_pointer);
	}
	private static native void nglMultiTexCoordPointerEXT(int texunit, int size, int type, int stride, Buffer pointer, int pointer_position, long function_pointer);
	public static void glMultiTexCoordPointerEXT(int texunit, int size, int type, int stride, long pointer_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glMultiTexCoordPointerEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOenabled(caps);
		nglMultiTexCoordPointerEXTBO(texunit, size, type, stride, pointer_buffer_offset, function_pointer);
	}
	private static native void nglMultiTexCoordPointerEXTBO(int texunit, int size, int type, int stride, long pointer_buffer_offset, long function_pointer);

	public static void glMultiTexEnvfEXT(int texunit, int target, int pname, float param) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glMultiTexEnvfEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglMultiTexEnvfEXT(texunit, target, pname, param, function_pointer);
	}
	private static native void nglMultiTexEnvfEXT(int texunit, int target, int pname, float param, long function_pointer);

	public static void glMultiTexEnvEXT(int texunit, int target, int pname, FloatBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glMultiTexEnvfvEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		params = NondirectBufferWrapper.wrapBuffer(params, 4);
		nglMultiTexEnvfvEXT(texunit, target, pname, params, params.position(), function_pointer);
	}
	private static native void nglMultiTexEnvfvEXT(int texunit, int target, int pname, FloatBuffer params, int params_position, long function_pointer);

	public static void glMultiTexEnviEXT(int texunit, int target, int pname, int param) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glMultiTexEnviEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglMultiTexEnviEXT(texunit, target, pname, param, function_pointer);
	}
	private static native void nglMultiTexEnviEXT(int texunit, int target, int pname, int param, long function_pointer);

	public static void glMultiTexEnvEXT(int texunit, int target, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glMultiTexEnvivEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		params = NondirectBufferWrapper.wrapBuffer(params, 4);
		nglMultiTexEnvivEXT(texunit, target, pname, params, params.position(), function_pointer);
	}
	private static native void nglMultiTexEnvivEXT(int texunit, int target, int pname, IntBuffer params, int params_position, long function_pointer);

	public static void glMultiTexGendEXT(int texunit, int coord, int pname, double param) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glMultiTexGendEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglMultiTexGendEXT(texunit, coord, pname, param, function_pointer);
	}
	private static native void nglMultiTexGendEXT(int texunit, int coord, int pname, double param, long function_pointer);

	public static void glMultiTexGenEXT(int texunit, int coord, int pname, DoubleBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glMultiTexGendvEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		params = NondirectBufferWrapper.wrapBuffer(params, 4);
		nglMultiTexGendvEXT(texunit, coord, pname, params, params.position(), function_pointer);
	}
	private static native void nglMultiTexGendvEXT(int texunit, int coord, int pname, DoubleBuffer params, int params_position, long function_pointer);

	public static void glMultiTexGenfEXT(int texunit, int coord, int pname, float param) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glMultiTexGenfEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglMultiTexGenfEXT(texunit, coord, pname, param, function_pointer);
	}
	private static native void nglMultiTexGenfEXT(int texunit, int coord, int pname, float param, long function_pointer);

	public static void glMultiTexGenEXT(int texunit, int coord, int pname, FloatBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glMultiTexGenfvEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		params = NondirectBufferWrapper.wrapBuffer(params, 4);
		nglMultiTexGenfvEXT(texunit, coord, pname, params, params.position(), function_pointer);
	}
	private static native void nglMultiTexGenfvEXT(int texunit, int coord, int pname, FloatBuffer params, int params_position, long function_pointer);

	public static void glMultiTexGeniEXT(int texunit, int coord, int pname, int param) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glMultiTexGeniEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglMultiTexGeniEXT(texunit, coord, pname, param, function_pointer);
	}
	private static native void nglMultiTexGeniEXT(int texunit, int coord, int pname, int param, long function_pointer);

	public static void glMultiTexGenEXT(int texunit, int coord, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glMultiTexGenivEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		params = NondirectBufferWrapper.wrapBuffer(params, 4);
		nglMultiTexGenivEXT(texunit, coord, pname, params, params.position(), function_pointer);
	}
	private static native void nglMultiTexGenivEXT(int texunit, int coord, int pname, IntBuffer params, int params_position, long function_pointer);

	public static void glGetMultiTexEnvEXT(int texunit, int target, int pname, FloatBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glGetMultiTexEnvfvEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		FloatBuffer params_saved = params;
		params = NondirectBufferWrapper.wrapNoCopyBuffer(params, 4);
		nglGetMultiTexEnvfvEXT(texunit, target, pname, params, params.position(), function_pointer);
		NondirectBufferWrapper.copy(params, params_saved);
	}
	private static native void nglGetMultiTexEnvfvEXT(int texunit, int target, int pname, FloatBuffer params, int params_position, long function_pointer);

	public static void glGetMultiTexEnvEXT(int texunit, int target, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glGetMultiTexEnvivEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer params_saved = params;
		params = NondirectBufferWrapper.wrapNoCopyBuffer(params, 4);
		nglGetMultiTexEnvivEXT(texunit, target, pname, params, params.position(), function_pointer);
		NondirectBufferWrapper.copy(params, params_saved);
	}
	private static native void nglGetMultiTexEnvivEXT(int texunit, int target, int pname, IntBuffer params, int params_position, long function_pointer);

	public static void glGetMultiTexGenEXT(int texunit, int coord, int pname, DoubleBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glGetMultiTexGendvEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		DoubleBuffer params_saved = params;
		params = NondirectBufferWrapper.wrapNoCopyBuffer(params, 4);
		nglGetMultiTexGendvEXT(texunit, coord, pname, params, params.position(), function_pointer);
		NondirectBufferWrapper.copy(params, params_saved);
	}
	private static native void nglGetMultiTexGendvEXT(int texunit, int coord, int pname, DoubleBuffer params, int params_position, long function_pointer);

	public static void glGetMultiTexGenEXT(int texunit, int coord, int pname, FloatBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glGetMultiTexGenfvEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		FloatBuffer params_saved = params;
		params = NondirectBufferWrapper.wrapNoCopyBuffer(params, 4);
		nglGetMultiTexGenfvEXT(texunit, coord, pname, params, params.position(), function_pointer);
		NondirectBufferWrapper.copy(params, params_saved);
	}
	private static native void nglGetMultiTexGenfvEXT(int texunit, int coord, int pname, FloatBuffer params, int params_position, long function_pointer);

	public static void glGetMultiTexGenEXT(int texunit, int coord, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glGetMultiTexGenivEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer params_saved = params;
		params = NondirectBufferWrapper.wrapNoCopyBuffer(params, 4);
		nglGetMultiTexGenivEXT(texunit, coord, pname, params, params.position(), function_pointer);
		NondirectBufferWrapper.copy(params, params_saved);
	}
	private static native void nglGetMultiTexGenivEXT(int texunit, int coord, int pname, IntBuffer params, int params_position, long function_pointer);

	public static void glMultiTexParameteriEXT(int texunit, int target, int pname, int param) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glMultiTexParameteriEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglMultiTexParameteriEXT(texunit, target, pname, param, function_pointer);
	}
	private static native void nglMultiTexParameteriEXT(int texunit, int target, int pname, int param, long function_pointer);

	public static void glMultiTexParameterEXT(int texunit, int target, int pname, IntBuffer param) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glMultiTexParameterivEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		param = NondirectBufferWrapper.wrapBuffer(param, 4);
		nglMultiTexParameterivEXT(texunit, target, pname, param, param.position(), function_pointer);
	}
	private static native void nglMultiTexParameterivEXT(int texunit, int target, int pname, IntBuffer param, int param_position, long function_pointer);

	public static void glMultiTexParameterfEXT(int texunit, int target, int pname, float param) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glMultiTexParameterfEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglMultiTexParameterfEXT(texunit, target, pname, param, function_pointer);
	}
	private static native void nglMultiTexParameterfEXT(int texunit, int target, int pname, float param, long function_pointer);

	public static void glMultiTexParameterEXT(int texunit, int target, int pname, FloatBuffer param) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glMultiTexParameterfvEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		param = NondirectBufferWrapper.wrapBuffer(param, 4);
		nglMultiTexParameterfvEXT(texunit, target, pname, param, param.position(), function_pointer);
	}
	private static native void nglMultiTexParameterfvEXT(int texunit, int target, int pname, FloatBuffer param, int param_position, long function_pointer);

	public static void glMultiTexImage1DEXT(int texunit, int target, int level, int internalformat, int width, int border, int format, int type, ByteBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glMultiTexImage1DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		if (pixels != null)
			pixels = NondirectBufferWrapper.wrapBuffer(pixels, GLChecks.calculateTexImage1DStorage(pixels, format, type, width));
		nglMultiTexImage1DEXT(texunit, target, level, internalformat, width, border, format, type, pixels, pixels != null ? pixels.position() : 0, function_pointer);
	}
	public static void glMultiTexImage1DEXT(int texunit, int target, int level, int internalformat, int width, int border, int format, int type, DoubleBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glMultiTexImage1DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		if (pixels != null)
			pixels = NondirectBufferWrapper.wrapBuffer(pixels, GLChecks.calculateTexImage1DStorage(pixels, format, type, width));
		nglMultiTexImage1DEXT(texunit, target, level, internalformat, width, border, format, type, pixels, pixels != null ? pixels.position() << 3 : 0, function_pointer);
	}
	public static void glMultiTexImage1DEXT(int texunit, int target, int level, int internalformat, int width, int border, int format, int type, FloatBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glMultiTexImage1DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		if (pixels != null)
			pixels = NondirectBufferWrapper.wrapBuffer(pixels, GLChecks.calculateTexImage1DStorage(pixels, format, type, width));
		nglMultiTexImage1DEXT(texunit, target, level, internalformat, width, border, format, type, pixels, pixels != null ? pixels.position() << 2 : 0, function_pointer);
	}
	public static void glMultiTexImage1DEXT(int texunit, int target, int level, int internalformat, int width, int border, int format, int type, IntBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glMultiTexImage1DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		if (pixels != null)
			pixels = NondirectBufferWrapper.wrapBuffer(pixels, GLChecks.calculateTexImage1DStorage(pixels, format, type, width));
		nglMultiTexImage1DEXT(texunit, target, level, internalformat, width, border, format, type, pixels, pixels != null ? pixels.position() << 2 : 0, function_pointer);
	}
	public static void glMultiTexImage1DEXT(int texunit, int target, int level, int internalformat, int width, int border, int format, int type, ShortBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glMultiTexImage1DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		if (pixels != null)
			pixels = NondirectBufferWrapper.wrapBuffer(pixels, GLChecks.calculateTexImage1DStorage(pixels, format, type, width));
		nglMultiTexImage1DEXT(texunit, target, level, internalformat, width, border, format, type, pixels, pixels != null ? pixels.position() << 1 : 0, function_pointer);
	}
	private static native void nglMultiTexImage1DEXT(int texunit, int target, int level, int internalformat, int width, int border, int format, int type, Buffer pixels, int pixels_position, long function_pointer);
	public static void glMultiTexImage1DEXT(int texunit, int target, int level, int internalformat, int width, int border, int format, int type, long pixels_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glMultiTexImage1DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOenabled(caps);
		nglMultiTexImage1DEXTBO(texunit, target, level, internalformat, width, border, format, type, pixels_buffer_offset, function_pointer);
	}
	private static native void nglMultiTexImage1DEXTBO(int texunit, int target, int level, int internalformat, int width, int border, int format, int type, long pixels_buffer_offset, long function_pointer);

	public static void glMultiTexImage2DEXT(int texunit, int target, int level, int internalformat, int width, int height, int border, int format, int type, ByteBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glMultiTexImage2DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		if (pixels != null)
			pixels = NondirectBufferWrapper.wrapBuffer(pixels, GLChecks.calculateTexImage2DStorage(pixels, format, type, width, height));
		nglMultiTexImage2DEXT(texunit, target, level, internalformat, width, height, border, format, type, pixels, pixels != null ? pixels.position() : 0, function_pointer);
	}
	public static void glMultiTexImage2DEXT(int texunit, int target, int level, int internalformat, int width, int height, int border, int format, int type, DoubleBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glMultiTexImage2DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		if (pixels != null)
			pixels = NondirectBufferWrapper.wrapBuffer(pixels, GLChecks.calculateTexImage2DStorage(pixels, format, type, width, height));
		nglMultiTexImage2DEXT(texunit, target, level, internalformat, width, height, border, format, type, pixels, pixels != null ? pixels.position() << 3 : 0, function_pointer);
	}
	public static void glMultiTexImage2DEXT(int texunit, int target, int level, int internalformat, int width, int height, int border, int format, int type, FloatBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glMultiTexImage2DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		if (pixels != null)
			pixels = NondirectBufferWrapper.wrapBuffer(pixels, GLChecks.calculateTexImage2DStorage(pixels, format, type, width, height));
		nglMultiTexImage2DEXT(texunit, target, level, internalformat, width, height, border, format, type, pixels, pixels != null ? pixels.position() << 2 : 0, function_pointer);
	}
	public static void glMultiTexImage2DEXT(int texunit, int target, int level, int internalformat, int width, int height, int border, int format, int type, IntBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glMultiTexImage2DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		if (pixels != null)
			pixels = NondirectBufferWrapper.wrapBuffer(pixels, GLChecks.calculateTexImage2DStorage(pixels, format, type, width, height));
		nglMultiTexImage2DEXT(texunit, target, level, internalformat, width, height, border, format, type, pixels, pixels != null ? pixels.position() << 2 : 0, function_pointer);
	}
	public static void glMultiTexImage2DEXT(int texunit, int target, int level, int internalformat, int width, int height, int border, int format, int type, ShortBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glMultiTexImage2DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		if (pixels != null)
			pixels = NondirectBufferWrapper.wrapBuffer(pixels, GLChecks.calculateTexImage2DStorage(pixels, format, type, width, height));
		nglMultiTexImage2DEXT(texunit, target, level, internalformat, width, height, border, format, type, pixels, pixels != null ? pixels.position() << 1 : 0, function_pointer);
	}
	private static native void nglMultiTexImage2DEXT(int texunit, int target, int level, int internalformat, int width, int height, int border, int format, int type, Buffer pixels, int pixels_position, long function_pointer);
	public static void glMultiTexImage2DEXT(int texunit, int target, int level, int internalformat, int width, int height, int border, int format, int type, long pixels_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glMultiTexImage2DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOenabled(caps);
		nglMultiTexImage2DEXTBO(texunit, target, level, internalformat, width, height, border, format, type, pixels_buffer_offset, function_pointer);
	}
	private static native void nglMultiTexImage2DEXTBO(int texunit, int target, int level, int internalformat, int width, int height, int border, int format, int type, long pixels_buffer_offset, long function_pointer);

	public static void glMultiTexSubImage1DEXT(int texunit, int target, int level, int xoffset, int width, int format, int type, ByteBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glMultiTexSubImage1DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		pixels = NondirectBufferWrapper.wrapBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, 1, 1));
		nglMultiTexSubImage1DEXT(texunit, target, level, xoffset, width, format, type, pixels, pixels.position(), function_pointer);
	}
	public static void glMultiTexSubImage1DEXT(int texunit, int target, int level, int xoffset, int width, int format, int type, DoubleBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glMultiTexSubImage1DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		pixels = NondirectBufferWrapper.wrapBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, 1, 1));
		nglMultiTexSubImage1DEXT(texunit, target, level, xoffset, width, format, type, pixels, pixels.position() << 3, function_pointer);
	}
	public static void glMultiTexSubImage1DEXT(int texunit, int target, int level, int xoffset, int width, int format, int type, FloatBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glMultiTexSubImage1DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		pixels = NondirectBufferWrapper.wrapBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, 1, 1));
		nglMultiTexSubImage1DEXT(texunit, target, level, xoffset, width, format, type, pixels, pixels.position() << 2, function_pointer);
	}
	public static void glMultiTexSubImage1DEXT(int texunit, int target, int level, int xoffset, int width, int format, int type, IntBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glMultiTexSubImage1DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		pixels = NondirectBufferWrapper.wrapBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, 1, 1));
		nglMultiTexSubImage1DEXT(texunit, target, level, xoffset, width, format, type, pixels, pixels.position() << 2, function_pointer);
	}
	public static void glMultiTexSubImage1DEXT(int texunit, int target, int level, int xoffset, int width, int format, int type, ShortBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glMultiTexSubImage1DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		pixels = NondirectBufferWrapper.wrapBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, 1, 1));
		nglMultiTexSubImage1DEXT(texunit, target, level, xoffset, width, format, type, pixels, pixels.position() << 1, function_pointer);
	}
	private static native void nglMultiTexSubImage1DEXT(int texunit, int target, int level, int xoffset, int width, int format, int type, Buffer pixels, int pixels_position, long function_pointer);
	public static void glMultiTexSubImage1DEXT(int texunit, int target, int level, int xoffset, int width, int format, int type, long pixels_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glMultiTexSubImage1DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOenabled(caps);
		nglMultiTexSubImage1DEXTBO(texunit, target, level, xoffset, width, format, type, pixels_buffer_offset, function_pointer);
	}
	private static native void nglMultiTexSubImage1DEXTBO(int texunit, int target, int level, int xoffset, int width, int format, int type, long pixels_buffer_offset, long function_pointer);

	public static void glMultiTexSubImage2DEXT(int texunit, int target, int level, int xoffset, int yoffset, int width, int height, int format, int type, ByteBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glMultiTexSubImage2DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		pixels = NondirectBufferWrapper.wrapBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, 1));
		nglMultiTexSubImage2DEXT(texunit, target, level, xoffset, yoffset, width, height, format, type, pixels, pixels.position(), function_pointer);
	}
	public static void glMultiTexSubImage2DEXT(int texunit, int target, int level, int xoffset, int yoffset, int width, int height, int format, int type, DoubleBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glMultiTexSubImage2DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		pixels = NondirectBufferWrapper.wrapBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, 1));
		nglMultiTexSubImage2DEXT(texunit, target, level, xoffset, yoffset, width, height, format, type, pixels, pixels.position() << 3, function_pointer);
	}
	public static void glMultiTexSubImage2DEXT(int texunit, int target, int level, int xoffset, int yoffset, int width, int height, int format, int type, FloatBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glMultiTexSubImage2DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		pixels = NondirectBufferWrapper.wrapBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, 1));
		nglMultiTexSubImage2DEXT(texunit, target, level, xoffset, yoffset, width, height, format, type, pixels, pixels.position() << 2, function_pointer);
	}
	public static void glMultiTexSubImage2DEXT(int texunit, int target, int level, int xoffset, int yoffset, int width, int height, int format, int type, IntBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glMultiTexSubImage2DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		pixels = NondirectBufferWrapper.wrapBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, 1));
		nglMultiTexSubImage2DEXT(texunit, target, level, xoffset, yoffset, width, height, format, type, pixels, pixels.position() << 2, function_pointer);
	}
	public static void glMultiTexSubImage2DEXT(int texunit, int target, int level, int xoffset, int yoffset, int width, int height, int format, int type, ShortBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glMultiTexSubImage2DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		pixels = NondirectBufferWrapper.wrapBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, 1));
		nglMultiTexSubImage2DEXT(texunit, target, level, xoffset, yoffset, width, height, format, type, pixels, pixels.position() << 1, function_pointer);
	}
	private static native void nglMultiTexSubImage2DEXT(int texunit, int target, int level, int xoffset, int yoffset, int width, int height, int format, int type, Buffer pixels, int pixels_position, long function_pointer);
	public static void glMultiTexSubImage2DEXT(int texunit, int target, int level, int xoffset, int yoffset, int width, int height, int format, int type, long pixels_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glMultiTexSubImage2DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOenabled(caps);
		nglMultiTexSubImage2DEXTBO(texunit, target, level, xoffset, yoffset, width, height, format, type, pixels_buffer_offset, function_pointer);
	}
	private static native void nglMultiTexSubImage2DEXTBO(int texunit, int target, int level, int xoffset, int yoffset, int width, int height, int format, int type, long pixels_buffer_offset, long function_pointer);

	public static void glCopyMultiTexImage1DEXT(int texunit, int target, int level, int internalformat, int x, int y, int width, int border) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glCopyMultiTexImage1DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglCopyMultiTexImage1DEXT(texunit, target, level, internalformat, x, y, width, border, function_pointer);
	}
	private static native void nglCopyMultiTexImage1DEXT(int texunit, int target, int level, int internalformat, int x, int y, int width, int border, long function_pointer);

	public static void glCopyMultiTexImage2DEXT(int texunit, int target, int level, int internalformat, int x, int y, int width, int height, int border) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glCopyMultiTexImage2DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglCopyMultiTexImage2DEXT(texunit, target, level, internalformat, x, y, width, height, border, function_pointer);
	}
	private static native void nglCopyMultiTexImage2DEXT(int texunit, int target, int level, int internalformat, int x, int y, int width, int height, int border, long function_pointer);

	public static void glCopyMultiTexSubImage1DEXT(int texunit, int target, int level, int xoffset, int x, int y, int width) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glCopyMultiTexSubImage1DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglCopyMultiTexSubImage1DEXT(texunit, target, level, xoffset, x, y, width, function_pointer);
	}
	private static native void nglCopyMultiTexSubImage1DEXT(int texunit, int target, int level, int xoffset, int x, int y, int width, long function_pointer);

	public static void glCopyMultiTexSubImage2DEXT(int texunit, int target, int level, int xoffset, int yoffset, int x, int y, int width, int height) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glCopyMultiTexSubImage2DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglCopyMultiTexSubImage2DEXT(texunit, target, level, xoffset, yoffset, x, y, width, height, function_pointer);
	}
	private static native void nglCopyMultiTexSubImage2DEXT(int texunit, int target, int level, int xoffset, int yoffset, int x, int y, int width, int height, long function_pointer);

	public static void glGetMultiTexImageEXT(int texunit, int target, int level, int format, int type, ByteBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glGetMultiTexImageEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		ByteBuffer pixels_saved = pixels;
		pixels = NondirectBufferWrapper.wrapNoCopyBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, 1, 1, 1));
		nglGetMultiTexImageEXT(texunit, target, level, format, type, pixels, pixels.position(), function_pointer);
		NondirectBufferWrapper.copy(pixels, pixels_saved);
	}
	public static void glGetMultiTexImageEXT(int texunit, int target, int level, int format, int type, DoubleBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glGetMultiTexImageEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		DoubleBuffer pixels_saved = pixels;
		pixels = NondirectBufferWrapper.wrapNoCopyBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, 1, 1, 1));
		nglGetMultiTexImageEXT(texunit, target, level, format, type, pixels, pixels.position() << 3, function_pointer);
		NondirectBufferWrapper.copy(pixels, pixels_saved);
	}
	public static void glGetMultiTexImageEXT(int texunit, int target, int level, int format, int type, FloatBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glGetMultiTexImageEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		FloatBuffer pixels_saved = pixels;
		pixels = NondirectBufferWrapper.wrapNoCopyBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, 1, 1, 1));
		nglGetMultiTexImageEXT(texunit, target, level, format, type, pixels, pixels.position() << 2, function_pointer);
		NondirectBufferWrapper.copy(pixels, pixels_saved);
	}
	public static void glGetMultiTexImageEXT(int texunit, int target, int level, int format, int type, IntBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glGetMultiTexImageEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		IntBuffer pixels_saved = pixels;
		pixels = NondirectBufferWrapper.wrapNoCopyBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, 1, 1, 1));
		nglGetMultiTexImageEXT(texunit, target, level, format, type, pixels, pixels.position() << 2, function_pointer);
		NondirectBufferWrapper.copy(pixels, pixels_saved);
	}
	public static void glGetMultiTexImageEXT(int texunit, int target, int level, int format, int type, ShortBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glGetMultiTexImageEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		ShortBuffer pixels_saved = pixels;
		pixels = NondirectBufferWrapper.wrapNoCopyBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, 1, 1, 1));
		nglGetMultiTexImageEXT(texunit, target, level, format, type, pixels, pixels.position() << 1, function_pointer);
		NondirectBufferWrapper.copy(pixels, pixels_saved);
	}
	private static native void nglGetMultiTexImageEXT(int texunit, int target, int level, int format, int type, Buffer pixels, int pixels_position, long function_pointer);
	public static void glGetMultiTexImageEXT(int texunit, int target, int level, int format, int type, long pixels_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glGetMultiTexImageEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOenabled(caps);
		nglGetMultiTexImageEXTBO(texunit, target, level, format, type, pixels_buffer_offset, function_pointer);
	}
	private static native void nglGetMultiTexImageEXTBO(int texunit, int target, int level, int format, int type, long pixels_buffer_offset, long function_pointer);

	public static void glGetMultiTexParameterEXT(int texunit, int target, int pname, FloatBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glGetMultiTexParameterfvEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		FloatBuffer params_saved = params;
		params = NondirectBufferWrapper.wrapNoCopyBuffer(params, 4);
		nglGetMultiTexParameterfvEXT(texunit, target, pname, params, params.position(), function_pointer);
		NondirectBufferWrapper.copy(params, params_saved);
	}
	private static native void nglGetMultiTexParameterfvEXT(int texunit, int target, int pname, FloatBuffer params, int params_position, long function_pointer);

	public static void glGetMultiTexParameterEXT(int texunit, int target, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glGetMultiTexParameterivEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer params_saved = params;
		params = NondirectBufferWrapper.wrapNoCopyBuffer(params, 4);
		nglGetMultiTexParameterivEXT(texunit, target, pname, params, params.position(), function_pointer);
		NondirectBufferWrapper.copy(params, params_saved);
	}
	private static native void nglGetMultiTexParameterivEXT(int texunit, int target, int pname, IntBuffer params, int params_position, long function_pointer);

	public static void glGetMultiTexLevelParameterEXT(int texunit, int target, int level, int pname, FloatBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glGetMultiTexLevelParameterfvEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		FloatBuffer params_saved = params;
		params = NondirectBufferWrapper.wrapNoCopyBuffer(params, 4);
		nglGetMultiTexLevelParameterfvEXT(texunit, target, level, pname, params, params.position(), function_pointer);
		NondirectBufferWrapper.copy(params, params_saved);
	}
	private static native void nglGetMultiTexLevelParameterfvEXT(int texunit, int target, int level, int pname, FloatBuffer params, int params_position, long function_pointer);

	public static void glGetMultiTexLevelParameterEXT(int texunit, int target, int level, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glGetMultiTexLevelParameterivEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer params_saved = params;
		params = NondirectBufferWrapper.wrapNoCopyBuffer(params, 4);
		nglGetMultiTexLevelParameterivEXT(texunit, target, level, pname, params, params.position(), function_pointer);
		NondirectBufferWrapper.copy(params, params_saved);
	}
	private static native void nglGetMultiTexLevelParameterivEXT(int texunit, int target, int level, int pname, IntBuffer params, int params_position, long function_pointer);

	public static void glMultiTexImage3DEXT(int texunit, int target, int level, int internalformat, int width, int height, int depth, int border, int format, int type, ByteBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glMultiTexImage3DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		if (pixels != null)
			pixels = NondirectBufferWrapper.wrapBuffer(pixels, GLChecks.calculateTexImage3DStorage(pixels, format, type, width, height, depth));
		nglMultiTexImage3DEXT(texunit, target, level, internalformat, width, height, depth, border, format, type, pixels, pixels != null ? pixels.position() : 0, function_pointer);
	}
	public static void glMultiTexImage3DEXT(int texunit, int target, int level, int internalformat, int width, int height, int depth, int border, int format, int type, DoubleBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glMultiTexImage3DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		if (pixels != null)
			pixels = NondirectBufferWrapper.wrapBuffer(pixels, GLChecks.calculateTexImage3DStorage(pixels, format, type, width, height, depth));
		nglMultiTexImage3DEXT(texunit, target, level, internalformat, width, height, depth, border, format, type, pixels, pixels != null ? pixels.position() << 3 : 0, function_pointer);
	}
	public static void glMultiTexImage3DEXT(int texunit, int target, int level, int internalformat, int width, int height, int depth, int border, int format, int type, FloatBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glMultiTexImage3DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		if (pixels != null)
			pixels = NondirectBufferWrapper.wrapBuffer(pixels, GLChecks.calculateTexImage3DStorage(pixels, format, type, width, height, depth));
		nglMultiTexImage3DEXT(texunit, target, level, internalformat, width, height, depth, border, format, type, pixels, pixels != null ? pixels.position() << 2 : 0, function_pointer);
	}
	public static void glMultiTexImage3DEXT(int texunit, int target, int level, int internalformat, int width, int height, int depth, int border, int format, int type, IntBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glMultiTexImage3DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		if (pixels != null)
			pixels = NondirectBufferWrapper.wrapBuffer(pixels, GLChecks.calculateTexImage3DStorage(pixels, format, type, width, height, depth));
		nglMultiTexImage3DEXT(texunit, target, level, internalformat, width, height, depth, border, format, type, pixels, pixels != null ? pixels.position() << 2 : 0, function_pointer);
	}
	public static void glMultiTexImage3DEXT(int texunit, int target, int level, int internalformat, int width, int height, int depth, int border, int format, int type, ShortBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glMultiTexImage3DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		if (pixels != null)
			pixels = NondirectBufferWrapper.wrapBuffer(pixels, GLChecks.calculateTexImage3DStorage(pixels, format, type, width, height, depth));
		nglMultiTexImage3DEXT(texunit, target, level, internalformat, width, height, depth, border, format, type, pixels, pixels != null ? pixels.position() << 1 : 0, function_pointer);
	}
	private static native void nglMultiTexImage3DEXT(int texunit, int target, int level, int internalformat, int width, int height, int depth, int border, int format, int type, Buffer pixels, int pixels_position, long function_pointer);
	public static void glMultiTexImage3DEXT(int texunit, int target, int level, int internalformat, int width, int height, int depth, int border, int format, int type, long pixels_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glMultiTexImage3DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOenabled(caps);
		nglMultiTexImage3DEXTBO(texunit, target, level, internalformat, width, height, depth, border, format, type, pixels_buffer_offset, function_pointer);
	}
	private static native void nglMultiTexImage3DEXTBO(int texunit, int target, int level, int internalformat, int width, int height, int depth, int border, int format, int type, long pixels_buffer_offset, long function_pointer);

	public static void glMultiTexSubImage3DEXT(int texunit, int target, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, ByteBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glMultiTexSubImage3DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		pixels = NondirectBufferWrapper.wrapBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, depth));
		nglMultiTexSubImage3DEXT(texunit, target, level, xoffset, yoffset, zoffset, width, height, depth, format, type, pixels, pixels.position(), function_pointer);
	}
	public static void glMultiTexSubImage3DEXT(int texunit, int target, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, DoubleBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glMultiTexSubImage3DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		pixels = NondirectBufferWrapper.wrapBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, depth));
		nglMultiTexSubImage3DEXT(texunit, target, level, xoffset, yoffset, zoffset, width, height, depth, format, type, pixels, pixels.position() << 3, function_pointer);
	}
	public static void glMultiTexSubImage3DEXT(int texunit, int target, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, FloatBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glMultiTexSubImage3DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		pixels = NondirectBufferWrapper.wrapBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, depth));
		nglMultiTexSubImage3DEXT(texunit, target, level, xoffset, yoffset, zoffset, width, height, depth, format, type, pixels, pixels.position() << 2, function_pointer);
	}
	public static void glMultiTexSubImage3DEXT(int texunit, int target, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, IntBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glMultiTexSubImage3DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		pixels = NondirectBufferWrapper.wrapBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, depth));
		nglMultiTexSubImage3DEXT(texunit, target, level, xoffset, yoffset, zoffset, width, height, depth, format, type, pixels, pixels.position() << 2, function_pointer);
	}
	public static void glMultiTexSubImage3DEXT(int texunit, int target, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, ShortBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glMultiTexSubImage3DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		pixels = NondirectBufferWrapper.wrapBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, depth));
		nglMultiTexSubImage3DEXT(texunit, target, level, xoffset, yoffset, zoffset, width, height, depth, format, type, pixels, pixels.position() << 1, function_pointer);
	}
	private static native void nglMultiTexSubImage3DEXT(int texunit, int target, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, Buffer pixels, int pixels_position, long function_pointer);
	public static void glMultiTexSubImage3DEXT(int texunit, int target, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, long pixels_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glMultiTexSubImage3DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOenabled(caps);
		nglMultiTexSubImage3DEXTBO(texunit, target, level, xoffset, yoffset, zoffset, width, height, depth, format, type, pixels_buffer_offset, function_pointer);
	}
	private static native void nglMultiTexSubImage3DEXTBO(int texunit, int target, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, long pixels_buffer_offset, long function_pointer);

	public static void glCopyMultiTexSubImage3DEXT(int texunit, int target, int level, int xoffset, int yoffset, int zoffset, int x, int y, int width, int height) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glCopyMultiTexSubImage3DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglCopyMultiTexSubImage3DEXT(texunit, target, level, xoffset, yoffset, zoffset, x, y, width, height, function_pointer);
	}
	private static native void nglCopyMultiTexSubImage3DEXT(int texunit, int target, int level, int xoffset, int yoffset, int zoffset, int x, int y, int width, int height, long function_pointer);

	public static void glEnableClientStateIndexedEXT(int array, int index) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glEnableClientStateIndexedEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglEnableClientStateIndexedEXT(array, index, function_pointer);
	}
	private static native void nglEnableClientStateIndexedEXT(int array, int index, long function_pointer);

	public static void glDisableClientStateIndexedEXT(int array, int index) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glDisableClientStateIndexedEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglDisableClientStateIndexedEXT(array, index, function_pointer);
	}
	private static native void nglDisableClientStateIndexedEXT(int array, int index, long function_pointer);

	public static void glGetFloatIndexedEXT(int pname, int index, FloatBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glGetFloatIndexedvEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		FloatBuffer params_saved = params;
		params = NondirectBufferWrapper.wrapNoCopyBuffer(params, 16);
		nglGetFloatIndexedvEXT(pname, index, params, params.position(), function_pointer);
		NondirectBufferWrapper.copy(params, params_saved);
	}
	private static native void nglGetFloatIndexedvEXT(int pname, int index, FloatBuffer params, int params_position, long function_pointer);

	public static void glGetDoubleIndexeEXT(int pname, int index, DoubleBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glGetDoubleIndexedvEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		DoubleBuffer params_saved = params;
		params = NondirectBufferWrapper.wrapNoCopyBuffer(params, 16);
		nglGetDoubleIndexedvEXT(pname, index, params, params.position(), function_pointer);
		NondirectBufferWrapper.copy(params, params_saved);
	}
	private static native void nglGetDoubleIndexedvEXT(int pname, int index, DoubleBuffer params, int params_position, long function_pointer);

	public static java.nio.ByteBuffer glGetPointerIndexedEXT(int pname, int index, long result_size) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glGetPointerIndexedvEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		java.nio.ByteBuffer __result = nglGetPointerIndexedvEXT(pname, index, result_size, function_pointer);
		return __result;
	}
	private static native java.nio.ByteBuffer nglGetPointerIndexedvEXT(int pname, int index, long result_size, long function_pointer);

	public static void glEnableIndexedEXT(int cap, int index) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glEnableIndexedEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglEnableIndexedEXT(cap, index, function_pointer);
	}
	private static native void nglEnableIndexedEXT(int cap, int index, long function_pointer);

	public static void glDisableIndexedEXT(int cap, int index) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glDisableIndexedEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglDisableIndexedEXT(cap, index, function_pointer);
	}
	private static native void nglDisableIndexedEXT(int cap, int index, long function_pointer);

	public static boolean glIsEnabledIndexedEXT(int cap, int index) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glIsEnabledIndexedEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		boolean __result = nglIsEnabledIndexedEXT(cap, index, function_pointer);
		return __result;
	}
	private static native boolean nglIsEnabledIndexedEXT(int cap, int index, long function_pointer);

	public static void glGetIntegerIndexedEXT(int pname, int index, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glGetIntegerIndexedvEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer params_saved = params;
		params = NondirectBufferWrapper.wrapNoCopyBuffer(params, 16);
		nglGetIntegerIndexedvEXT(pname, index, params, params.position(), function_pointer);
		NondirectBufferWrapper.copy(params, params_saved);
	}
	private static native void nglGetIntegerIndexedvEXT(int pname, int index, IntBuffer params, int params_position, long function_pointer);

	public static void glGetBooleanIndexedEXT(int pname, int index, ByteBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glGetBooleanIndexedvEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		ByteBuffer params_saved = params;
		params = NondirectBufferWrapper.wrapNoCopyBuffer(params, 16);
		nglGetBooleanIndexedvEXT(pname, index, params, params.position(), function_pointer);
		NondirectBufferWrapper.copy(params, params_saved);
	}
	private static native void nglGetBooleanIndexedvEXT(int pname, int index, ByteBuffer params, int params_position, long function_pointer);

	public static void glNamedProgramStringEXT(int program, int target, int format, ByteBuffer string) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glNamedProgramStringEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		string = NondirectBufferWrapper.wrapDirect(string);
		nglNamedProgramStringEXT(program, target, format, (string.remaining()), string, string.position(), function_pointer);
	}
	private static native void nglNamedProgramStringEXT(int program, int target, int format, int len, Buffer string, int string_position, long function_pointer);

	public static void glNamedProgramLocalParameter4dEXT(int program, int target, int index, double x, double y, double z, double w) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glNamedProgramLocalParameter4dEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglNamedProgramLocalParameter4dEXT(program, target, index, x, y, z, w, function_pointer);
	}
	private static native void nglNamedProgramLocalParameter4dEXT(int program, int target, int index, double x, double y, double z, double w, long function_pointer);

	public static void glNamedProgramLocalParameter4EXT(int program, int target, int index, DoubleBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glNamedProgramLocalParameter4dvEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		params = NondirectBufferWrapper.wrapBuffer(params, 4);
		nglNamedProgramLocalParameter4dvEXT(program, target, index, params, params.position(), function_pointer);
	}
	private static native void nglNamedProgramLocalParameter4dvEXT(int program, int target, int index, DoubleBuffer params, int params_position, long function_pointer);

	public static void glNamedProgramLocalParameter4fEXT(int program, int target, int index, float x, float y, float z, float w) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glNamedProgramLocalParameter4fEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglNamedProgramLocalParameter4fEXT(program, target, index, x, y, z, w, function_pointer);
	}
	private static native void nglNamedProgramLocalParameter4fEXT(int program, int target, int index, float x, float y, float z, float w, long function_pointer);

	public static void glNamedProgramLocalParameter4EXT(int program, int target, int index, FloatBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glNamedProgramLocalParameter4fvEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		params = NondirectBufferWrapper.wrapBuffer(params, 4);
		nglNamedProgramLocalParameter4fvEXT(program, target, index, params, params.position(), function_pointer);
	}
	private static native void nglNamedProgramLocalParameter4fvEXT(int program, int target, int index, FloatBuffer params, int params_position, long function_pointer);

	public static void glGetNamedProgramLocalParameterEXT(int program, int target, int index, DoubleBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glGetNamedProgramLocalParameterdvEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		DoubleBuffer params_saved = params;
		params = NondirectBufferWrapper.wrapNoCopyBuffer(params, 4);
		nglGetNamedProgramLocalParameterdvEXT(program, target, index, params, params.position(), function_pointer);
		NondirectBufferWrapper.copy(params, params_saved);
	}
	private static native void nglGetNamedProgramLocalParameterdvEXT(int program, int target, int index, DoubleBuffer params, int params_position, long function_pointer);

	public static void glGetNamedProgramLocalParameterEXT(int program, int target, int index, FloatBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glGetNamedProgramLocalParameterfvEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		FloatBuffer params_saved = params;
		params = NondirectBufferWrapper.wrapNoCopyBuffer(params, 4);
		nglGetNamedProgramLocalParameterfvEXT(program, target, index, params, params.position(), function_pointer);
		NondirectBufferWrapper.copy(params, params_saved);
	}
	private static native void nglGetNamedProgramLocalParameterfvEXT(int program, int target, int index, FloatBuffer params, int params_position, long function_pointer);

	public static void glGetNamedProgramEXT(int program, int target, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glGetNamedProgramivEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer params_saved = params;
		params = NondirectBufferWrapper.wrapNoCopyBuffer(params, 4);
		nglGetNamedProgramivEXT(program, target, pname, params, params.position(), function_pointer);
		NondirectBufferWrapper.copy(params, params_saved);
	}
	private static native void nglGetNamedProgramivEXT(int program, int target, int pname, IntBuffer params, int params_position, long function_pointer);

	public static void glGetNamedProgramStringEXT(int program, int target, int pname, ByteBuffer string) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glGetNamedProgramStringEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		ByteBuffer string_saved = string;
		string = NondirectBufferWrapper.wrapNoCopyDirect(string);
		nglGetNamedProgramStringEXT(program, target, pname, string, string.position(), function_pointer);
		NondirectBufferWrapper.copy(string, string_saved);
	}
	private static native void nglGetNamedProgramStringEXT(int program, int target, int pname, ByteBuffer string, int string_position, long function_pointer);

	public static void glCompressedTextureImage3DEXT(int texture, int target, int level, int internalformat, int width, int height, int depth, int border, ByteBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glCompressedTextureImage3DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		data = NondirectBufferWrapper.wrapDirect(data);
		nglCompressedTextureImage3DEXT(texture, target, level, internalformat, width, height, depth, border, (data.remaining()), data, data.position(), function_pointer);
	}
	private static native void nglCompressedTextureImage3DEXT(int texture, int target, int level, int internalformat, int width, int height, int depth, int border, int imageSize, ByteBuffer data, int data_position, long function_pointer);
	public static void glCompressedTextureImage3DEXT(int texture, int target, int level, int internalformat, int width, int height, int depth, int border, int imageSize, long data_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glCompressedTextureImage3DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOenabled(caps);
		nglCompressedTextureImage3DEXTBO(texture, target, level, internalformat, width, height, depth, border, imageSize, data_buffer_offset, function_pointer);
	}
	private static native void nglCompressedTextureImage3DEXTBO(int texture, int target, int level, int internalformat, int width, int height, int depth, int border, int imageSize, long data_buffer_offset, long function_pointer);

	public static void glCompressedTextureImage2DEXT(int texture, int target, int level, int internalformat, int width, int height, int border, ByteBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glCompressedTextureImage2DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		data = NondirectBufferWrapper.wrapDirect(data);
		nglCompressedTextureImage2DEXT(texture, target, level, internalformat, width, height, border, (data.remaining()), data, data.position(), function_pointer);
	}
	private static native void nglCompressedTextureImage2DEXT(int texture, int target, int level, int internalformat, int width, int height, int border, int imageSize, ByteBuffer data, int data_position, long function_pointer);
	public static void glCompressedTextureImage2DEXT(int texture, int target, int level, int internalformat, int width, int height, int border, int imageSize, long data_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glCompressedTextureImage2DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOenabled(caps);
		nglCompressedTextureImage2DEXTBO(texture, target, level, internalformat, width, height, border, imageSize, data_buffer_offset, function_pointer);
	}
	private static native void nglCompressedTextureImage2DEXTBO(int texture, int target, int level, int internalformat, int width, int height, int border, int imageSize, long data_buffer_offset, long function_pointer);

	public static void glCompressedTextureImage1DEXT(int texture, int target, int level, int internalformat, int width, int border, ByteBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glCompressedTextureImage1DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		data = NondirectBufferWrapper.wrapDirect(data);
		nglCompressedTextureImage1DEXT(texture, target, level, internalformat, width, border, (data.remaining()), data, data.position(), function_pointer);
	}
	private static native void nglCompressedTextureImage1DEXT(int texture, int target, int level, int internalformat, int width, int border, int imageSize, ByteBuffer data, int data_position, long function_pointer);
	public static void glCompressedTextureImage1DEXT(int texture, int target, int level, int internalformat, int width, int border, int imageSize, long data_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glCompressedTextureImage1DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOenabled(caps);
		nglCompressedTextureImage1DEXTBO(texture, target, level, internalformat, width, border, imageSize, data_buffer_offset, function_pointer);
	}
	private static native void nglCompressedTextureImage1DEXTBO(int texture, int target, int level, int internalformat, int width, int border, int imageSize, long data_buffer_offset, long function_pointer);

	public static void glCompressedTextureSubImage3DEXT(int texture, int target, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, ByteBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glCompressedTextureSubImage3DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		data = NondirectBufferWrapper.wrapDirect(data);
		nglCompressedTextureSubImage3DEXT(texture, target, level, xoffset, yoffset, zoffset, width, height, depth, format, (data.remaining()), data, data.position(), function_pointer);
	}
	private static native void nglCompressedTextureSubImage3DEXT(int texture, int target, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int imageSize, ByteBuffer data, int data_position, long function_pointer);
	public static void glCompressedTextureSubImage3DEXT(int texture, int target, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int imageSize, long data_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glCompressedTextureSubImage3DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOenabled(caps);
		nglCompressedTextureSubImage3DEXTBO(texture, target, level, xoffset, yoffset, zoffset, width, height, depth, format, imageSize, data_buffer_offset, function_pointer);
	}
	private static native void nglCompressedTextureSubImage3DEXTBO(int texture, int target, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int imageSize, long data_buffer_offset, long function_pointer);

	public static void glCompressedTextureSubImage2DEXT(int texture, int target, int level, int xoffset, int yoffset, int width, int height, int format, ByteBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glCompressedTextureSubImage2DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		data = NondirectBufferWrapper.wrapDirect(data);
		nglCompressedTextureSubImage2DEXT(texture, target, level, xoffset, yoffset, width, height, format, (data.remaining()), data, data.position(), function_pointer);
	}
	private static native void nglCompressedTextureSubImage2DEXT(int texture, int target, int level, int xoffset, int yoffset, int width, int height, int format, int imageSize, ByteBuffer data, int data_position, long function_pointer);
	public static void glCompressedTextureSubImage2DEXT(int texture, int target, int level, int xoffset, int yoffset, int width, int height, int format, int imageSize, long data_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glCompressedTextureSubImage2DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOenabled(caps);
		nglCompressedTextureSubImage2DEXTBO(texture, target, level, xoffset, yoffset, width, height, format, imageSize, data_buffer_offset, function_pointer);
	}
	private static native void nglCompressedTextureSubImage2DEXTBO(int texture, int target, int level, int xoffset, int yoffset, int width, int height, int format, int imageSize, long data_buffer_offset, long function_pointer);

	public static void glCompressedTextureSubImage1DEXT(int texture, int target, int level, int xoffset, int width, int format, ByteBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glCompressedTextureSubImage1DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		data = NondirectBufferWrapper.wrapDirect(data);
		nglCompressedTextureSubImage1DEXT(texture, target, level, xoffset, width, format, (data.remaining()), data, data.position(), function_pointer);
	}
	private static native void nglCompressedTextureSubImage1DEXT(int texture, int target, int level, int xoffset, int width, int format, int imageSize, ByteBuffer data, int data_position, long function_pointer);
	public static void glCompressedTextureSubImage1DEXT(int texture, int target, int level, int xoffset, int width, int format, int imageSize, long data_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glCompressedTextureSubImage1DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOenabled(caps);
		nglCompressedTextureSubImage1DEXTBO(texture, target, level, xoffset, width, format, imageSize, data_buffer_offset, function_pointer);
	}
	private static native void nglCompressedTextureSubImage1DEXTBO(int texture, int target, int level, int xoffset, int width, int format, int imageSize, long data_buffer_offset, long function_pointer);

	public static void glGetCompressedTextureImageEXT(int texture, int target, int level, ByteBuffer img) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glGetCompressedTextureImageEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		ByteBuffer img_saved = img;
		img = NondirectBufferWrapper.wrapNoCopyDirect(img);
		nglGetCompressedTextureImageEXT(texture, target, level, img, img.position(), function_pointer);
		NondirectBufferWrapper.copy(img, img_saved);
	}
	public static void glGetCompressedTextureImageEXT(int texture, int target, int level, IntBuffer img) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glGetCompressedTextureImageEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		IntBuffer img_saved = img;
		img = NondirectBufferWrapper.wrapNoCopyDirect(img);
		nglGetCompressedTextureImageEXT(texture, target, level, img, img.position() << 2, function_pointer);
		NondirectBufferWrapper.copy(img, img_saved);
	}
	public static void glGetCompressedTextureImageEXT(int texture, int target, int level, ShortBuffer img) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glGetCompressedTextureImageEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		ShortBuffer img_saved = img;
		img = NondirectBufferWrapper.wrapNoCopyDirect(img);
		nglGetCompressedTextureImageEXT(texture, target, level, img, img.position() << 1, function_pointer);
		NondirectBufferWrapper.copy(img, img_saved);
	}
	private static native void nglGetCompressedTextureImageEXT(int texture, int target, int level, Buffer img, int img_position, long function_pointer);
	public static void glGetCompressedTextureImageEXT(int texture, int target, int level, long img_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glGetCompressedTextureImageEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOenabled(caps);
		nglGetCompressedTextureImageEXTBO(texture, target, level, img_buffer_offset, function_pointer);
	}
	private static native void nglGetCompressedTextureImageEXTBO(int texture, int target, int level, long img_buffer_offset, long function_pointer);

	public static void glCompressedMultiTexImage3DEXT(int texunit, int target, int level, int internalformat, int width, int height, int depth, int border, ByteBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glCompressedMultiTexImage3DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		data = NondirectBufferWrapper.wrapDirect(data);
		nglCompressedMultiTexImage3DEXT(texunit, target, level, internalformat, width, height, depth, border, (data.remaining()), data, data.position(), function_pointer);
	}
	private static native void nglCompressedMultiTexImage3DEXT(int texunit, int target, int level, int internalformat, int width, int height, int depth, int border, int imageSize, ByteBuffer data, int data_position, long function_pointer);
	public static void glCompressedMultiTexImage3DEXT(int texunit, int target, int level, int internalformat, int width, int height, int depth, int border, int imageSize, long data_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glCompressedMultiTexImage3DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOenabled(caps);
		nglCompressedMultiTexImage3DEXTBO(texunit, target, level, internalformat, width, height, depth, border, imageSize, data_buffer_offset, function_pointer);
	}
	private static native void nglCompressedMultiTexImage3DEXTBO(int texunit, int target, int level, int internalformat, int width, int height, int depth, int border, int imageSize, long data_buffer_offset, long function_pointer);

	public static void glCompressedMultiTexImage2DEXT(int texunit, int target, int level, int internalformat, int width, int height, int border, ByteBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glCompressedMultiTexImage2DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		data = NondirectBufferWrapper.wrapDirect(data);
		nglCompressedMultiTexImage2DEXT(texunit, target, level, internalformat, width, height, border, (data.remaining()), data, data.position(), function_pointer);
	}
	private static native void nglCompressedMultiTexImage2DEXT(int texunit, int target, int level, int internalformat, int width, int height, int border, int imageSize, ByteBuffer data, int data_position, long function_pointer);
	public static void glCompressedMultiTexImage2DEXT(int texunit, int target, int level, int internalformat, int width, int height, int border, int imageSize, long data_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glCompressedMultiTexImage2DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOenabled(caps);
		nglCompressedMultiTexImage2DEXTBO(texunit, target, level, internalformat, width, height, border, imageSize, data_buffer_offset, function_pointer);
	}
	private static native void nglCompressedMultiTexImage2DEXTBO(int texunit, int target, int level, int internalformat, int width, int height, int border, int imageSize, long data_buffer_offset, long function_pointer);

	public static void glCompressedMultiTexImage1DEXT(int texunit, int target, int level, int internalformat, int width, int border, ByteBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glCompressedMultiTexImage1DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		data = NondirectBufferWrapper.wrapDirect(data);
		nglCompressedMultiTexImage1DEXT(texunit, target, level, internalformat, width, border, (data.remaining()), data, data.position(), function_pointer);
	}
	private static native void nglCompressedMultiTexImage1DEXT(int texunit, int target, int level, int internalformat, int width, int border, int imageSize, ByteBuffer data, int data_position, long function_pointer);
	public static void glCompressedMultiTexImage1DEXT(int texunit, int target, int level, int internalformat, int width, int border, int imageSize, long data_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glCompressedMultiTexImage1DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOenabled(caps);
		nglCompressedMultiTexImage1DEXTBO(texunit, target, level, internalformat, width, border, imageSize, data_buffer_offset, function_pointer);
	}
	private static native void nglCompressedMultiTexImage1DEXTBO(int texunit, int target, int level, int internalformat, int width, int border, int imageSize, long data_buffer_offset, long function_pointer);

	public static void glCompressedMultiTexSubImage3DEXT(int texunit, int target, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, ByteBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glCompressedMultiTexSubImage3DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		data = NondirectBufferWrapper.wrapDirect(data);
		nglCompressedMultiTexSubImage3DEXT(texunit, target, level, xoffset, yoffset, zoffset, width, height, depth, format, (data.remaining()), data, data.position(), function_pointer);
	}
	private static native void nglCompressedMultiTexSubImage3DEXT(int texunit, int target, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int imageSize, ByteBuffer data, int data_position, long function_pointer);
	public static void glCompressedMultiTexSubImage3DEXT(int texunit, int target, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int imageSize, long data_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glCompressedMultiTexSubImage3DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOenabled(caps);
		nglCompressedMultiTexSubImage3DEXTBO(texunit, target, level, xoffset, yoffset, zoffset, width, height, depth, format, imageSize, data_buffer_offset, function_pointer);
	}
	private static native void nglCompressedMultiTexSubImage3DEXTBO(int texunit, int target, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int imageSize, long data_buffer_offset, long function_pointer);

	public static void glCompressedMultiTexSubImage2DEXT(int texunit, int target, int level, int xoffset, int yoffset, int width, int height, int format, ByteBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glCompressedMultiTexSubImage2DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		data = NondirectBufferWrapper.wrapDirect(data);
		nglCompressedMultiTexSubImage2DEXT(texunit, target, level, xoffset, yoffset, width, height, format, (data.remaining()), data, data.position(), function_pointer);
	}
	private static native void nglCompressedMultiTexSubImage2DEXT(int texunit, int target, int level, int xoffset, int yoffset, int width, int height, int format, int imageSize, ByteBuffer data, int data_position, long function_pointer);
	public static void glCompressedMultiTexSubImage2DEXT(int texunit, int target, int level, int xoffset, int yoffset, int width, int height, int format, int imageSize, long data_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glCompressedMultiTexSubImage2DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOenabled(caps);
		nglCompressedMultiTexSubImage2DEXTBO(texunit, target, level, xoffset, yoffset, width, height, format, imageSize, data_buffer_offset, function_pointer);
	}
	private static native void nglCompressedMultiTexSubImage2DEXTBO(int texunit, int target, int level, int xoffset, int yoffset, int width, int height, int format, int imageSize, long data_buffer_offset, long function_pointer);

	public static void glCompressedMultiTexSubImage1DEXT(int texunit, int target, int level, int xoffset, int width, int format, ByteBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glCompressedMultiTexSubImage1DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		data = NondirectBufferWrapper.wrapDirect(data);
		nglCompressedMultiTexSubImage1DEXT(texunit, target, level, xoffset, width, format, (data.remaining()), data, data.position(), function_pointer);
	}
	private static native void nglCompressedMultiTexSubImage1DEXT(int texunit, int target, int level, int xoffset, int width, int format, int imageSize, ByteBuffer data, int data_position, long function_pointer);
	public static void glCompressedMultiTexSubImage1DEXT(int texunit, int target, int level, int xoffset, int width, int format, int imageSize, long data_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glCompressedMultiTexSubImage1DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOenabled(caps);
		nglCompressedMultiTexSubImage1DEXTBO(texunit, target, level, xoffset, width, format, imageSize, data_buffer_offset, function_pointer);
	}
	private static native void nglCompressedMultiTexSubImage1DEXTBO(int texunit, int target, int level, int xoffset, int width, int format, int imageSize, long data_buffer_offset, long function_pointer);

	public static void glGetCompressedMultiTexImageEXT(int texunit, int target, int level, ByteBuffer img) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glGetCompressedMultiTexImageEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		ByteBuffer img_saved = img;
		img = NondirectBufferWrapper.wrapNoCopyDirect(img);
		nglGetCompressedMultiTexImageEXT(texunit, target, level, img, img.position(), function_pointer);
		NondirectBufferWrapper.copy(img, img_saved);
	}
	public static void glGetCompressedMultiTexImageEXT(int texunit, int target, int level, IntBuffer img) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glGetCompressedMultiTexImageEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		IntBuffer img_saved = img;
		img = NondirectBufferWrapper.wrapNoCopyDirect(img);
		nglGetCompressedMultiTexImageEXT(texunit, target, level, img, img.position() << 2, function_pointer);
		NondirectBufferWrapper.copy(img, img_saved);
	}
	public static void glGetCompressedMultiTexImageEXT(int texunit, int target, int level, ShortBuffer img) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glGetCompressedMultiTexImageEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		ShortBuffer img_saved = img;
		img = NondirectBufferWrapper.wrapNoCopyDirect(img);
		nglGetCompressedMultiTexImageEXT(texunit, target, level, img, img.position() << 1, function_pointer);
		NondirectBufferWrapper.copy(img, img_saved);
	}
	private static native void nglGetCompressedMultiTexImageEXT(int texunit, int target, int level, Buffer img, int img_position, long function_pointer);
	public static void glGetCompressedMultiTexImageEXT(int texunit, int target, int level, long img_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glGetCompressedMultiTexImageEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOenabled(caps);
		nglGetCompressedMultiTexImageEXTBO(texunit, target, level, img_buffer_offset, function_pointer);
	}
	private static native void nglGetCompressedMultiTexImageEXTBO(int texunit, int target, int level, long img_buffer_offset, long function_pointer);

	public static void glGetCompressedTexImage(int target, int lod, ByteBuffer img) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glGetCompressedTexImage_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		ByteBuffer img_saved = img;
		img = NondirectBufferWrapper.wrapNoCopyDirect(img);
		nglGetCompressedTexImage(target, lod, img, img.position(), function_pointer);
		NondirectBufferWrapper.copy(img, img_saved);
	}
	public static void glGetCompressedTexImage(int target, int lod, IntBuffer img) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glGetCompressedTexImage_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		IntBuffer img_saved = img;
		img = NondirectBufferWrapper.wrapNoCopyDirect(img);
		nglGetCompressedTexImage(target, lod, img, img.position() << 2, function_pointer);
		NondirectBufferWrapper.copy(img, img_saved);
	}
	public static void glGetCompressedTexImage(int target, int lod, ShortBuffer img) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glGetCompressedTexImage_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		ShortBuffer img_saved = img;
		img = NondirectBufferWrapper.wrapNoCopyDirect(img);
		nglGetCompressedTexImage(target, lod, img, img.position() << 1, function_pointer);
		NondirectBufferWrapper.copy(img, img_saved);
	}
	private static native void nglGetCompressedTexImage(int target, int lod, Buffer img, int img_position, long function_pointer);
	public static void glGetCompressedTexImage(int target, int lod, long img_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glGetCompressedTexImage_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOenabled(caps);
		nglGetCompressedTexImageBO(target, lod, img_buffer_offset, function_pointer);
	}
	private static native void nglGetCompressedTexImageBO(int target, int lod, long img_buffer_offset, long function_pointer);

	public static void glMatrixLoadTransposeEXT(int matrixMode, FloatBuffer m) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glMatrixLoadTransposefEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		m = NondirectBufferWrapper.wrapBuffer(m, 16);
		nglMatrixLoadTransposefEXT(matrixMode, m, m.position(), function_pointer);
	}
	private static native void nglMatrixLoadTransposefEXT(int matrixMode, FloatBuffer m, int m_position, long function_pointer);

	public static void glMatrixLoadTransposeEXT(int matrixMode, DoubleBuffer m) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glMatrixLoadTransposedEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		m = NondirectBufferWrapper.wrapBuffer(m, 16);
		nglMatrixLoadTransposedEXT(matrixMode, m, m.position(), function_pointer);
	}
	private static native void nglMatrixLoadTransposedEXT(int matrixMode, DoubleBuffer m, int m_position, long function_pointer);

	public static void glMatrixMultTransposeEXT(int matrixMode, FloatBuffer m) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glMatrixMultTransposefEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		m = NondirectBufferWrapper.wrapBuffer(m, 16);
		nglMatrixMultTransposefEXT(matrixMode, m, m.position(), function_pointer);
	}
	private static native void nglMatrixMultTransposefEXT(int matrixMode, FloatBuffer m, int m_position, long function_pointer);

	public static void glMatrixMultTransposeEXT(int matrixMode, DoubleBuffer m) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glMatrixMultTransposedEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		m = NondirectBufferWrapper.wrapBuffer(m, 16);
		nglMatrixMultTransposedEXT(matrixMode, m, m.position(), function_pointer);
	}
	private static native void nglMatrixMultTransposedEXT(int matrixMode, DoubleBuffer m, int m_position, long function_pointer);

	public static void glNamedBufferDataEXT(int buffer, long size, int usage) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glNamedBufferDataEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglNamedBufferDataEXT(buffer, size, null, 0, usage, function_pointer);
	}
	public static void glNamedBufferDataEXT(int buffer, ByteBuffer data, int usage) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glNamedBufferDataEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		data = NondirectBufferWrapper.wrapDirect(data);
		nglNamedBufferDataEXT(buffer, (data.remaining()), data, data.position(), usage, function_pointer);
	}
	public static void glNamedBufferDataEXT(int buffer, DoubleBuffer data, int usage) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glNamedBufferDataEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		data = NondirectBufferWrapper.wrapDirect(data);
		nglNamedBufferDataEXT(buffer, (data.remaining() << 3), data, data.position() << 3, usage, function_pointer);
	}
	public static void glNamedBufferDataEXT(int buffer, FloatBuffer data, int usage) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glNamedBufferDataEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		data = NondirectBufferWrapper.wrapDirect(data);
		nglNamedBufferDataEXT(buffer, (data.remaining() << 2), data, data.position() << 2, usage, function_pointer);
	}
	public static void glNamedBufferDataEXT(int buffer, IntBuffer data, int usage) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glNamedBufferDataEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		data = NondirectBufferWrapper.wrapDirect(data);
		nglNamedBufferDataEXT(buffer, (data.remaining() << 2), data, data.position() << 2, usage, function_pointer);
	}
	public static void glNamedBufferDataEXT(int buffer, ShortBuffer data, int usage) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glNamedBufferDataEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		data = NondirectBufferWrapper.wrapDirect(data);
		nglNamedBufferDataEXT(buffer, (data.remaining() << 1), data, data.position() << 1, usage, function_pointer);
	}
	private static native void nglNamedBufferDataEXT(int buffer, long size, Buffer data, int data_position, int usage, long function_pointer);

	public static void glNamedBufferSubDataEXT(int buffer, long offset, ByteBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glNamedBufferSubDataEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		data = NondirectBufferWrapper.wrapDirect(data);
		nglNamedBufferSubDataEXT(buffer, offset, (data.remaining()), data, data.position(), function_pointer);
	}
	public static void glNamedBufferSubDataEXT(int buffer, long offset, DoubleBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glNamedBufferSubDataEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		data = NondirectBufferWrapper.wrapDirect(data);
		nglNamedBufferSubDataEXT(buffer, offset, (data.remaining() << 3), data, data.position() << 3, function_pointer);
	}
	public static void glNamedBufferSubDataEXT(int buffer, long offset, FloatBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glNamedBufferSubDataEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		data = NondirectBufferWrapper.wrapDirect(data);
		nglNamedBufferSubDataEXT(buffer, offset, (data.remaining() << 2), data, data.position() << 2, function_pointer);
	}
	public static void glNamedBufferSubDataEXT(int buffer, long offset, IntBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glNamedBufferSubDataEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		data = NondirectBufferWrapper.wrapDirect(data);
		nglNamedBufferSubDataEXT(buffer, offset, (data.remaining() << 2), data, data.position() << 2, function_pointer);
	}
	public static void glNamedBufferSubDataEXT(int buffer, long offset, ShortBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glNamedBufferSubDataEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		data = NondirectBufferWrapper.wrapDirect(data);
		nglNamedBufferSubDataEXT(buffer, offset, (data.remaining() << 1), data, data.position() << 1, function_pointer);
	}
	private static native void nglNamedBufferSubDataEXT(int buffer, long offset, long size, Buffer data, int data_position, long function_pointer);

	public static java.nio.ByteBuffer glMapNamedBufferEXT(int buffer, int access, java.nio.ByteBuffer old_buffer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glMapNamedBufferEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (old_buffer != null)
			BufferChecks.checkDirect(old_buffer);
		java.nio.ByteBuffer __result = nglMapNamedBufferEXT(buffer, access, GLChecks.getNamedBufferObjectSize(caps, buffer), old_buffer, function_pointer);
		return __result;
	}
	private static native java.nio.ByteBuffer nglMapNamedBufferEXT(int buffer, int access, long result_size, java.nio.ByteBuffer old_buffer, long function_pointer);

	public static boolean glUnmapNamedBufferEXT(int buffer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glUnmapNamedBufferEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		boolean __result = nglUnmapNamedBufferEXT(buffer, function_pointer);
		return __result;
	}
	private static native boolean nglUnmapNamedBufferEXT(int buffer, long function_pointer);

	public static void glGetNamedBufferParameterEXT(int buffer, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glGetNamedBufferParameterivEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer params_saved = params;
		params = NondirectBufferWrapper.wrapNoCopyBuffer(params, 4);
		nglGetNamedBufferParameterivEXT(buffer, pname, params, params.position(), function_pointer);
		NondirectBufferWrapper.copy(params, params_saved);
	}
	private static native void nglGetNamedBufferParameterivEXT(int buffer, int pname, IntBuffer params, int params_position, long function_pointer);

	public static java.nio.ByteBuffer glGetNamedBufferPointerEXT(int buffer, int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glGetNamedBufferPointervEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		java.nio.ByteBuffer __result = nglGetNamedBufferPointervEXT(buffer, pname, GLChecks.getNamedBufferObjectSize(caps, buffer), function_pointer);
		return __result;
	}
	private static native java.nio.ByteBuffer nglGetNamedBufferPointervEXT(int buffer, int pname, long result_size, long function_pointer);

	public static void glGetNamedBufferSubDataEXT(int buffer, long offset, ByteBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glGetNamedBufferSubDataEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		ByteBuffer data_saved = data;
		data = NondirectBufferWrapper.wrapNoCopyDirect(data);
		nglGetNamedBufferSubDataEXT(buffer, offset, (data.remaining()), data, data.position(), function_pointer);
		NondirectBufferWrapper.copy(data, data_saved);
	}
	public static void glGetNamedBufferSubDataEXT(int buffer, long offset, DoubleBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glGetNamedBufferSubDataEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		DoubleBuffer data_saved = data;
		data = NondirectBufferWrapper.wrapNoCopyDirect(data);
		nglGetNamedBufferSubDataEXT(buffer, offset, (data.remaining() << 3), data, data.position() << 3, function_pointer);
		NondirectBufferWrapper.copy(data, data_saved);
	}
	public static void glGetNamedBufferSubDataEXT(int buffer, long offset, FloatBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glGetNamedBufferSubDataEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		FloatBuffer data_saved = data;
		data = NondirectBufferWrapper.wrapNoCopyDirect(data);
		nglGetNamedBufferSubDataEXT(buffer, offset, (data.remaining() << 2), data, data.position() << 2, function_pointer);
		NondirectBufferWrapper.copy(data, data_saved);
	}
	public static void glGetNamedBufferSubDataEXT(int buffer, long offset, IntBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glGetNamedBufferSubDataEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer data_saved = data;
		data = NondirectBufferWrapper.wrapNoCopyDirect(data);
		nglGetNamedBufferSubDataEXT(buffer, offset, (data.remaining() << 2), data, data.position() << 2, function_pointer);
		NondirectBufferWrapper.copy(data, data_saved);
	}
	public static void glGetNamedBufferSubDataEXT(int buffer, long offset, ShortBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glGetNamedBufferSubDataEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		ShortBuffer data_saved = data;
		data = NondirectBufferWrapper.wrapNoCopyDirect(data);
		nglGetNamedBufferSubDataEXT(buffer, offset, (data.remaining() << 1), data, data.position() << 1, function_pointer);
		NondirectBufferWrapper.copy(data, data_saved);
	}
	private static native void nglGetNamedBufferSubDataEXT(int buffer, long offset, long size, Buffer data, int data_position, long function_pointer);

	public static void glProgramUniform1fEXT(int program, int location, float v0) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glProgramUniform1fEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglProgramUniform1fEXT(program, location, v0, function_pointer);
	}
	private static native void nglProgramUniform1fEXT(int program, int location, float v0, long function_pointer);

	public static void glProgramUniform2fEXT(int program, int location, float v0, float v1) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glProgramUniform2fEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglProgramUniform2fEXT(program, location, v0, v1, function_pointer);
	}
	private static native void nglProgramUniform2fEXT(int program, int location, float v0, float v1, long function_pointer);

	public static void glProgramUniform3fEXT(int program, int location, float v0, float v1, float v2) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glProgramUniform3fEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglProgramUniform3fEXT(program, location, v0, v1, v2, function_pointer);
	}
	private static native void nglProgramUniform3fEXT(int program, int location, float v0, float v1, float v2, long function_pointer);

	public static void glProgramUniform4fEXT(int program, int location, float v0, float v1, float v2, float v3) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glProgramUniform4fEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglProgramUniform4fEXT(program, location, v0, v1, v2, v3, function_pointer);
	}
	private static native void nglProgramUniform4fEXT(int program, int location, float v0, float v1, float v2, float v3, long function_pointer);

	public static void glProgramUniform1iEXT(int program, int location, int v0) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glProgramUniform1iEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglProgramUniform1iEXT(program, location, v0, function_pointer);
	}
	private static native void nglProgramUniform1iEXT(int program, int location, int v0, long function_pointer);

	public static void glProgramUniform2iEXT(int program, int location, int v0, int v1) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glProgramUniform2iEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglProgramUniform2iEXT(program, location, v0, v1, function_pointer);
	}
	private static native void nglProgramUniform2iEXT(int program, int location, int v0, int v1, long function_pointer);

	public static void glProgramUniform3iEXT(int program, int location, int v0, int v1, int v2) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glProgramUniform3iEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglProgramUniform3iEXT(program, location, v0, v1, v2, function_pointer);
	}
	private static native void nglProgramUniform3iEXT(int program, int location, int v0, int v1, int v2, long function_pointer);

	public static void glProgramUniform4iEXT(int program, int location, int v0, int v1, int v2, int v3) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glProgramUniform4iEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglProgramUniform4iEXT(program, location, v0, v1, v2, v3, function_pointer);
	}
	private static native void nglProgramUniform4iEXT(int program, int location, int v0, int v1, int v2, int v3, long function_pointer);

	public static void glProgramUniform1EXT(int program, int location, FloatBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glProgramUniform1fvEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		value = NondirectBufferWrapper.wrapDirect(value);
		nglProgramUniform1fvEXT(program, location, (value.remaining()), value, value.position(), function_pointer);
	}
	private static native void nglProgramUniform1fvEXT(int program, int location, int count, FloatBuffer value, int value_position, long function_pointer);

	public static void glProgramUniform2EXT(int program, int location, FloatBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glProgramUniform2fvEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		value = NondirectBufferWrapper.wrapDirect(value);
		nglProgramUniform2fvEXT(program, location, (value.remaining()) >> 1, value, value.position(), function_pointer);
	}
	private static native void nglProgramUniform2fvEXT(int program, int location, int count, FloatBuffer value, int value_position, long function_pointer);

	public static void glProgramUniform3EXT(int program, int location, FloatBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glProgramUniform3fvEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		value = NondirectBufferWrapper.wrapDirect(value);
		nglProgramUniform3fvEXT(program, location, (value.remaining()) / 3, value, value.position(), function_pointer);
	}
	private static native void nglProgramUniform3fvEXT(int program, int location, int count, FloatBuffer value, int value_position, long function_pointer);

	public static void glProgramUniform4EXT(int program, int location, FloatBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glProgramUniform4fvEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		value = NondirectBufferWrapper.wrapDirect(value);
		nglProgramUniform4fvEXT(program, location, (value.remaining()) >> 2, value, value.position(), function_pointer);
	}
	private static native void nglProgramUniform4fvEXT(int program, int location, int count, FloatBuffer value, int value_position, long function_pointer);

	public static void glProgramUniform1EXT(int program, int location, IntBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glProgramUniform1ivEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		value = NondirectBufferWrapper.wrapDirect(value);
		nglProgramUniform1ivEXT(program, location, (value.remaining()), value, value.position(), function_pointer);
	}
	private static native void nglProgramUniform1ivEXT(int program, int location, int count, IntBuffer value, int value_position, long function_pointer);

	public static void glProgramUniform2EXT(int program, int location, IntBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glProgramUniform2ivEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		value = NondirectBufferWrapper.wrapDirect(value);
		nglProgramUniform2ivEXT(program, location, (value.remaining()) >> 1, value, value.position(), function_pointer);
	}
	private static native void nglProgramUniform2ivEXT(int program, int location, int count, IntBuffer value, int value_position, long function_pointer);

	public static void glProgramUniform3EXT(int program, int location, IntBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glProgramUniform3ivEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		value = NondirectBufferWrapper.wrapDirect(value);
		nglProgramUniform3ivEXT(program, location, (value.remaining()) / 3, value, value.position(), function_pointer);
	}
	private static native void nglProgramUniform3ivEXT(int program, int location, int count, IntBuffer value, int value_position, long function_pointer);

	public static void glProgramUniform4EXT(int program, int location, IntBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glProgramUniform4ivEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		value = NondirectBufferWrapper.wrapDirect(value);
		nglProgramUniform4ivEXT(program, location, (value.remaining()) >> 2, value, value.position(), function_pointer);
	}
	private static native void nglProgramUniform4ivEXT(int program, int location, int count, IntBuffer value, int value_position, long function_pointer);

	public static void glProgramUniformMatrix2EXT(int program, int location, boolean transpose, FloatBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glProgramUniformMatrix2fvEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		value = NondirectBufferWrapper.wrapDirect(value);
		nglProgramUniformMatrix2fvEXT(program, location, (value.remaining()) >> 2, transpose, value, value.position(), function_pointer);
	}
	private static native void nglProgramUniformMatrix2fvEXT(int program, int location, int count, boolean transpose, FloatBuffer value, int value_position, long function_pointer);

	public static void glProgramUniformMatrix3EXT(int program, int location, boolean transpose, FloatBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glProgramUniformMatrix3fvEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		value = NondirectBufferWrapper.wrapDirect(value);
		nglProgramUniformMatrix3fvEXT(program, location, (value.remaining()) / (3 * 3), transpose, value, value.position(), function_pointer);
	}
	private static native void nglProgramUniformMatrix3fvEXT(int program, int location, int count, boolean transpose, FloatBuffer value, int value_position, long function_pointer);

	public static void glProgramUniformMatrix4EXT(int program, int location, boolean transpose, FloatBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glProgramUniformMatrix4fvEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		value = NondirectBufferWrapper.wrapDirect(value);
		nglProgramUniformMatrix4fvEXT(program, location, (value.remaining()) >> 4, transpose, value, value.position(), function_pointer);
	}
	private static native void nglProgramUniformMatrix4fvEXT(int program, int location, int count, boolean transpose, FloatBuffer value, int value_position, long function_pointer);

	public static void glProgramUniformMatrix2x3EXT(int program, int location, boolean transpose, FloatBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glProgramUniformMatrix2x3fvEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		value = NondirectBufferWrapper.wrapDirect(value);
		nglProgramUniformMatrix2x3fvEXT(program, location, (value.remaining()) / (2 * 3), transpose, value, value.position(), function_pointer);
	}
	private static native void nglProgramUniformMatrix2x3fvEXT(int program, int location, int count, boolean transpose, FloatBuffer value, int value_position, long function_pointer);

	public static void glProgramUniformMatrix3x2EXT(int program, int location, boolean transpose, FloatBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glProgramUniformMatrix3x2fvEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		value = NondirectBufferWrapper.wrapDirect(value);
		nglProgramUniformMatrix3x2fvEXT(program, location, (value.remaining()) / (3 * 2), transpose, value, value.position(), function_pointer);
	}
	private static native void nglProgramUniformMatrix3x2fvEXT(int program, int location, int count, boolean transpose, FloatBuffer value, int value_position, long function_pointer);

	public static void glProgramUniformMatrix2x4EXT(int program, int location, boolean transpose, FloatBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glProgramUniformMatrix2x4fvEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		value = NondirectBufferWrapper.wrapDirect(value);
		nglProgramUniformMatrix2x4fvEXT(program, location, (value.remaining()) >> 3, transpose, value, value.position(), function_pointer);
	}
	private static native void nglProgramUniformMatrix2x4fvEXT(int program, int location, int count, boolean transpose, FloatBuffer value, int value_position, long function_pointer);

	public static void glProgramUniformMatrix4x2EXT(int program, int location, boolean transpose, FloatBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glProgramUniformMatrix4x2fvEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		value = NondirectBufferWrapper.wrapDirect(value);
		nglProgramUniformMatrix4x2fvEXT(program, location, (value.remaining()) >> 3, transpose, value, value.position(), function_pointer);
	}
	private static native void nglProgramUniformMatrix4x2fvEXT(int program, int location, int count, boolean transpose, FloatBuffer value, int value_position, long function_pointer);

	public static void glProgramUniformMatrix3x4EXT(int program, int location, boolean transpose, FloatBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glProgramUniformMatrix3x4fvEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		value = NondirectBufferWrapper.wrapDirect(value);
		nglProgramUniformMatrix3x4fvEXT(program, location, (value.remaining()) / (3 * 4), transpose, value, value.position(), function_pointer);
	}
	private static native void nglProgramUniformMatrix3x4fvEXT(int program, int location, int count, boolean transpose, FloatBuffer value, int value_position, long function_pointer);

	public static void glProgramUniformMatrix4x3EXT(int program, int location, boolean transpose, FloatBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glProgramUniformMatrix4x3fvEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		value = NondirectBufferWrapper.wrapDirect(value);
		nglProgramUniformMatrix4x3fvEXT(program, location, (value.remaining()) / (4 * 3), transpose, value, value.position(), function_pointer);
	}
	private static native void nglProgramUniformMatrix4x3fvEXT(int program, int location, int count, boolean transpose, FloatBuffer value, int value_position, long function_pointer);

	public static void glTextureBufferEXT(int texture, int target, int internalformat, int buffer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glTextureBufferEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglTextureBufferEXT(texture, target, internalformat, buffer, function_pointer);
	}
	private static native void nglTextureBufferEXT(int texture, int target, int internalformat, int buffer, long function_pointer);

	public static void glMultiTexBufferEXT(int texunit, int target, int internalformat, int buffer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glMultiTexBufferEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglMultiTexBufferEXT(texunit, target, internalformat, buffer, function_pointer);
	}
	private static native void nglMultiTexBufferEXT(int texunit, int target, int internalformat, int buffer, long function_pointer);

	public static void glTextureParameterIEXT(int texture, int target, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glTextureParameterIivEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		params = NondirectBufferWrapper.wrapBuffer(params, 4);
		nglTextureParameterIivEXT(texture, target, pname, params, params.position(), function_pointer);
	}
	private static native void nglTextureParameterIivEXT(int texture, int target, int pname, IntBuffer params, int params_position, long function_pointer);

	public static void glTextureParameterIuEXT(int texture, int target, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glTextureParameterIuivEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		params = NondirectBufferWrapper.wrapBuffer(params, 4);
		nglTextureParameterIuivEXT(texture, target, pname, params, params.position(), function_pointer);
	}
	private static native void nglTextureParameterIuivEXT(int texture, int target, int pname, IntBuffer params, int params_position, long function_pointer);

	public static void glGetTextureParameterIEXT(int texture, int target, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glGetTextureParameterIivEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer params_saved = params;
		params = NondirectBufferWrapper.wrapNoCopyBuffer(params, 4);
		nglGetTextureParameterIivEXT(texture, target, pname, params, params.position(), function_pointer);
		NondirectBufferWrapper.copy(params, params_saved);
	}
	private static native void nglGetTextureParameterIivEXT(int texture, int target, int pname, IntBuffer params, int params_position, long function_pointer);

	public static void glGetTextureParameterIuEXT(int texture, int target, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glGetTextureParameterIuivEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer params_saved = params;
		params = NondirectBufferWrapper.wrapNoCopyBuffer(params, 4);
		nglGetTextureParameterIuivEXT(texture, target, pname, params, params.position(), function_pointer);
		NondirectBufferWrapper.copy(params, params_saved);
	}
	private static native void nglGetTextureParameterIuivEXT(int texture, int target, int pname, IntBuffer params, int params_position, long function_pointer);

	public static void glMultiTexParameterIEXT(int texunit, int target, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glMultiTexParameterIivEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		params = NondirectBufferWrapper.wrapBuffer(params, 4);
		nglMultiTexParameterIivEXT(texunit, target, pname, params, params.position(), function_pointer);
	}
	private static native void nglMultiTexParameterIivEXT(int texunit, int target, int pname, IntBuffer params, int params_position, long function_pointer);

	public static void glMultiTexParameterIuEXT(int texunit, int target, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glMultiTexParameterIuivEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		params = NondirectBufferWrapper.wrapBuffer(params, 4);
		nglMultiTexParameterIuivEXT(texunit, target, pname, params, params.position(), function_pointer);
	}
	private static native void nglMultiTexParameterIuivEXT(int texunit, int target, int pname, IntBuffer params, int params_position, long function_pointer);

	public static void glGetMultiTexParameterIEXT(int texunit, int target, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glGetMultiTexParameterIivEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer params_saved = params;
		params = NondirectBufferWrapper.wrapNoCopyBuffer(params, 4);
		nglGetMultiTexParameterIivEXT(texunit, target, pname, params, params.position(), function_pointer);
		NondirectBufferWrapper.copy(params, params_saved);
	}
	private static native void nglGetMultiTexParameterIivEXT(int texunit, int target, int pname, IntBuffer params, int params_position, long function_pointer);

	public static void glGetMultiTexParameterIuEXT(int texunit, int target, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glGetMultiTexParameterIuivEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer params_saved = params;
		params = NondirectBufferWrapper.wrapNoCopyBuffer(params, 4);
		nglGetMultiTexParameterIuivEXT(texunit, target, pname, params, params.position(), function_pointer);
		NondirectBufferWrapper.copy(params, params_saved);
	}
	private static native void nglGetMultiTexParameterIuivEXT(int texunit, int target, int pname, IntBuffer params, int params_position, long function_pointer);

	public static void glProgramUniform1uiEXT(int program, int location, int v0) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glProgramUniform1uiEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglProgramUniform1uiEXT(program, location, v0, function_pointer);
	}
	private static native void nglProgramUniform1uiEXT(int program, int location, int v0, long function_pointer);

	public static void glProgramUniform2uiEXT(int program, int location, int v0, int v1) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glProgramUniform2uiEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglProgramUniform2uiEXT(program, location, v0, v1, function_pointer);
	}
	private static native void nglProgramUniform2uiEXT(int program, int location, int v0, int v1, long function_pointer);

	public static void glProgramUniform3uiEXT(int program, int location, int v0, int v1, int v2) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glProgramUniform3uiEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglProgramUniform3uiEXT(program, location, v0, v1, v2, function_pointer);
	}
	private static native void nglProgramUniform3uiEXT(int program, int location, int v0, int v1, int v2, long function_pointer);

	public static void glProgramUniform4uiEXT(int program, int location, int v0, int v1, int v2, int v3) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glProgramUniform4uiEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglProgramUniform4uiEXT(program, location, v0, v1, v2, v3, function_pointer);
	}
	private static native void nglProgramUniform4uiEXT(int program, int location, int v0, int v1, int v2, int v3, long function_pointer);

	public static void glProgramUniform1uEXT(int program, int location, IntBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glProgramUniform1uivEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		value = NondirectBufferWrapper.wrapDirect(value);
		nglProgramUniform1uivEXT(program, location, (value.remaining()), value, value.position(), function_pointer);
	}
	private static native void nglProgramUniform1uivEXT(int program, int location, int count, IntBuffer value, int value_position, long function_pointer);

	public static void glProgramUniform2uEXT(int program, int location, IntBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glProgramUniform2uivEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		value = NondirectBufferWrapper.wrapDirect(value);
		nglProgramUniform2uivEXT(program, location, (value.remaining()) >> 1, value, value.position(), function_pointer);
	}
	private static native void nglProgramUniform2uivEXT(int program, int location, int count, IntBuffer value, int value_position, long function_pointer);

	public static void glProgramUniform3uEXT(int program, int location, IntBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glProgramUniform3uivEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		value = NondirectBufferWrapper.wrapDirect(value);
		nglProgramUniform3uivEXT(program, location, (value.remaining()) / 3, value, value.position(), function_pointer);
	}
	private static native void nglProgramUniform3uivEXT(int program, int location, int count, IntBuffer value, int value_position, long function_pointer);

	public static void glProgramUniform4uEXT(int program, int location, IntBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glProgramUniform4uivEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		value = NondirectBufferWrapper.wrapDirect(value);
		nglProgramUniform4uivEXT(program, location, (value.remaining()) >> 2, value, value.position(), function_pointer);
	}
	private static native void nglProgramUniform4uivEXT(int program, int location, int count, IntBuffer value, int value_position, long function_pointer);

	public static void glNamedProgramLocalParameters4EXT(int program, int target, int index, FloatBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glNamedProgramLocalParameters4fvEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		params = NondirectBufferWrapper.wrapDirect(params);
		nglNamedProgramLocalParameters4fvEXT(program, target, index, (params.remaining()) >> 2, params, params.position(), function_pointer);
	}
	private static native void nglNamedProgramLocalParameters4fvEXT(int program, int target, int index, int count, FloatBuffer params, int params_position, long function_pointer);

	public static void glNamedProgramLocalParameterI4iEXT(int program, int target, int index, int x, int y, int z, int w) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glNamedProgramLocalParameterI4iEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglNamedProgramLocalParameterI4iEXT(program, target, index, x, y, z, w, function_pointer);
	}
	private static native void nglNamedProgramLocalParameterI4iEXT(int program, int target, int index, int x, int y, int z, int w, long function_pointer);

	public static void glNamedProgramLocalParameterI4EXT(int program, int target, int index, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glNamedProgramLocalParameterI4ivEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		params = NondirectBufferWrapper.wrapBuffer(params, 4);
		nglNamedProgramLocalParameterI4ivEXT(program, target, index, params, params.position(), function_pointer);
	}
	private static native void nglNamedProgramLocalParameterI4ivEXT(int program, int target, int index, IntBuffer params, int params_position, long function_pointer);

	public static void glNamedProgramLocalParametersI4EXT(int program, int target, int index, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glNamedProgramLocalParametersI4ivEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		params = NondirectBufferWrapper.wrapDirect(params);
		nglNamedProgramLocalParametersI4ivEXT(program, target, index, (params.remaining()) >> 2, params, params.position(), function_pointer);
	}
	private static native void nglNamedProgramLocalParametersI4ivEXT(int program, int target, int index, int count, IntBuffer params, int params_position, long function_pointer);

	public static void glNamedProgramLocalParameterI4uiEXT(int program, int target, int index, int x, int y, int z, int w) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glNamedProgramLocalParameterI4uiEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglNamedProgramLocalParameterI4uiEXT(program, target, index, x, y, z, w, function_pointer);
	}
	private static native void nglNamedProgramLocalParameterI4uiEXT(int program, int target, int index, int x, int y, int z, int w, long function_pointer);

	public static void glNamedProgramLocalParameterI4uEXT(int program, int target, int index, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glNamedProgramLocalParameterI4uivEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		params = NondirectBufferWrapper.wrapBuffer(params, 4);
		nglNamedProgramLocalParameterI4uivEXT(program, target, index, params, params.position(), function_pointer);
	}
	private static native void nglNamedProgramLocalParameterI4uivEXT(int program, int target, int index, IntBuffer params, int params_position, long function_pointer);

	public static void glNamedProgramLocalParametersI4uEXT(int program, int target, int index, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glNamedProgramLocalParametersI4uivEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		params = NondirectBufferWrapper.wrapDirect(params);
		nglNamedProgramLocalParametersI4uivEXT(program, target, index, (params.remaining()) >> 2, params, params.position(), function_pointer);
	}
	private static native void nglNamedProgramLocalParametersI4uivEXT(int program, int target, int index, int count, IntBuffer params, int params_position, long function_pointer);

	public static void glGetNamedProgramLocalParameterIEXT(int program, int target, int index, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glGetNamedProgramLocalParameterIivEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer params_saved = params;
		params = NondirectBufferWrapper.wrapNoCopyBuffer(params, 4);
		nglGetNamedProgramLocalParameterIivEXT(program, target, index, params, params.position(), function_pointer);
		NondirectBufferWrapper.copy(params, params_saved);
	}
	private static native void nglGetNamedProgramLocalParameterIivEXT(int program, int target, int index, IntBuffer params, int params_position, long function_pointer);

	public static void glGetNamedProgramLocalParameterIuEXT(int program, int target, int index, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glGetNamedProgramLocalParameterIuivEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer params_saved = params;
		params = NondirectBufferWrapper.wrapNoCopyBuffer(params, 4);
		nglGetNamedProgramLocalParameterIuivEXT(program, target, index, params, params.position(), function_pointer);
		NondirectBufferWrapper.copy(params, params_saved);
	}
	private static native void nglGetNamedProgramLocalParameterIuivEXT(int program, int target, int index, IntBuffer params, int params_position, long function_pointer);

	public static void glNamedRenderbufferStorageEXT(int renderbuffer, int internalformat, int width, int height) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glNamedRenderbufferStorageEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglNamedRenderbufferStorageEXT(renderbuffer, internalformat, width, height, function_pointer);
	}
	private static native void nglNamedRenderbufferStorageEXT(int renderbuffer, int internalformat, int width, int height, long function_pointer);

	public static void glGetNamedRenderbufferParameterEXT(int renderbuffer, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glGetNamedRenderbufferParameterivEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer params_saved = params;
		params = NondirectBufferWrapper.wrapNoCopyBuffer(params, 4);
		nglGetNamedRenderbufferParameterivEXT(renderbuffer, pname, params, params.position(), function_pointer);
		NondirectBufferWrapper.copy(params, params_saved);
	}
	private static native void nglGetNamedRenderbufferParameterivEXT(int renderbuffer, int pname, IntBuffer params, int params_position, long function_pointer);

	public static void glNamedRenderbufferStorageMultisampleEXT(int renderbuffer, int samples, int internalformat, int width, int height) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glNamedRenderbufferStorageMultisampleEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglNamedRenderbufferStorageMultisampleEXT(renderbuffer, samples, internalformat, width, height, function_pointer);
	}
	private static native void nglNamedRenderbufferStorageMultisampleEXT(int renderbuffer, int samples, int internalformat, int width, int height, long function_pointer);

	public static void glNamedRenderbufferStorageMultisampleCoverageEXT(int renderbuffer, int coverageSamples, int colorSamples, int internalformat, int width, int height) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glNamedRenderbufferStorageMultisampleCoverageEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglNamedRenderbufferStorageMultisampleCoverageEXT(renderbuffer, coverageSamples, colorSamples, internalformat, width, height, function_pointer);
	}
	private static native void nglNamedRenderbufferStorageMultisampleCoverageEXT(int renderbuffer, int coverageSamples, int colorSamples, int internalformat, int width, int height, long function_pointer);

	public static int glCheckNamedFramebufferStatusEXT(int framebuffer, int target) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glCheckNamedFramebufferStatusEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		int __result = nglCheckNamedFramebufferStatusEXT(framebuffer, target, function_pointer);
		return __result;
	}
	private static native int nglCheckNamedFramebufferStatusEXT(int framebuffer, int target, long function_pointer);

	public static void glNamedFramebufferTexture1DEXT(int framebuffer, int attachment, int textarget, int texture, int level) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glNamedFramebufferTexture1DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglNamedFramebufferTexture1DEXT(framebuffer, attachment, textarget, texture, level, function_pointer);
	}
	private static native void nglNamedFramebufferTexture1DEXT(int framebuffer, int attachment, int textarget, int texture, int level, long function_pointer);

	public static void glNamedFramebufferTexture2DEXT(int framebuffer, int attachment, int textarget, int texture, int level) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glNamedFramebufferTexture2DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglNamedFramebufferTexture2DEXT(framebuffer, attachment, textarget, texture, level, function_pointer);
	}
	private static native void nglNamedFramebufferTexture2DEXT(int framebuffer, int attachment, int textarget, int texture, int level, long function_pointer);

	public static void glNamedFramebufferTexture3DEXT(int framebuffer, int attachment, int textarget, int texture, int level, int zoffset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glNamedFramebufferTexture3DEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglNamedFramebufferTexture3DEXT(framebuffer, attachment, textarget, texture, level, zoffset, function_pointer);
	}
	private static native void nglNamedFramebufferTexture3DEXT(int framebuffer, int attachment, int textarget, int texture, int level, int zoffset, long function_pointer);

	public static void glNamedFramebufferRenderbufferEXT(int framebuffer, int attachment, int renderbuffertarget, int renderbuffer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glNamedFramebufferRenderbufferEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglNamedFramebufferRenderbufferEXT(framebuffer, attachment, renderbuffertarget, renderbuffer, function_pointer);
	}
	private static native void nglNamedFramebufferRenderbufferEXT(int framebuffer, int attachment, int renderbuffertarget, int renderbuffer, long function_pointer);

	public static void glGetNamedFramebufferAttachmentParameterEXT(int framebuffer, int attachment, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glGetNamedFramebufferAttachmentParameterivEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer params_saved = params;
		params = NondirectBufferWrapper.wrapNoCopyBuffer(params, 4);
		nglGetNamedFramebufferAttachmentParameterivEXT(framebuffer, attachment, pname, params, params.position(), function_pointer);
		NondirectBufferWrapper.copy(params, params_saved);
	}
	private static native void nglGetNamedFramebufferAttachmentParameterivEXT(int framebuffer, int attachment, int pname, IntBuffer params, int params_position, long function_pointer);

	public static void glGenerateTextureMipmapEXT(int texture, int target) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glGenerateTextureMipmapEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglGenerateTextureMipmapEXT(texture, target, function_pointer);
	}
	private static native void nglGenerateTextureMipmapEXT(int texture, int target, long function_pointer);

	public static void glGenerateMultiTexMipmapEXT(int texunit, int target) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glGenerateMultiTexMipmapEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglGenerateMultiTexMipmapEXT(texunit, target, function_pointer);
	}
	private static native void nglGenerateMultiTexMipmapEXT(int texunit, int target, long function_pointer);

	public static void glFramebufferDrawBufferEXT(int framebuffer, int mode) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glFramebufferDrawBufferEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglFramebufferDrawBufferEXT(framebuffer, mode, function_pointer);
	}
	private static native void nglFramebufferDrawBufferEXT(int framebuffer, int mode, long function_pointer);

	public static void glFramebufferDrawBuffersEXT(int framebuffer, IntBuffer bufs) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glFramebufferDrawBuffersEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		bufs = NondirectBufferWrapper.wrapDirect(bufs);
		nglFramebufferDrawBuffersEXT(framebuffer, (bufs.remaining()), bufs, bufs.position(), function_pointer);
	}
	private static native void nglFramebufferDrawBuffersEXT(int framebuffer, int n, IntBuffer bufs, int bufs_position, long function_pointer);

	public static void glFramebufferReadBufferEXT(int framebuffer, int mode) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glFramebufferReadBufferEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglFramebufferReadBufferEXT(framebuffer, mode, function_pointer);
	}
	private static native void nglFramebufferReadBufferEXT(int framebuffer, int mode, long function_pointer);

	public static void glGetFramebufferParameterEXT(int framebuffer, int pname, IntBuffer param) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glGetFramebufferParameterivEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer param_saved = param;
		param = NondirectBufferWrapper.wrapNoCopyBuffer(param, 4);
		nglGetFramebufferParameterivEXT(framebuffer, pname, param, param.position(), function_pointer);
		NondirectBufferWrapper.copy(param, param_saved);
	}
	private static native void nglGetFramebufferParameterivEXT(int framebuffer, int pname, IntBuffer param, int param_position, long function_pointer);

	public static void glNamedFramebufferTextureEXT(int framebuffer, int attachment, int texture, int level) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glNamedFramebufferTextureEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglNamedFramebufferTextureEXT(framebuffer, attachment, texture, level, function_pointer);
	}
	private static native void nglNamedFramebufferTextureEXT(int framebuffer, int attachment, int texture, int level, long function_pointer);

	public static void glNamedFramebufferTextureLayerEXT(int framebuffer, int attachment, int texture, int level, int layer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glNamedFramebufferTextureLayerEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglNamedFramebufferTextureLayerEXT(framebuffer, attachment, texture, level, layer, function_pointer);
	}
	private static native void nglNamedFramebufferTextureLayerEXT(int framebuffer, int attachment, int texture, int level, int layer, long function_pointer);

	public static void glNamedFramebufferTextureFaceEXT(int framebuffer, int attachment, int texture, int level, int face) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glNamedFramebufferTextureFaceEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglNamedFramebufferTextureFaceEXT(framebuffer, attachment, texture, level, face, function_pointer);
	}
	private static native void nglNamedFramebufferTextureFaceEXT(int framebuffer, int attachment, int texture, int level, int face, long function_pointer);

	public static void glTextureRenderbufferEXT(int texture, int target, int renderbuffer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glTextureRenderbufferEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglTextureRenderbufferEXT(texture, target, renderbuffer, function_pointer);
	}
	private static native void nglTextureRenderbufferEXT(int texture, int target, int renderbuffer, long function_pointer);

	public static void glMultiTexRenderbufferEXT(int texunit, int target, int renderbuffer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.EXT_direct_state_access_glMultiTexRenderbufferEXT_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglMultiTexRenderbufferEXT(texunit, target, renderbuffer, function_pointer);
	}
	private static native void nglMultiTexRenderbufferEXT(int texunit, int target, int renderbuffer, long function_pointer);
}
