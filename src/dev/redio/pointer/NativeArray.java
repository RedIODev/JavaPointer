package dev.redio.pointer;

import java.util.Iterator;
import java.util.Objects;

@NativeObject
public class NativeArray<T> extends NativePointer<T> implements Iterable<T> {

    public final int length;
    
    protected NativeArray(long address, Class<? super T> type, int length) {
        super(address, type);
        this.length = length;
    }

    @Override
    public Modifier<T> modify(int index) {
        return super.modify(index);
    }

    @Override
    public void close() {
        for (var e : this) 
            if (e instanceof Destructible destructible)
                destructible.destructor();
        Keywords.free(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof NativeArray))
            return false;

        var array = (NativeArray<?>) obj;

        if (this.length != array.length)
            return false;
     
        for (int i = 0; i < array.length && i < this.length; i++) 
            if (!Objects.equals(array.get(i), this.get(i)))
                return false;
        return true;
    }

    @Override
    public T get(int index) {
        return super.get(index);
    }

    @Override
    public void set(T value, int offset) {
        super.set(value, offset);
    }

    

    @Override
    public void add(long value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void dec() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void inc() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void sub(long value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Array [");
        for (int i = 0; i < this.length; i++) {
            builder.append(this.get(i).toString());
            if (i != this.length-1)
                builder.append(", ");
        }
        builder.append("]");
        return builder.toString();
    }

    @Override
    public Iterator<T> iterator() {
        class Iter implements Iterator<T> {
            int index = 0;
            @Override
            public boolean hasNext() {
                return index < NativeArray.this.length;
            }

            @Override
            public T next() {
                return NativeArray.this.get(index);
            }
        }
        return new Iter();
    }

}
