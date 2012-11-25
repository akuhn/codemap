package com.example.lawofdemeter;

import java.util.Arrays;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;

public class IJavaElementVisitor {

	ASTParser parser;
	
	public void visit(ICompilationUnit unit) {
		parser = ASTParser.newParser(AST.JLS3);
		parser.setResolveBindings(true);
		parser.setProject(unit.getJavaProject());
		try {
			this.visitCompilationUnit(unit);
		} catch (JavaModelException ex) {
			System.out.println("EX: " + ex);
			throw new RuntimeException(ex);
		}
	}
	
	public void visitCompilationUnit(ICompilationUnit unit) throws JavaModelException {
		System.out.println(unit.getHandleIdentifier());
		for (IJavaElement each: unit.getChildren()) {
			if (each instanceof IType) {
				this.visitTopLevelType((IType) each);
			}
		}
	}

	public void visitTopLevelType(IType type) throws JavaModelException {
		System.out.println(type.getHandleIdentifier());
		for (IJavaElement each: type.getChildren()) {
			if (each instanceof IField) {
				this.visitField((IField) each);
			}
			else if (each instanceof IMethod) {
				this.visitMethod((IMethod) each);
			}
			else if (each instanceof IType) {
				this.visitInnerType((IType) each);
			}
		}
	}

	private void visitInnerType(IType each) {
		// TODO Auto-generated method stub
		
	}

	private void visitMethod(IMethod each) throws JavaModelException {
		System.out.println("IJavaElementVisitor.visitMethod()");
		// TODO Auto-generated method stub
		System.out.println(Arrays.asList(each.getChildren()));
	}

	private void visitField(IField field) throws JavaModelException {
		System.out.println("IJavaElementVisitor.visitField()");
		System.out.println(Arrays.asList(field.getChildren()));
		System.out.println(field.getClass());
		System.out.println(field);
		System.out.println(field.getSource());
		System.out.println(field.getTypeSignature());
		System.out.println(field.getTypeRoot());
//		System.out.println("**b*");
//		try {
//			parser.setProject(field.getJavaProject());
//			IBinding[] bindings = parser.createBindings(new IJavaElement[] { field }, null);
//			System.out.println(bindings[0]);
//			System.out.println(bindings[0].getClass());
//			System.out.println(((IVariableBinding) bindings[0]).getType().getQualifiedName());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		System.out.println("**e*");
	}
	
}
