package org.codemap.commands;

import org.codemap.MapPerProject;
import org.codemap.resources.MapValues;

public abstract class DropDownCommand<E extends Command> extends CompositeCommand<E> implements IConfigureMapValues {

    private String enabledCommand;

    public DropDownCommand(MapPerProject mapPerProject) {
        super(mapPerProject);
        enabledCommand = mapPerProject.getPropertyOrDefault(getKey(), makeIdentifier(getDefaultCommandClass()));        
    }

    @Override
    public void configure(MapValues mapValues) {
        for (E each : getCommands()) {
            each.configure(mapValues);
        }
    }
    
    public void applyState() {
        for(E each: getCommands()) {
            each.configure(getMyMap().getValues());
        }
    }    
    
    protected String makeIdentifier(Command c) {
        return makeIdentifier(c.getClass());
    }    

    protected String makeIdentifier(Class<?> clazz) {
        return clazz.getName();
    } 
    
    public void setEnabled(E command) {
        enabledCommand = makeIdentifier(command);
        getMyMap().setProperty(getKey(), enabledCommand);
    }

    public boolean getEnabled(E command) {
        return makeIdentifier(command).equals(enabledCommand);
    }    

    protected abstract String getKey();    
    
    protected abstract Class<?> getDefaultCommandClass();    

}
