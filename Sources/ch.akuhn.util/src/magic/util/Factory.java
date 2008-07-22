//  Copyright (c) 1998-2008 Adrian Kuhn <akuhn(a)students.unibe.ch>
//  
//  This file is part of ch.akuhn.util.
//  
//  ch.akuhn.util is free software: you can redistribute it and/or modify it
//  under the terms of the GNU Lesser General Public License as published by the
//  Free Software Foundation, either version 3 of the License, or (at your
//  option) any later version.
//  
//  ch.akuhn.util is distributed in the hope that it will be useful, but
//  WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
//  or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
//  License for more details.
//  
//  You should have received a copy of the GNU Lesser General Public License
//  along with ch.akuhn.util. If not, see <http://www.gnu.org/licenses/>.
//  

package magic.util;

import java.lang.reflect.Constructor;

/**
 * ACME = a factory making everything.
 * 
 * @author akuhn
 *
 */
public class Factory<T> {

	private Class<T> type;
	private Constructor<T> takeOne;
	private Constructor<T> takeTwo;
	private Constructor<T> takeThree;
	
	public Factory(Class<T> type) {
		this.type = type;
		Constructor<?>[] all = type.getDeclaredConstructors();
		this.takeOne = searchConstructor(all, 1);
		this.takeTwo = searchConstructor(all, 2);
		this.takeThree = searchConstructor(all, 3);
	}
	
	@SuppressWarnings("unchecked")
	private static final <T> Constructor<T> searchConstructor(Constructor<?>[] all, int parameterLength) {
		Constructor result = null;
		for (Constructor init : all) {
			if (init.getParameterTypes().length == parameterLength) {
				if (result != null) throw new AssertionError("Ambigous constructor");
				result = init;
			}
		}
		return (Constructor<T>) result;
	}
	
	public T create() {
		try {
			return type.newInstance();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
	public T create(Object obj) {
		try {
			return takeOne.newInstance(obj);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public T create(Object obj1, Object obj2) {
		try {
			return takeTwo.newInstance(obj1, obj2);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public T create(Object obj1, Object obj2, Object obj3) {
		try {
			return takeThree.newInstance(obj1, obj2, obj3);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
	
}
