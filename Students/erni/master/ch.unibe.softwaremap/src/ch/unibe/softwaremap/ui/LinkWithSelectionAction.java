package ch.unibe.softwaremap.ui;

import org.eclipse.jface.action.Action;

import ch.unibe.softwaremap.Icon;

public class LinkWithSelectionAction extends Action {

	public LinkWithSelectionAction() {

		super("Link with selection.", AS_CHECK_BOX);
		setImageDescriptor(Icon.getImageDescriptor(Icon.LINKED));
		setChecked(false);
	}

}
