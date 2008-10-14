package magic;

public class Version {

	private final static String LICENSE = 
		"Copyright (c) 1998-2008 Adrian Kuhn <akuhn(a)students.unibe.ch>\n" + 
		"\n" +
		"This file is part of ch.akuhn.util.\n" +
		"\n" +
		"ch.akuhn.util is free software: you can redistribute it and/or modify it\n" +
		"under the terms of the GNU Lesser General Public License as published by the\n" +
		"Free Software Foundation, either version 3 of the License, or (at your\n" +
		"option) any later version.\n" +
		"\n" +
		"ch.akuhn.util is distributed in the hope that it will be useful, but\n" +
		"WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY\n" +
		"or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public\n" +
		"License for more details.\n" +
		"\n" +
		"You should have received a copy of the GNU Lesser General Public License\n" +
		"along with ch.akuhn.util. If not, see <http://www.gnu.org/licenses/>.";
	
	
	public static void main(String[] args) {
		System.out.println("ch.akuhn.util");
		System.out.println("$Id$");
		System.out.println("");
		System.out.println(LICENSE);
	}

	
}
