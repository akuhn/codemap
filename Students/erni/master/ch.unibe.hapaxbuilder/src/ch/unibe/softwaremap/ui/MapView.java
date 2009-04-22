package ch.unibe.softwaremap.ui;


import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import ch.deif.meander.ui.EclipseProcessingBridge;

public class MapView extends ViewPart {

    public static final String ID = MapView.class.getName();
    private EclipseProcessingBridge softwareMap;


    class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {
        public String getColumnText(Object obj, int index) {
            return getText(obj);
        }
        public Image getColumnImage(Object obj, int index) {
            return getImage(obj);
        }
        @Override
        public Image getImage(Object obj) {
            return PlatformUI.getWorkbench().
                    getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
        }
    }


    public MapView() {
    }


    @Override
    public void createPartControl(Composite parent) {
        softwareMap = new EclipseProcessingBridge(parent);
    }   


    @Override
    public void setFocus() {
        softwareMap.setFocus();
    }
}