package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;


public interface UserStorage {

    Collection<User> findAll();

    User findById(long id);

    User create(User user);

    User update(User user);

    List<User> getFriendList(Long userId);

    void addFriend(Long userId, Long friendId);

    void deleteFriend(Long userId, Long friendId);

    void deleteUser(Long userId);

    boolean containUser(Long userId);
}