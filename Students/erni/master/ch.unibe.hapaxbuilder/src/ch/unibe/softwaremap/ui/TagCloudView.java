package ch.unibe.softwaremap.ui;

import static ch.unibe.eclipse.util.EclipseUtil.adapt;

import java.util.Collection;
import java.util.HashSet;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;

import processing.core.PApplet;


public class TagCloudView extends ViewPart implements ISelectionListener {

    private static final String CONTENT_OUTLINE_ID = "org.eclipse.ui.views.ContentOutline";

    public static final String PACKAGE_EXPLORER_ID = "org.eclipse.jdt.ui.PackageExplorer";

    private EclipseProcessingBridge2 view;

    @Override
    public void createPartControl(Composite parent) {
        view = new EclipseProcessingBridge2(parent);
        view.setApplet(new PApplet() {
            @Override
            public void draw() {
                rect(50,50,200,200);
            }
            @Override
            public void setup() {
                size(400,400);
            }
        });
        getSite().getWorkbenchWindow().getSelectionService()
                .addSelectionListener(PACKAGE_EXPLORER_ID, this);
        getSite().getWorkbenchWindow().getSelectionService()
                .addSelectionListener(CONTENT_OUTLINE_ID, this);
    }

    @Override
    public void setFocus() {
        view.setFocus();
    }

    @Override
    public void dispose() {
        getSite().getWorkbenchWindow().getSelectionService()
                .removeSelectionListener(PACKAGE_EXPLORER_ID, this);
        getSite().getWorkbenchWindow().getSelectionService()
                .removeSelectionListener(CONTENT_OUTLINE_ID, this);
        super.dispose();
    }

    @Override
    public void selectionChanged(IWorkbenchPart part, ISelection selection) {
        if (part == this) return;
        if (selection instanceof IStructuredSelection) 
                selectionChanged((IStructuredSelection) selection);
    }

    private void selectionChanged(IStructuredSelection selection) {
        IJavaProject project = null;
        Collection<ICompilationUnit> units = new HashSet<ICompilationUnit>(); 
        for (Object each: selection.toList()) {
            IJavaElement javaElement = adapt(each, IJavaElement.class);
            if (project == null) project = javaElement.getJavaProject();
            if (project != javaElement.getJavaProject()) {
                multipleProjectSelected();
                return;
            }
            if (javaElement instanceof ICompilationUnit) {
                units.add((ICompilationUnit) javaElement);
            }
            if (javaElement instanceof IMember) {
                javaElement = javaElement.getAncestor(IJavaElement.COMPILATION_UNIT);
                if (javaElement != null) units.add((ICompilationUnit) javaElement);
            }
        }
        if (project != null) compilationUnitsSelected(project, units);
    }

    private void compilationUnitsSelected(IJavaProject project, Collection<ICompilationUnit> units) {
        System.out.println(units.size() + " in " + project.getHandleIdentifier());
    }

    private void multipleProjectSelected() {
        System.out.println("!!! multiple projects selected !!!");
    }
    
    
}
