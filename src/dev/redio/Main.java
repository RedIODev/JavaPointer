package dev.redio;

import java.lang.invoke.VarHandle;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import dev.redio.pointer.AbstractNativeStruct;
import dev.redio.pointer.Pointer;

public class Main {
    public static void main(String[] args) {
        // System.out.println("while Test:");
        // for (Integer integer : Test.whileTest()) {
        //     System.out.println(integer);
        // }
        // IntStruct is = new IntStruct();
        // Pointer<IntStruct> ptr = new Pointer<>(null, null, is);
        // ptr.deref().setInt(5);
        // System.out.println(ptr.deref().getInt());
    }
}

//Cumbersome new Idea for NativeStruct Layout and ExactCopy Factory
class IntStruct extends AbstractNativeStruct<IntStruct> {

    @Override
    protected IntStruct exactStruct(List<VarHandle> handles) {
        return null;
    }

    int getInt() {
        return 0;
    }

    void setInt(int i) {

    }

}