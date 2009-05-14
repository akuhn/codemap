package ch.akuhn.hapax.resources;

import java.io.InputStream;

public class Resource {
	
	public static InputStream get(String name) {
		return Resource.class.getResourceAsStream(name);
	}

}
