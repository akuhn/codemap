package hapax.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;

import ch.akuhn.hapax.corpus.Terms;
import ch.akuhn.hapax.index.TermDocumentMatrix;
import ch.akuhn.util.Get;
import ch.unibe.jexample.Given;
import ch.unibe.jexample.JExample;


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
            tdm.putDocument(each[0], new Terms(each[1]));
        }
        assertEquals(9, tdm.documentCount());
        assertEquals(45, tdm.termCount());
        return tdm;
    }
    
    @Test
    @Given("#makeTermDocumentMatrix")
    public TermDocumentMatrix rejectStopWords(TermDocumentMatrix tdm) {
        tdm = tdm.toLowerCase();
        assertEquals(9, tdm.documentCount());
        assertEquals(42, tdm.termCount());
        tdm = tdm.rejectHapaxes();
        assertEquals(9, tdm.documentCount());
        assertEquals(16, tdm.termCount());
        tdm = tdm.toLowerCase().rejectStopwords();
        assertEquals(9, tdm.documentCount());
        assertEquals(12, tdm.termCount());
        assertEquals(SORTED, Get.sorted(tdm.terms().elementSet()).toString());
        return tdm;
    }
    
//    @Test
//    @Given("#rejectStopWords")
//    public void testImportExport(TermDocumentMatrix tdm) {
//        StringBuilder buf = new StringBuilder();
//        tdm.storeOn(buf);
//        TermDocumentMatrix tdm2 = TermDocumentMatrix.readFrom(new Scanner(buf.toString()));
//        StringBuilder buf2 = new StringBuilder();
//        tdm2.storeOn(buf2);
//        assertEquals(buf.toString(), buf2.toString());
//        assertEquals(tdm.documentCount(), tdm2.documentCount());
//        assertEquals(tdm.termCount(), tdm2.termCount());
//        assertEquals(tdm.density(), tdm2.density(), Double.MIN_VALUE);
//    }

}
