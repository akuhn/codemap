package ch.deif.meander;

import java.io.InputStream;
import java.util.Scanner;

public class StreamGobbler extends Thread {

    protected Scanner $;

    public StreamGobbler(InputStream is) {
        System.err.println(this);
        this.$ = new Scanner(is);
    }

    @Override
    public void run() {
        $.useDelimiter("\n+");
        while ($.hasNext())
            System.err.printf("%s %s\n", this, $.next());
    }

}
