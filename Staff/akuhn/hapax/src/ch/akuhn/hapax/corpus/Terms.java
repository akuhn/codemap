package ch.akuhn.hapax.corpus;

import java.io.File;
import java.util.Collection;

import ch.akuhn.util.Bag;

public class Terms extends Bag<String> implements ScannerClient {

    public Terms() {
        // do nothing
    }

    public Terms(File file) {
        new CamelCaseScanner().client(this).onFile(file).run();
    }

    public Terms(String text) {
        new CamelCaseScanner().client(this).onString(text).run();
    }
    
    public Terms(Collection<String> strings) {
        this.addAll(strings); // #addAll handles "instance of bag" special case
    }

    public Terms stem() {
        Stemmer stemmer = new PorterStemmer();
        Terms terms = new Terms();
        for (Count<String> each: this.counts()) {
            terms.add(stemmer.stem(each.element), each.count);
        }
        return terms;
    }

    public Terms toLowerCase() {
        Terms terms = new Terms();
        for (Count<String> each: this.counts()) {
            terms.add(each.element.toString().toLowerCase(), each.count);
        }
        return terms;
    }

    //@Override
    public void yield(CharSequence term) {
        this.add(term.toString());
    }

}
