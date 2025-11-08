package ru.yandex.practicum.filmorate.storage;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();

    @Getter
    private long maxIdUsers = 0;

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    public User findById(long id) {
        return users.get(id);
    }

    @Override
    public User create(User user) {
        user.setId(++maxIdUsers);
        log.debug("Id пользователя - {}", user.getId());
        log.debug("Общее количество пользователей - {}", users.size());
        users.put(user.getId(), user);
        log.debug("Новый пользователь добавлен ,общее количество пользователей - {}", users.size());
        return users.get(user.getId());
    }

    @Override
    public User update(User user) {
        users.put(user.getId(), user);
        log.debug("Пользователь обновлен");
        return users.get(user.getId());
    }

    public void deleteAll() {
        log.debug("Общее количество пользователей - {}", users.size());
        if (!users.isEmpty()) {
            users.clear();
            maxIdUsers = 0;
            log.debug("Список пользователей пуст - {}", 0);
        }
    }

    @Override
    public boolean containUser(Long id) {
        return users.containsKey(id);
    }

    @Override
    public List<User> findCommonFriends(Long userId, Long anotherUserId) {
        throw new RuntimeException("Not Implements");
    }

    @Override
    public List<User> getFriendList(Long userId) {
        throw new RuntimeException("Not Implements");
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        throw new RuntimeException("Not Implements");
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {
        throw new RuntimeException("Not Implements");
    }

    @Override
    public void deleteUser(Long userId) {
        throw new RuntimeException("Not Implements");
    }
}
