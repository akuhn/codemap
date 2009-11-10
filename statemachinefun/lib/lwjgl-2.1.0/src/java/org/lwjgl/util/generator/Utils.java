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
 * Various utility methods to the generator.
 *
 * @author elias_naur <elias_naur@users.sourceforge.net>
 * @version $Revision: 2983 $
 * $Id: Utils.java 2983 2008-04-07 18:36:09Z matzon $
 */

import com.sun.mirror.type.*;
import java.nio.Buffer;
import java.io.*;
import java.util.*;

import com.sun.mirror.declaration.*;

public class Utils {
	public static final String TYPEDEF_POSTFIX = "PROC";
	public static final String FUNCTION_POINTER_VAR_NAME = "function_pointer";
	public static final String FUNCTION_POINTER_POSTFIX = "_pointer";
	public static final String CHECKS_CLASS_NAME = "GLChecks";
	public static final String CONTEXT_CAPS_CLASS_NAME = "ContextCapabilities";
	public static final String STUB_INITIALIZER_NAME = "initNativeStubs";
	public static final String BUFFER_OBJECT_METHOD_POSTFIX = "BO";
	public static final String BUFFER_OBJECT_PARAMETER_POSTFIX = "_buffer_offset";
	public static final String RESULT_SIZE_NAME = "result_size";
	public static final String RESULT_VAR_NAME = "__result";
	public static final String CACHED_BUFFER_NAME = "old_buffer";
	private static final String OVERLOADED_METHOD_PREFIX = "n";

	public static String getTypedefName(MethodDeclaration method) {
		return method.getSimpleName() + TYPEDEF_POSTFIX;
	}
	
	public static String getFunctionAddressName(InterfaceDeclaration interface_decl, MethodDeclaration method) {
		return interface_decl.getSimpleName() + "_" + method.getSimpleName() + FUNCTION_POINTER_POSTFIX;
	}

	public static boolean isFinal(InterfaceDeclaration d) {
		Extension extension_annotation = d.getAnnotation(Extension.class);
		return extension_annotation == null || extension_annotation.isFinal();
	}

	private static class AnnotationMirrorComparator implements Comparator<AnnotationMirror> {
		public int compare(AnnotationMirror a1, AnnotationMirror a2) {
			String n1 = a1.getAnnotationType().getDeclaration().getQualifiedName();
			String n2 = a2.getAnnotationType().getDeclaration().getQualifiedName();
			int result = n1.compareTo(n2);
			return result;
		}

		public boolean equals(AnnotationMirror a1, AnnotationMirror a2) {
			return compare(a1, a2) == 0;
		}
	}

	public static Collection<AnnotationMirror> getSortedAnnotations(Collection<AnnotationMirror> annotations) {
		List<AnnotationMirror> annotation_list = new ArrayList<AnnotationMirror>(annotations);
		Collections.sort(annotation_list, new AnnotationMirrorComparator());
		return annotation_list;
	}

	public static String getReferenceName(InterfaceDeclaration interface_decl, MethodDeclaration method, ParameterDeclaration param) {
		return interface_decl.getSimpleName() + "_" + method.getSimpleName() + "_" + param.getSimpleName();
	}

	public static boolean isAddressableType(TypeMirror type) {
		return isAddressableType(getJavaType(type));
	}

	public static boolean isAddressableType(Class type) {
		return Buffer.class.isAssignableFrom(type) || String.class.equals(type);
	}

	public static Class getJavaType(TypeMirror type_mirror) {
		JavaTypeTranslator translator = new JavaTypeTranslator();
		type_mirror.accept(translator);
		return translator.getType();
	}

	private static boolean hasParameterMultipleTypes(ParameterDeclaration param) {
		int num_native_annotations = 0;
		for (AnnotationMirror annotation : param.getAnnotationMirrors())
			if (NativeTypeTranslator.getAnnotation(annotation, NativeType.class) != null)
				num_native_annotations++;
		return num_native_annotations > 1;
	}

	public static boolean isParameterMultiTyped(ParameterDeclaration param) {
		boolean result = Buffer.class.equals(Utils.getJavaType(param.getType()));
		if (!result && hasParameterMultipleTypes(param))
			throw new RuntimeException(param + " not defined as java.nio.Buffer but has multiple types");
		return result;
	}

	public static ParameterDeclaration findParameter(MethodDeclaration method, String name) {
		for (ParameterDeclaration param : method.getParameters())
			if (param.getSimpleName().equals(name))
				return param;
		throw new RuntimeException("Parameter " + name + " not found");
	}

	public static void printDocComment(PrintWriter writer, Declaration decl) {
		String doc_comment = decl.getDocComment();
		if (doc_comment != null) {
			writer.println("\t/**");
			StringTokenizer doc_lines = new StringTokenizer(doc_comment, "\n");
			while (doc_lines.hasMoreTokens())
				writer.println("\t *" + doc_lines.nextToken());
			writer.println("\t */");
		}
	}

	public static AnnotationMirror getParameterAutoAnnotation(ParameterDeclaration param) {
		for (AnnotationMirror annotation : param.getAnnotationMirrors())
			if (NativeTypeTranslator.getAnnotation(annotation, Auto.class) != null)
				return annotation;
		return null;
	}

	public static boolean isMethodIndirect(boolean generate_error_checks, boolean context_specific, MethodDeclaration method) {
		for (ParameterDeclaration param : method.getParameters()) {
			if (isAddressableType(param.getType()) || getParameterAutoAnnotation(param) != null ||
					param.getAnnotation(Constant.class) != null)
				return true;
		}
		return hasMethodBufferObjectParameter(method) || method.getAnnotation(Code.class) != null ||
			method.getAnnotation(CachedResult.class) != null ||
			(generate_error_checks && method.getAnnotation(NoErrorCheck.class) == null) ||
			context_specific;
	}

	public static String getNativeQualifiedName(String qualified_name) {
		return qualified_name.replaceAll("\\.", "_");
	}

	public static String getQualifiedNativeMethodName(String qualified_class_name, String method_name) {
		return "Java_" + getNativeQualifiedName(qualified_class_name) + "_" + method_name;
	}

	public static String getQualifiedNativeMethodName(String qualified_class_name, MethodDeclaration method, boolean generate_error_checks, boolean context_specific) {
		String method_name = getSimpleNativeMethodName(method, generate_error_checks, context_specific);
		return getQualifiedNativeMethodName(qualified_class_name, method_name);
	}

	public static ParameterDeclaration getResultParameter(MethodDeclaration method) {
		ParameterDeclaration result_param = null;
		for (ParameterDeclaration param : method.getParameters()) {
			if (param.getAnnotation(Result.class) != null) {
				if (result_param != null)
					throw new RuntimeException("Multiple parameters annotated with Result in method " + method);
				result_param = param;
			}
		}
		return result_param;
	}

	public static TypeMirror getMethodReturnType(MethodDeclaration method) {
		TypeMirror result_type;
		ParameterDeclaration result_param = getResultParameter(method);
		if (result_param != null) {
			result_type = result_param.getType();
		} else
			result_type = method.getReturnType();
		return result_type;
	}

	public static boolean needResultSize(MethodDeclaration method) {
		return getNIOBufferType(getMethodReturnType(method)) != null && method.getAnnotation(AutoResultSize.class) == null;
	}
	
	public static void printExtraCallArguments(PrintWriter writer, MethodDeclaration method, String size_parameter_name) {
		writer.print(size_parameter_name);
		if (method.getAnnotation(CachedResult.class) != null) {
			writer.print(", " + CACHED_BUFFER_NAME);
		}
	}

	private static String getClassName(InterfaceDeclaration interface_decl, String opengl_name) {
		Extension extension_annotation = interface_decl.getAnnotation(Extension.class);
		if (extension_annotation != null && !"".equals(extension_annotation.className())) {
			return extension_annotation.className();
		}
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < opengl_name.length(); i++) {
			int ch = opengl_name.codePointAt(i);
			if (ch == '_') {
				i++;
				result.appendCodePoint(Character.toUpperCase(opengl_name.codePointAt(i)));
			} else
				result.appendCodePoint(ch);
		}
		return result.toString();
	}

	public static boolean hasMethodBufferObjectParameter(MethodDeclaration method) {
		for (ParameterDeclaration param : method.getParameters()) {
			if (param.getAnnotation(BufferObject.class) != null) {
				return true;
			}
		}
		return false;
	}

	public static String getQualifiedClassName(InterfaceDeclaration interface_decl) {
		return interface_decl.getPackage().getQualifiedName() + "." + getSimpleClassName(interface_decl);
	}

	public static String getSimpleClassName(InterfaceDeclaration interface_decl) {
		return getClassName(interface_decl, interface_decl.getSimpleName());
	}

	public static Class<?> getNIOBufferType(TypeMirror t) {
		Class<?> param_type = getJavaType(t);
		if (Buffer.class.isAssignableFrom(param_type))
			return param_type;
		else
			return null;
	}

	public static String getSimpleNativeMethodName(MethodDeclaration method, boolean generate_error_checks, boolean context_specific) {
		String method_name = method.getSimpleName();
		if (isMethodIndirect(generate_error_checks, context_specific, method))
			method_name = OVERLOADED_METHOD_PREFIX + method_name;
		return method_name;
	}

}
