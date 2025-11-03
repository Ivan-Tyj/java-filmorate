package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.dal.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    private static final String MESSAGE_OF_ID_FILM = "Некорректный Id фильма";
    private static final String MESSAGE_OF_NOT_FOUND_LIKE = "Лайк не найден";


    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film findById(long id) {
        if (id <= 0) {
            throw new ValidationException(MESSAGE_OF_ID_FILM);
        }
        return filmStorage.findById(id);
    }

    public Film create(Film film) {
        validateFilms(film);
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        if (film.getId() < 0) {
            log.error("Некорректный Id фильма - {}", film.getId());
            throw new ValidationException(MESSAGE_OF_ID_FILM);
        }
        if (!filmStorage.containFilm(film.getId())) {
            throw new NotFoundException("Фильм с id = " + film.getId() + " не найден");
        }
        validateFilms(film);
        return filmStorage.update(film);
    }

    public void deleteAll() {
        filmStorage.deleteAll();
    }

    public void addLike(long filmId, long userId) {
        validateLikes(filmId, userId);
        filmStorage.findById(filmId).addLike(userId);
        log.debug("Лайк поставлен, количество лайков = {}",
                filmStorage.findById(filmId).getLikes());
    }

    public void deleteLike(long filmId, long userId) {
        validateLikes(filmId, userId);
        if (!filmStorage.findById(filmId).getLikes().contains(userId)) {
            throw new ValidationException(MESSAGE_OF_NOT_FOUND_LIKE);
        }
        filmStorage.findById(filmId).deleteLike(userId);
        log.debug("Лайк удален, количество лайков = {}", filmStorage.findById(filmId).getLikes());
    }

    public Collection<Film> findPopularFilms(int count) {
        return filmStorage.findAll().stream()
                .sorted((f1, f2) ->
                        Integer.compare(f2.getLikes().size(),
                                f1.getLikes().size()))
                .limit(count > 0 ? count : 10)
                .collect(Collectors.toList());
    }


}
