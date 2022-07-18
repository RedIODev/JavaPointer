package dev.redio;

import java.lang.invoke.VarHandle;
import java.util.List;

import dev.redio.pointer.AbstractNativeStruct;
import dev.redio.pointer.Pointer;

public class Main {
    
}

//Cumbersome new Idea for NativeStruct Layout and ExactCopy Factory
class IntStruct extends AbstractNativeStruct<IntStruct> {

    @Override
    protected IntStruct exactStruct(List<VarHandle> handles) {
        return 
    }

}