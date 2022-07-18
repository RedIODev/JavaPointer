package dev.redio.pointer;

import java.lang.invoke.VarHandle;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import jdk.incubator.foreign.*;

public abstract class AbstractNativeStruct<SELF extends AbstractNativeStruct<SELF>> {
    
    private final List<VarHandle> handles;
    private final boolean isExact;

    protected AbstractNativeStruct(Class<?>... types) {
        Objects.requireNonNull(types);
        this.handles = createHandles(types, createLayout(types));
        this.isExact = false;
    }

    /**
     * Creates a NativeStruct instance with exact varHandles already backed by a MemorySegment.
     * @param handles
     */
    protected AbstractNativeStruct(List<VarHandle> handles) {
        this.handles = handles;
        this.isExact = true;
    }

    public List<VarHandle> fieldHandles() {
        return Collections.unmodifiableList(handles);
    }

    public boolean isExact() {
        return this.isExact;
    }

    protected abstract SELF exactStruct(List<VarHandle> handles);

    private static GroupLayout createLayout(Class<?>[] types) {
        return null;
    }

    private static List<VarHandle> createHandles(Class<?>[] types, GroupLayout structLayout) {
        return null;
    }
    
}
