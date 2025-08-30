package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FilmControllerTests {
    static FilmController filmController = new FilmController();

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
    void returnFilms() {
        Film film1 = new Film("Film1", "description", LocalDate.of(1985, 12, 28),
                1);
        filmController.create(film1);
        assertEquals(1, filmController.findAll().size());
    }
}
