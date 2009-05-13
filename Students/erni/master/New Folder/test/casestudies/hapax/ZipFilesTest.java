package casestudies.hapax;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;


public class ZipFilesTest {

	public static void main(String... args) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ZipOutputStream zout = new ZipOutputStream(baos);
		
		zout.putNextEntry(new ZipEntry("hello.txt"));
		zout.write("Hello, worlds!".getBytes());
		zout.closeEntry();
		zout.putNextEntry(new ZipEntry("readme.txt"));
		zout.write("Yo sup, I herd you liek zips so I put a zip in your zip so you can zip while you zip.".getBytes());
		zout.closeEntry();
		zout.close();
		
		System.out.println("length = " + baos.size());
		
		ZipInputStream zin = new ZipInputStream(new ByteArrayInputStream(baos.toByteArray()));
		while (true) {
			ZipEntry nextEntry = zin.getNextEntry();
			if (nextEntry == null) break;
			System.out.println(nextEntry);
			while (true) {
				int value = zin.read();
				if (value < 0) break;
				System.out.print((char) value);
			}
			System.out.println(" <<< EOF");
		}
	}
	
}
