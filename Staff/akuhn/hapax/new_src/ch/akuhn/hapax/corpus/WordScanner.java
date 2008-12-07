package ch.akuhn.hapax.corpus;

import static java.lang.Character.isWhitespace;

public class WordScanner
        extends Scanner {

    @Override
    protected void scan() {
        while (true) {
            while (isWhitespace(ch))
                next();
            this.mark();
            while (!isWhitespace(ch))
                next();
            this.yank();
        }
    }

}
