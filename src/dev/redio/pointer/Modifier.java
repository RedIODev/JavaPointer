package dev.redio.pointer;

public abstract class Modifier<T> implements AutoCloseable {
    public T value;

    protected Modifier(T value) {
        this.value = value;
    }
}
