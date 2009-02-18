package meander.jsme2009;

import static ch.akuhn.util.Files.openWrite;
import static ch.akuhn.util.Get.each;
import static ch.akuhn.util.Strings.camelCase;
import static ch.akuhn.util.Strings.letters;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import ch.akuhn.fame.Repository;
import ch.akuhn.util.Bag;
import ch.akuhn.util.Files;
import ch.akuhn.util.Throw;

public class Main implements Runnable {

    public static void main(String[] args) {
        Main m = new Main("\\\\.PSF\\.Home\\Desktop\\junit");
        m.run();
        System.out.println("done");
    }

    private String folder;

    private Repository m;

    public Main(String folder) {
        this.folder = folder;
    }

    private boolean containsSourceArchive(File each) throws ZipException, IOException {
        boolean hasSource = false;
        ZipFile zip = new ZipFile(each);
        for (ZipEntry entry: each(zip.entries())) {
            if (isArchiveFile(entry)) {
                hasSource = true;
            }
        }
        return hasSource;
    }

    private boolean isArchiveFile(ZipEntry entry) {
        return !entry.isDirectory()
                && (entry.getName().toLowerCase().endsWith("src.zip") || entry.getName().toLowerCase().endsWith(
                        "src.jar"));
    }

    private boolean isJavaFile(ZipEntry entry) {
        return !entry.isDirectory() && entry.getName().toLowerCase().endsWith(".java");
    }

    private boolean isZipFile(File each) {
        return each.isFile() && each.getName().toLowerCase().endsWith(".zip");
    }

    private Bag<String> parseBag(InputStream in) throws IOException {
        Bag<String> bag = new Bag<String>();
        BufferedReader buf = new BufferedReader(new InputStreamReader(in));
        String line;
        while ((line = buf.readLine()) != null)
            for (CharSequence each: letters(line))
                for (CharSequence part: camelCase(each))
                    bag.add(part.toString().toLowerCase());
        return bag;
    }

    private void processNestedSourceArchive(InputStream inputStream, String version) throws IOException {
        ZipInputStream in = new ZipInputStream(inputStream);
        ZipEntry entry = null;
        while ((entry = in.getNextEntry()) != null) {
            if (isJavaFile(entry)) {
                HapaxDoc doc = new HapaxDoc();
                doc.name = entry.getName();
                doc.setVersion(version);
                doc.terms = parseBag(in);
                m.add(doc);
            }
        }
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
        for (ZipEntry entry: each(zip.entries())) {
            if (isJavaFile(entry)) {
                HapaxDoc doc = new HapaxDoc();
                doc.name = entry.getName();
                doc.setVersion(version);
                doc.terms = parseBag(zip.getInputStream(entry));
                m.add(doc);
            }
        }
    }

    private void processZipfileWithSourceArchive(File each) throws ZipException, IOException {
        String version = each.getName();
        ZipFile zip = new ZipFile(each);
        for (ZipEntry entry: each(zip.entries())) {
            if (isArchiveFile(entry)) {
                processNestedSourceArchive(zip.getInputStream(entry), version);
            }
        }
    }

    //@Override
    public void run() {
        try {
            unsafeRun();
        } catch (Exception any) {
            Throw.exception(any);
        }
    }

    public void unsafeRun() throws Exception {
        m = new Repository(HapaxModel.metamodel());
        for (File each: Files.all(folder))
            if (isZipFile(each)) processZipfile(each);
        m.exportMSE(openWrite(folder + "\\model.h.mse"));
        m.exportMSE(openWrite("C:\\Documents and Settings\\akuhn\\My Documents\\Smalltalk\\model.h.mse"));
        HapaxModel.metamodel().exportMSE(openWrite(folder + "\\h.fm3.mse"));
    }

}
