package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Integer, User> users = new HashMap<>();

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private static final String MESSAGE_OF_VALID_EMAIL_EMPTY = "Электронная почта не может быть пустой";
    private static final String MESSAGE_OF_VALID_EMAIL_SYMBOL = "Символ @ - не передан";
    private static final String MESSAGE_OF_VALID_LOGIN_EMPTY = "Логин не может быть пустой";
    private static final String MESSAGE_OF_VALID_LOGIN_BLANK = "Логин не может состоять из пробелов";
    private static final String MESSAGE_OF_VALID_LOGIN_SPACE = "Логин не может содержать пробелов";
    private static final String MESSAGE_OF_VALID_BIRTHDAY = "Дата рождения не может быть в будущем";
    private static final String MESSAGE_OF_ID_USER = "Некорректный Id пользователя";

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        validateUser(user);
        user.setId(getNextId());
        log.debug("Id пользователя - {}", user.getId());
        log.debug("Общее количество пользователей - {}", users.size());
        users.put(user.getId(), user);
        log.debug("Новый пользователь добавлен ,общее количество пользователей - {}", users.size());
        return user;
    }

    private int getNextId() {
        int currentMaxId = users.keySet()
                .stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    private void validateUser(User user) {
        if (user.getEmail().isBlank()) {
            log.error("Передано пустое значение эл. почты - {}", user.getEmail());
            throw new ValidationException(MESSAGE_OF_VALID_EMAIL_EMPTY);
        }
        if (!user.getEmail().contains("@")) {
            log.error("В наименование email отсутствует символ @ - {}", user.getEmail());
            throw new ValidationException(MESSAGE_OF_VALID_EMAIL_SYMBOL);
        }

        if (user.getLogin().isEmpty()) {
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

        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Дата рождения находится в будущем - {}", user.getBirthday());
            throw new ValidationException(MESSAGE_OF_VALID_BIRTHDAY);
        }

        if (user.getName() == null || user.getName().isBlank()) {
            log.debug("Имя пользователя пустое, будет использован логин - {}", user.getLogin());
            user.setName(user.getLogin());
        }
    }

    @PutMapping
    public User update(@RequestBody User user) {
        // проверяем необходимые условия
        if (user.getId() < 0 || user.getId() > users.size()) {
            log.error("Некорректный Id пользователя - {}", user.getId());
            throw new ValidationException(MESSAGE_OF_ID_USER);
        }
        if (users.containsKey(user.getId())) {
            validateUser(user);
            users.put(user.getId(), user);
            log.debug("Фильм обновлен");
        } else {
            throw new ValidationException("Пользователь с id = " + user.getId() + " не найден");
        }
        return user;
    }

    public void clearAllUsers() {
        users.clear();
    }
}
