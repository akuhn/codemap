/**
 * 
 */
package org.codemap.mapview;

import org.codemap.CodemapCore;
import org.codemap.MapPerProject;
import org.eclipse.jface.action.IAction;

class LabelAction extends CodemapAction {
	
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
			if (!isChecked()) return;
			getCore().getLabelScheme().useNoLabels();
			getCore().redrawCodemap();
		}
	}
	
	public static class IdentifierLabelAction extends LabelAction {

		public IdentifierLabelAction() {
			super("Class Name");
			setChecked(true);
		}

		@Override
		public void run() {
			if (!isChecked()) return;
			getCore().getLabelScheme().useIdentifierLabels();
			getCore().redrawCodemap();
		}
	}
	
	public static class LogLHLabelAction extends LabelAction {

		public LogLHLabelAction() {
			super("Log-likelihood");
		}

		@Override
		public void run() {
			if (!isChecked()) return;			
			System.out.println("show LogLH labels");
		}
	}

	@Override
	public void configureAction(MapPerProject map) {
		// TODO Auto-generated method stub
		
	}
}