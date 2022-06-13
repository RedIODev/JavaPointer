package dev.redio.pointer;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

final class ObjectSizeCalculator {

    private static final Map<Class<?>, Long> typeSizeCache = new HashMap<>();

    public static final long ADDRESS_SIZE = Utils.getUnsafe().addressSize();

    private ObjectSizeCalculator() {}

    public static long calculateSize(Object obj) {
        if(obj == null)
            throw new NullPointerException("Can't get size of null.");
        return calculateSize(obj.getClass());
    }

    public static long calculateSize(Class<?> clazz) {
        if(clazz == null)
            throw new NullPointerException("Can't get size of null.");
        Long size = typeSizeCache.get(clazz);
        if(size != null)
            return size;
        long calcSize = calculateSize(clazz,0 , null);
        typeSizeCache.put(clazz,calcSize);
        return calcSize;
    }

    private static long calculateSize(Class<?> type, long offset, Field lastField) {
        if (type.isPrimitive()) {
            return getPrimitiveSizes(type);
        }
        var fields = type.getDeclaredFields();

        for (var field : fields) {
            if((field.getModifiers() & java.lang.reflect.Modifier.STATIC) == 0) {
                long fieldOffset = Utils.getUnsafe().objectFieldOffset(field);
                if(fieldOffset > offset) {
                    offset = fieldOffset;
                    lastField = field;
                }
            }
        }
        var superClass = type.getSuperclass();
        if(superClass != null)
            return calculateSize(superClass, offset, lastField);
        long finalSize = 0;
        if(lastField != null) {
            var fieldClass = lastField.getType();
            if (fieldClass.isPrimitive())
                finalSize = getPrimitiveSizes(fieldClass);
             else
                finalSize = 4;
            finalSize += offset;
        }
        long overhead = finalSize % ADDRESS_SIZE;
        if(overhead==0)
            return finalSize;
        return finalSize + ADDRESS_SIZE - overhead;
    }

    private static long getPrimitiveSizes(Class<?> type) {
        if (type == byte.class || type == boolean.class)
            return 1;
        else if (type == short.class || type == char.class)
            return 2;
        else if (type == int.class || type == float.class)
            return 4;
        else if (type == long.class || type == double.class)
            return 8;
        else
            throw new IllegalStateException("Unknown primitive!");
    }
}
