//	Copyright (c) 2007 Adrian Kuhn <akuhn(a)iam.unibe.ch>
//	
//	This file is part of 'Fame for Java'.
//	
//	'Fame for Java' is free software: you can redistribute it and/or modify
//	it under the terms of the GNU Lesser General Public License as published by
//	the Free Software Foundation, either version 3 of the License, or
//	(at your option) any later version.
//	
//	'Fame for Java' is distributed in the hope that it will be useful,
//	but WITHOUT ANY WARRANTY; without even the implied warranty of
//	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//	GNU Lesser General Public License for more details.
//	
//	You should have received a copy of the GNU Lesser General Public License
//	along with 'Fame for Java'.  If not, see <http://www.gnu.org/licenses/>.
//	
package ch.akuhn.util;

import java.io.*;
import java.util.*;

public class License {

	private final static String LGPL = "//\tCopyright (c) 2007 Adrian Kuhn <akuhn(a)iam.unibe.ch>\n"
			+ "//\t\n"
			+ "//\tThis file is part of %1$s.\n"
			+ "//\t\n"
			+ "//\t%1$s is free software: you can redistribute it and/or modify\n"
			+ "//\tit under the terms of the GNU Lesser General Public License as published by\n"
			+ "//\tthe Free Software Foundation, either version 3 of the License, or\n"
			+ "//\t(at your option) any later version.\n"
			+ "//\t\n"
			+ "//\t%1$s is distributed in the hope that it will be useful,\n"
			+ "//\tbut WITHOUT ANY WARRANTY; without even the implied warranty of\n"
			+ "//\tMERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the\n"
			+ "//\tGNU Lesser General Public License for more details.\n"
			+ "//\t\n"
			+ "//\tYou should have received a copy of the GNU Lesser General Public License\n"
			+ "//\talong with 'Fame for Java'.  If not, see <http://www.gnu.org/licenses/>.\n"
			+ "//\t";

	public static void main(String... strings) {
		File root = new File("src");
		process(root);
	}

	public static void process(File folder) {
		for (File each : folder.listFiles()) {
			if (each.isDirectory()) {
				process(each);
			}
			if (each.isFile()) {
				if (each.getName().endsWith(".java")) {
					List<String> lines = readLines(each);
					updateLicense(lines);
					writeLines(each, lines);
				}
			}
		}
	}

	private static List<String> readLines(File each) {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(each)));
			List<String> lines = new ArrayList();
			while (true) {
				String line = reader.readLine();
				if (line == null) break;
				lines.add(line);
			}
			return lines;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static void updateLicense(List<String> lines) {
		for (Iterator iterator = lines.iterator(); iterator.hasNext();) {
			String line = (String) iterator.next();
			if (line.isEmpty() || line.startsWith("//")) {
				iterator.remove();
			} else {
				break;
			}
		}
		lines.add(0, LGPL);
	}

	private static void writeLines(File each, Collection<String> lines) {
		try {
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(each)));
			for (String line : lines) {
				writer.write(line);
				writer.write('\n');
			}
			writer.close();
			System.out.println(each);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
