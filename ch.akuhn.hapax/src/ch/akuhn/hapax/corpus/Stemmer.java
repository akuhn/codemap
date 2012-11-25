package ch.akuhn.hapax.corpus;

public interface Stemmer {

    public static final Stemmer NULL = new Stemmer() {
        //@Override
        public String stem(CharSequence string) {
            return string.toString();
        }
    };

    public String stem(CharSequence string);

}
