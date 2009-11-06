package org.codemap.mapview;

import javax.imageio.ImageIO;

import org.codemap.CodemapCore;
import org.codemap.util.IFileNameCallback;
import org.codemap.util.SafeSaveDialog;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;

public class SaveAsPNGAction extends Action implements IFileNameCallback {
    
//    TODO: implement the save as PNG action.
    
    private static final String PNG_SUFFIX = ".png";
    private static final String DEFAULT_FILENAME = "codemap" + PNG_SUFFIX;
    
    public SaveAsPNGAction() {
        super();
        setText("Save as PNG");
    }

    @Override
    public void run() {
        SafeSaveDialog dialog = new SafeSaveDialog(Display.getDefault().getActiveShell(), this);
        
        dialog.setFileName(DEFAULT_FILENAME);
        String[] filterExt = { "*" + PNG_SUFFIX };
        dialog.setFilterExtensions(filterExt);
        String userHome = System.getProperty("user.home");
        if (userHome != null) {
            dialog.setFilterPath(userHome);
        }
        
        String path = dialog.open();
        if (path == null) return;

        saveTo(path);
    }

    private void saveTo(String path) {
        Image image = CodemapCore.getPlugin().getMapView().getCodemapImage();

        ImageLoader loader = new ImageLoader();
        loader.data = new ImageData[] {image.getImageData()};
        loader.save(path, SWT.IMAGE_PNG);
        // disposing sometimes crashes eclipse so maybe we should not ... 
        // image.dispose();
    }

    @Override
    public String checkFileName(String path) {
        if (!path.endsWith(PNG_SUFFIX)) {
            path += PNG_SUFFIX;
        }
        return path;
    }
    
}
