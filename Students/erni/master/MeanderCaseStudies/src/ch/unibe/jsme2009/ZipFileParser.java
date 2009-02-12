package ch.unibe.jsme2009;

import static ch.akuhn.util.Get.each;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import ch.akuhn.hapax.corpus.Corpus;
import ch.akuhn.hapax.corpus.Document;
import ch.akuhn.hapax.corpus.Terms;

public class ZipFileParser {

    public final File file;
    
    public ZipFileParser(File file) {
        this.file = file;
    }
    
    public Corpus parseFiles(String extension) throws ZipException, IOException {
        Corpus corpus = new Corpus();
        ZipFile zip = new ZipFile(file);
        for (ZipEntry entry: each(zip.entries())) {
            if (entry.getName().endsWith(extension)) {
                Document doc = new Document();
                doc.handle = entry.getName();
                InputStream in = zip.getInputStream(entry);
                Terms terms = new Terms(in);
                doc.terms.addAll(terms.intern());
                corpus.add(doc);
            }
        }
        return corpus;
    }

}
