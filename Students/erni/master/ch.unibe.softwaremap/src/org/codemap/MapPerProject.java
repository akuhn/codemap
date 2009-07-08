package org.codemap;

import org.codemap.builder.HapaxBuilder;
import org.codemap.builder.MapMakerBackgroundJob;
import org.codemap.mapview.MapView;
import org.codemap.util.Log;
import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import ch.akuhn.hapax.Hapax;
import ch.akuhn.hapax.index.TermDocumentMatrix;
import ch.deif.meander.Configuration;
import ch.deif.meander.builder.Meander;
import ch.deif.meander.visual.CurrentSelectionOverlay;
import ch.deif.meander.visual.Layer;
import ch.deif.meander.visual.MapVisualization;
import ch.deif.meander.visual.OpenFilesOverlay;
import ch.deif.meander.visual.YouAreHereOverlay;
import ch.unibe.scg.util.Extension;

/**
 * Holds corpus, map and visualization of a project. Use this class to store project specific information.
 * 
 */
public class MapPerProject {

	private static final int MINIMAL_SIZE = 300;

	private final IProject project;
	private TermDocumentMatrix tdm;
	private MapVisualization mapViz;
	private boolean mapBeingCalculated = false;
	private boolean builderIsRunning = false;
	// private Set<MapModifier> modifiers;
	private int mapSize = MINIMAL_SIZE;


	private Hapax hapax;

	private Configuration configuration;
	private Layer layer;

	public MapPerProject(IProject project) {
		this.project = project;
		// modifiers = new HashSet<MapModifier>();
	}

	public MapPerProject enableBuilder() {
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
					if (getProject() != null) {
						getProject()
								.build(IncrementalProjectBuilder.FULL_BUILD, HapaxBuilder.BUILDER_ID, null, monitor);
					}
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
		if (project == null || !project.isOpen()) return;

		IProjectDescription desc = project.getDescription();
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
		new MapMakerBackgroundJob(MapPerProject.this).schedule();
	}

	public MapVisualization getVisualization() {
		if (tdm == null) return null;
		if (mapViz != null && mapViz.getWidth() == mapSize) return mapViz;
		updateMap();
		return null;
	}

	public void updateMap() {
		if (mapBeingCalculated) return;
		mapViz.redraw();
		startBackgroundTask();
	}

	public IProject getProject() {
		return project;
	}

	public IStatus makeMap(IProgressMonitor monitor) {
		if (mapBeingCalculated) return Status.OK_STATUS;
		mapBeingCalculated = true;
		monitor.beginTask("Making map", 5);
		if (hapax == null) {
			hapax = Hapax.legomenon()
					.addCorpus(tdm)
					.closeCorpus()
					.createIndex();
			configuration = Meander.builder()
					.addCorpus(hapax)
					.makeMap();
			layer = Meander.visualization()
					.withLabels(CodemapCore.getPlugin().getLabelScheme())
					.withColors(CodemapCore.getPlugin().getColorScheme())
					.withSelection(new CurrentSelectionOverlay(), CodemapCore.getPlugin().getCurrentSelection())
					.withSelection(new OpenFilesOverlay(), CodemapCore.getPlugin().getOpenFilesSelection())
					.withSelection(new YouAreHereOverlay(), CodemapCore.getPlugin().getYouAreHereSelection())
					.makeLayer();
		}

		mapViz = new MapVisualization(configuration.withSize(mapSize), layer);

		// mapViz = Meander(hapax)
		// .makeMap(mapSize)
		// .applyModifier(modifiers)
		// .useHillshading()
		// .add(LabelsOverlay.class)
		// .add(CurrentSelectionOverlay.class)
		// .add(new OpenFilesOverlay(openFilesSelection))
		// .add(new YouAreHereOverlay(youAreHereSelection))
		// .runNearestNeighborAlgorithm()
		// .getVisualization();
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

	public MapPerProject updateSize(int newMapDimension) {
		// if (project != null) {
		// System.out.println("adapting map of " + project.getName() + " to dimension " + newMapDimension);
		// }
		// XXX: ugly hardcoded map-size
		this.mapSize = Math.max(newMapDimension, MINIMAL_SIZE);
		return this;
	}

	// public void addModifier(MapModifier mod) {
	// boolean added = modifiers.add(mod);
	// if (!added) {
	// modifiers.remove(mod);
	// modifiers.add(mod);
	// }
	// }

}
