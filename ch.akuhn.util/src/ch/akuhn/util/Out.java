package ch.akuhn.util;

public class Out {

	private static final PrintOn out = new PrintOn(System.out);
	
    private Out() {
        // cannot instantiate
    }    
    
    public static final PrintOn puts(Object object) {
    	out.print(object).cr();
    	return out;
    }
    
}
