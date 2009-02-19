package hapax.test;

import java.io.ByteArrayInputStream;

import ch.akuhn.hapax.corpus.Scanner;

public class InputStreamScannerTest extends CamelCaseScannerTest {

    @Override
    protected Scanner scannerOn(String string) {
        return scanner.onStream(new ByteArrayInputStream(string.getBytes()));
    }

}
