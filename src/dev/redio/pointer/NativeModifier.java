package dev.redio.pointer;

public final class NativeModifier<T> extends Modifier<T> {

    private final NativePointer<T> ptr;

    public NativeModifier(NativePointer<T> ptr, int offset) {
        super(ptr.get(offset));
        ptr = new NativePointer<>(ptr);
        ptr.address += ptr.size * offset;
        this.ptr = ptr;
    }

    @Override
    public void close() {
        ptr.set(value);
    }
    
}
