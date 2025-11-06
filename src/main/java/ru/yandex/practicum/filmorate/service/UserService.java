package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.validator.FriendValidator;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.util.Collection;

@Slf4j
@Service
public class UserService {

    private final UserStorage userStorage;

    private static final String MESSAGE_OF_ID_USER = "Некорректный Id пользователя";
    private static final String MESSAGE_OF_NULL_ID_USER = "Id пользователя не указан";

    public UserService(@Qualifier("userRepository") UserStorage userStorage) {
        this.userStorage = userStorage;
    }


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

    public Collection<User> getFriendList(Long userId) {
        log.info("Запрос на получение списка друзей, пользователя с Id = {}", userId);
        findById(userId);
        return userStorage.getFriendList(userId);
    }


    public void addFriend(long userId, long friendId) {
        log.info("Пользователь с ID = {} пытается добавить в друзья пользователя с ID = {}",
                userId, friendId);
        FriendValidator.validateFriend(userId, friendId, userStorage);
        log.info("Проверка прошла успешно, размер списка друзей пользователя - {}",
                userStorage.getFriendList(userId));
        userStorage.addFriend(userId, friendId);
        log.info("Друг добавлен в список пользователя, размер списка - {}",
                userStorage.getFriendList(userId));
    }

    public void deleteFriend(long userId, long friendId) {
        log.info("Пользователь с ID = {} пытается удалить из друзей пользователя с ID = {}",
                userId, friendId);
        FriendValidator.validateFriend(userId, friendId, userStorage);
        log.info("Проверка прошла успешно");
        userStorage.deleteFriend(userId, friendId);
        log.info("Друг удален из списка друзей пользователя, размер списка - {}",
                userStorage.getFriendList(userId));
    }

    public Collection<User> findCommonFriends(Long userId, Long anotherUserId) {
        log.info("Поиск общих друзей пользователей с ID 1 = {} и ID 2 = {}", userId, anotherUserId);
        FriendValidator.validateFriend(userId, anotherUserId, userStorage);
        return userStorage.findCommonFriends(userId, anotherUserId);
    }

    public void deleteUser(Long id) {
        userStorage.deleteUser(id);
    }

    public void deleteAll() {
        userStorage.deleteAll();
    }
}
