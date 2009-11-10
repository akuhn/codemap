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

/**
 * An implementation of ContextAttribs for MacOS X.
 * <p/><p/>
 * ---- WIP - No XGL_create_context has been defined for MacOS X yet ----
 *
 * @author spasi <spasi@users.sourceforge.net>
 */
final class MacOSXContextAttribs implements ContextAttribsImplementation {

	private static final int XGL_CONTEXT_MAJOR_VERSION_ARB = 0x2091;
	private static final int XGL_CONTEXT_MINOR_VERSION_ARB = 0x2092;
	private static final int XGL_CONTEXT_LAYER_PLANE_ARB = 0x2093;
	private static final int XGL_CONTEXT_FLAGS_ARB = 0x2094;

	private static final int XGL_CONTEXT_DEBUG_BIT_ARB = 0x0001;
	private static final int XGL_CONTEXT_FORWARD_COMPATIBLE_BIT_ARB = 0x0002;

	MacOSXContextAttribs() {
	}

	public int getMajorVersionAttrib() {
		return XGL_CONTEXT_MAJOR_VERSION_ARB;
	}

	public int getMinorVersionAttrib() {
		return XGL_CONTEXT_MINOR_VERSION_ARB;
	}

	public int getLayerPlaneAttrib() {
		return XGL_CONTEXT_LAYER_PLANE_ARB;
	}

	public int getFlagsAttrib() {
		return XGL_CONTEXT_FLAGS_ARB;
	}

	public int getDebugBit() {
		return XGL_CONTEXT_DEBUG_BIT_ARB;
	}

	public int getForwardCompatibleBit() {
		return XGL_CONTEXT_FORWARD_COMPATIBLE_BIT_ARB;
	}

}