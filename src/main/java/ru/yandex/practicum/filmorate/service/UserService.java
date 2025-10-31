package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    private static final String MESSAGE_OF_ID_USER = "Некорректный Id пользователя";
    private static final String MESSAGE_OF_NULL_ID_USER = "Id пользователя не указан";
    private static final String MESSAGE_OF_NOT_FOUND_USER = "Пользователь не найден";
    private static final String MESSAGE_OF_INVALID_BY_MYSELF = "Id пользователя и друга совпадают";


    public Collection<User> findAll() {
        log.info("Запрос на получение списка пользователей");
        return userStorage.findAll();
    }

    public User findById(long id) {
        log.info("Запрос на получение пользователя по id = {}", id);
        if (id <= 0) {
            throw new ValidationException(MESSAGE_OF_ID_USER);
        }
        if (!userStorage.containUser(id)) {
            throw new NotFoundException("Пользователь с id = " + id + " не найден");
        }
        return userStorage.findById(id);
    }

    public User create(User user) {
        log.info("Запрос создания пользователя");
        UserValidator.validateUser(user);
        return userStorage.create(user);
    }

    public User update(User user) {
        log.info("Запрос на обновление информации о пользователе: {}", user);
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
        UserValidator.validateUser(user);
        return userStorage.update(user);
    }

    public void deleteAll() {
        log.info("Удаление всех пользователей");
        if (!findAll().isEmpty()) {
            userStorage.deleteAll();
        }
    }

    public List<User> getFriendList(Long userId) {
        log.info("Запрос на получение списка друзей, пользователя с Id = {}", userId);
        findById(userId);
        return userStorage.getFriendList(userId);
    }


    public void addFriend(long userId, long friendId) {
        log.info("Запрос на дружбу между {} и {}", userId, friendId);
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
