package org.codemap.util;

import org.eclipse.core.runtime.Platform;

public class Adaptables {

	@SuppressWarnings("unchecked")
	public static <T> T adapt(Object object, Class<T> adapter) {
		return (T) Platform.getAdapterManager().getAdapter(object, adapter);
	}
	
}
