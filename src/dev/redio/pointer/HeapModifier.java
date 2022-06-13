package dev.redio.pointer;

import java.util.Objects;

public final class HeapModifier<T> extends Modifier<T> {
    
    private final T ref;

    public HeapModifier(T object) {
        super(object);
        Objects.requireNonNull(object);
        ref = object;
    }

    @Override
    public void close() {
        Utils.memcopy(value, 0, ref, 0, Keywords.sizeof(ref));
    }
}
