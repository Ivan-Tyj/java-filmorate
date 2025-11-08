package ru.yandex.practicum.filmorate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    ErrorResponse handleValidationException(final ValidationException e) {
        return new ErrorResponse("Ошибка валидации: " + e.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler
    ErrorResponse handleNotFoundException(final NotFoundException e) {
        return new ErrorResponse("Объект не найден: " + e.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    ErrorResponse handleInternalServerError(final Exception e) {
        return new ErrorResponse("Ошибка сервера:" + e.getMessage());
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ExceptionHandler
    ErrorResponse handleNullContent(final NullContentException e) {
        return new ErrorResponse("Действие не требуется:" + e.getMessage());
    }

    record ErrorResponse(String error) {
    }
}

