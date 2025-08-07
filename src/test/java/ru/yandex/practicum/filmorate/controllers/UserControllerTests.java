package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserControllerTests {
    static UserController userController = new UserController();

    @Test
    void createdUser() {
        User user = new User("mail@", "login", LocalDate.now());
        assertEquals(user, userController.create(user));
    }

    @Test
    void updateUser() {
        User user2 = new User("user2@email", "userLogin", LocalDate.now());
        user2.setId(1);
        assertEquals(user2, userController.update(user2));
    }

    @Test
    void returnFilms() {
        User user = new User("user@email", "userLogin", LocalDate.now());
        userController.create(user);
        assertEquals(1, userController.findAll().size());
    }
}
