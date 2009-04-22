package ch.unibe.hapaxbuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;

import ch.akuhn.hapax.corpus.Terms;
import ch.akuhn.hapax.index.TermDocumentMatrix;
import ch.akuhn.util.Throw;
import ch.unibe.hapaxbuilder.util.Get;

public class MSEBuilder extends IncrementalProjectBuilder {

    private static class HapaxVisitor implements IResourceVisitor {

        private IPath workspaceLocation;
        private TermDocumentMatrix TDM;

        public HapaxVisitor(IPath workspaceLocation) {
            this.workspaceLocation = workspaceLocation;
            TDM = new TermDocumentMatrix();
        }

        public boolean visit(IResource resource) throws CoreException {
            IJavaElement javaElement = JavaCore.create(resource);
            if (javaElement.getElementType() != IJavaElement.COMPILATION_UNIT) return true;
            
            String absolutePath = workspaceLocation.toOSString() + javaElement.getPath().toOSString();
            TDM.makeDocument(absolutePath).addTerms(new Terms(new File(absolutePath)));
            // do not descend any further (we are at a leaf anyway ...)
            return false;
        }
        
        public TermDocumentMatrix getResult() {
            return TDM;
        }
    }

    public static final String BUILDER_ID = MSEBuilder.class.getName();

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
        // TODO use IResourceProxyVisitor for better performance (what are
        // member flags?)
        try {
            // TODO select all java files within a source folder, how can this
            // be done?
            System.out.println("Java nature enabled: "
                    + getProject().isNatureEnabled(JavaCore.NATURE_ID));
            if (getProject().isNatureEnabled(JavaCore.NATURE_ID)) {
                IPath location = getProject().getWorkspace().getRoot()
                        .getLocation();
                IJavaProject javaProject = JavaCore.create(getProject());
                List<IPackageFragmentRoot> sourcePackageRoots = Get
                        .sourcePackageRoots(javaProject
                                .getPackageFragmentRoots());
                for (IPackageFragmentRoot packageFragmentRoot : sourcePackageRoots) {
                    HapaxVisitor hapaxVisitor = new HapaxVisitor(location);
                    IResource correspondingResource = packageFragmentRoot
                            .getCorrespondingResource();
                    correspondingResource.accept(hapaxVisitor);
                    System.out.println(hapaxVisitor.getResult().rejectAndWeight());
                }
            }
            // we do not need this at the moment
        } catch (CoreException e) {
            e.printStackTrace();
        }
    }
}
