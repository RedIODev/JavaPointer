package dev.redio.newpointer;

import java.lang.invoke.VarHandle;

public interface Deref {
    
    VarHandle[] memoryAccessHandle();
}
