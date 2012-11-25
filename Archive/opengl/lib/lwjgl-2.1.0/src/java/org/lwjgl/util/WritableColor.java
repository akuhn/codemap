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
package org.lwjgl.util;

import java.nio.ByteBuffer;

/**
 * Write interface for Colors
 * @author $Author: matzon $
 * @version $Revision: 2983 $
 * $Id: WritableColor.java 2983 2008-04-07 18:36:09Z matzon $
 */
public interface WritableColor {
	/**
	 * Set a color
	 */
	public void set(int r, int g, int b, int a);
	/**
	 * Set a color
	 */
	public void set(byte r, byte g, byte b, byte a);
	/**
	 * Set a color
	 */
	public void set(int r, int g, int b);
	/**
	 * Set a color
	 */
	public void set(byte r, byte g, byte b);
	/**
	 * Set the Red component
	 */
	public void setRed(int red);
	/**
	 * Set the Green component
	 */
	public void setGreen(int green);
	/**
	 * Set the Blue component
	 */
	public void setBlue(int blue);
	/**
	 * Set the Alpha component
	 */
	public void setAlpha(int alpha);
	/**
	 * Set the Red component
	 */
	public void setRed(byte red);
	/**
	 * Set the Green component
	 */
	public void setGreen(byte green);
	/**
	 * Set the Blue component
	 */
	public void setBlue(byte blue);
	/**
	 * Set the Alpha component
	 */
	public void setAlpha(byte alpha);
	/**
	 * Read a color from a byte buffer
	 * @param src The source buffer
	 */
	public void readRGBA(ByteBuffer src);
	/**
	 * Read a color from a byte buffer
	 * @param src The source buffer
	 */
	public void readRGB(ByteBuffer src);
	/**
	 * Read a color from a byte buffer
	 * @param src The source buffer
	 */
	public void readARGB(ByteBuffer src);
	/**
	 * Read a color from a byte buffer
	 * @param src The source buffer
	 */
	public void readBGRA(ByteBuffer src);
	/**
	 * Read a color from a byte buffer
	 * @param src The source buffer
	 */
	public void readBGR(ByteBuffer src);
	/**
	 * Read a color from a byte buffer
	 * @param src The source buffer
	 */
	public void readABGR(ByteBuffer src);
	/**
	 * Set this color's color by copying another color
	 * @param src The source color
	 */
	public void setColor(ReadableColor src);
}