package hapax.model;

import static ch.akuhn.util.Extensions.each;
import static ch.akuhn.util.Files.openWrite;
import static ch.akuhn.util.Strings.letters;
import static ch.akuhn.util.Strings.camelCase;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import ch.akuhn.util.Files;
import ch.akuhn.util.Throw;
import ch.akuhn.util.Bag;
import ch.akuhn.fame.Repository;

public class Main implements Runnable {

    private String folder;
    private Repository m;

    public Main(String folder) {
        this.folder = folder;
    }

    public static void main(String[] args) {
        Main m = new Main("\\\\.PSF\\.Home\\Desktop\\junit");
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
            if (isZipFile(each)) processZipfile(each);
        m.exportMSE(openWrite(folder + "\\model.h.mse"));
        m.exportMSE(openWrite("C:\\Documents and Settings\\akuhn\\My Documents\\Smalltalk\\model.h.mse"));
        HapaxModel.metamodel().exportMSE(openWrite(folder + "\\h.fm3.mse"));
    }

    private void processZipfile(File each) throws ZipException, IOException {
        if (containsSourceArchive(each)) {
            processZipfileWithSourceArchive(each);
        } else {
            processZipfileWithoutSourceArchive(each);
        }
    }

    private void processZipfileWithoutSourceArchive(File each) throws ZipException, IOException {
        String version = each.getName();
        ZipFile zip = new ZipFile(each);
        for (ZipEntry entry : each(zip.entries())) {
            if (isJavaFile(entry)) {
                Document doc = new Document();
                doc.name = entry.getName();
                doc.version = version;
                doc.terms = parseBag(zip.getInputStream(entry));
                m.add(doc);
            }
        }
    }

    private void processZipfileWithSourceArchive(File each) throws ZipException, IOException {
        String version = each.getName();
        ZipFile zip = new ZipFile(each);
        for (ZipEntry entry : each(zip.entries())) {
            if (isArchiveFile(entry)) {
                processNestedSourceArchive(zip.getInputStream(entry), version);
            }
        }
    }

    private void processNestedSourceArchive(InputStream inputStream, String version) throws IOException {
        ZipInputStream in = new ZipInputStream(inputStream);
        ZipEntry entry = null;
        while ((entry = in.getNextEntry()) != null) {
            if (isJavaFile(entry)) {
                Document doc = new Document();
                doc.name = entry.getName();
                doc.version = version;
                doc.terms = parseBag(in);
                m.add(doc);
            }
        }
    }

    private boolean containsSourceArchive(File each) throws ZipException, IOException {
        boolean hasSource = false;
        ZipFile zip = new ZipFile(each);
        for (ZipEntry entry : each(zip.entries())) {
            if (isArchiveFile(entry)) {
                hasSource = true;
            }
        }
        return hasSource;
    }

    private Bag<String> parseBag(InputStream in) throws IOException {
        Bag<String> bag = new Bag<String>();
        BufferedReader buf = new BufferedReader(new InputStreamReader(in));
        String line;
        while ((line = buf.readLine()) != null)
            for (CharSequence each : letters(line))
                for (CharSequence part : camelCase(each))
                    bag.add(part.toString().toLowerCase());
        return bag;
    }

    private boolean isZipFile(File each) {
        return each.isFile() && each.getName().toLowerCase().endsWith(".zip");
    }

    private boolean isJavaFile(ZipEntry entry) {
        return !entry.isDirectory() && entry.getName().toLowerCase().endsWith(".java");
    }

    private boolean isArchiveFile(ZipEntry entry) {
        return !entry.isDirectory()
                && (entry.getName().toLowerCase().endsWith("src.zip") || entry.getName().toLowerCase().endsWith(
                        "src.jar"));
    }

}
