package org.codemap.mapview.action;

import static org.codemap.util.CodemapIcons.FORCE_SELECTION;

import org.codemap.mapview.MapSelectionProvider;
import org.codemap.util.CodemapIcons;
import org.codemap.util.Tag;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.IMemento;

public class ForceSelectionAction extends Action {
	
	public static final boolean DEFAULT_CHECKED = true;
	private MapSelectionProvider selectionProvider;

	public ForceSelectionAction(MapSelectionProvider provider, IMemento memento) {
		super("Force Package Explorer Selection", AS_CHECK_BOX);
		selectionProvider = provider;
		setChecked(DEFAULT_CHECKED);
		setImageDescriptor(CodemapIcons.descriptor(FORCE_SELECTION));
		init(memento);
	}

	@Override
	public void setChecked(boolean checked) {
		super.setChecked(checked);
		selectionProvider.setForceEnabled(checked);
	}

	@Override
	public void run() {
		super.run();
	    selectionProvider.setForceEnabled(isChecked());
	}

    public void saveState(IMemento memento) {
        memento.putBoolean(Tag.FORCE_SELECTION, isChecked());
    }
    
    protected void init(IMemento memento) {
        if (memento == null) return;
        Boolean checked = memento.getBoolean(Tag.FORCE_SELECTION);
        if (checked != null) setChecked(checked);
    }

}
