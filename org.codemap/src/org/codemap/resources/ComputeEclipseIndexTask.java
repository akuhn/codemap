package org.codemap.resources;

import java.io.InputStream;
import java.util.Collection;

import org.codemap.util.Resources;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

import ch.akuhn.hapax.CorpusBuilder;
import ch.akuhn.values.Value;
import ch.deif.meander.map.ComputeIndexTask;

public class ComputeEclipseIndexTask extends ComputeIndexTask {

    public ComputeEclipseIndexTask(Value<Collection<String>> elements) {
        super(elements);
    }

    @Override
    protected void parseElement(CorpusBuilder builder, String path) {
        try {
            IResource resource = Resources.asResource(path);
            if (resource.getType() != IResource.FILE) return;
            InputStream stream = ((IFile) resource).getContents();
            builder.addDocument(path, stream);
        } catch (CoreException ex) {
            throw new RuntimeException(ex);
        }
    }
    
}
