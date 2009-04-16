package ch.deif.meander.resources;

import java.io.InputStream;

public class MSE {

	public static InputStream junit() {
//	    return MSE.class.getResourceAsStream("junit_with_terms.mse");
		return new Resources().junitStream();
	}
}

class Resources {

	public InputStream junitStream() {
		return this.getClass().getResourceAsStream("junit_with_terms.mse");
	}

}
