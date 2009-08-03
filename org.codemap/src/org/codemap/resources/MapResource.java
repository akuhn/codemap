package org.codemap.resources;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.runtime.IProgressMonitor;

import ch.akuhn.hapax.CorpusBuilder;
import ch.akuhn.hapax.Hapax;
import ch.akuhn.hapax.index.LatentSemanticIndex;
import ch.deif.meander.Configuration;
import ch.deif.meander.builder.Meander;

public class MapResource implements Serializable {

	private static final long serialVersionUID = 1337L;

	private static final int VERSION_1 = 0x20090830;
	
	private Collection<String> projects; 
	private Collection<String> fileExtensions;
	private Collection<Object> elements;
	
	private LatentSemanticIndex lsi;
	private Configuration mds;
	private String name;
	
	private void writeObject(ObjectOutputStream out) throws IOException {
		out.writeInt(VERSION_1);
		out.writeObject(name);
		out.writeObject(mds);
		out.writeObject(lsi);
		out.writeObject(projects);
		out.writeObject(fileExtensions);
		out.writeObject(elements);
	}
	
	@SuppressWarnings("unchecked")
	private void readObject(ObjectInputStream in) throws Exception {
		int version = in.readInt();
		if (version != VERSION_1) throw new Error();
		name = (String) in.readObject();
		mds = (Configuration) in.readObject();
		lsi = (LatentSemanticIndex) in.readObject();
		projects = (Collection<String>) in.readObject();
		fileExtensions = (Collection<String>) in.readObject();
		elements = (Collection<Object>) in.readObject();
		if (!this.invariant()) throw new Error();
	}
	
	private boolean invariant() {
		return true; // TODO
	}

	public MapResource(String name, Collection<String> projects, Collection<String> fileExtensions) {
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
//		for (IResource each: projects) {
//			if (each instanceof IContainer) gatherFromContainer((IContainer) each);
//			else elements.add(each);
//		}
	}

	private void gatherFromContainer(IContainer each) {
		throw null;
	}
	
}
