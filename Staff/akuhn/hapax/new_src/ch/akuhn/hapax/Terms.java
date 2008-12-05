package ch.akuhn.hapax;

import ch.akuhn.util.Bag;

public class Terms extends Bag<CharSequence> implements ScannerClient {

    public Terms() {
        // do nothing
    }

    public Terms(String text) {
        new CamelCaseScanner().client(this).onString(text).run();
    }

    @Override
    public void yield(CharSequence term) {
        this.add(term);
    }

}
