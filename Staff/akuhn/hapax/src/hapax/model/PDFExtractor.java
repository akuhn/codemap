package hapax.model;

import java.io.File;

import ch.akuhn.util.Strings;
import ch.akuhn.util.Throw;
import ch.akuhn.util.Bag;

import tool.doc.ExtractText;

public class PDFExtractor {

    public final String fname;

    public PDFExtractor(String fname) {
        this.fname = fname;
    }

    @Override
    public String toString() {
        try {
            return new ExtractText().extract(new File(fname).toURI());
        } catch (Exception ex) {
            throw Throw.exception(ex);
        }
    }

    public Bag<String> asBagOfTerms() {
        Bag<String> bag = new Bag();
        for (String each : Strings.letters(this.toString())) {
            bag.add(each.toLowerCase());
        }
        return bag;
    }

}
