package ru.yandex.practicum.filmorate.validator;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Slf4j
public final class UserValidator {

    private static final String MESSAGE_OF_VALID_EMAIL_EMPTY = "Электронная почта не может быть пустой";
    private static final String MESSAGE_OF_VALID_EMAIL_SYMBOL = "Символ @ - не передан";
    private static final String MESSAGE_OF_VALID_LOGIN_EMPTY = "Логин не может быть пустой";
    private static final String MESSAGE_OF_VALID_LOGIN_BLANK = "Логин не может состоять из пробелов";
    private static final String MESSAGE_OF_VALID_LOGIN_SPACE = "Логин не может содержать пробелов";
    private static final String MESSAGE_OF_VALID_BIRTHDAY = "Дата рождения не может быть в будущем";
    private static final String MESSAGE_OF_NULL_BIRTHDAY = "Дата рождения не указана";

    public static void validateUser(User user) {
        if (user.getEmail() == null || user.getEmail().isEmpty() || user.getEmail().isBlank()) {
            log.error("Передано пустое значение эл. почты - {}", user.getEmail());
            throw new ValidationException(MESSAGE_OF_VALID_EMAIL_EMPTY);
        }
        if (!user.getEmail().contains("@")) {
            log.error("В наименование email отсутствует символ @ - {}", user.getEmail());
            throw new ValidationException(MESSAGE_OF_VALID_EMAIL_SYMBOL);
        }

        if (user.getLogin() == null || user.getLogin().isEmpty()) {
            log.error("Логин пустой - {}", user.getLogin());
            throw new ValidationException(MESSAGE_OF_VALID_LOGIN_EMPTY);
        }
        if (user.getLogin().isBlank()) {
            log.error("Логин состоит из пробелов - {}", user.getLogin());
            throw new ValidationException(MESSAGE_OF_VALID_LOGIN_BLANK);
        }
        if (user.getLogin().contains(" ")) {
            log.error("Логин содержит пробелы - {}", user.getLogin());
            throw new ValidationException(MESSAGE_OF_VALID_LOGIN_SPACE);
        }

        if (user.getBirthday() == null) {
            log.error(MESSAGE_OF_NULL_BIRTHDAY);
            throw new ValidationException(MESSAGE_OF_NULL_BIRTHDAY);
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Дата рождения находится в будущем - {}", user.getBirthday());
            throw new ValidationException(MESSAGE_OF_VALID_BIRTHDAY);
        }
        if (user.getName() == null || user.getName().isEmpty() || user.getName().isBlank()) {
            log.debug("Имя пользователя пустое, будет использован логин - {}", user.getLogin());
            user.setName(user.getLogin());
        }
    }
}
