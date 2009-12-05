/**
 * 
 */
package org.codemap.mapview.action;

import org.codemap.CodemapCore;
import org.codemap.DefaultLabelScheme;
import org.codemap.util.MapScheme;
import org.eclipse.jface.action.IAction;


abstract class LabelAction extends MenuAction {

	public LabelAction(String text) {
		super(text, IAction.AS_RADIO_BUTTON);
		setChecked(isDefaultChecked());
	}

	protected CodemapCore getCore() {
		return CodemapCore.getPlugin();
	}

	public static class NoLabelAction extends LabelAction {

		public NoLabelAction() {
			super("No Labels");
		}

		@Override
		public void run() {
			super.run();
			if (!isChecked()) return;
			getCore().getActiveMap().getValues().labelScheme.setValue(new MapScheme<String>(null));

		}

		@Override
		protected String getKey() {
			return "no_labels";
		}
	}

	public static class IdentifierLabelAction extends LabelAction {

		public IdentifierLabelAction() {
			super("Class Name");
		}

		@Override
		public void run() {
			super.run();
			if (!isChecked()) return;
            getCore().getActiveMap().getValues().labelScheme.setValue(new DefaultLabelScheme());			
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

		public LogLHLabelAction() {
			super("Log-likelihood");
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