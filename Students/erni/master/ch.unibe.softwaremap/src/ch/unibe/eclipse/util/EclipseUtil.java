package ch.unibe.eclipse.util;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.ICompilationUnit;

public class EclipseUtil {

	@SuppressWarnings("unchecked")
	public static final <T> T adapt(Object object, Class<T> adapter) {
		return (T) Platform.getAdapterManager().getAdapter(object, adapter);
	}
	
	public static String shortenCompilationUnitName(ICompilationUnit compilationUnit) {
		// shorten the fullname which looks like: =Fame/test<ch.akuhn.fame.test{ArrayFieldTest.java
		String[] split = compilationUnit.getHandleIdentifier().split("\\{");
		if (!(split.length == 2)) return compilationUnit.getHandleIdentifier();
		String shorter = split[split.length-1];
		shorter = shorter.replaceAll(".java", "");
		return shorter;
	}	

}
