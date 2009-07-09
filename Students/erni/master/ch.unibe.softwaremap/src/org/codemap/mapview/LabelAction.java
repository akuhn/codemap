/**
 * 
 */
package org.codemap.mapview;

import org.codemap.CodemapCore;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;

class LabelAction extends Action {
	
	public LabelAction(String text) {
		super(text, IAction.AS_RADIO_BUTTON);
	}
	
	protected CodemapCore getCore() {
		return CodemapCore.getPlugin();
	}
	
	public static class NoLabelAction extends LabelAction {

		public NoLabelAction(String text) {
			super(text);
		}

		@Override
		public void run() {
			if (!isChecked()) return;
			getCore().getLabelScheme().useNoLabels();
			getCore().redrawCodemap();
		}
	}
	
	public static class IdentifierLabelAction extends LabelAction {

		public IdentifierLabelAction(String text) {
			super(text);
		}

		@Override
		public void run() {
			if (!isChecked()) return;
			getCore().getLabelScheme().useIdentifierLabels();
			getCore().redrawCodemap();
		}
	}
	
	public static class LogLHLabelAction extends LabelAction {

		public LogLHLabelAction(String text) {
			super(text);
		}

		@Override
		public void run() {
			if (!isChecked()) return;			
			System.out.println("show LogLH labels");
		}
	}
}