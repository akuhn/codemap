package org.codemap.resources;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicReference;

import org.codemap.CodemapCore;
import org.codemap.util.Log;
import org.codemap.util.Resources;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.IProgressService;

import ch.akuhn.hapax.CorpusBuilder;
import ch.akuhn.hapax.Hapax;
import ch.akuhn.hapax.index.LatentSemanticIndex;
import ch.akuhn.util.Files;
import ch.deif.meander.Configuration;
import ch.deif.meander.MapInstance;
import ch.deif.meander.Point;
import ch.deif.meander.builder.Meander;
import ch.deif.meander.util.MapScheme;

public class MapResource implements Serializable {

	enum Is { MISSING, RUNNING, DONE }
	
	private static final long serialVersionUID = 1337L;
	private static final int VERSION_1 = 0x20090830;
	
	private Collection<String> elements;
	private Collection<String> fileExtensions;
	private Collection<String> projects;

	private LatentSemanticIndex lsi;
	private Configuration mds;
	private String name;

	private AtomicReference<Is> state = new AtomicReference<Is>(Is.MISSING);

	public MapResource(String name, Collection<String> projects, Collection<String> fileExtensions) {
		this.name = name;
		this.projects = projects;
		this.fileExtensions = fileExtensions; 
	}

	public Collection<String> getElements() {
		return Collections.unmodifiableCollection(elements);
	}
	
	public Collection<String> getFileExtensions() {
		return Collections.unmodifiableCollection(fileExtensions);
	}
	
	public LatentSemanticIndex getLSI() {
		this.startBackgroundComputation();
		return lsi;
	} 
	
	public Configuration getMDS() {
		this.startBackgroundComputation();
		return mds;
	}
	
	public String getName() {
		return name;
	}

	public Collection<String> getProjects() {
		return Collections.unmodifiableCollection(projects);
	}
	
	private boolean invariant() {
		return true; // TODO
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
		elements = (Collection<String>) in.readObject();
		if (!this.invariant()) throw new Error();
	}
	
	private void startBackgroundComputation() {
		if (!state.compareAndSet(Is.MISSING, Is.RUNNING)) return;
		IProgressService progressService = PlatformUI.getWorkbench().getProgressService();
		new BackgroundJob().schedule();
	}

	private void writeObject(ObjectOutputStream out) throws IOException {
		out.writeInt(VERSION_1);
		out.writeObject(name);
		out.writeObject(mds);
		out.writeObject(lsi);
		out.writeObject(projects);
		out.writeObject(fileExtensions);
		out.writeObject(elements);
	}
	
	private class BackgroundJob extends Job {
		
		public BackgroundJob() {
			super("Making map configuration");
		}

		@Override
		public IStatus run(IProgressMonitor monitor) {
			monitor.beginTask("Creating map " + this, 5);
			try {
				elements = computeElements();
				monitor.worked(1);
				lsi = computeLSI();
				monitor.worked(2);
				mds = computeMDS();
				monitor.worked(2);
				if (!invariant()) throw new Error();
				state.set(Is.DONE);
			} catch (Throwable ex) {
				Log.error(ex);
				state.set(Is.MISSING);
				return Status.CANCEL_STATUS;
			}
			monitor.done();
			CodemapCore.getPlugin().getMapView().updateVisualization();
			return Status.OK_STATUS;
		}
		
		private Configuration computeMDS() {
			return Meander.builder().addCorpus(lsi).makeMap();
		}

		private LatentSemanticIndex computeLSI() throws CoreException {
			CorpusBuilder builder = Hapax.newCorpus()
					.ignoreCase()
					.useCamelCaseScanner()
					.rejectRareTerms()
					.rejectStopwords()
					.latentDimensions(25)
					.useTFIDF();
			for (String path: elements) {
				IResource resource = Resources.asResource(path);
				if (resource.getType() != IResource.FILE) continue;
				InputStream stream = ((IFile) resource).getContents();
				builder.addDocument(path, stream);
			}
			return builder.makeTDM().createIndex();
		}

		private Collection<String> computeElements() throws CoreException {
			final Collection<String> result = new HashSet<String>();
			for (String path: projects) {
				IResource project = Resources.asResource(path);
				project.accept(new IResourceVisitor() {
					@Override
					public boolean visit(IResource resource) throws CoreException {
						if (resource.getType() == IResource.FILE) {
							for (String pattern: fileExtensions) {
								if (Files.match(pattern, resource.getName())) result.add(Resources.asPath(resource));
							}
						}
						return true;
					}
				});
			}
			return new ArrayList<String>(result);
		}
		
	}

	public MapInstance makeMapInstanceWithSize(int size) {
		return mds.withSize(size, new MapScheme<Double>() {
			@Override
			public Double forLocation(Point location) {
				return Math.sqrt(Resources.getFilesize(location.getDocument()));
				
			}
		});
	}

}
