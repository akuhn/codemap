package ch.unibe.softwaremap;

import static ch.unibe.scg.util.Extension.$;

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
import ch.deif.meander.Meander;
import ch.deif.meander.viz.LabelsOverlay;
import ch.deif.meander.viz.MapVisualization;
import ch.unibe.softwaremap.builder.HapaxBuilder;
import ch.unibe.softwaremap.builder.MapMakerBackgroundJob;
import ch.unibe.softwaremap.ui.MapView;

public class ProjectMap {

	private final IProject project;
	private TermDocumentMatrix tdm;
	private MapVisualization<?> map;
	private boolean mapBeingCalculated = false;
	private boolean builderIsRunning = false;

	public ProjectMap(IProject project) {
		this.project = project;
	}

	public ProjectMap enableBuilder() {
		try {
			addBuilderToProjectDescriptionCommands();
			if (tdm == null) makeBuilderBackgroundJob().schedule();
		} catch (CoreException ex) {
			throw new RuntimeException(ex);
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
					new MapMakerBackgroundJob(ProjectMap.this).schedule();
				} catch (CoreException ex) {
					throw new RuntimeException(ex);
				} finally {
					builderIsRunning = false;
				}
				return Status.OK_STATUS;
			}
		};
	}

	private void addBuilderToProjectDescriptionCommands() throws CoreException {
		IProjectDescription desc = getProject().getDescription();
		ICommand[] commands = desc.getBuildSpec();
		for (ICommand command: commands) {
			if (command.getBuilderName().equals(HapaxBuilder.BUILDER_ID)) {
				;
			}
		}
		ICommand newCommand = desc.newCommand();
		newCommand.setBuilderName(HapaxBuilder.BUILDER_ID);
		commands = $(commands).copyWith(newCommand);
		desc.setBuildSpec(commands);
		getProject().setDescription(desc, null);
	}

	public void putTDM(TermDocumentMatrix tdm) {
		this.tdm = tdm;
		this.startBackgroundTask();
	}

	private void startBackgroundTask() {
		// TODO implement background task ...
	}

	public MapVisualization<?> getVisualization() {
		if (tdm == null) return null;
		if (map != null) return map;
		if (mapBeingCalculated) return null;
		mapBeingCalculated = true;
		new MapMakerBackgroundJob(this).schedule();
		return null;
	}

	public IProject getProject() {
		return project;
	}

	public IStatus makeMap(IProgressMonitor monitor) {
		monitor.beginTask("Making map", 5);
		map = Meander.script().useCorpus(tdm).makeMap().useHillshading().add(LabelsOverlay.class).getVisualization();
		notifyMapView();
		monitor.done();
		return Status.OK_STATUS;
	}

	private void notifyMapView() {
		MapView mapView = SoftwareMapCore.getMapView();
		if (mapView != null) mapView.newProjectMapAvailable(project);
	}

}
