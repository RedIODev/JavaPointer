package dev.redio.newpointer;

import java.lang.invoke.VarHandle;

import jdk.incubator.foreign.*;

public abstract class AbstractDerefType implements Deref {
    private NativeType type;
    private MemorySegment data;
    
    @Override
    public VarHandle[] memoryAccessHandle() {
        // TODO Auto-generated method stub
        return null;
    }
}
