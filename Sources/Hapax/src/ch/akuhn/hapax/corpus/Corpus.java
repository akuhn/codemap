package ch.akuhn.hapax.corpus;

import static ch.akuhn.util.Get.each;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import ch.akuhn.util.CacheMap;
import ch.akuhn.util.Files;
import ch.akuhn.util.Throw;


public class Corpus {

    private Collection<Document> documents;
    private Map<String,VersionNumber> versions;

    public Corpus() {
        this.documents = new ArrayList<Document>();
        this.versions = CacheMap.<String,VersionNumber>instances(VersionNumber.class);
    }

    public Iterable<Document> documents() {
        return documents;
    }

    public int documentSize() {
        return documents.size();
    }

    public void add(Document document) {
        documents.add(document);
    }
    
    public void addAll(Corpus other) {
        for (Document each: other.documents) this.add(each);
    }

    public Corpus importAllFiles(File folder, String... extensions) {
        for (File each: Files.find(folder, extensions)) {
            addDocument(each.getAbsolutePath(), new Terms(each));
        }
        return this;
    }
    
    public Corpus importAllZipArchives(File folder, String... extensions) {
        for (File file: Files.find(folder, ".zip", ".jar")) {
            this.importZipArchive(file, extensions);
        }
        return this;
    }

    public Corpus importZipArchive(File file, String... extensions) {
        try {
            ZipFile zip = new ZipFile(file);
            for (ZipEntry entry: each(zip.entries())) {
                for (String each: extensions) {
                    if (!each.equals(entry.getName())) continue;
                    InputStream in = zip.getInputStream(entry);
                    Terms terms = new Terms(in).intern();
                    this.addDocument(entry.getName(), file.getName(), terms);
                }
            }
            return this;
        } catch (ZipException ex) {
            throw Throw.exception(ex);
        } catch (IOException ex) {
            throw Throw.exception(ex);
        }
    }
    
    public void addDocument(String name, Collection<String> terms) {
        this.add(new Document(name, terms));
    }

    public void addDocument(String name, String version, Collection<String> terms) {
        this.add(new Document(name, versions.get(version), terms));
    }

    public Terms terms() {
        Terms terms = new Terms();
        for (Document each: documents) terms.addAll(each.terms());
        return terms;
    }

    public int termSize() {
        return terms().uniqueSize();
    }

    @Override
    public String toString() {
        return String.format("Corpus (%d documents, %d terms)", documentSize(), termSize());
    }

}
