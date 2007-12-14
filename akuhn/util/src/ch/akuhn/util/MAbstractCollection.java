//  Copyright (c) 1998-2007 Adrian Kuhn <akuhn(a)iam.unibe.ch>
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

import java.util.AbstractCollection;
import java.util.Collection;

public abstract class MAbstractCollection<T> extends AbstractCollection<T> implements
		MCollection<T> {

	@Override
	public boolean allSatisfy(Predicate<T> block) {
		return Magic.allSatisfy(this, block);
	}

	@Override
	public T any() {
		return Magic.any(this);
	}

	@Override
	public boolean anySatisfy(Predicate<T> block) {
		return Magic.anySatisfy(this, block);
	}

	@Override
	public <V> Collection<V> collect(Function<V,T> block) {
		return Magic.collect(this, block);
	}

	@Override
	public int count(Predicate<T> block) {
		return Magic.count(this, block);
	}

	@Override
	public T detect(Predicate<T> block) {
		return Magic.detect(this, block);
	}

	@Override
	public void forEach(Procedure<T> block) {
		Magic.forEach(this, block);
	}

	@Override
	public <V> V inject(V initialValue, BinaryFunction<V,V,T> block) {
		return Magic.inject(this, initialValue, block);
	}

	@Override
	public Collection<T> reject(Predicate<T> block) {
		return Magic.reject(this, block);
	}

	@Override
	public void remove(Predicate<T> block) {
		Magic.remove(this, block);
	}

	@Override
	public Collection<T> select(Predicate<T> block) {
		return Magic.select(this, block);
	}

	@Override
	public Collection<T> splice(Predicate<T> block) {
		return Magic.splice(this, block);
	}

}
