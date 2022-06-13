package dev.redio.pointer.exceptions;

public class PointerTypeNotSetException extends RuntimeException {
    public PointerTypeNotSetException() {
        super("The type of the pointer is not set or invalid.");
    }

    public PointerTypeNotSetException(String message) {
        super(message);
    }

    public PointerTypeNotSetException(String message, Throwable cause) {
        super(message, cause);
    }

    public PointerTypeNotSetException(Throwable cause) {
        super(cause);
    }
}
