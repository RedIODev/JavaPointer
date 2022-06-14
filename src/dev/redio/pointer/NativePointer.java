package dev.redio.pointer;


import java.util.ArrayList;
import java.util.List;

@NativeObject
public class NativePointer<T> implements Pointer<T> {

    static final List<Class<?>> typeCache = new ArrayList<>();

    static {
        typeCache.add(void.class);
        typeCache.add(boolean.class);
        typeCache.add(byte.class);
        typeCache.add(char.class);
        typeCache.add(double.class);
        typeCache.add(float.class);
        typeCache.add(int.class);
        typeCache.add(long.class);
        typeCache.add(short.class);
    }

    protected long address = 0;
    protected long size = 0;
    protected int typeId = 0;

    public NativePointer(long address, Class<? super T> type) {
        this.address = address;
        if(type == null || type == void.class)
            return;
        this.size = Keywords.sizeof(type);
        int index = typeCache.indexOf(type);
        if(index == -1) {
            typeCache.add(type);
            typeId = typeCache.size()-1;
        } else
            typeId = index;
    }

    protected NativePointer(long address, long size, int typeId) {
        this.address = address;
        this.size = size;
        this.typeId = typeId;
    }

    public NativePointer(NativePointer<T> copy) {
        this.address = copy.address;
        this.size = copy.size;
        this.typeId = copy.typeId;
    }
    
    @Override
    public long getAddress() {
        return address;
    }

    @Override
    public Class<?> getType() {
        return typeCache.get(this.typeId);
    }


    public <U> NativePointer<U> cast(Class<? super U> type) {
        return new NativePointer<>(address,type);
    }

    @Override
    public Modifier<T> modify() {
        return new NativeModifier<>(this);
    }

    protected Modifier<T> modify(int offset) {
        return new NativeModifier<>(this, offset);
    }

    public T get() {
        return get(0);
    }

    @SuppressWarnings("unchecked")
    protected T get(int offset) {
        Utils.requireValidPointer(this);
        long byteOffset = offset * size;
        if(typeId <= 8)
            return primitiveGet(byteOffset);
        T heapCopy;
        try {
            heapCopy = (T) Utils.getUnsafe().allocateInstance(typeCache.get(typeId));
        } catch (InstantiationException e) {
            throw new Error(e);
        }
        Utils.memcopy(address + byteOffset, heapCopy,0,size);
        return heapCopy;
    }

    public void set(T value) {
        set(value,0);
    }

    protected void set(T value, int offset) {
        Utils.requireValidPointer(this);
        long byteOffset = offset * size;
        if(typeId <= 8) {
            primitiveSet(value, byteOffset);
            return;
        }
        Utils.memcopy(value,0,address + byteOffset, size);
    }

    public void inc() {
        address += size;
    }

    public void dec() {
        address -= size;
    }

    public void add(long value) {
        address += value * size;
    }

    public void sub(long value) {
        address -= value * size;
    }

    @Override
    public void close() {
        if (get() instanceof Destructible destructible)
            destructible.destructor();
        Keywords.free(this);
    }

   

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        return obj instanceof Pointer<?> nPtr && this.address == nPtr.getAddress();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (address ^ (address >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "0x" + Long.toHexString(address);
    }

    
    @SuppressWarnings("unchecked")
    private T primitiveGet(long offset) {
        var unsafe = Utils.getUnsafe();
        return switch (typeId) {
            case 1 -> (T)(Boolean) (unsafe.getByte(address + offset) != 0);
            case 2 -> (T)(Byte) unsafe.getByte(address + offset);
            case 3 -> (T)(Character) unsafe.getChar(address + offset);
            case 4 -> (T)(Double) unsafe.getDouble(address + offset);
            case 5 -> (T)(Float) unsafe.getFloat(address + offset);
            case 6 -> (T)(Integer) unsafe.getInt(address + offset);
            case 7 -> (T)(Long) unsafe.getLong(address + offset);
            case 8 -> (T)(Short) unsafe.getShort(address + offset);
            default -> throw new IllegalStateException("Unknown primitive id:" + typeId);
        };
    }

    private void primitiveSet(T value, long offset) {
        var unsafe = Utils.getUnsafe();
        switch (typeId) {
            case 1 -> unsafe.putByte(address + offset, (byte) ((Boolean) value ? 1 : 0));
            case 2 -> unsafe.putByte(address + offset, (Byte) value);
            case 3 -> unsafe.putChar(address + offset, (Character) value);
            case 4 -> unsafe.putDouble(address + offset, (Double) value);
            case 5 -> unsafe.putFloat(address + offset, (Float) value);
            case 6 -> unsafe.putInt(address + offset, (Integer) value);
            case 7 -> unsafe.putLong(address + offset, (Long) value);
            case 8 -> unsafe.putShort(address + offset, (Short) value);
            default -> throw new IllegalStateException("Unknown primitive id:" + typeId);
        }
    }

    
}
