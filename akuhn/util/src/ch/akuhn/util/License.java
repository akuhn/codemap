//  Copyright (c) 2007 Adrian Kuhn <akuhn(a)iam.unibe.ch>
//
//  This file is part of "Adrian Kuhn's Utilities for Java".
//
//  "Adrian Kuhn's Utilities for Java" is free software: you can redistribute it
//  and/or modify it under the terms of the GNU Lesser General Public License as
//  published by the Free Software Foundation, either version 3 of the License,
//  or (at your option) any later version.
//
//  "Adrian Kuhn's Utilities for Java" is distributed in the hope that it will be
//  useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser
//  General Public License for more details.
//
//  You should have received a copy of the GNU Lesser General Public License along
//  with "Adrian Kuhn's Utilities for Java". If not, see
//  <http://www.gnu.org/licenses/>.
//

package ch.akuhn.util;

import static ch.akuhn.util.Files.endsWith;
import static ch.akuhn.util.Files.files;
import static ch.akuhn.util.Strings.lines;
import static ch.akuhn.util.Strings.words;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Formatter;
import java.util.Iterator;
import java.util.List;

public class License {

	private final static String LGPL = "Copyright (c) 2007 Adrian Kuhn <akuhn(a)iam.unibe.ch>\n"
			+ "This file is part of %1$s.\n"
			+ "%1$s is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.\n"
			+ "%1$s is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.\n"
			+ "You should have received a copy of the GNU Lesser General Public License along with %1$s.  If not, see <http://www.gnu.org/licenses/>.\n";

	private static String breakAt76(String paragraph) {
		StringBuilder builder = new StringBuilder();
		int length = 0;
		int breakAt = 80;
		builder.append("// ");
		for (String word : words(paragraph)) {
			int newLength = length + 1 + word.length();
			if (newLength > breakAt) {
				builder.append("\n// ");
				newLength = 4 + word.length();
			}
			builder.append(' ');
			builder.append(word);
			length = newLength;
		}
		return builder.toString();
	}

	private static String getLGPL() {
		StringBuilder builder;
		// do the printf dance
		builder = new StringBuilder();
		Formatter formatter = new Formatter(builder);
		formatter.format(LGPL, "\"Adrian Kuhn's Utilities for Java\"");
		String license = builder.toString();
		// introduce line breaks
		builder = new StringBuilder();
		for (String line : lines(license)) {
			builder.append(breakAt76(line));
			builder.append("\n//\n");
		}
		return builder.toString();
	}

	public static void main(String... strings) {
		File root = new File("src");
		process(root);
	}

	public static void process(File folder) {
		for (File each : files(folder, endsWith(".java"))) {
			List<String> lines = readLines(each);
			updateLicense(lines);
			writeLines(each, lines);
		}
	}

	private static List<String> readLines(File each) {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(each)));
			List<String> lines = new ArrayList<String>();
			while (true) {
				String line = reader.readLine();
				if (line == null)
					break;
				lines.add(line);
			}
			return lines;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static void updateLicense(List<String> lines) {
		for (Iterator<String> iterator = lines.iterator(); iterator.hasNext();) {
			String line = iterator.next();
			if (line.isEmpty() || line.startsWith("//")) {
				iterator.remove();
			} else {
				break;
			}
		}
		lines.add(0, getLGPL());
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
