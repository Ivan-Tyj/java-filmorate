package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class FilmControllerTests {

    @Autowired
    private FilmController filmController;

    @Autowired
    private UserController userController;

    @Test
    void findAllFilms() {
        Film film1 = new Film("Film1", "description", LocalDate.of(1985, 12, 28),
                1);
        filmController.create(film1);
        assertEquals(1, filmController.findAll().size());
    }

    @Test
    void findById() {
        Film film1 = new Film("Film1", "description", LocalDate.of(1985, 12, 28),
                1);
        filmController.create(film1);
        assertEquals(film1, filmController.findById(1));
    }

    @Test
    void createdFilm() {
        Film film = new Film("Film", "description", LocalDate.of(1985, 12, 28),
                1);
        assertEquals(film, filmController.create(film));
    }

    @Test
    void updateFilm() {
        Film film2 = new Film("Film2", "description", LocalDate.of(1985, 12, 28),
                1);
        film2.setId(1);
        assertEquals(film2, filmController.update(film2));
    }

    @Test
    void addLike() {
        Film film = new Film("Film", "description", LocalDate.of(1985, 12, 28),
                1);
        User user = new User("user@mail", "user", LocalDate.of(2000, 1, 1));
        filmController.create(film);
        userController.create(user);
        filmController.addLike(1, 1);
        assertEquals(1, film.getLikesSize());
    }

    @Test
    void deleteLike() {
        Film film = new Film("Film", "description", LocalDate.of(1985, 12, 28),
                1);
        User user = new User("user@mail", "user", LocalDate.of(2000, 1, 1));
        filmController.create(film);
        userController.create(user);
        filmController.addLike(1, 1);
        filmController.deleteLike(1, 1);
        assertEquals(0, film.getLikesSize());
    }

    @Test
    void findPopularFilms() {
        Film film1 = new Film("Film1", "description", LocalDate.of(1985, 12, 28),
                1);
        Film film2 = new Film("Film2", "description", LocalDate.of(1985, 12, 28),
                1);
        filmController.create(film1);
        filmController.create(film2);
        assertEquals(1, filmController.findPopularFilms(1).size());
        assertEquals(2, filmController.findPopularFilms(0).size());
    }
}
