package org.codemap.plugin.eclemma;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.codemap.util.Resources;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

import ch.akuhn.util.Pair;

import com.mountainminds.eclemma.core.CoverageTools;
import com.mountainminds.eclemma.core.analysis.IJavaCoverageListener;
import com.mountainminds.eclemma.core.analysis.IJavaElementCoverage;
import com.mountainminds.eclemma.core.analysis.IJavaModelCoverage;

public class CoverageListener implements IJavaCoverageListener {

	protected final class CoverageResourceVisitor implements IResourceVisitor {
		private final List<Pair<String, Double>> identifiers;

		protected CoverageResourceVisitor(List<Pair<String, Double>> identifiers) {
			this.identifiers = identifiers;
		}

		@Override
		public boolean visit(IResource resource) throws CoreException {
			IJavaElement javaElement = JavaCore.create(resource);
			if (javaElement == null) return true;
			if (javaElement.getElementType() != IJavaElement.COMPILATION_UNIT) return true;
			ICompilationUnit compilationUnit = (ICompilationUnit) javaElement.getAdapter(ICompilationUnit.class);
			return visit(compilationUnit);
		}

		private boolean visit(ICompilationUnit compilationUnit) {
			
			String identifier = Resources.asPath(compilationUnit);
			IJavaElementCoverage coverageInfo = CoverageTools.getCoverageInfo(compilationUnit);
			if (coverageInfo == null) {
				// for interfaces where we do not have coverage information
				return false;
			}
			identifiers.add(new Pair<String, Double>(identifier, coverageInfo.getLineCounter().getRatio()));
//			System.out.println(identifier + " " + coverageInfo.getLineCounter().getRatio());
			return false;
		}
	}

	@Override
	public void coverageChanged() {
		IJavaModelCoverage coverage = CoverageTools.getJavaModelCoverage();
		if (coverage == null) return;
		
		List<IJavaProject> projects = Arrays.asList(coverage.getInstrumentedProjects());
		for(IJavaProject each: projects) {
			if (! each.isOpen()) continue;
//			System.out.println("coverage changed for: " + each.getHandleIdentifier());
//			IJavaElementCoverage coverageInfo = CoverageTools.getCoverageInfo(each);
//			System.out.println(coverageInfo.getMethodCounter().getRatio());
			
			List<Pair<String, Double>> coverageInfo = compilationUnitCoverage(each);
			
			displayCoverage(coverageInfo);
		}
	}

	private void displayCoverage(List<Pair<String, Double>> coverageInfo) {
		ShowCoverageAction showCoverageAction = EclemmaOverlay.getPlugin().getCoverageAction();
		showCoverageAction.newCoverageAvailable(coverageInfo);		
	}

	private List<Pair<String, Double>> compilationUnitCoverage(IJavaProject project) {
		List<Pair<String, Double>> identifiers = new ArrayList<Pair<String,Double>>();
		try {
			for(IPackageFragmentRoot root: project.getPackageFragmentRoots()) {
				IResource resource = root.getResource();
				if (resource == null) continue;
				resource.accept(new CoverageResourceVisitor(identifiers));
			}
		} catch (JavaModelException e) {
			e.printStackTrace();
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return identifiers;
	}
}
