package ch.unibe.softwaremap;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import ch.akuhn.hapax.index.TermDocumentMatrix;
import ch.deif.meander.MapModifier;
import ch.deif.meander.Meander;
import ch.deif.meander.viz.LabelsOverlay;
import ch.deif.meander.viz.MapVisualization;
import ch.deif.meander.viz.SelectionOverlay;
import ch.unibe.scg.util.Extension;
import ch.unibe.softwaremap.builder.HapaxBuilder;
import ch.unibe.softwaremap.builder.MapMakerBackgroundJob;
import ch.unibe.softwaremap.ui.MapView;
import ch.unibe.softwaremap.util.Log;

/**
 * Caches the map of a project.
 * 
 * @author Adrian Kuhn
 * 
 */
public class ProjectMap {

	private final IProject project;
	private TermDocumentMatrix tdm;
	private MapVisualization mapViz;
	private boolean mapBeingCalculated = false;
	private boolean builderIsRunning = false;
	private Set<MapModifier> modifiers;
	
	// XXX: ugly hardcoded dimension
	int mapDimension = 250;

	public ProjectMap(IProject project) {
		this.project = project;
		modifiers = new HashSet<MapModifier>();
	}

	public ProjectMap enableBuilder() {
		try {
			addBuilderToProjectDescriptionCommands();
			if (tdm == null) {
				makeBuilderBackgroundJob().schedule();
			}
		} catch (CoreException e) {
			Log.error(e);
		}
		return this;
	}

	private Job makeBuilderBackgroundJob() {
		return new Job("Initial build of Hapax vocabulary.") {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				if (builderIsRunning) return Status.OK_STATUS;
				try {
					builderIsRunning = true;
					getProject().build(IncrementalProjectBuilder.FULL_BUILD, HapaxBuilder.BUILDER_ID, null, monitor);
				} catch (CoreException e) {
					Log.error(e);
				} finally {
					builderIsRunning = false;
				}
				return Status.OK_STATUS;
			}
		};
	}

	private void addBuilderToProjectDescriptionCommands() throws CoreException {
		IProject project = getProject();
		if (! project.isOpen()) return;
		
		IProjectDescription desc  = project.getDescription();
		ICommand[] commands = desc.getBuildSpec();
		for (ICommand command: commands) {
			if (command.getBuilderName().equals(HapaxBuilder.BUILDER_ID)) {
				return;
			}
		}
		ICommand newCommand = desc.newCommand();
		newCommand.setBuilderName(HapaxBuilder.BUILDER_ID);
		commands = Extension.$(commands).copyWith(newCommand);
		desc.setBuildSpec(commands);
		getProject().setDescription(desc, null);
	}

	public void putTDM(TermDocumentMatrix tdm) {
		this.tdm = tdm;
		this.startBackgroundTask();
	}

	private void startBackgroundTask() {
		new MapMakerBackgroundJob(ProjectMap.this).schedule();
	}

	public MapVisualization getVisualization() {
		if (tdm == null) return null;
		if (mapViz != null && mapViz.getWidth() == mapDimension) return mapViz;
		updateMap();
		return null;
	}

	public void updateMap() {
		if (mapBeingCalculated) return;
		startBackgroundTask();
	}

	public IProject getProject() {
		return project;
	}

	public IStatus makeMap(IProgressMonitor monitor) {
		if (mapBeingCalculated) return Status.OK_STATUS;
		mapBeingCalculated = true;
		monitor.beginTask("Making map", 5);
		mapViz = Meander.script().useCorpus(tdm)
								 .makeMap(mapDimension)
								 .applyModifier(modifiers)
								 .useHillshading()
								 .add(LabelsOverlay.class)
								 .add(SelectionOverlay.class)
								 .runNearestNeighborAlgorithm()
								 .getVisualization();
		notifyMapView();
		monitor.done();
		mapBeingCalculated = false;
		return Status.OK_STATUS;
	}

	private void notifyMapView() {
		MapView mapView = CodemapCore.getPlugin().getMapView();
		if (mapView != null) {
			mapView.newProjectMapAvailable(project);
		}
	}

	public ProjectMap updateSize(int newMapDimension) {
//		if (project != null) {
//			System.out.println("adapting map of " + project.getName() + " to dimension " + newMapDimension);
//		}
		// XXX: ugly hardcoded map-size
		this.mapDimension = Math.max(newMapDimension, 512);
		return this;
	}

	public void addModifier(MapModifier mod) {
		boolean added = modifiers.add(mod);
		if (!added) {
			modifiers.remove(mod);
			modifiers.add(mod);
		}
	}

}
