//  Copyright (c) 1998-2008 Adrian Kuhn <akuhn(a)iam.unibe.ch>
//  
//  This file is part of "Adrian Kuhn's Utilities for Java".
//  
//  "Adrian Kuhn's Utilities for Java" is free software: you can redistribute
//  it and/or modify it under the terms of the GNU Lesser General Public License
//  as published by the Free Software Foundation, either version 3 of the
//  License, or (at your option) any later version.
//  
//  "Adrian Kuhn's Utilities for Java" is distributed in the hope that it will
//  be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser
//  General Public License for more details.
//  
//  You should have received a copy of the GNU Lesser General Public License
//  along with "Adrian Kuhn's Utilities for Java". If not, see
//  <http://www.gnu.org/licenses/>.
//  

package ch.akuhn.util;

import static ch.akuhn.util.Files.all;
import static ch.akuhn.util.Strings.*;

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

	public enum Kind {
		GPL("Copyright (c) %4$s %2$s <%3$s>\n"
			+ "This file is part of %1$s.\n"
			+ "%1$s is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.\n"
			+ "%1$s is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.\n"
			+ "You should have received a copy of the GNU General Public License along with %1$s.  If not, see <http://www.gnu.org/licenses/>.\n"),
		LESSER_GPL("Copyright (c) %4$s %2$s <%3$s>\n"
				+ "This file is part of %1$s.\n"
				+ "%1$s is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.\n"
				+ "%1$s is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.\n"
				+ "You should have received a copy of the GNU Lesser General Public License along with %1$s.  If not, see <http://www.gnu.org/licenses/>.\n"),
		PROPRIETARY("Copyright (c) %4$s %2$s <%3$s>\n"
				+ "This file is part of %1$s.\n"
				+ "PROPRIETARY/CONFIDENTIAL SOFTWARE. Use at your own risk.");
		public final String template;
		private Kind(String template) {
			this.template = template;
		}
	}
	
	public String appName;
	public String author;
	public String email;
	public String year;
	public Kind kind;

	private String getLicenseNote() {
		StringBuilder builder;
		String license;
		// do the printf dance
		builder = new StringBuilder();
		Formatter formatter = new Formatter(builder);
		formatter.format(kind.template, appName, author, email, year);
		license = builder.toString();
		// introduce line breaks
		builder = new StringBuilder();
		for (String line : lines(license)) {
			builder.append(reformatParagraph(line));
			builder.append("\n\n");
		}
		license = builder.toString();
		// prepend // to each line
		builder = new StringBuilder();
		for (String line : lines(license)) {
			builder.append("//  ");
			builder.append(line);
			builder.append('\n');
		}
		return builder.toString();
	}

	public static void main(String... strings) {
		File root = new File("src");
		License l = new License();
		l.kind = Kind.LESSER_GPL;
		l.appName = "\"Adrian Kuhn's Utilities for Java\"";
		l.author = "Adrian Kuhn";
		l.email = "akuhn(a)iam.unibe.ch";
		l.year = "1998-2008";
		l.process(root);
	}

	public void process(File folder) {
		for (File each : all(folder, "*.java")) {
			List<String> lines = readLines(each);
			updateLicense(lines);
			writeLines(each, lines);
		}
	}

	private List<String> readLines(File each) {
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

	private void updateLicense(List<String> lines) {
		for (Iterator<String> iterator = lines.iterator(); iterator.hasNext();) {
			String line = iterator.next();
			if (line.length() == 0 || line.startsWith("//")) {
				iterator.remove();
			} else {
				break;
			}
		}
		lines.add(0, getLicenseNote());
	}

	private void writeLines(File each, Collection<String> lines) {
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
