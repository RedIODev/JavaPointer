package dev.redio.pointer;

import java.util.Objects;

public final class HeapPointer<T> implements Pointer<T> {

    private final T ref;

    public HeapPointer(T value) {
        ref = value;
    }

    @Override
    public Modifier<T> modify() {
        return new HeapModifier<>(ref);
    }

    @Override
    public Class<?> getType() {
        Objects.requireNonNull(ref);
        return ref.getClass();
    }

    @Override
    public long getAddress() {
        Objects.requireNonNull(ref);
        return Utils.rawAddressOf(ref);
    }

    @Override
    public T get() {
        return ref;
    }

    @Override
    public void close() {}
    
}
