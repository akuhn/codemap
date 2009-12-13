package org.codemap.commands;

import org.codemap.DefaultLabelScheme;
import org.codemap.MapPerProject;
import org.codemap.mapview.action.CommandAction;
import org.codemap.mapview.action.ShowClassNameLabelAction;
import org.codemap.mapview.action.ShowNoLabelAction;
import org.codemap.resources.MapValues;
import org.codemap.util.MapScheme;

public class LabelingCommand extends DropDownCommand<AbstractLabelingCommand> implements IConfigureMapValues {

    private static final String LABELING_KEY = makeCommandId("labeling");
    
    public LabelingCommand(MapPerProject mapPerProject) {
        super(mapPerProject);
        add(new ClassNameLabeling(this));
        add(new NoLabeling(this));
    }

    @Override
    protected String getKey() {
        return LABELING_KEY;
    }

    @Override
    protected Class<?> getDefaultCommandClass() {
        return ClassNameLabeling.class;
    }
}

abstract class AbstractLabelingCommand extends Command {

    private LabelingCommand labeling;

    public AbstractLabelingCommand(LabelingCommand labelingCommand) {
        labeling = labelingCommand;
        enabled = getLabeling().getEnabled(this);
    }

    protected LabelingCommand getLabeling() {
        return labeling;
    }

    @Override
    protected void applyState() {
        if (isEnabled()) {
            getLabeling().setEnabled(this);
        }
        getLabeling().applyState();
    }

    @Override
    protected boolean initEnabled() {
        return false;
    }
}

class NoLabeling extends AbstractLabelingCommand {

    public NoLabeling(LabelingCommand labelingCommand) {
        super(labelingCommand);
    }

    @Override
    protected Class<? extends CommandAction> getActionID() {
        return ShowNoLabelAction.class;
    }

    @Override
    public void configure(MapValues mapValues) {
        if (!isEnabled()) return;
        mapValues.labelScheme.setValue(new MapScheme<String>(null));
    }
}

class ClassNameLabeling extends AbstractLabelingCommand {

    public ClassNameLabeling(LabelingCommand labelingCommand) {
        super(labelingCommand);
    }

    @Override
    protected Class<? extends CommandAction> getActionID() {
        return ShowClassNameLabelAction.class;
    }

    @Override
    public void configure(MapValues mapValues) {
        if (!isEnabled()) return;
        mapValues.labelScheme.setValue(new DefaultLabelScheme());
    }
}
