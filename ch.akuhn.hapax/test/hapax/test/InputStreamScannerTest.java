package hapax.test;

import java.io.ByteArrayInputStream;

import ch.akuhn.hapax.corpus.TermScanner;

public class InputStreamScannerTest extends CamelCaseScannerTest {

    @Override
    protected TermScanner scannerOn(String string) {
        return scanner.onStream(new ByteArrayInputStream(string.getBytes()));
    }

}
