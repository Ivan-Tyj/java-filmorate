package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserStorage userStorage;

    private static final String MESSAGE_OF_VALID_EMAIL_EMPTY = "Электронная почта не может быть пустой";
    private static final String MESSAGE_OF_VALID_EMAIL_SYMBOL = "Символ @ - не передан";
    private static final String MESSAGE_OF_VALID_LOGIN_EMPTY = "Логин не может быть пустой";
    private static final String MESSAGE_OF_VALID_LOGIN_BLANK = "Логин не может состоять из пробелов";
    private static final String MESSAGE_OF_VALID_LOGIN_SPACE = "Логин не может содержать пробелов";
    private static final String MESSAGE_OF_VALID_BIRTHDAY = "Дата рождения не может быть в будущем";
    private static final String MESSAGE_OF_ID_USER = "Некорректный Id пользователя";
    private static final String MESSAGE_OF_NULL_ID_USER = "Id пользователя не указан";
    private static final String MESSAGE_OF_NOT_FOUND_USER = "Пользователь не найден";
    private static final String MESSAGE_OF_INVALID_BY_MYSELF = "Id пользователя и друга совпадают";


    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public User findById(long id) {
        if (id <= 0) {
            throw new ValidationException(MESSAGE_OF_ID_USER);
        }
        if (!userStorage.containUser(id)) {
            throw new NotFoundException("Пользователь с id = " + id + " не найден");
        }
        return userStorage.findById(id);
    }

    public User create(User user) {
        validateUser(user);
        return userStorage.create(user);
    }

    public User update(User user) {
        if (user.getId() == null) {
            log.debug("ID не указан при обновлении");
            throw new ValidationException(MESSAGE_OF_NULL_ID_USER);
        }
        if (user.getId() < 0) {
            log.error(MESSAGE_OF_ID_USER + " - {}", user.getId());
            throw new RuntimeException(MESSAGE_OF_ID_USER);
        }
        if (!userStorage.containUser(user.getId())) {
            throw new NotFoundException("Пользователь с id = " + user.getId() + " не найден");
        }
        validateUser(user);
        return userStorage.update(user);
    }

    public void deleteAll() {
        userStorage.deleteAll();
    }

    public void addFriend(long userId, long idFriend) {
        validateFriend(userId, idFriend);

        userStorage.findById(userId).addFriend(idFriend);
        log.debug("Друг добавлен в список пользователя, размер списка - {}",
                userStorage.findById(userId).getFriends());

        userStorage.findById(idFriend).addFriend(userId);
        log.debug("Пользователь добавлен в список друга, размер списка - {}",
                userStorage.findById(idFriend).getFriends());
    }

    public void deleteFriend(long userId, long idFriend) {
        validateFriend(userId, idFriend);
        userStorage.findById(userId).deleteFriend(idFriend);
        userStorage.findById(idFriend).deleteFriend(userId);
    }

    public Collection<User> findAllFriends(Long userId) {
        if (!userStorage.containUser(userId)) {
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }
        Collection<Long> userFriends = userStorage.findById(userId).getFriends();
        log.debug("Список друзей получен, размер - {}", userFriends.size());
        return userFriends.stream()
                .map(userStorage::findById)
                .collect(Collectors.toList());
    }

    public Collection<User> findCommonFriends(long userId, long anotherUserId) {
        validateFriend(userId, anotherUserId);
        Set<Long> userFriends = userStorage.findById(userId).getFriends();
        Set<Long> anotherUserFriends = userStorage.findById(anotherUserId).getFriends();
        Collection<User> commons = new ArrayList<>();

        for (Long userFriend : userFriends) {
            for (Long anotherUserFriend : anotherUserFriends) {
                if (Objects.equals(userFriend, anotherUserFriend)) {
                    commons.add(userStorage.findById(userFriend));
                    log.debug("Общий друг найден и добавлен в список, размер списка - {}", commons.size());
                }
            }
        }
        return commons;
    }

    private void validateUser(User user) {
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
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Дата рождения находится в будущем - {}", user.getBirthday());
            throw new ValidationException(MESSAGE_OF_VALID_BIRTHDAY);
        }
        if (user.getName() == null || user.getName().isEmpty() || user.getName().isBlank()) {
            log.debug("Имя пользователя пустое, будет использован логин - {}", user.getLogin());
            user.setName(user.getLogin());
        }
    }

    private void validateFriend(Long userId, Long idFriend) {
        if (!userStorage.containUser(userId)) {
            throw new NotFoundException(MESSAGE_OF_NOT_FOUND_USER);
        }
        if (!userStorage.containUser(idFriend)) {
            throw new NotFoundException(MESSAGE_OF_NOT_FOUND_USER);
        }
        if (userId.equals(idFriend)) {
            throw new ValidationException(MESSAGE_OF_INVALID_BY_MYSELF);
        }
    }
}
