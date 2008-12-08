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

package ch.akuhn.util.blocks;

import java.util.AbstractCollection;
import java.util.Collection;

public abstract class MAbstractCollection<T> extends AbstractCollection<T> implements MCollection<T> {

    public boolean allSatisfy(Predicate<T> block) {
        return Blocks.allSatisfy(this, block);
    }

    public T any() {
        return Blocks.any(this);
    }

    public boolean anySatisfy(Predicate<T> block) {
        return Blocks.anySatisfy(this, block);
    }

    public <V> Collection<V> collect(Function<V,T> block) {
        return Blocks.collect(this, block);
    }

    public int count(Predicate<T> block) {
        return Blocks.count(this, block);
    }

    public T detect(Predicate<T> block) {
        return Blocks.detect(this, block);
    }

    public void forEach(Procedure<T> block) {
        Blocks.forEach(this, block);
    }

    public <V> V inject(V initialValue, BinaryFunction<V,V,T> block) {
        return Blocks.inject(this, initialValue, block);
    }

    public Collection<T> reject(Predicate<T> block) {
        return Blocks.reject(this, block);
    }

    public void remove(Predicate<T> block) {
        Blocks.remove(this, block);
    }

    public Collection<T> select(Predicate<T> block) {
        return Blocks.select(this, block);
    }

    public Collection<T> splice(Predicate<T> block) {
        return Blocks.splice(this, block);
    }

}
