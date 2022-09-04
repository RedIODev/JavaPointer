package dev.redio.newpointer;

import java.lang.invoke.VarHandle;

import jdk.incubator.foreign.*;

public final class NativeType {
    
    private final MemoryLayout layout;
    private final VarHandle[] fieldHandle;

    protected NativeType(MemoryLayout layout) {
        this.layout = layout;
        this.fieldHandle = calculateFieldHandle(layout);
    }

    public VarHandle[] fieldHandle() {
        return fieldHandle.clone();
    }

    private static VarHandle[] calculateFieldHandle(MemoryLayout layout) {
        return null;
    }
}
