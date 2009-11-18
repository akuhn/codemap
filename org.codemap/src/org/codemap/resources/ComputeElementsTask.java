package org.codemap.resources;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import org.codemap.util.Log;
import org.codemap.util.Resources;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.internal.junit.launcher.JUnit3TestFinder;
import org.eclipse.jdt.internal.junit.launcher.JUnit4TestFinder;
import org.eclipse.jdt.junit.JUnitCore;

import ch.akuhn.util.Files;
import ch.akuhn.util.ProgressMonitor;
import ch.akuhn.values.Arguments;
import ch.akuhn.values.TaskValue;
import ch.akuhn.values.Value;

public class ComputeElementsTask extends TaskValue<Collection<String>> {
    
    private Value<Boolean> testsEnabled;

    public ComputeElementsTask(Value<Collection<String>> projects, Value<Collection<String>> extensions, Value<Boolean> showTests) {
        super("Collecting selected resources", projects, extensions, showTests);
        this.testsEnabled = showTests;
    }

    @Override
    protected Collection<String> computeValue(ProgressMonitor monitor, Arguments args) {
        Collection<String> projects = args.nextOrFail();
        final Collection<String> extensions = args.nextOrFail();
        final Collection<String> result = new HashSet<String>();
        for (String path: projects) {
            IResource project = Resources.asResource(path);
            // fix error for closed projects
            if (!project.isAccessible()) continue;
            
            try {
                boolean showTests = testsEnabled.getValue();
                FindElementsVisitor visitor = new FindElementsVisitor(extensions, showTests);
                project.accept(visitor);
                visitor.storeResult(result);
            } catch (CoreException ex) {
                Log.error(ex);
            }
        }
        return new ArrayList<String>(result);
    }
}

class FindElementsVisitor implements IResourceVisitor {

    private Collection<String> extensions;
    private Collection<String> myResult;
    private boolean showTests;

    public FindElementsVisitor(Collection<String> extensions, boolean showTests) {
        this.extensions = extensions;
        this.showTests = showTests;
        this.myResult = new ArrayList<String>();
    }

    public void storeResult(Collection<String> result) {
        result.addAll(myResult);
    }

    @Override
    public boolean visit(IResource resource) throws CoreException {
        if (resource.getType() == IResource.FILE) {
            for (String pattern: extensions) {
                if (!Files.match(pattern, resource.getName())) continue;
                if (!showTests) {
                    IJavaElement javaElement = Resources.asJavaElement(resource);
                    IType[] findTestTypes = JUnitCore.findTestTypes(javaElement, null);
                    // we have found a test within the resource, in our case this means the
                    // resource is a test as the resources represent files a.k.a. compilation units
                    if (findTestTypes.length >= 1) continue;
                }
                myResult.add(Resources.asPath(resource));
            }
        }
        return true;
    }
}
