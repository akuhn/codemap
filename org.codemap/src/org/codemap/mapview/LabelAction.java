/**
 * 
 */
package org.codemap.mapview;

import org.codemap.CodemapCore;
import org.codemap.MapPerProject;
import org.eclipse.jface.action.IAction;

abstract class LabelAction extends MenuAction {
	
	public LabelAction(String text) {
		super(text, IAction.AS_RADIO_BUTTON);
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
			getCore().getLabelScheme().useNoLabels();
			getCore().redrawCodemap();
		}

		@Override
		protected String getKey() {
			return "no_labels";
		}
	}
	
	public static class IdentifierLabelAction extends LabelAction {

		public IdentifierLabelAction() {
			super("Class Name");
			setChecked(true);
		}

		@Override
		public void run() {
			super.run();			
			if (!isChecked()) return;
			getCore().getLabelScheme().useIdentifierLabels();
			getCore().redrawCodemap();
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