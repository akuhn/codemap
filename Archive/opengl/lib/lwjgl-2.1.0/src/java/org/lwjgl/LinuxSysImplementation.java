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


/**
 *
 * @author elias_naur <elias_naur@users.sourceforge.net>
 * @version $Revision: 3122 $
 * $Id: LinuxSysImplementation.java 3122 2008-09-07 08:21:28Z elias_naur $
 */
final class LinuxSysImplementation extends J2SESysImplementation {
	private final static int JNI_VERSION = 17;

	static {
		java.awt.Toolkit.getDefaultToolkit(); // This will make sure libjawt.so is loaded
	}

	public int getRequiredJNIVersion() {
		return JNI_VERSION;
	}

	public boolean openURL(final String url) {
		// Linux may as well resort to pure Java hackery, as there's no Linux native way of doing it
		// right anyway.

		String[] browsers = {"xdg-open", "firefox", "mozilla", "opera", "konqueror", "nautilus", "galeon", "netscape"};

		for (int i = 0; i < browsers.length; i ++) {
			final String browser = browsers[i];
			try {
				LWJGLUtil.execPrivileged(new String[] { browser, url });
				return true;
			} catch (Exception e) {
				// Ignore
				e.printStackTrace(System.err);
			}
		}

		// Seems to have failed
		return false;
	}

	public boolean has64Bit() {
		return true;
	}
}
