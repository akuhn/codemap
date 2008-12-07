package ch.akuhn.hapax.corpus;

import java.util.HashSet;
import java.util.Set;

import ch.akuhn.util.Strings;


@SuppressWarnings("serial")
public class Stopwords extends HashSet<String> {

    public static final Stopwords BASIC_ENGLISH;
    static {
        BASIC_ENGLISH = new Stopwords();
        BASIC_ENGLISH.readFromResource("ch/akuhn/hapax/resources/stopwords_SMART.txt");
    }
    
    public static boolean contains(String word) {
        return ((Set<String>) BASIC_ENGLISH).contains(word);
    }
    
    public void readFromResource(String path) {
        CharSequence str = Strings.fromResource(path);
        for (String line : Strings.lines(str)) {
            if (line.startsWith("#")) continue;
            for (String word : Strings.letters(line)) {
                this.add(word);
            }
        }
    }
    
}
