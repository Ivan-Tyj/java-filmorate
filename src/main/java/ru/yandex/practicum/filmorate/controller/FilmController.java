package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {

    private final Map<Integer, Film> films = new HashMap<>();

    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    private static final int MAX_LENGTH_DESCRIPTION = 200;
    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1985, 12, 28);

    private static final String MESSAGE_OF_VALID_NAME = "Название не может быть пустым";
    private static final String MESSAGE_OF_VALID_DESCRIPTION = "Максимальная длина описания превышена";
    private static final String MESSAGE_OF_VALID_RELEASE_DATE = "Дата релиза должна быть позже чем " + MIN_RELEASE_DATE;
    private static final String MESSAGE_OF_VALID_DURATION = "Продолжительность фильма должна быть положительным числом";
    private static final String MESSAGE_OF_ID_FILM = "Некорректный Id фильма";

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        validateFilms(film);
        film.setId(getNextId());
        log.debug("Id фильма - {}", film.getId());
        log.debug("Общее количество фильмов - {}", films.size());
        films.put(film.getId(), film);
        log.debug("Новый фильм добавлен ,общее количество фильмов - {}", films.size());
        return film;
    }

    private int getNextId() {
        int currentMaxId = films.keySet()
                .stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    private void validateFilms(Film film) {
        if (film.getName() == null || film.getName().isEmpty() || film.getName().isBlank()) {
            log.error("Передано некорректное наименование фильма - {}", film.getName());
            throw new ValidationException(MESSAGE_OF_VALID_NAME);
        }
        if (film.getDescription().length() > MAX_LENGTH_DESCRIPTION) {
            log.error("Превышена максимальная длина описания: {}", film.getDescription().length());
            throw new ValidationException(MESSAGE_OF_VALID_DESCRIPTION);
        }
        if (film.getReleaseDate().isBefore(MIN_RELEASE_DATE)) {
            log.error("Некорректная дата релиза - {}", film.getReleaseDate());
            throw new ValidationException(MESSAGE_OF_VALID_RELEASE_DATE);
        }
        if (film.getDuration() <= 0) {
            log.error("Некорректная продолжительность фильма, сек - {}", film.getDuration());
            throw new ValidationException(MESSAGE_OF_VALID_DURATION);
        }
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        // проверяем необходимые условия
        if (film.getId() < 0) {
            log.error("Некорректный Id фильма - {}", film.getId());
            throw new ValidationException(MESSAGE_OF_ID_FILM);
        }
        if (films.containsKey(film.getId())) {
            validateFilms(film);
            films.put(film.getId(), film);
            log.debug("Фильм обновлен");
        } else {
            throw new ValidationException("Фильм с id = " + film.getId() + " не найден");
        }
        return film;
    }

    public void clearAllFilms() {
        films.clear();
    }
}
