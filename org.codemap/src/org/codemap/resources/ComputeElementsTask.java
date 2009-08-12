package org.codemap.resources;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import org.codemap.util.Resources;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;

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
        Collection<String> projects = args.next();
        final Collection<String> extensions = args.next();
        final Collection<String> result = new HashSet<String>();
        for (String path: projects) {
            IResource project = Resources.asResource(path);
            try {
                project.accept(new IResourceVisitor() {
                    @Override
                    public boolean visit(IResource resource) throws CoreException {
                        if (resource.getType() == IResource.FILE) {
                            for (String pattern: extensions) {
                                if (Files.match(pattern, resource.getName())) result.add(Resources.asPath(resource));
                            }
                        }
                        return true;
                    }
                });
            } catch (CoreException ex) {
                throw new RuntimeException(ex);
            }
        }
        return new ArrayList<String>(result);
    }

}
