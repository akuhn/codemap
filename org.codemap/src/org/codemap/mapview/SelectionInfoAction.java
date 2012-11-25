package org.codemap.mapview;

import java.util.ArrayList;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;

public class SelectionInfoAction extends Action {
    
    private static final String PREFIX = "selection: ";
    private static final String EMPTY = "<empty>";    
    
    public SelectionInfoAction(ISelection selection) {
        if (!(selection instanceof IStructuredSelection)) {
            // only structured selections make sense in our context.
            // thus if it is not a structured selection, it must be an empty one.
            setText(PREFIX + EMPTY);
            return;
        }
        
        IStructuredSelection structuredSelection = (IStructuredSelection) selection;
        ArrayList<ICompilationUnit> compilationUnits = new ArrayList<ICompilationUnit>();
        for(Object entry: structuredSelection.toList()) {
            if (!(entry instanceof ICompilationUnit)) continue;
            compilationUnits.add((ICompilationUnit) entry);
        }
        setText(PREFIX + makeText(compilationUnits));
    }

    private String makeText(ArrayList<ICompilationUnit> compilationUnits) {
        if (compilationUnits.isEmpty()) {
            return EMPTY;
        }
        if (compilationUnits.size() == 1) {
            ICompilationUnit unit = compilationUnits.get(0);
            return unit.getElementName();
        }
        return compilationUnits.size() + " Classes";
    }
}
