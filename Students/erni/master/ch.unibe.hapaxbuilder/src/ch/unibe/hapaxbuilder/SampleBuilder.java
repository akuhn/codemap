package ch.unibe.hapaxbuilder;

import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

public class SampleBuilder extends IncrementalProjectBuilder {

	public static final String BUILDER_ID = SampleBuilder.class.getName();

	
	protected IProject[] build(int kind, Map args, IProgressMonitor monitor)
			throws CoreException {
		if (kind == FULL_BUILD) {
			fullBuild(monitor);
		} else {
			IResourceDelta delta = getDelta(getProject());
			if (delta == null) {
				fullBuild(monitor);
			} else {
				incrementalBuild(delta, monitor);
			}
		}
		return null;
	}

    private void incrementalBuild(IResourceDelta delta, IProgressMonitor monitor) {
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
        
    }

    private void fullBuild(IProgressMonitor monitor) {
        System.out.println("fullbuild of" + getProject());
    }
}
