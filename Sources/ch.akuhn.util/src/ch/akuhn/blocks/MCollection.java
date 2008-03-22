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

package ch.akuhn.blocks;

import java.util.Collection;


public interface MCollection<T> extends Collection<T> {

	public <V> Collection<V> collect(Function<V,T> block);
	
	public Collection<T> select(Predicate<T> block);

	public Collection<T> reject(Predicate<T> block);

	public T detect(Predicate<T> block);

	public <V> V inject(V initialValue, BinaryFunction<V,V,T> block);

	public int count(Predicate<T> block);

	public boolean anySatisfy(Predicate<T> block);

	public boolean allSatisfy(Predicate<T> block);

	public void forEach(Procedure<T> block);
	
	public void remove(Predicate<T> block);
	
	public Collection<T> splice(Predicate<T> block);

	public T any();
	
}
