package ch.akuhn.hapax.corpus;


public class TermBagCorpus extends Corpus {

    private class Doc extends Document {

        public Doc(String name) {
            super(name);
        }

        private Terms terms = new Terms();
        
        @Override
        public int termSize() {
            return terms.uniqueSize();
        }

        @Override
        public Terms terms() {
            return terms;
        }

        @Override
        public Document addTerms(Terms terms) {
            terms.addAll(terms);
            return this;
        }
        
    }

    @Override
    protected Document makeDocument(String name) {
        return new Doc(name);
    }


}
