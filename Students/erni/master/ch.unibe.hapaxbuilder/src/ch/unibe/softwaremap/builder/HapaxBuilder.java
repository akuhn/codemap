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
import ch.unibe.softwaremap.SoftwareMapCore;

public class HapaxBuilder extends IncrementalProjectBuilder {

	public static final String BUILDER_ID = SoftwareMapCore.makeID(HapaxBuilder.class);

	@Override
	@SuppressWarnings("unchecked")
	protected IProject[] build(int kind, Map args, IProgressMonitor monitor) throws CoreException {
		if (kind == FULL_BUILD) return fullBuild(monitor);
		IResourceDelta delta = getDelta(getProject());
		if (delta == null) return fullBuild(monitor);
		return incrementalBuild(delta, monitor);
	}

	private IProject[] incrementalBuild(IResourceDelta delta, IProgressMonitor monitor) {
		System.out.println(delta);

		try {
			delta.accept(new IResourceDeltaVisitor() {

				public boolean visit(IResourceDelta delta) throws CoreException {
					switch (delta.getKind()) {
					case IResourceDelta.ADDED:
						System.out.print("ADDED");
						break;
					case IResourceDelta.REMOVED:
						System.out.print("REMOVED");
						break;
					case IResourceDelta.CHANGED:
						System.out.print("CHANGED");
						break;
					default:
						System.out.print("[" + delta.getKind() + "]");
						break;
					}
					System.out.println(delta.getResource());
					return true;
				}

			});
		} catch (CoreException e) {
			e.printStackTrace();
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
			if (each.isArchive()) continue;
			each.getCorrespondingResource().accept(visitor);
		}
		TermDocumentMatrix result = visitor.getResult();
		SoftwareMapCore.at(getProject()).putTDM(result);
		return defaultReturnValue();
	}
}
