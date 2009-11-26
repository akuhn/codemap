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
package org.lwjgl;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.nio.LongBuffer;

/**
 * <p>A class to check buffer boundaries in general. If there is unsufficient space
 * in the buffer when the call is made then a buffer overflow would otherwise
 * occur and cause unexpected behaviour, a crash, or worse, a security risk.
 *
 * Internal class, don't use.
 * </p>
 * @author cix_foo <cix_foo@users.sourceforge.net>
 * @author elias_naur <elias_naur@users.sourceforge.net>
 * @version $Revision: 3116 $
 * $Id: BufferChecks.java 3116 2008-08-19 16:46:03Z spasi $
 */
public class BufferChecks {
	/** Static methods only! */
	private BufferChecks() {
	}

	/**
	 * Helper methods to ensure a function pointer is not-null (0)
	 */
	public static void checkFunctionAddress(long pointer) {
		if (pointer == 0) {
			throw new IllegalStateException("Function is not supported");
		}
	}

	/**
	 * Helper methods to ensure a ByteBuffer is null-terminated
	 */
	public static void checkNullTerminated(ByteBuffer buf) {
		if (buf.get(buf.limit() - 1) != 0) {
			throw new IllegalArgumentException("Missing null termination");
		}
	}

	public static void checkNotNull(Object o) {
		if (o == null)
			throw new IllegalArgumentException("Null argument");
	}

	/**
	 * Helper methods to ensure a buffer is direct (and, implicitly, non-null).
	 */
	public static void checkDirect(ByteBuffer buf) {
		if (!buf.isDirect()) {
			throw new IllegalArgumentException("ByteBuffer is not direct");
		}
	}

	public static void checkDirect(ShortBuffer buf) {
		if (!buf.isDirect()) {
			throw new IllegalArgumentException("ShortBuffer is not direct");
		}
	}

	public static void checkDirect(IntBuffer buf) {
		if (!buf.isDirect()) {
			throw new IllegalArgumentException("IntBuffer is not direct");
		}
	}

	public static void checkDirect(LongBuffer buf) {
		if (!buf.isDirect()) {
			throw new IllegalArgumentException("LongBuffer is not direct");
		}
	}

	public static void checkDirect(FloatBuffer buf) {
		if (!buf.isDirect()) {
			throw new IllegalArgumentException("FloatBuffer is not direct");
		}
	}

	public static void checkDirect(DoubleBuffer buf) {
		if (!buf.isDirect()) {
			throw new IllegalArgumentException("DoubleBuffer is not direct");
		}
	}

	/**
	 * This is a separate call to help inline checkBufferSize.
	 */
	private static void throwBufferSizeException(Buffer buf, int size) {
		throw new IllegalArgumentException("Number of remaining buffer elements is " + buf.remaining() + ", must be at least " + size);
	}

	/**
	 * Helper method to ensure a buffer is big enough to receive data from a
	 * glGet* operation.
	 *
	 * @param buf
	 *            The buffer to check
	 * @param size
	 * 			  The minimum buffer size
	 * @throws IllegalArgumentException
	 */
	public static void checkBufferSize(Buffer buf, int size) {
		if (buf.remaining() < size) {
			throwBufferSizeException(buf, size);
		}
	}

	public static void checkBuffer(ByteBuffer buf, int size) {
		checkBufferSize(buf, size);
		checkDirect(buf);
	}

	public static void checkBuffer(ShortBuffer buf, int size) {
		checkBufferSize(buf, size);
		checkDirect(buf);
	}

	public static void checkBuffer(IntBuffer buf, int size) {
		checkBufferSize(buf, size);
		checkDirect(buf);
	}

	public static void checkBuffer(LongBuffer buf, int size) {
		checkBufferSize(buf, size);
		checkDirect(buf);
	}

	public static void checkBuffer(FloatBuffer buf, int size) {
		checkBufferSize(buf, size);
		checkDirect(buf);
	}

	public static void checkBuffer(DoubleBuffer buf, int size) {
		checkBufferSize(buf, size);
		checkDirect(buf);
	}
}
