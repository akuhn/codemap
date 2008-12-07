package ch.akuhn.hapax.corpus;

public interface ScannerClient {

    public static final ScannerClient DEBUG = new ScannerClient() {
        @Override
        public void yield(CharSequence term) {
            System.out.println("\""+term+"\"");
        }
    };

    public static final ScannerClient NULL = new ScannerClient() {
        @Override
        public void yield(CharSequence term) {
            // do nothing
        }
    };

    public void yield(CharSequence term);
    
}
