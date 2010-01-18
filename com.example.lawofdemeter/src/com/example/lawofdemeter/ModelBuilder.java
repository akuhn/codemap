package com.example.lawofdemeter;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IParent;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;

import com.example.lawofdemeter.model.Model;
import com.example.lawofdemeter.model.Type;

public class ModelBuilder {

	private IJavaProject project;
	private Model model;
	private Collection<IJavaElement> toBeResolved;

	public ModelBuilder(IJavaProject project) {
		this.project = project;
		this.model = new Model();
		this.toBeResolved = new ArrayList<IJavaElement>();
	}
	
	public void run() {
		this.visit(project);
		this.resolveElements();
		System.out.println(model);
	}

	private void resolveElements() {
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setResolveBindings(true);
		parser.setProject(project);
		IJavaElement[] elements = toBeResolved.toArray(new IJavaElement[toBeResolved.size()]);
		IBinding[] bindings = parser.createBindings(elements, null);
		for (IBinding each: bindings) this.resolveBinding(each);
	}

	private Void resolveBinding(IBinding binding) {
		if (binding == null) return null;
		switch (binding.getKind()) {
		case IBinding.METHOD: return resolveMethodBinding((IMethodBinding) binding);
		case IBinding.VARIABLE: return resolveVariableBinding((IVariableBinding) binding);
		default: System.out.println(binding.getClass());
		return null;
		}
	}

	private Void resolveVariableBinding(IVariableBinding binding) {
		Type declaringType = findType(binding.getDeclaringClass());
		if (declaringType == null) return null;
		Type fieldType = findType(binding.getType());
		declaringType.putField(binding.getName(), fieldType);
		return null;
	}

	private Type findType(ITypeBinding binding) {
		if (binding == null) return null;
		IJavaElement javaElement = binding.getJavaElement();
		if (javaElement == null) return null;
		return model.get(javaElement.getHandleIdentifier());
	}
	
	private Type[] findTypes(ITypeBinding[] bindings) {
		Type[] types = new Type[bindings.length];
		for (int i = 0; i < types.length; i++) types[i] = findType(bindings[i]);
		return types;
	}

	private Void resolveMethodBinding(IMethodBinding binding) {
		if (binding.isConstructor()) return null;
		Type declaringType = findType(binding.getDeclaringClass());
		if (declaringType == null) return null;
		Type returnType = findType(binding.getReturnType());
		Type[] parameterTypes = findTypes(binding.getParameterTypes());
		declaringType.putMethod(binding.getName(), returnType, parameterTypes);
		return null;
	}

	private void visit(IJavaElement element) {
		if (this.dispatchVisit(element) && element instanceof IParent) this.visitChildren((IParent) element);
	}

	private void visitChildren(IParent element) {
		try {
			for (IJavaElement child: element.getChildren()) this.visit(child);
		} catch (JavaModelException ex) {
			throw new RuntimeException(ex);
		}
	}

	private boolean dispatchVisit(IJavaElement element) {
		switch (element.getElementType()) {
		case IJavaElement.COMPILATION_UNIT: return visitCompilationUnit((ICompilationUnit) element);
		case IJavaElement.TYPE: return visitType((IType) element);
		case IJavaElement.FIELD: return visitField((IField) element);
		case IJavaElement.METHOD: return visitMethod((IMethod) element);
		case IJavaElement.PACKAGE_FRAGMENT: return visitPackageFragment((IPackageFragment) element);
		case IJavaElement.PACKAGE_FRAGMENT_ROOT: return visitPackageFragmentRoot((IPackageFragmentRoot) element);
		default: return true;
		}	
	}

	private boolean visitPackageFragmentRoot(IPackageFragmentRoot element) {
		return !element.isArchive();
	}

	private boolean visitPackageFragment(IPackageFragment element) {
		return true;
	}

	private boolean visitField(IField element) {
		toBeResolved.add(element);
		return true;
	}

	private boolean visitMethod(IMethod element) {
		toBeResolved.add(element);
		return true;
	}

	private boolean visitType(IType element) {
		Type type = model.get(element.getHandleIdentifier());
		type.setName(element.getFullyQualifiedName());
		return true;
	}

	private boolean visitCompilationUnit(ICompilationUnit element) {
		return true;
	}
	
	
	
}
