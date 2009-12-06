/**
 * 
 */
package org.codemap.mapview.action;

import org.codemap.DefaultLabelScheme;
import org.codemap.mapview.MapController;
import org.codemap.util.MapScheme;
import org.eclipse.jface.action.IAction;


abstract class LabelAction extends MenuAction {

	public LabelAction(String text, MapController theController) {
		super(text, IAction.AS_RADIO_BUTTON, theController);
		setChecked(isDefaultChecked());
	}

	public static class NoLabelAction extends LabelAction {

		public NoLabelAction(MapController theController) {
			super("No Labels", theController);
		}

		@Override
		public void run() {
			super.run();
			if (!isChecked()) return;
			getController().getActiveMap().getValues().labelScheme.setValue(new MapScheme<String>(null));

		}

		@Override
		protected String getKey() {
			return "no_labels";
		}
	}

	public static class IdentifierLabelAction extends LabelAction {

		public IdentifierLabelAction(MapController theController) {
			super("Class Name", theController);
		}

		@Override
		public void run() {
			super.run();
			if (!isChecked()) return;
            getController().getActiveMap().getValues().labelScheme.setValue(new DefaultLabelScheme());			
		}

		@Override
		protected String getKey() {
			return "identifier_labels";
		}

		@Override
		protected boolean isDefaultChecked() {
			return true;
		}
	}

	public static class LogLHLabelAction extends LabelAction {

		public LogLHLabelAction(MapController theController) {
			super("Log-likelihood", theController);
		}

		@Override
		public void run() {
			super.run();
			if (!isChecked()) return;
			System.out.println("show LogLH labels");
		}

		@Override
		protected String getKey() {
			return "log_lh_labels";
		}
	}

	@Override
	protected boolean isDefaultChecked() {
		return false;
	}
}