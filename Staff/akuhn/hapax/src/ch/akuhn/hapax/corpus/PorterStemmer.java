package ch.akuhn.hapax.corpus;

import org.tartarus.snowball.SnowballStemmer;
import org.tartarus.snowball.ext.porterStemmer;

public class PorterStemmer implements Stemmer {

    private SnowballStemmer stemmer = new porterStemmer();

    public String stem(CharSequence string) {
        stemmer.setCurrent(string.toString());
        stemmer.stem();
        return stemmer.getCurrent();
    }

}
