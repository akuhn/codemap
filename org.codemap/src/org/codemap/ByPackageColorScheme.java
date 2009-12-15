package org.codemap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.codemap.util.CodemapColors;
import org.codemap.util.GoodColorGenerator;
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
        colorScheme = new CodemapColors();
        HashMap<String, List<String>> forPackage = findPackageDeclarations();
        
        GoodColorGenerator colors = new GoodColorGenerator(forPackage.keySet().size());
        
        for(String pack: forPackage.keySet()) {
            MColor col = colors.next();
            List<String> classes = forPackage.get(pack);
            for(String clazz: classes) {
                colorScheme.setColor(clazz, col);
            }
        }
    }
    
    /**
     * maps all packages in the current codemap to their classes in the 
     * current codemap.
     */
    private HashMap<String, List<String>> findPackageDeclarations() {
        HashMap<String, List<String>> forPackage = new HashMap<String, List<String>>();        
        for(Point each: mapPerProject.getConfiguration().points()) {
            String fileName = each.getDocument();
            IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
            IResource resource = root.findMember(new Path(fileName));
            IJavaElement create = JavaCore.create(resource);
            
            if (!(create instanceof ICompilationUnit)) continue;
            ICompilationUnit unit = (ICompilationUnit) create;
            try {
                IPackageDeclaration[] packageDeclarations = unit.getPackageDeclarations();
                if (packageDeclarations.length != 1) continue;
                
                IPackageDeclaration pack = packageDeclarations[0];
                String packageName = pack.getElementName();
                
                List<String> classes = forPackage.get(packageName);
                if (classes == null) {
                    classes = new ArrayList<String>();
                    forPackage.put(packageName, classes);
                }
                classes.add(fileName);
            } catch (JavaModelException e) {
                Log.error(e);
            }
        }
        return forPackage;
    }
}
