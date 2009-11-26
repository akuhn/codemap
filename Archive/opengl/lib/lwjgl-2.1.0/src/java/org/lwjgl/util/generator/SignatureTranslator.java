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

package org.lwjgl.util.generator;

/**
 *
 * A TypeVisitor that translates types to JNI signatures.
 *
 * @author elias_naur <elias_naur@users.sourceforge.net>
 * @version $Revision: 2983 $
 * $Id: SignatureTranslator.java 2983 2008-04-07 18:36:09Z matzon $
 */

import com.sun.mirror.type.*;
import com.sun.mirror.util.*;

import java.nio.*;

class SignatureTranslator implements TypeVisitor {
	private final boolean add_position_signature;
	private final StringBuilder signature = new StringBuilder();

	SignatureTranslator(boolean add_position_signature) {
		this.add_position_signature = add_position_signature;
	}

	private static String getNativeNameFromClassName(String class_name) {
		return class_name.replaceAll("\\.", "/");
	}

	public String getSignature() {
		return signature.toString();
	}

	public void visitAnnotationType(AnnotationType t) {
		throw new RuntimeException(t + " is not allowed");
	}

	public void visitArrayType(ArrayType t) {
		throw new RuntimeException(t + " is not allowed");
	}

	public void visitClassType(ClassType t) {
		String type_name = getNativeNameFromClassName(t.getDeclaration().getQualifiedName());
		signature.append("L");
		signature.append(type_name);
		signature.append(";");
		if (add_position_signature && Buffer.class.isAssignableFrom(NativeTypeTranslator.getClassFromType(t))) {
			signature.append("I");
		}
	}

	public void visitDeclaredType(DeclaredType t) {
		throw new RuntimeException(t + " is not allowed");
	}

	public void visitEnumType(EnumType t) {
		throw new RuntimeException(t + " is not allowed");
	}

	public void visitInterfaceType(InterfaceType t) {
		throw new RuntimeException(t + " is not allowed");
	}

	public void visitPrimitiveType(PrimitiveType t) {
		switch (t.getKind()) {
			case BOOLEAN:
				signature.append("Z");
				break;
			case INT:
				signature.append("I");
				break;
			case FLOAT:
				signature.append("F");
				break;
			case SHORT:
				signature.append("S");
				break;
			case DOUBLE:
				signature.append("D");
				break;
			case BYTE:
				signature.append("B");
				break;
			case LONG:
				signature.append("J");
				break;
			default:
				throw new RuntimeException("Unsupported type " + t);
		}
	}

	public void visitReferenceType(ReferenceType t) {
		throw new RuntimeException(t + " is not allowed");
	}

	public void visitTypeMirror(TypeMirror t) {
		throw new RuntimeException(t + " is not allowed");
	}

	public void visitTypeVariable(TypeVariable t) {
		throw new RuntimeException(t + " is not allowed");
	}

	public void visitVoidType(VoidType t) {
		signature.append("V");
	}

	public void visitWildcardType(WildcardType t) {
		throw new RuntimeException(t + " is not allowed");
	}
}
