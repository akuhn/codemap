package org.codemap.communication.views;


import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.part.ViewPart;


/**
 * This sample class demonstrates how to plug-in a new
 * workbench view. The view shows data obtained from the
 * model. The sample creates a dummy model on the fly,
 * but a real implementation would connect to the model
 * available either in this or another plug-in (e.g. the workspace).
 * The view is connected to the model using a content provider.
 * <p>
 * The view uses a label provider to define how model
 * objects should be presented in the view. Each
 * view can present the same model objects using
 * different labels and icons, if needed. Alternatively,
 * a single label provider can be shared between views
 * in order to ensure that objects of the same type are
 * presented in the same way everywhere.
 * <p>
 */

public class ECFTestView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "org.codemap.communication.views.ECFTestView";
	
	
	private static class MyDemoAction extends Action {
	    
	    public MyDemoAction() {
	        super();
	        setText("foo");
	    }
	}

	@Override
	public void createPartControl(Composite parent) {

	    IMenuManager menuManager = getViewSite().getActionBars().getMenuManager();
	    
	    menuManager.add(new MyDemoAction());
	    menuManager.add(new CodemapRosterMenuItem());
	    
	    menuManager.add(new Separator());
	    menuManager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS)); 	    
	    
//	    menuManager.add(new SelectEntryModeAction(ViewSettings.ENTRYMODE_PROJECTS, settings,
//	        this));
//	    menuManager.add(new SelectEntryModeAction(ViewSettings.ENTRYMODE_PACKAGEROOTS,
//	        settings, this));
//	    menuManager.add(new SelectEntryModeAction(ViewSettings.ENTRYMODE_PACKAGES, settings,
//	        this));
//	    menuManager.add(new SelectEntryModeAction(ViewSettings.ENTRYMODE_TYPES, settings,
//	        this));
//	    menuManager.add(new Separator());
//	    menuManager.add(new SelectCounterModeAction(0, settings, this));
//	    menuManager.add(new SelectCounterModeAction(1, settings, this));
//	    menuManager.add(new SelectCounterModeAction(2, settings, this));
//	    menuManager.add(new SelectCounterModeAction(3, settings, this));
//	    menuManager.add(new SelectCounterModeAction(4, settings, this));
//	    menuManager.add(new Separator());
//	    menuManager.add(new HideUnusedTypesAction(settings, this));
//	    menuManager.add(new Separator());
//	    menuManager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));	    
	    

	}
	
//      experimental createPartControl content
//	    try {
//	        IContainerFactory factory = ContainerFactory.getDefault();
//	        List descriptions = factory.getDescriptions();
//	        for(Object each: descriptions) {
//	            System.out.println(each);
//	        }
//            IContainer container = ContainerFactory.getDefault().createContainer("ecf.xmpps.smack");
//            ID newID = IDFactory.getDefault().createID("ecf.xmpps","deifair@gmail.com");
//            container.connect(newID, null);
//            
//        } catch (ContainerCreateException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (ContainerConnectException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
	
    @Override
    public void setFocus() {
        // TODO Auto-generated method stub
    }

}