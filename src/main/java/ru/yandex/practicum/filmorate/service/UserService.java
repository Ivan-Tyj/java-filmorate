package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
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

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public User findById(long id) {
        if (id <= 0 || id > userStorage.getUsersSize()) {
            throw new ValidationException(MESSAGE_OF_ID_USER);
        }
        return userStorage.findById(id);
    }

    public User create(User user) {
        validateUser(user);
        return userStorage.create(user);
    }

    public User update(User user) {
        if (user.getId() < 0 || user.getId() > userStorage.getUsersSize()) {
            log.error(MESSAGE_OF_ID_USER + " - {}", user.getId());
            throw new RuntimeException(MESSAGE_OF_ID_USER);
        }
        if (findById(user.getId()) == null) {
            throw new ValidationException("Пользователь с id = " + user.getId() + " не найден");
        }
        validateUser(user);
        return userStorage.update(user);
    }

    public void deleteAll() {
        userStorage.deleteAll();
    }

    public Set<Long> addFriend(long userId, long idFriend) {
        Set<Long> userFriends = userStorage.findById(userId).getFriends();
        log.debug("Список пользователя получен, размер - {}", userFriends.size());

        Set<Long> friendFriends = userStorage.findById(idFriend).getFriends();
        log.debug("Список друзей друга получен, размер - {}", userFriends.size());

        userFriends.add(idFriend);
        log.debug("Друг добавлен в список пользователя, размер списка - {}", userFriends.size());
        userStorage.findById(userId).setFriends(userFriends);
        log.debug("Список друзей пользователя обновлен, размер - {}",
                userStorage.findById(userId).getFriends().size());

        friendFriends.add(userId);
        log.debug("Пользователь добавлен в список друга, размер списка - {}", friendFriends.size());
        userStorage.findById(idFriend).setFriends(friendFriends);
        log.debug("Список друзей друга обновлен, размер - {}",
                userStorage.findById(idFriend).getFriends().size());
        return userFriends;
    }

    public Set<Long> deleteFriend(long userId, long idFriend) {
        Set<Long> userFriends = userStorage.findById(userId).getFriends();
        log.debug("Список друзей получен, размер - {}", userFriends.size());
        if (userStorage.findById(idFriend) != null) {
            userFriends.remove(idFriend);
            log.debug("Друг удален, размер списка - {}", userFriends.size());
        }
        userStorage.findById(userId).setFriends(userFriends);
        log.debug("Список друзей обновлен, размер - {}", userStorage.findById(userId).getFriends().size());
        return userFriends;
    }

    public Collection<User> findAllFriends(long userId) {
        Set<Long> userFriends = userStorage.findById(userId).getFriends();
        log.debug("Список друзей получен, размер - {}", userFriends.size());
        return userFriends.stream()
                .map(userStorage::findById)
                .collect(Collectors.toList());
    }

    public Collection<User> findCommonFriends(long userId, long anotherUserId) {
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
}
