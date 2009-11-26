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

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLUtil;

import java.nio.IntBuffer;

/**
 * This class represents the context attributes passed to CreateContextAttribs of the XGL_create_context extension.
 * These attributes can be used to indicate at context creation which OpenGL interface will be used. This includes the
 * OpenGL version, the layer plane on which rendering takes place and also optional debug and forward combatibility modes.
 * (read the XGL_create_context spec for details)
 * <p/>
 * Use of this class is optional. If an OpenGL context is created without passing an instance of this class
 * (or XGL_create_context is not supported), the old context creation code will be used. Use of ContextAttribs is required
 * to create an OpenGL 3.0 or newer context. Support for debug and forward compatible mobes is not guaranteed by the OpenGL
 * implementation. Developers may encounter debug contexts being the same as non-debug contexts or forward compatible
 * contexts having support for deprecated functionality.
 * <p/>
 * Warning: This functionality is currently available on the Windows platform only. However, if the forwardCompatible
 * attribute is used, LWJGL will not load the deprecated functionality (as defined in the OpenGL 3.0 specification). This
 * means that developers can start working on cleaning up their applications without an OpenGL 3.0 complaint driver.
 *
 * @author spasi <spasi@users.sourceforge.net>
 */
public final class ContextAttribs {

	private int majorVersion;
	private int minorVersion;

	private int layerPlane;

	private boolean debug;
	private boolean forwardCompatible;

	public ContextAttribs() {
		this(1, 0);
	}

	public ContextAttribs(final int majorVersion, final int minorVersion) {
		if ( majorVersion < 0 ||
		     3 < majorVersion ||
		     minorVersion < 0 ||
		     (majorVersion == 3 && 0 < minorVersion) ||
		     (majorVersion == 2 && 1 < minorVersion) ||
		     (majorVersion == 1 && 5 < minorVersion) )
			throw new IllegalArgumentException("Invalid OpenGL version specified: " + majorVersion + '.' + minorVersion);

		this.majorVersion = majorVersion;
		this.minorVersion = minorVersion;

		this.layerPlane = 0;

		this.debug = false;
		this.forwardCompatible = false;
	}

	private ContextAttribs(final ContextAttribs attribs) {
		this.majorVersion = attribs.majorVersion;
		this.minorVersion = attribs.minorVersion;

		this.layerPlane = attribs.layerPlane;

		this.debug = attribs.debug;
		this.forwardCompatible = attribs.forwardCompatible;
	}

	public int getMajorVersion() {
		return majorVersion;
	}

	public int getMinorVersion() {
		return minorVersion;
	}

	public int getLayerPlane() {
		return layerPlane;
	}

	public boolean isDebug() {
		return debug;
	}

	public boolean isForwardCompatible() {
		return forwardCompatible;
	}

	public ContextAttribs withLayer(final int layerPlane) {
		if ( layerPlane < 0 )
			throw new IllegalArgumentException("Invalid layer plane specified: " + layerPlane);

		final ContextAttribs attribs = new ContextAttribs(this);
		attribs.layerPlane = layerPlane;
		return attribs;
	}

	public ContextAttribs withDebug(final boolean debug) {
		final ContextAttribs attribs = new ContextAttribs(this);
		attribs.debug = debug;
		return attribs;
	}

	public ContextAttribs withForwardCompatible(final boolean forwardCompatible) {
		final ContextAttribs attribs = new ContextAttribs(this);
		attribs.forwardCompatible = forwardCompatible;
		return attribs;
	}

	private static ContextAttribsImplementation getImplementation() {
		switch ( LWJGLUtil.getPlatform() ) {
			case LWJGLUtil.PLATFORM_LINUX:
				return new LinuxContextAttribs();
			case LWJGLUtil.PLATFORM_WINDOWS:
				return new WindowsContextAttribs();
			case LWJGLUtil.PLATFORM_MACOSX:
				return new MacOSXContextAttribs();
			default:
				throw new IllegalStateException("Unsupported platform");
		}
	}

	IntBuffer getAttribList() {
		ContextAttribsImplementation implementation = getImplementation();

		int attribCount = 0;

		if ( !(majorVersion == 1 && minorVersion == 0) )
			attribCount += 2;
		if ( 0 < layerPlane )
			attribCount++;

		int flags = 0;
		if ( debug )
			flags |= implementation.getDebugBit();
		if ( forwardCompatible )
			flags |= implementation.getForwardCompatibleBit();
		if ( 0 < flags )
			attribCount++;

		if ( attribCount == 0 )
			return null;

		final IntBuffer attribs = BufferUtils.createIntBuffer((attribCount * 2) + 1);

		if ( !(majorVersion == 1 && minorVersion == 0) ) {
			attribs.put(implementation.getMajorVersionAttrib()).put(majorVersion);
			attribs.put(implementation.getMinorVersionAttrib()).put(minorVersion);
		}
		if ( 0 < layerPlane )
			attribs.put(implementation.getLayerPlaneAttrib()).put(layerPlane);
		if ( 0 < flags )
			attribs.put(implementation.getFlagsAttrib()).put(flags);

		attribs.put(0);
		attribs.rewind();
		return attribs;
	}

}