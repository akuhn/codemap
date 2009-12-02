package org.codemap.mapview.action;

import org.codemap.CodemapCore;
import org.codemap.util.EclipseUtil;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;

public class SaveAsPNGAction extends Action {
    
    public SaveAsPNGAction() {
        super();
        setText("Save as PNG");
    }

    @Override
    public void run() {
        String fname = EclipseUtil.filenameFromUser("Codemap of "+CodemapCore.activeProjectName().replace('.', '-'), ".png");
        if (fname == null) return;
        saveTo(fname);
    }

    private void saveTo(String path) {
        Image image = CodemapCore.getPlugin().getMapView().getCodemapImage();
        ImageLoader loader = new ImageLoader();
        loader.data = new ImageData[] {image.getImageData()};
        loader.save(path, SWT.IMAGE_PNG);
        image.dispose(); // of course we MUST dispose the image!
    }
    
}
