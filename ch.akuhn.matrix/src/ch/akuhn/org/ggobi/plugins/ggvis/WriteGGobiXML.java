package ch.akuhn.org.ggobi.plugins.ggvis;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import ch.akuhn.hapax.Hapax;
import ch.akuhn.hapax.linalg.Matrix;
import ch.akuhn.hapax.linalg.Vector;
import ch.akuhn.hapax.linalg.Vector.Entry;


public class WriteGGobiXML {

    private static final double THRESHOLD = -999;

    public static void main(String[] args) throws IOException {

        String folder = "..";
        Hapax hapax = Hapax.newCorpus()
        .useTFIDF()
        .useCamelCaseScanner()
        .addFiles(folder, ".java")
        .build();
        Matrix corr = hapax.getIndex().documentCorrelation();
        PrintWriter f = new PrintWriter("data.xml");
        f.println("<?xml version=\"1.0\"?>");
        f.println("<!DOCTYPE ggobidata SYSTEM \"ggobi.dtd\">");
        f.println("<ggobidata count=\"2\">");
        f.println("<data name=\"points\">");
        f.println("<variables count=\"1\">");
        f.println("  <realvariable name=\"x\" nickname=\"x\" />");
        f.println("</variables>");
        f.printf("<records count=\"%d\">\n", hapax.getIndex().documentCount());
        int n = 0;
        for (String doc: hapax.getIndex().documents()) {
            f.printf("<record id=\"%d\" label=\"%s\"> 0 </record>\n",
                    ++n,
                    new File(doc).getName());
        }
        f.print("</records>\n</data>\n\n");
        f.println("<data name=\"distance\">");
        f.println("<variables count=\"1\">");
        f.println("  <realvariable name=\"D\" nickname=\"D\" />");
        f.println("</variables>");
        int tally = 0;
        for (Vector row: corr.rows()) {
            for (Entry each: row.entries()) {
                if (Matrix.indexOf(row) >= each.index) continue;
                if (each.value > THRESHOLD) tally++;
            }
        }
        System.out.println(tally);
        f.printf("<records count=\"%d\" glyph=\"fr 1\" color=\"0\">\n", tally);
        for (Vector row: corr.rows()) {
            for (Entry each: row.entries()) {
                if (Matrix.indexOf(row) >= each.index) continue;
                if (each.value > THRESHOLD) {
                    f.printf("<record source=\"%d\" destination=\"%d\"> %f </record>\n",
                            Matrix.indexOf(row)+1,
                            each.index+1,
                            metrize(each.value));
                }
            }
        }
        f.print("</records>\n</data>\n\n</ggobidata>\n");
        f.close();
    }

    private static double metrize(double sim) {
        return Math.pow(Math.max(0,1 - sim), 0.5);
    }
}
