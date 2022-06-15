package dev.redio.pointer;


public interface Pointer<T> extends Modifiable<T>, AutoCloseable {

    Class<?> getType();

    long getAddress();

    T get();

    void set(T value);

}