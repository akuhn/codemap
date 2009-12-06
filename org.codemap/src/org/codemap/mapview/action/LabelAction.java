/**
 * 
 */
package org.codemap.mapview.action;

import org.codemap.LabelingCommand;
import org.codemap.MapPerProject;
import org.codemap.LabelingCommand.Labeling;
import org.codemap.mapview.MapController;
import org.eclipse.jface.action.IAction;


abstract class LabelAction extends MenuAction {

	private LabelingCommand labelingCommand;

    public LabelAction(String text, MapController theController) {
		super(text, IAction.AS_RADIO_BUTTON, theController);
		configureAction(theController.getActiveMap());
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
	protected String getKey() {
	    return "unusedSoPleaseDeactivateASAP";
	}
	
	@Override
	public void run() {
	    if (!isChecked()) return;
	    labelingCommand.setCurrentLabeling(getMyLabeling());
	}

    @Override
    protected boolean isDefaultChecked() {
        return false;
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