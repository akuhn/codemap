package ch.akuhn.hapax.svd;

import java.io.InputStream;
import java.util.Scanner;

public class StreamGobbler extends Thread {
    
    protected Scanner $;
    
    public StreamGobbler(InputStream is) {
        this.$ = new Scanner(is);
    }
    
    public void run() {
        $.useDelimiter("\n+");
        while ($.hasNext()) System.err.printf("%s\n", $.next());
    }


}
