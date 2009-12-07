package org.codemap.commands;

import org.codemap.MapPerProject;
import org.codemap.Point;
import org.codemap.util.CodemapColors;
import org.codemap.util.ColorBrewer;
import org.codemap.util.Log;
import org.codemap.util.MColor;
import org.codemap.util.MapScheme;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageDeclaration;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

public class ByPackageColorScheme extends MapScheme<MColor> {
    
    private final MapPerProject mapPerProject;
    private CodemapColors colorScheme;

    public ByPackageColorScheme(MapPerProject mapPerProject) {
        this.mapPerProject = mapPerProject;
    }

    @Override
    public MColor forLocation(Point location) {
        if (colorScheme == null) {
            init();
        }
        return colorScheme.forLocation(location);
    }
    
    private void init() {
        ColorBrewer brewer = new ColorBrewer();
        colorScheme = new CodemapColors();
        
        for(Point each: mapPerProject.getConfiguration().points()) {
            
            String fileName = each.getDocument();
            IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
            IResource resource = root.findMember(new Path(fileName));
            IJavaElement create = JavaCore.create(resource);
            
            if (!(create instanceof ICompilationUnit)) return;
            ICompilationUnit unit = (ICompilationUnit) create;
            try {
                IPackageDeclaration[] packageDeclarations = unit.getPackageDeclarations();
                if (packageDeclarations.length != 1) return;
                
                IPackageDeclaration pack = packageDeclarations[0];
                String packageName = pack.getElementName();
                MColor color = brewer.forPackage(packageName);
                colorScheme.setColor(fileName, color);

            } catch (JavaModelException e) {
                Log.error(e);
            }
        }        
    }
}
