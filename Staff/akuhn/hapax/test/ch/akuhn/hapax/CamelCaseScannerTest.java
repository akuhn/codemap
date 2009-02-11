package ch.akuhn.hapax;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import ch.akuhn.hapax.corpus.CamelCaseScanner;
import ch.akuhn.hapax.corpus.Scanner;
import ch.akuhn.hapax.corpus.ScannerClient;

public class CamelCaseScannerTest implements ScannerClient {

    private Scanner scanner = new CamelCaseScanner().client(this);
    private List<String> terms = new ArrayList<String>();

    @Test
    public void empty() {
        scanner.onString("").run();
        assertEquals("[]", terms.toString());
    }

    @Test
    public void innerManySpace() {
        scanner.onString("Camel\t\t\tCase\t\t\tScanner").run();
        assertEquals("[Camel, Case, Scanner]", terms.toString());
    }

    @Test
    public void innerSpace() {
        scanner.onString("Camel\tCase\tScanner").run();
        assertEquals("[Camel, Case, Scanner]", terms.toString());
    }

    @Test
    public void leadingManySpace() {
        scanner.onString("\t\t\tCamelCaseScanner").run();
        assertEquals("[Camel, Case, Scanner]", terms.toString());
    }

    @Test
    public void leadingSpace() {
        scanner.onString("\tCamelCaseScanner").run();
        assertEquals("[Camel, Case, Scanner]", terms.toString());
    }

    @Test
    public void manyLower() {
        scanner.onString("xxx").run();
        assertEquals("[xxx]", terms.toString());
    }

    @Test
    public void manySpace() {
        scanner.onString("\t\t\t").run();
        assertEquals("[]", terms.toString());
    }

    @Test
    public void manyUpper() {
        scanner.onString("XXX").run();
        assertEquals("[XXX]", terms.toString());
    }

    @Test
    public void manyWord() {
        scanner.onString("FooFoo").run();
        assertEquals("[Foo, Foo]", terms.toString());
    }

    @Test
    public void oneLower() {
        scanner.onString("x").run();
        assertEquals("[x]", terms.toString());
    }

    @Test
    public void oneSpace() {
        scanner.onString("\t").run();
        assertEquals("[]", terms.toString());
    }

    @Test
    public void oneUpper() {
        scanner.onString("X").run();
        assertEquals("[X]", terms.toString());
    }

    @Test
    public void oneWord() {
        scanner.onString("Foo").run();
        assertEquals("[Foo]", terms.toString());
    }

    @Test
    public void simple() {
        scanner.onString("CamelCaseScanner").run();
        assertEquals("[Camel, Case, Scanner]", terms.toString());
    }

    @Test
    public void testABRRSpaceWord() {
        scanner.onString("ABBR\tWord").run();
        assertEquals("[ABBR, Word]", terms.toString());
    }

    @Test
    public void testABRRWord() {
        scanner.onString("ABBRWord").run();
        assertEquals("[ABBR, Word]", terms.toString());
    }

    @Test
    public void trailingManySpace() {
        scanner.onString("CamelCaseScanner\t\t\t").run();
        assertEquals("[Camel, Case, Scanner]", terms.toString());
    }

    @Test
    public void trailingSpace() {
        scanner.onString("CamelCaseScanner\t").run();
        assertEquals("[Camel, Case, Scanner]", terms.toString());
    }

    //@Override
    public void yield(CharSequence term) {
        terms.add(term.toString());
    }

}