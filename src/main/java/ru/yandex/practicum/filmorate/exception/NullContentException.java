package ru.yandex.practicum.filmorate.exception;

public class NullContentException extends RuntimeException {
    public NullContentException(String message) {
        super(message);
    }
}