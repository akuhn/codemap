package meander.jsme2009.struts;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipException;

import ch.akuhn.hapax.corpus.Corpus;
import ch.akuhn.hapax.corpus.Document;
import ch.akuhn.util.Files;
import ch.deif.meander.Serializer;
import ch.deif.meander.Serializer.MSEDocument;
import ch.deif.meander.Serializer.MSERelease;
import ch.unibe.jsme2009.ZipFileParser;

public class ExtractCorpusFromArchives {

    public static void main(String[] args) throws ZipException, IOException {
        
        Serializer ser = new Serializer();
        ser.project("Struts");
        for (File file: Files.all("data/struts")) {
            if (file.getName().endsWith(".zip")) {
                ser.release(file.getName());
                Corpus corpus = new ZipFileParser(file).parseFiles("java");
                for (Document each: corpus.documents()) {
                    ser.document(each.handle.toString(), each.terms);
                }
                System.out.println(ser.model().size());
            }
        }
        // TODO implement #exportMSEZipFile
        ser.model().exportMSEFile("mse/struts_input.mse");
        
        System.out.println("#release\t= " + ser.model().all(MSERelease.class).size());
        System.out.println("#document\t= " + ser.model().all(MSEDocument.class).size());
        
    }

}
