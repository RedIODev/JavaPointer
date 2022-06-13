package dev.redio.pointer;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public final class Keywords {
    
    private Keywords() {}

    public static NativePointer<Void> malloc(long bytes) {
        return new NativePointer<>(Utils.getUnsafe().allocateMemory(bytes), null);
    }

    public static NativePointer<Void> calloc(long number, long size) {
        var bytes = number * size;
        var ptr = new NativePointer<Void>(Utils.getUnsafe().allocateMemory(bytes), null);
        Utils.memset(ptr.address, bytes, (byte) 0);
        return ptr;
    }

    public static <T> NativePointer<T> realloc(NativePointer<T> oldPtr, long bytes) {
        long newAddress = Utils.getUnsafe().reallocateMemory(oldPtr.address, bytes);
        oldPtr.address = 0;
        return new NativePointer<>(newAddress, oldPtr.size, oldPtr.typeId);
    }

    public static void free(NativePointer<?> ptr) {
        Utils.getUnsafe().freeMemory(ptr.address);
        ptr.address = 0;
    }

    public static long sizeof(Object obj) {
        return ObjectSizeCalculator.calculateSize(obj);
    }

    public static long sizeof(Class<?> type) {
        return ObjectSizeCalculator.calculateSize(type);
    }

    public static <T> HeapPointer<T> pointerTo(T obj) {
        return new HeapPointer<>(obj);
    }

    public static <T> NativePointer<T> cast(long address, Class<? super T> type) {
        return new NativePointer<>(address,type);
    }

    public static <T> NativePointer<T> newN(Class<? super T> type) {
        NativePointer<T> ptr = allocateMemory(type,1);
        if(type.isPrimitive()) {
            Utils.memset(ptr.address,sizeof(type),(byte) 0);
            return ptr;
        }
        Utils.requireNativeObject(type);
        initializeNativeObject(ptr);
        return ptr;
    }

    public static <T> NativePointer<T> newN(Class<? super T> type, T value) {
        Utils.requireNativeObject(type);
        NativePointer<T> ptr = allocateMemory(type,1);
        ptr.set(value);
        return ptr;
    }

    public static void delete(NativePointer<?> ptr) {
        ptr.close();
    }

    private static <T> Constructor<T> getConstructor(Class<T> type) {
        try {
            return type.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> void initializeNativeObject(NativePointer<T> ptr) {
        try {
            T value = getConstructor((Class<T>)NativePointer.typeCache.get(ptr.typeId)).newInstance();
            ptr.set(value);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static <T> NativePointer<T> allocateMemory(Class<? super T> type, long size) {
        return new NativePointer<>(Utils.getUnsafe().allocateMemory(sizeof(type) * size),type);
    }


}
