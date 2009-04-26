package ch.unibe.eclipse.util;

import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.part.ViewPart;

import ch.unibe.softwaremap.ui.MapView;


public class EclipseUtil {

    @SuppressWarnings("unchecked")
    public static final <T> T adapt(Object object, Class<T> adapter) {
        return (T) Platform.getAdapterManager().getAdapter(object, adapter);
    }
}
