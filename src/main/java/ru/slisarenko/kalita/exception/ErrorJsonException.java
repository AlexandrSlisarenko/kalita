package ru.slisarenko.kalita.exception;

public class ErrorJsonException extends RuntimeException {
    public ErrorJsonException(String message) {
        super(message);
    }
}
