package ch.unibe.eclipse.util;

import org.eclipse.core.runtime.Platform;

public class EclipseUtil {

	@SuppressWarnings("unchecked")
	public static final <T> T adapt(Object object, Class<T> adapter) {
		return (T) Platform.getAdapterManager().getAdapter(object, adapter);
	}
}
