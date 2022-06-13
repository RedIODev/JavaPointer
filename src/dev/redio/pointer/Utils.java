package dev.redio.pointer;

import java.io.InvalidClassException;
import java.util.Arrays;

import dev.redio.pointer.exceptions.InvalidPointerException;
import dev.redio.pointer.exceptions.PointerTypeNotSetException;
import sun.misc.Unsafe;

public final class Utils {

    private Utils() {
    }

    private static final Unsafe theUnsafe;

    static {
        Unsafe unsafe;
        try {
            var instance = Unsafe.class.getDeclaredField("theUnsafe");
            instance.setAccessible(true);
            unsafe = (Unsafe) instance.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            unsafe = null;
        }
        theUnsafe = unsafe;
    }

    public static Unsafe getUnsafe() {
        return theUnsafe;
    }

    public static long rawAddressOf(Object obj) {
        var array = new Object[] { obj };
        long baseOffset = theUnsafe.arrayBaseOffset(Object[].class);
        return normalize(theUnsafe.getInt(array, baseOffset));
    }

    public static void memcopy(Object src, long srcOffset, Object dest, long destOffset, long bytes) {
        for (int i = 0; i < bytes; i++)
            theUnsafe.putByte(dest, destOffset + i, theUnsafe.getByte(src, srcOffset + i));
    }

    public static void memcopy(Object src, long srcOffset, long destAddress, long bytes) {
        for (int i = 0; i < bytes; i++)
            theUnsafe.putByte(destAddress + i, theUnsafe.getByte(src, srcOffset + i));
    }

    public static void memcopy(long srcAddress, Object dest, long destOffset, long bytes) {
        for (int i = 0; i < bytes; i++)
            theUnsafe.putByte(dest, destOffset + i, theUnsafe.getByte(srcAddress + i));
    }

    public static void memcopy(long scrAddress, long destAddress, long bytes) {
        for (int i = 0; i < bytes; i++)
            theUnsafe.putByte(destAddress + i, theUnsafe.getByte(scrAddress + i));
    }

    public static void memset(long address, long bytes, byte value) {
        theUnsafe.setMemory(address, bytes, value);
    }

    public static void requireValidPointer(NativePointer<?> ptr) {
        if(ptr.address == 0)
            throw new InvalidPointerException(new NullPointerException());
        if(ptr.typeId == 0)
            throw new InvalidPointerException(new PointerTypeNotSetException());
    }

    public static void requireNativeObject(Object obj) {
        requireNativeObject(obj.getClass());
    }

    public static void requireNativeObject(Class<?> type) {
        if(type.isAnnotationPresent(NativeObject.class))
            return;
        if(Arrays.stream(type.getDeclaredFields()).anyMatch(field -> !field.getType().isPrimitive()))
            throw new RuntimeException(new InvalidClassException(
                    "Class cannot be stored in native memory because it contains object references."));
        var superClass = type.getSuperclass();
        if(superClass == null)
            return;
        requireNativeObject(superClass);
    }



    private static long normalize(int value) {
        if (value >= 0)
            return value;
        return (~0L >>> 32) & value;
    }
}
