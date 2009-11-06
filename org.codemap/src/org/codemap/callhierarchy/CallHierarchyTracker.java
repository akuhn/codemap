package org.codemap.callhierarchy;

import static org.codemap.util.ID.CALL_HIERARCHY_REF;

import java.lang.reflect.Field;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.internal.corext.callhierarchy.CallerMethodWrapper;
import org.eclipse.jdt.internal.corext.callhierarchy.MethodWrapper;
import org.eclipse.jdt.internal.ui.callhierarchy.CallHierarchyViewPart;
import org.eclipse.jdt.internal.ui.callhierarchy.CancelSearchAction;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.ITreeViewerListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeExpansionEvent;
import org.eclipse.jface.viewers.TreePath;
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
    
    /**
     * When new call hierarchy data is available the selection jumps to the root node. 
     * We use this information to fire an event whenever we think that the call-hierarchy
     * displayed has changed.
     * 
     */
    private ISelectionChangedListener changeListener = new ISelectionChangedListener() {
        
        @Override
        public void selectionChanged(SelectionChangedEvent event) {
            ISelection selection = event.getSelection();
            if (selection.isEmpty()) return;
            if (!(selection instanceof ITreeSelection)) return;
            
            ITreeSelection treeSelection = (ITreeSelection) selection;
            if (treeSelection.size() != 1) return;
            // might be a new call hierarchy only if exactly one element is selected (the root)
            TreePath path = treeSelection.getPaths()[0];
            int segmentCount = path.getSegmentCount();
            if (segmentCount != 1) return;
            // we are sure that root is selected, but it might be an old one
            Object methodWrapperObject = treeSelection.toList().get(0);
            if (! (methodWrapperObject instanceof MethodWrapper)) return;
            MethodWrapper rootMethod = (MethodWrapper) methodWrapperObject;
            
            onTreeRootSelected(rootMethod);
        }
    };
    
    private ITreeViewerListener treeListener = new ITreeViewerListener() {
        
        @Override
        public void treeExpanded(TreeExpansionEvent event) {
            waitForResults();
            onCallHierarchyExpanded(event);
        }

        @Override
        public void treeCollapsed(TreeExpansionEvent event) {
            onCallHierarchyCollapsed(event);
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
    private CallModel callModel;
    private CallHierarchyViewPart callHierarchyPart;
    private MethodWrapper currentRootMethod;
    
    public CallHierarchyTracker() {
        callModel = new CallModel();
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        page.addPartListener(partListener);
    }


    protected void onTreeRootSelected(MethodWrapper rootMethod) {
        // need an identity check here, if the roots are not identical then
        // the call hierarchy was reloaded
        if (currentRootMethod == rootMethod) return;
        currentRootMethod = rootMethod;
        callModel.newRoot(currentRootMethod);
        waitForResults();
        onCallHierarchyResultsLoaded(currentRootMethod);
    }
    
    protected void onCallHierarchyCollapsed(TreeExpansionEvent event) {
        Object methodWrapperObject = event.getElement();
        if (!(methodWrapperObject instanceof MethodWrapper)) return;
        MethodWrapper source = (MethodWrapper) methodWrapperObject;
        callModel.collapse(source);
    }

    protected void onCallHierarchyExpanded(TreeExpansionEvent event) {
        Object methodWrapperObject = event.getElement();
        if (!(methodWrapperObject instanceof MethodWrapper)) return;
        MethodWrapper source = (MethodWrapper) methodWrapperObject;
        onCallHierarchyResultsLoaded(source);
    }
    
    protected void onCallHierarchyResultsLoaded(MethodWrapper source) {
        List<MethodWrapper> targets = List.from(source.getCalls(new NullProgressMonitor()));
        callModel.expand(source, targets);
    }

    protected void waitForResults() {
        new Impatient().waitFor(callHierarchyPart);
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

    public void disable() {
        callModel.disable();
    }

    public void enable() {
        callModel.enable();
    }
}

class Impatient {
    
    private static final int STEP = 50;
    private static final int MAX_SLEEP_TIME = 200;
    private int sleepTime;

    public Impatient() {
        sleepTime = 50;
    }
    
    /**
     * wait until an ongoing search for callees has finished and poll for results.
     * @param callHierarchyPart the current CallHierarchyViewPart
     */
    public void waitFor(CallHierarchyViewPart callHierarchyPart) {
        Class<? extends CallHierarchyViewPart> callHiearachyClass = callHierarchyPart.getClass();
        try {
            Field fCancelSearchActionField = callHiearachyClass.getDeclaredField(CallHierarchyTracker.CANCEL_SEARCH_ACTION_ATTRIBUTE);
            fCancelSearchActionField.setAccessible(true);
            Object cancelSearchActionObj = fCancelSearchActionField.get(callHierarchyPart);
            if (!(cancelSearchActionObj instanceof CancelSearchAction)) return;
            CancelSearchAction cancelSearchAction = (CancelSearchAction) cancelSearchActionObj;
            // TODO: maybe handle InterruptedExcpetions appropriately
            while(cancelSearchAction.isEnabled()) {
                Thread.sleep(sleepTime());                                
            }
        } catch (Exception e) {
            // TODO: handle, at least log
            e.printStackTrace();
        }
    }
    
    /**
     * for a bit less aggressive polling increase the sleep time for every call
     * @return the current sleep time
     */
    private long sleepTime() {
        if (sleepTime < MAX_SLEEP_TIME) {
            sleepTime += STEP;
        }
        return sleepTime;
    }
}