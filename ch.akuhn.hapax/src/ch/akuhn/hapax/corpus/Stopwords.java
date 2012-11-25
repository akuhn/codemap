package ch.akuhn.hapax.corpus;

import java.util.HashSet;

import ch.akuhn.hapax.resources.Resource;
import ch.akuhn.util.Strings;

@SuppressWarnings("serial")
public class Stopwords extends HashSet<String> {

    public static final Stopwords BASIC_ENGLISH = new Stopwords()
            .readFromResource("stopwords_SMART.txt");

    public Stopwords readFromResource(String name) {
        CharSequence str = Strings.fromInputStream(Resource.get(name));
        for (String line: Strings.lines(str)) {
            if (line.startsWith("#")) continue;
            for (String word: Strings.letters(line)) {
                this.add(word);
            }
        }
        return this;
    }

}
