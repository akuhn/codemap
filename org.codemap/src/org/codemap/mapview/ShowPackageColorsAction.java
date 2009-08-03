package org.codemap.mapview;

import static org.codemap.util.Icons.PACKAGES;

import org.codemap.CodemapCore;
import org.codemap.MapPerProject;
import org.codemap.util.CodemapColors;
import org.codemap.util.ColorBrewer;
import org.codemap.util.Icons;
import org.codemap.util.Log;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageDeclaration;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.action.Action;

import ch.deif.meander.Point;
import ch.deif.meander.util.MColor;

public class ShowPackageColorsAction extends Action {
	
	private static final boolean DEFAULT_VALUE = false;
	private final MapController theController;

	public ShowPackageColorsAction(MapController controller) {
		super("Color by Package", AS_CHECK_BOX);
		this.theController = controller;
		setChecked(DEFAULT_VALUE);
		setImageDescriptor(Icons.getImageDescriptor(PACKAGES));
	}

	@Override
	public void run() {
		if (isChecked()) {
			enable();
		} else {
			disable();
		}
		
	}

	private void disable() {
		// TODO Auto-generated method stub
		
	}

	private void enable() {
		MapPerProject mapForProject = CodemapCore.getPlugin().mapForProject(theController.getCurrentProject());
		
		ColorBrewer brewer = new ColorBrewer();
		CodemapColors colorScheme = CodemapCore.getPlugin().getColorScheme();
		
		for(Point each: mapForProject.getConfiguration().points()) {
//			System.out.println(each.getDocument());
			
			IJavaElement create = JavaCore.create(each.getDocument());
			if (!(create instanceof ICompilationUnit)) return;
			ICompilationUnit unit = (ICompilationUnit) create;
			try {
				IPackageDeclaration[] packageDeclarations = unit.getPackageDeclarations();
				if (packageDeclarations.length != 1) return;
				
				IPackageDeclaration pack = packageDeclarations[0];
				String packageName = pack.getElementName();
				MColor color = brewer.forPackage(packageName);
				colorScheme.setColor(each.getDocument(), color);

			} catch (JavaModelException e) {
				Log.error(e);
			}
		}
		CodemapCore.getPlugin().redrawCodemapBackground();
	}
	

}
