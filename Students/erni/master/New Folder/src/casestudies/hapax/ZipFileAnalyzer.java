package casestudies.hapax;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import ch.akuhn.util.Files;

public class ZipFileAnalyzer {

	private File file;

	public ZipFileAnalyzer(File each) {
		this.file = each;
	}

	public static void main(String... args) throws IOException {
		for (File each: Files.all("../MeanderCaseStudies/data/junit/")) {
			new ZipFileAnalyzer(each).run();
		}
	}

	private void run() throws IOException {
		printInfo("", file, new FileInputStream(file), "top level");
	}

	private void printInfo(String indent, Object file, InputStream in, Object owner) throws IOException {
		String match = (file.toString().indexOf("src") > 0) ? "*" : "";
		System.out.println(match + indent + "-- " + file + " in " + owner);
		ZipInputStream zip = new ZipInputStream(in);
		int tests = 0;
		int javas = 0;
		while (true) {
			ZipEntry entry = zip.getNextEntry();
			if (entry == null) break;
			String name = entry.getName();
			if (name.endsWith("Test.java")) tests++;
			if (name.endsWith(".java")) javas++;
			if (name.endsWith(".jar") || name.endsWith(".zip")) {
				printInfo(indent + "\t", entry, zip, file + " in " + owner);
			}
		}
		System.out.println(indent + javas + "(" + tests + ") java files.");
	}
	
}
