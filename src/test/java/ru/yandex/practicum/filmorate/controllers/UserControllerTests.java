package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserControllerTests {
    static UserController userController = new UserController();

    @BeforeAll
    static void clear() {
        userController.clearAllUsers();
    }

    @Test
    void createdUser() {
        User user = new User("user@email", "userLogin", "userName", LocalDate.now());
        assertEquals(user, userController.create(user));
    }

    @Test
    void updateUser() {
        User user1 = new User("user@email", "userLogin", "userName", LocalDate.now());
        User user2 = new User(1, "user2@email", "userLogin", "userName", LocalDate.now());
        userController.create(user1);
        assertEquals(user2, userController.update(user2));
    }

    @Test
    void returnFilms() {
        User user = new User("user@email", "userLogin", "userName", LocalDate.now());
        userController.create(user);
        assertEquals(1, userController.findAll().size());
    }
}
