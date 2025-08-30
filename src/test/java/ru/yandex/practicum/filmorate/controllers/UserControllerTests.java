package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UserControllerTests {
    @Autowired
    private UserController userController;

    @Test
    void findAll() {
        User user = new User("user@email", "userLogin", LocalDate.now());
        userController.create(user);
        assertEquals(1, userController.findAll().size());
    }

    @Test
    void findById() {
        User user = new User("mail@", "login", LocalDate.now());
        userController.create(user);
        assertEquals(user, userController.findById(1));
    }

    @Test
    void createdUser() {
        User user = new User("mail@", "login", LocalDate.now());
        assertEquals(user, userController.create(user));
    }

    @Test
    void updateUser() {
        User user = new User("user2@email", "userLogin", LocalDate.now());
        user.setId(1);
        assertEquals(user, userController.update(user));
    }

    @Test
    void addLike() {
        User user = new User("user@mail", "user", LocalDate.of(2000, 1, 1));
        User user2 = new User("user2@email", "userLogin", LocalDate.of(2000, 1, 1));
        userController.create(user);
        userController.create(user2);
        userController.addFriend(1, 2);
        assertEquals(1, user.getFriends().size());
        assertEquals(1, user2.getFriends().size());
    }

    @Test
    void deleteFriend() {
        User user = new User("user@mail", "user", LocalDate.of(2000, 1, 1));
        User user2 = new User("user2@email", "userLogin", LocalDate.of(2000, 1, 1));
        userController.create(user);
        userController.create(user2);
        userController.addFriend(1, 2);
        userController.deleteFriend(1, 2);
        assertEquals(0, user.getFriends().size());
        assertEquals(1, user2.getFriends().size());
    }

    @Test
    void findAllFriends() {
        User user = new User("user@mail", "user", LocalDate.of(2000, 1, 1));
        User user2 = new User("user2@email", "userLogin", LocalDate.of(2000, 1, 1));
        userController.create(user);
        userController.create(user2);
        userController.addFriend(1, 2);
        assertEquals(1, userController.findAllFriends(1).size());
        assertEquals(1, userController.findAllFriends(2).size());
    }

    @Test
    void findCommonFriends() {
        User user = new User("user@mail", "user", LocalDate.of(2000, 1, 1));
        User user2 = new User("user2@email", "userLogin", LocalDate.of(2000, 1, 1));
        User user3 = new User("user3@email", "userLogin", LocalDate.of(2000, 1, 1));
        userController.create(user);
        userController.create(user2);
        userController.create(user3);
        userController.addFriend(1, 2);
        userController.addFriend(3, 2);
        assertEquals(1, userController.findCommonFriends(1,3).size());
    }
}
