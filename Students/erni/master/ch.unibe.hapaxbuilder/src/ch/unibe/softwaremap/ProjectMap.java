package ch.unibe.softwaremap;

import static ch.unibe.scg.util.Extension.$;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;

import ch.akuhn.hapax.index.TermDocumentMatrix;
import ch.deif.meander.Meander;
import ch.deif.meander.viz.LabelsOverlay;
import ch.deif.meander.viz.MapVisualization;
import ch.unibe.softwaremap.builder.HapaxBuilder;

public class ProjectMap {

    public final IProject project;
    private TermDocumentMatrix tdm;

    public ProjectMap(IProject project) {
        this.project = project;
    }

    public ProjectMap enableBuilder()  {
        try { 
            unsafeEnableBuilder(); 
        } catch (CoreException ex) { 
            throw new RuntimeException(ex); 
        }
        return this;
    }
    
    public void unsafeEnableBuilder() throws CoreException {
        IProjectDescription desc = project.getDescription();
        ICommand[] commands = desc.getBuildSpec();
        for (ICommand command: commands) {
        	if (command.getBuilderName().equals(HapaxBuilder.BUILDER_ID)) {
        		System.out.println("Builder already active");
        		return;        	
        	}
        }
        ICommand newCommand = desc.newCommand();
        newCommand.setBuilderName(HapaxBuilder.BUILDER_ID);
        commands = $(commands).copyWith(newCommand);
        desc.setBuildSpec(commands);
        System.out.println("adding builder to" + project.getName());
        project.setDescription(desc, null);
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
    	return Meander.script()
    			.useCorpus(tdm)
    			.makeMap()
    			.useHillshading()
    			.add(LabelsOverlay.class)
    			.getVisualization();
    	
    }
    
}
