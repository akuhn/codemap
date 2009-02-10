package ch.unibe.jsme2009;

import ch.akuhn.util.Bag;
import ch.akuhn.util.Strings;
import ch.akuhn.util.Throw;

public class PDFExtractor {

    public final String fname;

    public PDFExtractor(String fname) {
        this.fname = fname;
    }

    public Bag<String> asBagOfTerms() {
        Bag<String> bag = new Bag<String>();
        for (String each: Strings.letters(this.toString())) {
            bag.add(each.toLowerCase());
        }
        return bag;
    }

    @Override
    public String toString() {
        try {
            return ""; // new ExtractText().extract(new File(fname).toURI());
        } catch (Exception ex) {
            throw Throw.exception(ex);
        }
    }

}
