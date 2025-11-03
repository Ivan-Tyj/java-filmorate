package ru.yandex.practicum.filmorate.dal;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;


public interface UserStorage {

    Collection<User> findAll();

    User findById(long id);

    User create(User user);

    User update(User user);

    Collection<User> getFriendList(Long userId);

    void addFriend(Long userId, Long friendId);

    void deleteFriend(Long userId, Long friendId);

    void deleteUser(Long userId);

    boolean containUser(Long userId);

    Collection<User> findCommonFriends(Long userId, Long anotherUserId);
}