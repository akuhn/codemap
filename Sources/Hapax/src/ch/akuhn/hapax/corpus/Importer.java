package ch.akuhn.hapax.corpus;

import static ch.akuhn.util.Get.each;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import ch.akuhn.util.Files;
import ch.akuhn.util.Throw;

public class Importer {

    private Corpus corpus;

    public Importer(Corpus corpus) {
        this.corpus = corpus;
    }
    
    public Corpus importAllFiles(File folder, String... extensions) {
        for (File each : Files.find(folder, extensions)) {
            corpus.addDocument(each.getAbsolutePath(), null, new Terms(each));
        }
        return corpus;
    }

    public Corpus importAllZipArchives(File folder, String... extensions) {
        for (File file : Files.find(folder, ".zip", ".jar")) {
            this.importZipArchive(file, extensions);
        }
        return corpus;
    }
    
    public Corpus importAllZipArchivesPackageWise(File folder, String... extensions) {
        for (File file : Files.find(folder, ".zip", ".jar")) {
            System.out.printf("importing file: %s\n", file.getName());
            this.importZipArchivePackageWise(file, extensions);
        }
        return corpus;
    }    

    public Corpus importZipArchivePackageWise(String path, String... extensions) {
        return this.importZipArchivePackageWise(new File(path), extensions);
    }

    public Corpus importZipArchivePackageWise(File file, String... extensions) {
        try {
            Map<String, Document> packages = new TreeMap<String, Document>();
            ZipFile zip = new ZipFile(file);
            VersionNumber version = new VersionNumber(file.getName());
            String fileSeparator = System.getProperty("file.separator");
            
            for (ZipEntry entry : each(zip.entries())) {
                String name = entry.getName();
                int endIndex = name.lastIndexOf(fileSeparator);
                if (endIndex < 0 || entry.isDirectory()) continue;
                
                for (String suffix : extensions) {
                    if (!name.endsWith(suffix)) continue;
                    
                    InputStream in = zip.getInputStream(entry);
                    Terms terms = new Terms(in).intern();                    
                    
                    String directory = name.substring(0, ++endIndex);
                    Document pack = packages.get(directory);                    
                    if (pack == null) {
                        pack = new Document(name, version, Collections
                                .<String> emptyList());
                        packages.put(directory, pack);
                    }
                    pack.addTerms(terms);
                    
                    break;
                }
            }

            for (Document each : packages.values()) {
                if (!each.terms().isEmpty()) 
                    corpus.addDocument(each.name(), 
                            each.version().name(), each.terms());
            }
            
            return corpus;
        } catch (ZipException ex) {
            throw Throw.exception(ex);
        } catch (IOException ex) {
            throw Throw.exception(ex);
        }
    }

    public Corpus importZipArchive(File file, String... extensions) {
        try {
            ZipFile zip = new ZipFile(file);
            for (ZipEntry entry : each(zip.entries())) {
                for (String suffix : extensions) {
                    if (!entry.getName().endsWith(suffix))
                        continue;
                    InputStream in = zip.getInputStream(entry);
                    Terms terms = new Terms(in).intern();
                    corpus.addDocument(entry.getName(), file.getName(), terms);
                }
            }
            return corpus;
        } catch (ZipException ex) {
            throw Throw.exception(ex);
        } catch (IOException ex) {
            throw Throw.exception(ex);
        }
    }
    
    public void importZipArchive(String name, String extensions) {
        this.importZipArchive(new File(name), extensions);
    }
    
}
