/**
 * 
 */
package org.codemap.mapview.action;

import org.codemap.MapPerProject;
import org.codemap.commands.LabelingCommand;
import org.codemap.commands.LabelingCommand.Labeling;
import org.codemap.mapview.MapController;
import org.eclipse.jface.action.IAction;


abstract class LabelAction extends MenuAction {

	private LabelingCommand labelingCommand;

    public LabelAction(String text, MapController theController) {
		super(text, IAction.AS_RADIO_BUTTON, theController);
	}
	
	@Override
	public void configureAction(MapPerProject map) {
	    labelingCommand = map.getCommands().getLabelingCommand();
	    setChecked(isMyLabeling(labelingCommand.getCurrentLabeling()));
	}

	private boolean isMyLabeling(Labeling currentLabeling) {
	    return getMyLabeling().equals(currentLabeling);
    }
	
	@Override
	public void run() {
	    if (!isChecked()) return;
	    labelingCommand.setCurrentLabeling(getMyLabeling());
	}

    protected abstract Labeling getMyLabeling();

    public static class NoLabelAction extends LabelAction {

		public NoLabelAction(MapController theController) {
			super("No Labels", theController);
		}

        @Override
        protected Labeling getMyLabeling() {
            return LabelingCommand.Labeling.NONE;
        }
	}

	public static class IdentifierLabelAction extends LabelAction {

		public IdentifierLabelAction(MapController theController) {
			super("Class Name", theController);
		}

        @Override
        protected Labeling getMyLabeling() {
            return LabelingCommand.Labeling.DEFAULT;
        }
	}
}