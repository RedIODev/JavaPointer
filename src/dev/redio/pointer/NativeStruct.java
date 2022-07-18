package dev.redio.pointer;

import java.lang.invoke.VarHandle;
import java.util.List;

public class NativeStruct extends AbstractNativeStruct<NativeStruct> {

    public NativeStruct(Class<?>... types) {
        super(types);
    }

    NativeStruct(List<VarHandle> handles) {
        super(handles);
    }

    @Override
    protected NativeStruct exactStruct(List<VarHandle> handles) {
        return new NativeStruct(handles);
    }
    
}
