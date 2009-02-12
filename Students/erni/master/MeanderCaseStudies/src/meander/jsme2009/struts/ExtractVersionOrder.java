package meander.jsme2009.struts;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import ch.akuhn.fame.FameDescription;
import ch.akuhn.fame.FamePackage;
import ch.akuhn.fame.FameProperty;
import ch.akuhn.util.Files;
import ch.akuhn.util.Get;
import ch.akuhn.util.Out;
import ch.akuhn.util.Throw;

@FamePackage("Meander")
public class ExtractVersionOrder implements Runnable {

    public static void main(String[] args) throws ZipException, IOException {
        new ExtractVersionOrder().folder("data/groovy").run();
    }

    private String folderName;

    private ExtractVersionOrder folder(String name) {
        this.folderName = name;
        return this;
    }

    public void run() {
        try {
            checkedRun();
        } catch (Exception ex) {
            throw Throw.exception(ex);
        }
    }
    
    public void checkedRun() throws Exception {
    
        VersionOrder order = new VersionOrder();
        
        for (File file: Files.all(folderName)) {
            if (file.getName().endsWith(".zip")) {
                long min = Long.MAX_VALUE;
                long max = Long.MIN_VALUE;
                ZipFile zip = new ZipFile(file);
                for (ZipEntry each: Get.each(zip.entries())) {
                    if (each.isDirectory()) continue;
                    long time = each.getTime();
                    if (time == -1) continue;
                    min = Math.min(min, time);
                    max = Math.max(max, time);
                    //System.out.println(new Date(time) + "\t" + each.getName());
                }
                //if (file.getName().contains("beta-2")) System.exit(-1);
                
                order.versions.add(new Version(file.getName(), max));
            }
        }

        order.versions = Get.sorted(order.versions);
        
        Out.puts(order.versions);
        
    }

    @FameDescription 
    public static class VersionOrder {
        @FameProperty Collection<Version> versions = new ArrayList<Version>();
    }
    
    public static class Version implements Comparable<Version> {
        @FameProperty String name;
        @FameProperty long time;
        public Version(String name, long time) {
            this.name = name;
            this.time = time;
        }
        public int compareTo(Version other) {
            // GOTCHA must use signum, or else conversion fail.
            return (int) Math.signum(this.time - other.time);
        }
        @Override
        public String toString() {
            return getDate() + "\t" + name;
        }
        public Date getDate() {
            return new Date(time);
        }
    }

}
