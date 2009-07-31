package org.codemap.resources;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;

import ch.akuhn.hapax.CorpusBuilder;
import ch.akuhn.hapax.Hapax;
import ch.akuhn.hapax.index.LatentSemanticIndex;
import ch.deif.meander.Configuration;
import ch.deif.meander.builder.Meander;

public class MapResource {

	private Collection<IResource> projects;
	private Collection<String> fileExtensions;
	private Collection<Object> elements;
	
	private LatentSemanticIndex lsi;
	private Configuration mds;
	private String name;
	
	public MapResource(String name, Collection<IResource> projects, Collection<String> fileExtensions) {
		this.name = name;
		this.projects = projects;
		this.fileExtensions = fileExtensions; 
	}
	
	public void computeElementsLatentSemanticIndexAndMultiDimensionalScaling(IProgressMonitor monitor) {
		monitor.beginTask("Creating " + name, 7);
		gatherElements();
		monitor.worked(1);
		CorpusBuilder corpus = buildCorpus();
		monitor.worked(2);
		Hapax hapax = corpus.build();
		lsi = hapax.getIndex();
		monitor.worked(2);
		mds = Meander.builder().addCorpus(hapax).makeMap();
		monitor.worked(2);
	}

	private CorpusBuilder buildCorpus() {
		// TODO Auto-generated method stub
		return null;
	}

	private void gatherElements() {
		elements = new ArrayList<Object>();
		for (IResource each: projects) {
			if (each instanceof IContainer) gatherFromContainer((IContainer) each);
			else elements.add(each);
		}
	}

	private void gatherFromContainer(IContainer each) {
		throw null;
	}
	
}
