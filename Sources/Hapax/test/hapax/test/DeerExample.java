package hapax.test;

import static org.junit.Assert.assertEquals;

import java.util.Scanner;

import ch.unibe.jexample.Given;
import ch.unibe.jexample.JExample;

import org.junit.Test;
import org.junit.runner.RunWith;

import ch.akuhn.hapax.corpus.Terms;
import ch.akuhn.hapax.index.TermDocumentMatrix;
import ch.akuhn.util.Get;


@RunWith(JExample.class)
public class DeerExample {

    public static final String SORTED = 
        "[computer, eps, graph, human, interface, minors, response, survey, system, time, trees, user]";

    public static final String[][] DATA = {
        { "c1", "Human machine interface for Lab ABC computer applications" },
        { "c2", "A survey of user opinion of computer system response time" },
        { "c3", "The EPS user interface management system" },
        { "c4", "System and human system engineering testing of EPS" },
        { "c5", "Relation of user-perceived response time to error measurement" },
        { "m1", "The generation of random, binary, unordered trees" },
        { "m2", "The intersection graph of paths in trees" },
        { "m3", "Graph minors IV: Widths of trees and well-quasi-ordering" },
        { "m4", "Graph minors: A survey" }};
    
    
    @Test
    public TermDocumentMatrix makeTermDocumentMatrix() {
        TermDocumentMatrix tdm = new TermDocumentMatrix();
        for (String[] each: DATA) {
            tdm.makeDocument(each[0]).addTerms(new Terms(each[1]));
        }
        assertEquals(9, tdm.documentSize());
        assertEquals(45, tdm.termSize());
        return tdm;
    }
    
    @Test
    @Given("#makeTermDocumentMatrix")
    public TermDocumentMatrix rejectStopWords(TermDocumentMatrix tdm) {
        tdm = tdm.toLowerCase();
        assertEquals(9, tdm.documentSize());
        assertEquals(42, tdm.termSize());
        tdm = tdm.rejectHapaxes();
        assertEquals(9, tdm.documentSize());
        assertEquals(16, tdm.termSize());
        tdm = tdm.toLowerCase().rejectStopwords();
        assertEquals(9, tdm.documentSize());
        assertEquals(12, tdm.termSize());
        assertEquals(SORTED, Get.sorted(tdm.terms().elementSet()).toString());
        return tdm;
    }
    
    @Test
    @Given("#rejectStopWords")
    public void testImportExport(TermDocumentMatrix tdm) {
        StringBuilder buf = new StringBuilder();
        tdm.storeOn(buf);
        TermDocumentMatrix tdm2 = TermDocumentMatrix.readFrom(new Scanner(buf.toString()));
        StringBuilder buf2 = new StringBuilder();
        tdm2.storeOn(buf2);
        assertEquals(buf.toString(), buf2.toString());
        assertEquals(tdm.documentSize(), tdm2.documentSize());
        assertEquals(tdm.termSize(), tdm2.termSize());
        assertEquals(tdm.density(), tdm2.density());
    }

}
