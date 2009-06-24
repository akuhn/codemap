package ch.unibe.softwaremap.builder;

import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;

import ch.akuhn.hapax.index.TermDocumentMatrix;
import ch.unibe.softwaremap.Log;
import ch.unibe.softwaremap.SoftwareMap;

/**
 * Creates TDM in the background.
 * 
 * @author Adrian Kuhn
 * 
 */
public class HapaxBuilder extends IncrementalProjectBuilder {

	public static final String BUILDER_ID = SoftwareMap.makeID(HapaxBuilder.class);

	@Override
	@SuppressWarnings("unchecked")
	protected IProject[] build(int kind, Map args, IProgressMonitor monitor) throws CoreException {
		if (kind == FULL_BUILD) return fullBuild(monitor);
		IResourceDelta delta = getDelta(getProject());
		if (delta == null) return fullBuild(monitor);
		return incrementalBuild(delta, monitor);
	}

	private IProject[] incrementalBuild(IResourceDelta delta, IProgressMonitor monitor) {
		try {
			// TODO actually update the TDM here
			delta.accept(new IResourceDeltaVisitor() {
				public boolean visit(IResourceDelta delta) throws CoreException {
					switch (delta.getKind()) {

					}
					// System.out.println(delta.getResource());
					return true;
				}

			});
		} catch (CoreException e) {
			Log.error(e);
		}
		return defaultReturnValue();
	}

	private IProject[] defaultReturnValue() {
		return null; // defaults to current project
	}

	private IProject[] fullBuild(IProgressMonitor monitor) throws CoreException {
		if (!getProject().isNatureEnabled(JavaCore.NATURE_ID)) return defaultReturnValue();
		FullBuildVisitor visitor = new FullBuildVisitor();
		IJavaProject javaProject = JavaCore.create(getProject());
		for (IPackageFragmentRoot each: javaProject.getPackageFragmentRoots()) {
			if (each.isArchive()) {
				continue;
			}
			each.getCorrespondingResource().accept(visitor);
		}
		TermDocumentMatrix result = visitor.getResult();
		SoftwareMap.core().mapForChangedProject(getProject()).putTDM(result);
		return defaultReturnValue();
	}
}
