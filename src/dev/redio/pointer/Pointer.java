package dev.redio.pointer;

import java.lang.invoke.VarHandle;
import java.util.List;
import java.util.function.Function;

import jdk.incubator.foreign.*;

public final class Pointer<T extends AbstractNativeStruct<T>> implements Addressable {

    private final MemoryLayout layout;
    private final MemorySegment data;
    private final T struct;

    public Pointer(MemoryLayout layout, MemorySegment data, T struct) {
        this.layout = layout;
        this.data = data;
        this.struct = struct;
    }

    public T deref() {
        if (layout instanceof SequenceLayout)
            throw new UnsupportedOperationException("This Pointer points to an array. Use the indexed overload instead.");
        return struct.exactStruct(null);
    }

    public T deref(long index) {
        if (!(layout instanceof SequenceLayout))
            throw new UnsupportedOperationException("This Pointer doesn't point to an array. Use the non indexed overload instead.");
        return struct.exactStruct(null);
    }

    @Override
    public MemoryAddress address() {
        return data.address();
    }

}
