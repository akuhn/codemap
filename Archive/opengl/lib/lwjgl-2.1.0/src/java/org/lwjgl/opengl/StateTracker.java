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

final class StateTracker {
	private final ReferencesStack references_stack;
	private final StateStack attrib_stack;

	StateTracker() {
		references_stack = new ReferencesStack();
		attrib_stack = new StateStack(0);
	}

	static void popAttrib(ContextCapabilities caps) {
		caps.tracker.doPopAttrib();
	}

	private void doPopAttrib() {
		if ((attrib_stack.popState() & GL11.GL_CLIENT_VERTEX_ARRAY_BIT) != 0) {
			references_stack.popState();
		}
	}

	static void pushAttrib(ContextCapabilities caps, int mask) {
		caps.tracker.doPushAttrib(mask);
	}

	private void doPushAttrib(int mask) {
		attrib_stack.pushState(mask);
		if ((mask & GL11.GL_CLIENT_VERTEX_ARRAY_BIT) != 0) {
			references_stack.pushState();
		}
	}

	static ReferencesStack getReferencesStack(ContextCapabilities caps) {
		return caps.tracker.references_stack;
	}

        static void bindBuffer(ContextCapabilities caps, int target, int buffer) {
            ReferencesStack references_stack = getReferencesStack(caps);
            switch(target) {
                case GL15.GL_ELEMENT_ARRAY_BUFFER:
                    references_stack.getReferences().elementArrayBuffer = buffer;
                    break;
                case GL15.GL_ARRAY_BUFFER:
                    references_stack.getReferences().arrayBuffer = buffer;
                    break;
            }
        }
}
