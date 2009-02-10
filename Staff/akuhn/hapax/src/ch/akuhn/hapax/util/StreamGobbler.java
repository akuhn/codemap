package ch.akuhn.hapax.util;

import java.io.InputStream;
import java.util.Scanner;

public class StreamGobbler extends Thread {

    protected Scanner $;
    private boolean running = true;

    public StreamGobbler(InputStream is) {
        this.$ = new Scanner(is);
    }

    @Override
    public void run() {
        $.useDelimiter("\n+");
        while (running && $.hasNext())
            System.err.printf("%s\n", $.next());
    }
    
    public void kill() {
        running  = false;
    }

    public void consume(String... words) {
        for (String word: words) {
            String next = $.next();
            if (word == null || word.equals(next)) continue;
            throw new Error("Expected " + word + " but found " + next);
        }
    }

    public int consumeInt(String... words) {
        consume(words);
        return $.nextInt();
    }

    public double consumeDouble(String... words) {
        consume(words);
        return $.nextDouble();
    }

    public void expectEOF() {
        if (running && $.hasNext()) throw new Error();
    }

}
