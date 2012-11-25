package hapax.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import ch.akuhn.hapax.corpus.CamelCaseScanner;
import ch.akuhn.hapax.corpus.ScannerClient;
import ch.akuhn.hapax.corpus.TermScanner;

public class CamelCaseScannerTest implements ScannerClient {

    protected TermScanner scanner = new CamelCaseScanner().client(this);
    private List<String> terms = new ArrayList<String>();

    @Test
    public void empty() {
        scannerOn("").run();
        assertEquals("[]", terms.toString());
    }

    @Test
    public void innerManySpace() {
        scannerOn("Camel\t\t\tCase\t\t\tScanner").run();
        assertEquals("[Camel, Case, Scanner]", terms.toString());
    }

    @Test
    public void innerSpace() {
        scannerOn("Camel\tCase\tScanner").run();
        assertEquals("[Camel, Case, Scanner]", terms.toString());
    }

    @Test
    public void leadingManySpace() {
        scannerOn("\t\t\tCamelCaseScanner").run();
        assertEquals("[Camel, Case, Scanner]", terms.toString());
    }

    @Test
    public void leadingSpace() {
        scannerOn("\tCamelCaseScanner").run();
        assertEquals("[Camel, Case, Scanner]", terms.toString());
    }

    @Test
    public void manyLower() {
        scannerOn("xxx").run();
        assertEquals("[xxx]", terms.toString());
    }

    @Test
    public void manySpace() {
        scannerOn("\t\t\t").run();
        assertEquals("[]", terms.toString());
    }

    @Test
    public void manyUpper() {
        scannerOn("XXX").run();
        assertEquals("[XXX]", terms.toString());
    }

    @Test
    public void manyWord() {
        scannerOn("FooFoo").run();
        assertEquals("[Foo, Foo]", terms.toString());
    }

    @Test
    public void oneLower() {
        scannerOn("x").run();
        assertEquals("[x]", terms.toString());
    }

    @Test
    public void oneSpace() {
        scannerOn("\t").run();
        assertEquals("[]", terms.toString());
    }

    @Test
    public void oneUpper() {
        scannerOn("X").run();
        assertEquals("[X]", terms.toString());
    }

    @Test
    public void oneWord() {
        scannerOn("Foo").run();
        assertEquals("[Foo]", terms.toString());
    }

    @Test
    public void simple() {
        scannerOn("CamelCaseScanner").run();
        assertEquals("[Camel, Case, Scanner]", terms.toString());
    }

    @Test
    public void testABRRSpaceWord() {
        scannerOn("ABBR\tWord").run();
        assertEquals("[ABBR, Word]", terms.toString());
    }

    @Test
    public void testABRRWord() {
        scannerOn("ABBRWord").run();
        assertEquals("[ABBR, Word]", terms.toString());
    }

    @Test
    public void trailingManySpace() {
        scannerOn("CamelCaseScanner\t\t\t").run();
        assertEquals("[Camel, Case, Scanner]", terms.toString());
    }

    protected TermScanner scannerOn(String string) {
        return scanner.onString(string);
    }

    @Test
    public void trailingSpace() {
        scannerOn("CamelCaseScanner\t").run();
        assertEquals("[Camel, Case, Scanner]", terms.toString());
    }

    //@Override
    public void yield(CharSequence term) {
        terms.add(term.toString());
    }

}