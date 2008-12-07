package ch.akuhn.hapax.corpus;

import static java.lang.Character.isLetter;
import static java.lang.Character.isLowerCase;
import static java.lang.Character.isUpperCase;

public class CamelCaseScanner extends Scanner {

    @Override
    protected void scan() {
        while (true) {
            while (!isLetter(ch)) next();
            this.mark();
            if (isLowerCase(ch)) {
                next();
                while (isLowerCase(ch)) next();
            } else {
                next();
                if (isLowerCase(ch)) {
                    while (isLowerCase(ch)) next();
                }
                else {
                    while (isUpperCase(ch)) next();
                    if (isLowerCase(ch)) backtrack();
                }
            }
            this.yank();
        }
    }

    
    
}
