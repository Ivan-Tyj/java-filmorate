package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

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
}
