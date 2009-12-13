package org.codemap.tasks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import org.codemap.util.Log;
import org.codemap.util.Resources;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.junit.JUnitCore;

import ch.akuhn.util.Files;
import ch.akuhn.util.ProgressMonitor;
import ch.akuhn.values.Arguments;
import ch.akuhn.values.TaskValue;
import ch.akuhn.values.Value;

public class ComputeElementsTask extends TaskValue<Collection<String>> {
    
    public ComputeElementsTask(Value<Collection<String>> projects, Value<Collection<String>> extensions) {
        super("Collecting selected resources", projects, extensions);
    }

    @Override
    protected Collection<String> computeValue(ProgressMonitor monitor, Arguments args) {
        Collection<String> projects = args.nextOrFail();
        final Collection<String> extensions = args.nextOrFail();
        final Collection<String> result = new HashSet<String>();
        
        for (String path: projects) {
            IResource resource = Resources.asResource(path);
            // fix error for closed projects
            if (!resource.isAccessible()) continue;
            FindElementsVisitor visitor = new FindElementsVisitor(extensions);            
            try {
                resource.accept(visitor);
            } catch (CoreException e) {
                Log.error(e);
            }
            visitor.storeResult(result);
        }
        return new ArrayList<String>(result);
    }
}

class FindElementsVisitor implements IResourceVisitor {

    private Collection<String> extensions;
    private Collection<String> myResult;

    public FindElementsVisitor(Collection<String> extensions) {
        this.extensions = extensions;
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
                if (isJavaTestFile(resource)) continue;
                
                myResult.add(Resources.asPath(resource));
            }
        }
        return true;
    }
    
    private boolean isJavaTestFile(IResource resource) {
    	/*
    	 * If the resource contains compilation errors, it maybe that
    	 * we get a false positive. As a work around we could look at 
    	 * the path (does it include test) and at the name (does it end
    	 * with Test).
    	 * 
    	 * This is a TODO for a world where days have more than 24h.
    	 * 
    	 */
        IJavaElement javaElement = Resources.asJavaElement(resource);
        if (javaElement == null) return false;
        // find all tests for the given file
        IType[] findTestTypes;
        try {
            findTestTypes = JUnitCore.findTestTypes(javaElement, null);
        } catch (OperationCanceledException e) {
            return false; // be conservative
        } catch (CoreException e) {
            return false; // be conservative
        }
        // if we found one (or maybe more) then we have a java test file
        return findTestTypes.length > 0;
    }
}
