package hapax.model;

import static ch.akuhn.util.Files.openWrite;
import ch.akuhn.hapax.corpus.Stopwords;

import java.io.File;
import java.util.Iterator;

import ch.akuhn.fame.Repository;

import ch.akuhn.util.Files;
import ch.akuhn.util.Throw;
import ch.akuhn.util.Bag;

public class PdfCasestudy implements Runnable {

    private String folder;
    private Repository m;

    public PdfCasestudy(String folder) {
        this.folder = folder;
    }

    public static void main(String[] args) {
        PdfCasestudy m;
        m = new PdfCasestudy("data");
        // m = new Main("Z:/Documents/Papers/Conferences/WCRE/WCRE2004");
        // m = new Main("Z:/Desktop/WCRE 2007");
        m.run();
        System.out.println("done");
    }

    @Override
    public void run() {
        try {
            unsafeRun();
        } catch (Exception any) {
            Throw.exception(any);
        }
    }

    public void unsafeRun() throws Exception {
        m = new Repository(HapaxModel.metamodel());
        for (File each : Files.all(folder))
            if (isPdfFile(each)) processPdfFile(each);
        m.exportMSE(openWrite(folder + "/pdf_vocabulary.hapax.mse"));
        HapaxModel.metamodel().exportMSE(openWrite(folder + "/hapax.fm3.mse"));
    }

    private boolean isPdfFile(File each) {
        return each.getName().toLowerCase().endsWith(".pdf");
    }

    private void processPdfFile(File file) {
        System.out.println(file);
        Document doc = new Document();
        doc.name = file.toString();
        doc.terms = safeExtractTerms(file);
        m.add(doc);
    }

    private Bag<String> safeExtractTerms(File file) {
        try {
            PDFExtractor pdf = new PDFExtractor(file.getAbsolutePath());
            Bag<String> terms = pdf.asBagOfTerms();
            for (Iterator<String> it = terms.elements(); it.hasNext();) {
                String each = it.next();
                if (each.length() < 3 || Stopwords.contains(each)) it.remove();
            }
            return terms;
        } catch (Throwable th) {
            th.printStackTrace();
            return new Bag();
        }
    }

}
