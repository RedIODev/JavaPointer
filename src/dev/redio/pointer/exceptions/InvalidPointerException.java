package dev.redio.pointer.exceptions;

public class InvalidPointerException extends RuntimeException {

    public InvalidPointerException() {
    }

    public InvalidPointerException(String message) {
        super(message);
    }

    public InvalidPointerException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidPointerException(Throwable cause) {
        super(cause);
    }
}
