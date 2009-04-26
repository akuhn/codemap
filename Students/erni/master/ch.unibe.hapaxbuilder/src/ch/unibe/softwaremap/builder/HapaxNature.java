package ch.unibe.softwaremap.builder;

import static ch.unibe.scg.util.Extension.$;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;

import ch.unibe.softwaremap.SoftwareMapCore;

public class HapaxNature implements IProjectNature {

	public static final String NATURE_ID = SoftwareMapCore.makeID(HapaxNature.class);

	private IProject project;

	public void configure() throws CoreException {
		IProjectDescription desc = project.getDescription();
		ICommand[] commands = desc.getBuildSpec();
		for (ICommand command: commands)
			if (command.getBuilderName().equals(HapaxBuilder.BUILDER_ID)) return;
		ICommand newCommand = desc.newCommand();
		newCommand.setBuilderName(HapaxBuilder.BUILDER_ID);
		commands = $(commands).copyWith(newCommand);
		desc.setBuildSpec(commands);
		project.setDescription(desc, null);
	}

	public void deconfigure() throws CoreException {
		IProjectDescription description = getProject().getDescription();
		ICommand[] commands = description.getBuildSpec();
		for (int i = 0; i < commands.length; ++i) {
			if (commands[i].getBuilderName().equals(HapaxBuilder.BUILDER_ID)) {
				ICommand[] newCommands = new ICommand[commands.length - 1];
				System.arraycopy(commands, 0, newCommands, 0, i);
				System.arraycopy(commands, i + 1, newCommands, i, commands.length - i - 1);
				description.setBuildSpec(newCommands);
				return;
			}
		}
	}

	public IProject getProject() {
		return project;
	}

	public void setProject(IProject project) {
		this.project = project;
	}

}
