package flowmap.swt.main;

import org.eclipse.swt.graphics.GC;

import edu.berkeley.guir.prefuse.VisualItem;

public interface SWTRenderer {

    void renderSWT(GC gc, VisualItem vi);

}
