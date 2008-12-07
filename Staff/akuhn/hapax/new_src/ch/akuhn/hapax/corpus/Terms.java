package ch.akuhn.hapax.corpus;

import java.io.File;

import ch.akuhn.util.Bag;

public class Terms extends Bag<CharSequence> implements ScannerClient {

    public Terms() {
        // do nothing
    }

    public Terms(String text) {
        new CamelCaseScanner().client(this).onString(text).run();
    }

    public Terms(File file) {
        new CamelCaseScanner().client(this).onFile(file).run();
    }

    @Override
    public void yield(CharSequence term) {
        this.add(term);
    }

    public Terms toLowerCase() {
        Terms terms = new Terms();
        for (Count<CharSequence> each: this.counts()) {
            terms.add(each.element.toString().toLowerCase(), each.count);
        }
        return terms;
    }
    
}
