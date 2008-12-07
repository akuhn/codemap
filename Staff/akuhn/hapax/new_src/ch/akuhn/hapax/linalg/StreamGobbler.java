package ch.akuhn.hapax.linalg;

import java.io.InputStream;
import java.util.Scanner;

public class StreamGobbler
        extends Thread {

    protected Scanner $;

    public StreamGobbler(InputStream is) {
        this.$ = new Scanner(is);
    }

    @Override
    public void run() {
        $.useDelimiter("\n+");
        while ($.hasNext())
            System.err.printf("%s\n", $.next());
    }

}
