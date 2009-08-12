package org.codemap.resources;

import java.io.InputStream;
import java.util.Collection;

import org.codemap.util.JobValue;
import org.codemap.util.Resources;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

import ch.akuhn.hapax.CorpusBuilder;
import ch.akuhn.hapax.Hapax;
import ch.akuhn.hapax.index.LatentSemanticIndex;
import ch.akuhn.util.ProgressMonitor;
import ch.akuhn.values.Arguments;
import ch.akuhn.values.Value;

public class ComputeIndexTask extends JobValue<LatentSemanticIndex> {

    /*default*/ ComputeIndexTask(String name, Value<Collection<String>> elements) {
        super("Compute index of " + name, elements);
    }

    @Override
    protected LatentSemanticIndex computeValue(ProgressMonitor monitor, Arguments args) {
        Collection<String> elements = args.next();
        monitor.begin(elements.size());
        CorpusBuilder builder = Hapax.newCorpus()
                .ignoreCase()
                .useCamelCaseScanner()
                .rejectRareTerms()
                .rejectStopwords()
                .latentDimensions(25)
                .useTFIDF();
        for (String path: elements) {
            try {
                IResource resource = Resources.asResource(path);
                if (resource.getType() != IResource.FILE) continue;
                InputStream stream = ((IFile) resource).getContents();
                builder.addDocument(path, stream);
            } catch (CoreException ex) {
                throw new RuntimeException(ex);
            }
            monitor.worked(1);
        }
        return builder.makeTDM().createIndex();
    }
    
}
