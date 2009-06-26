package ch.unibe.softwaremap.builder;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.Job;

import ch.unibe.softwaremap.MapPerProject;


/**
 * Creates map from TDM.
 * 
 * @author Adrian Kuhn
 * 
 */
public class MapMakerBackgroundJob extends Job {

	// TODO, merge this class with builder

	private MapPerProject projectMap;

	public MapMakerBackgroundJob(MapPerProject projectMap) {
		super("Making map of " + projectMap.getProject().getName());
		this.projectMap = projectMap;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		return projectMap.makeMap(monitor);
	}

}
