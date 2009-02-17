package ch.akuhn.hapax.util;

import java.io.InputStream;
import java.util.Scanner;

public class StreamGobbler extends Thread {

    protected Scanner scan;
    private StringBuilder buf;
    private boolean running = true;
    private boolean verbose = false;

    public StreamGobbler(InputStream is) {
        this.scan = new Scanner(is);
        this.buf = new StringBuilder();
    }

    public StreamGobbler silent() {
        verbose = false;
        return this;
    }
    
    public StreamGobbler verbose() {
        verbose = true;
        return this;
    }
    
    @Override
    public void run() {
        scan.useDelimiter("\n+");
        while (running && scan.hasNext()) {
            String next = scan.next();
            buf.append(next).append('\n');
            if (verbose) System.err.printf("%s\n", next);
        }
    }
    
    public String kill() {
        running  = false;
        return buf.toString();
    }

    public void consume(String... words) {
        for (String word: words) {
            String next = scan.next();
            if (word == null || word.equals(next)) continue;
            throw new Error("Expected " + word + " but found " + next);
        }
    }

    public int consumeInt(String... words) {
        consume(words);
        return scan.nextInt();
    }

    public double consumeDouble(String... words) {
        consume(words);
        return scan.nextDouble();
    }

    public void expectEOF() {
        if (running && scan.hasNext()) throw new Error();
    }

}
