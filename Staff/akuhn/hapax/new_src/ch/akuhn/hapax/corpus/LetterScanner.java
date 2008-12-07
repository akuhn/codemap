package ch.akuhn.hapax.corpus;

import static java.lang.Character.isLetter;

public class LetterScanner
        extends Scanner {

    @Override
    protected void scan() {
        while (true) {
            while (!isLetter(ch))
                next();
            this.mark();
            while (isLetter(ch))
                next();
            this.yank();
        }
    }

}
