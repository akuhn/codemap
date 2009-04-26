package ch.unibe.softwaremap.builder;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.Job;

import ch.unibe.softwaremap.ProjectMap;

public class MapMakerBackgroundJob extends Job {

    private ProjectMap projectMap;

    public MapMakerBackgroundJob(ProjectMap projectMap) {
        super("Making map of " + projectMap.getProject().getName());
        this.projectMap = projectMap;
    }

    @Override
    protected IStatus run(IProgressMonitor monitor) {
        return projectMap.makeMap(monitor);
    }

}
