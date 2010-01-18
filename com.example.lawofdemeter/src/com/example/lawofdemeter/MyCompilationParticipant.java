package com.example.lawofdemeter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.compiler.BuildContext;
import org.eclipse.jdt.core.compiler.CompilationParticipant;
import org.eclipse.jdt.core.compiler.ReconcileContext;

public class MyCompilationParticipant extends CompilationParticipant {

	private Map<IJavaProject,BuildContext[]> cache;
	
	public MyCompilationParticipant() {
		cache = new HashMap<IJavaProject, BuildContext[]>();
	}
	
	@Override
	public boolean isActive(IJavaProject project) {
		return true;
	}
	
	@Override
	public void buildFinished(IJavaProject project) {
		System.out.println("MyCompilationParticipant.buildFinished()");
//		System.out.println(project.getElementName());
		new ModelBuilder(project).run();
		
	}
	
	@Override
	public int aboutToBuild(IJavaProject project) {
//		System.out.println("MyCompilationParticipant.aboutToBuild()");
//		System.out.println(project.getElementName());
		return READY_FOR_BUILD;
	}
	
	@Override
	public void buildStarting(BuildContext[] files, boolean isBatch) {
//		for (BuildContext each: files) {
//			IFile file = each.getFile();
//			ICompilationUnit unit = JavaCore.createCompilationUnitFrom(file);
//			new IJavaElementVisitor().visit(unit);
//		}
//		System.out.println("MyCompilationParticipant.buildStarting()");
//		if (files.length > 0) {
//			ICompilationUnit[] units = new ICompilationUnit[files.length];
//			for (int i = 0; i < units.length; i++) {
//				units[i] = (ICompilationUnit) JavaCore.create(files[i].getFile());
//			}
//			ASTParser parser = ASTParser.newParser(AST.JLS3);
//			parser.setResolveBindings(true);
//			parser.setProject(units[0].getJavaProject());
//			parser.createASTs(units, new String[] {}, new ASTRequestor() {
//				@Override
//				public void acceptAST(ICompilationUnit source, CompilationUnit ast) {
//					System.out.println(source.getElementName());
//				}
//				@Override
//				public void acceptBinding(String bindingKey, IBinding binding) {
//					System.out.println(bindingKey + "->" + binding);
//				}
//			}, null);
//		}
	}
	
	@Override
	public void cleanStarting(IJavaProject project) {
//		System.out.println("MyCompilationParticipant.cleanStarting()");
//		System.out.println(project.getElementName());
	}

	@Override
	public boolean isAnnotationProcessor() {
		return false;
	}
	
	@Override
	public void processAnnotations(BuildContext[] files) {
//		System.out.println("MyCompilationParticipant.processAnnotations()");
//		System.out.println(Arrays.asList(files));
	}
	
	@Override
	public void reconcile(ReconcileContext context) {
//		System.out.println("MyCompilationParticipant.reconcile()");
//		System.out.println(context);
//		System.out.println(context.getDelta());
//		try {
//			System.out.println(context.getAST3());
//		} catch (JavaModelException e) {
//			e.printStackTrace();
//		}
	}
	
}
