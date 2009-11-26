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

import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableCollection;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

import com.sun.mirror.apt.AnnotationProcessor;
import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.apt.AnnotationProcessorFactory;
import com.sun.mirror.apt.AnnotationProcessors;
import com.sun.mirror.apt.Filer;
import com.sun.mirror.apt.RoundCompleteEvent;
import com.sun.mirror.apt.RoundCompleteListener;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;
import com.sun.mirror.declaration.InterfaceDeclaration;
import com.sun.mirror.declaration.MethodDeclaration;
import com.sun.mirror.declaration.ParameterDeclaration;
import com.sun.mirror.declaration.TypeDeclaration;
import com.sun.mirror.util.DeclarationFilter;

/**
 *
 * Generator tool for creating the References class
 *
 * @author elias_naur <elias_naur@users.sourceforge.net>
 * @version $Revision: 2983 $
 * $Id: ReferencesGeneratorProcessorFactory.java 2983 2008-04-07 18:36:09Z matzon $
 */
public class ReferencesGeneratorProcessorFactory implements AnnotationProcessorFactory, RoundCompleteListener {
	private final static String REFERENCES_CLASS_NAME = "References";
	private final static String REFERENCES_PARAMETER_NAME = "references";

	private static boolean first_round = true;
	
	// Process any set of annotations
	private static final Collection<String> supportedAnnotations =
		unmodifiableCollection(Arrays.asList("*"));

	public Collection<String> supportedAnnotationTypes() {
		return supportedAnnotations;
	}

	public Collection<String> supportedOptions() {
		return emptyList();
	}

	public void roundComplete(RoundCompleteEvent event) {
		first_round = false;
	}

	public AnnotationProcessor getProcessorFor(Set<AnnotationTypeDeclaration> atds, AnnotationProcessorEnvironment env) {
		// Only process the initial types, not the generated ones
		if (first_round) {
			env.addListener(this);
			return new GeneratorProcessor(env);
		} else
			return AnnotationProcessors.NO_OP;
	}

	private static class GeneratorProcessor implements AnnotationProcessor {
		private final AnnotationProcessorEnvironment env;

		GeneratorProcessor(AnnotationProcessorEnvironment env) {
			this.env = env;
		}

		public void process() {
			try {
				generateReferencesSource();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		private static void generateClearsFromParameters(PrintWriter writer, InterfaceDeclaration interface_decl, MethodDeclaration method) {
			for (ParameterDeclaration param : method.getParameters()) {
				CachedReference cached_reference_annotation = param.getAnnotation(CachedReference.class);
				if (cached_reference_annotation != null && cached_reference_annotation.name().length() == 0) {
					Class nio_type = Utils.getNIOBufferType(param.getType());
					String reference_name = Utils.getReferenceName(interface_decl, method, param);
					writer.println("\t\tthis." + reference_name + " = null;");
				}
			}
		}
		
		private static void generateCopiesFromParameters(PrintWriter writer, InterfaceDeclaration interface_decl, MethodDeclaration method) {
			for (ParameterDeclaration param : method.getParameters()) {
				CachedReference cached_reference_annotation = param.getAnnotation(CachedReference.class);
				if (cached_reference_annotation != null && cached_reference_annotation.name().length() == 0) {
					Class nio_type = Utils.getNIOBufferType(param.getType());
					String reference_name = Utils.getReferenceName(interface_decl, method, param);
					writer.print("\t\tthis." + reference_name + " = ");
					writer.println(REFERENCES_PARAMETER_NAME + "." + reference_name + ";");
				}
			}
		}
		
		private static void generateClearsFromMethods(PrintWriter writer, InterfaceDeclaration interface_decl) {
			for (MethodDeclaration method : interface_decl.getMethods()) {
				generateClearsFromParameters(writer, interface_decl, method);
			}
		}

		private static void generateCopiesFromMethods(PrintWriter writer, InterfaceDeclaration interface_decl) {
			for (MethodDeclaration method : interface_decl.getMethods()) {
				generateCopiesFromParameters(writer, interface_decl, method);
			}
		}

		private static void generateReferencesFromParameters(PrintWriter writer, InterfaceDeclaration interface_decl, MethodDeclaration method) {
			for (ParameterDeclaration param : method.getParameters()) {
				CachedReference cached_reference_annotation = param.getAnnotation(CachedReference.class);
				if (cached_reference_annotation != null && cached_reference_annotation.name().length() == 0) {
					Class nio_type = Utils.getNIOBufferType(param.getType());
					if (nio_type == null)
						throw new RuntimeException(param + " in method " + method + " in " + interface_decl + " is annotated with "
								+ cached_reference_annotation.annotationType().getSimpleName() + " but the parameter is not a NIO buffer");
					writer.print("\t" + nio_type.getName() + " " + Utils.getReferenceName(interface_decl, method, param));
					writer.println(";");
				}
			}
		}
		
		private static void generateReferencesFromMethods(PrintWriter writer, InterfaceDeclaration interface_decl) {
			for (MethodDeclaration method : interface_decl.getMethods()) {
				generateReferencesFromParameters(writer, interface_decl, method);
			}
		}

		private void generateReferencesSource() throws IOException {
			PrintWriter writer = env.getFiler().createTextFile(Filer.Location.SOURCE_TREE, "org.lwjgl.opengl", new File(REFERENCES_CLASS_NAME + ".java"), null);
			writer.println("/* MACHINE GENERATED FILE, DO NOT EDIT */");
			writer.println();
			writer.println("package org.lwjgl.opengl;");
			writer.println();
			writer.println("class " + REFERENCES_CLASS_NAME + " extends BaseReferences {");
                        writer.println("\t" + REFERENCES_CLASS_NAME + "(ContextCapabilities caps) {");
                        writer.println("\t\tsuper(caps);");
                        writer.println("\t}");
			DeclarationFilter filter = DeclarationFilter.getFilter(InterfaceDeclaration.class);
			Collection<TypeDeclaration> interface_decls = filter.filter(env.getSpecifiedTypeDeclarations());
			for (TypeDeclaration typedecl : interface_decls) {
				InterfaceDeclaration interface_decl = (InterfaceDeclaration)typedecl;
				generateReferencesFromMethods(writer, interface_decl);
			}
			writer.println();
			writer.println("\tvoid copy(" + REFERENCES_CLASS_NAME + " " + REFERENCES_PARAMETER_NAME + ") {");
			writer.println("\t\tsuper.copy(" + REFERENCES_PARAMETER_NAME + ");");
			for (TypeDeclaration typedecl : interface_decls) {
				InterfaceDeclaration interface_decl = (InterfaceDeclaration)typedecl;
				generateCopiesFromMethods(writer, interface_decl);
			}
			writer.println("\t}");
			writer.println("\tvoid clear() {");
			writer.println("\t\tsuper.clear();");
			for (TypeDeclaration typedecl : interface_decls) {
				InterfaceDeclaration interface_decl = (InterfaceDeclaration)typedecl;
				generateClearsFromMethods(writer, interface_decl);
			}
			writer.println("\t}");
			writer.println("}");
			writer.close();
		}
	}
}
