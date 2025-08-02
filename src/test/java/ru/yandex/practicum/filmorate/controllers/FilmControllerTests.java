package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FilmControllerTests {
    static FilmController filmController = new FilmController();

    @BeforeAll
    static void clear() {
        filmController.clearAllFilms();
    }

    @Test
    void createdFilm() {
        Film film = new Film("Film", "description", LocalDate.of(1985, 12, 28),
                Duration.ofSeconds(1));
        assertEquals(film, filmController.create(film));
    }

    @Test
    void updateFilm() {
        Film film1 = new Film("Film1", "description", LocalDate.of(1985, 12, 28),
                Duration.ofSeconds(1));
        Film film2 = new Film(1, "Film2", "description", LocalDate.of(1985, 12, 28),
                Duration.ofSeconds(1));
        filmController.create(film1);
        assertEquals(film2, filmController.update(film2));
    }

    @Test
    void returnFilms() {
        Film film1 = new Film("Film1", "description", LocalDate.of(1985, 12, 28),
                Duration.ofSeconds(1));
        filmController.create(film1);
        assertEquals(1, filmController.findAll().size());
    }
}
