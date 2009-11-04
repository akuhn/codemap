package org.codemap;

import static org.codemap.util.ID.CALL_HIERARCHY_REF;

import java.lang.reflect.Field;

import org.eclipse.jdt.internal.corext.callhierarchy.CallerMethodWrapper;
import org.eclipse.jdt.internal.corext.callhierarchy.MethodWrapper;
import org.eclipse.jdt.internal.ui.callhierarchy.CallHierarchyViewPart;
import org.eclipse.jdt.internal.ui.callhierarchy.CancelSearchAction;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ITreeViewerListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeExpansionEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.PlatformUI;

import ch.akuhn.util.List;

public class CallHierarchyTracker {
    
    private ISelectionChangedListener changeListener = new ISelectionChangedListener() {
        
        @Override
        public void selectionChanged(SelectionChangedEvent event) {
            System.out.println("selection changed");
            ISelection selection = event.getSelection();
        }
    };
    
    private ITreeViewerListener treeListener = new ITreeViewerListener() {
        
        @Override
        public void treeExpanded(TreeExpansionEvent event) {
            System.out.println("tree expanded");
            waitForResults();
            newCallHierarchyResultsAvailable(event);
        }

        private void waitForResults() {
            Class<? extends CallHierarchyViewPart> callHiearachyClass = callHierarchyPart.getClass();
            try {
                Field fCancelSearchActionField = callHiearachyClass.getDeclaredField(CANCEL_SEARCH_ACTION_ATTRIBUTE);
                fCancelSearchActionField.setAccessible(true);
                Object cancelSearchActionObj = fCancelSearchActionField.get(callHierarchyPart);
                if (!(cancelSearchActionObj instanceof CancelSearchAction)) return;
                CancelSearchAction cancelSearchAction = (CancelSearchAction) cancelSearchActionObj;
                while(cancelSearchAction.isEnabled()) {
                    Thread.sleep(50);                                
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        @Override
        public void treeCollapsed(TreeExpansionEvent event) {
            System.out.println("tree collapsed");
        }
    };
    
    /**
     * Reports when a new CallHierarchyViewPart is opened or closed and attaches 
     * the treeListener and changeListener to the current CallHierarchyViewPart. 
     * 
     */
    private IPartListener2 partListener = new IPartListener2() {
        
        @Override
        public void partOpened(IWorkbenchPartReference partRef) {
            CallHierarchyViewPart callHierarchy = getCallHierarchyPart(partRef);
            if (callHierarchy == null) return;
            
            System.out.println("call hierarchy opened");
        }
        
        @Override
        public void partClosed(IWorkbenchPartReference partRef) {
            CallHierarchyViewPart callHierarchy = getCallHierarchyPart(partRef);
            if (callHierarchy == null) return;
            
            if (callTree != null) {
                callTree.removeTreeListener(treeListener);
                callTree.removeSelectionChangedListener(changeListener);
            }
            // there will be a different treeViewer once the CallHierarchy is reopened
            callTree = null;
            System.out.println("closed call hierarchy");
        }
        
        @Override
        public void partActivated(IWorkbenchPartReference partRef) {
            if (isInitialized()) return;
            
            callHierarchyPart = getCallHierarchyPart(partRef);
            if (callHierarchyPart == null) return;
            
            Class<? extends CallHierarchyViewPart> c = callHierarchyPart.getClass();
            try {
                Field field = c.getDeclaredField(CALL_HIERARCHY_VIEWER_ATTRIBUTE);
                field.setAccessible(true);
                Object treeViewerObject = field.get(callHierarchyPart);
                if (!(treeViewerObject instanceof TreeViewer)) return;
                
                callTree = (TreeViewer) treeViewerObject;
                callTree.addTreeListener(treeListener);
                callTree.addSelectionChangedListener(changeListener);
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        // we don't care about the following events
        @Override
        public void partInputChanged(IWorkbenchPartReference partRef) {}
        
        @Override
        public void partHidden(IWorkbenchPartReference partRef) {}
        
        @Override
        public void partDeactivated(IWorkbenchPartReference partRef) {}
        
        @Override
        public void partBroughtToTop(IWorkbenchPartReference partRef) {}
        
        @Override
        public void partVisible(IWorkbenchPartReference partRef) {}        
    };
    
    public static final String CALL_HIERARCHY_VIEWER_ATTRIBUTE = "fCallHierarchyViewer";
    public static final String CANCEL_SEARCH_ACTION_ATTRIBUTE = "fCancelSearchAction";

    private TreeViewer callTree;
    private CallHierarchyViewPart callHierarchyPart;
    
    public CallHierarchyTracker() {
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        page.addPartListener(partListener);
    }

    protected void newCallHierarchyResultsAvailable(TreeExpansionEvent event) {
        Object callerMethodWrapperObject = event.getElement();
        if (!(callerMethodWrapperObject instanceof CallerMethodWrapper)) return;
        CallerMethodWrapper source = (CallerMethodWrapper) callerMethodWrapperObject;
        List<MethodWrapper> targets = List.from(source.getCalls(null));
        System.out.println(targets);
    }

    private boolean isInitialized() {
        return callTree != null;
    }

    private CallHierarchyViewPart getCallHierarchyPart(IWorkbenchPartReference partRef) {
        if (! partRef.getId().equals(CALL_HIERARCHY_REF.id)) return null;
        IWorkbenchPart part = partRef.getPart(true);
        if (!(part instanceof CallHierarchyViewPart)) return null;
        return (CallHierarchyViewPart) part;        
    }
}
