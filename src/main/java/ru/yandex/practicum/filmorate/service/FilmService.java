package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genres;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.validator.FilmValidator;
import ru.yandex.practicum.filmorate.validator.LikesValidator;

import java.util.Collection;

@Slf4j
@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final RatingService ratingService;
    private final GenreService genreService;

    private static final String MESSAGE_OF_ID_FILM = "Некорректный Id фильма";
    private static final String MESSAGE_OF_NOT_FOUND_LIKE = "Лайк не найден";
    private static final String MESSAGE_OF_FOUND_LIKE = "Лайк уже поставлен";

    public FilmService(@Qualifier("filmRepository") FilmStorage filmStorage,
                       @Qualifier("userRepository") UserStorage userStorage, RatingService ratingService, GenreService genreService) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.ratingService = ratingService;
        this.genreService = genreService;
    }

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
        FilmValidator.validateFilms(film);
        if (film.getMpa() != null) {
            ratingService.getRatingMpaById(film.getMpa().getId());
        }

        if (film.getGenres() != null) {
            for (Genres genre : film.getGenres()) {
                genreService.getOneGenreById(genre.getId());
            }
        }
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        if (film.getId() == null) {
            log.error("Некорректный Id фильма - {}", film.getId());
            throw new ValidationException(MESSAGE_OF_ID_FILM);
        }
        if (!filmStorage.containFilm(film.getId())) {
            throw new NotFoundException("Фильм с id = " + film.getId() + " не найден");
        }
        if (film.getGenres() != null) {
            for (Genres genre : film.getGenres()) {
                genreService.getOneGenreById(genre.getId());
            }
        }
        FilmValidator.validateFilms(film);
        return filmStorage.update(film);
    }

    public void deleteAll() {
        filmStorage.deleteAll();
    }

    public void addLike(long filmId, long userId) {
        LikesValidator.validateLikes(filmId, userId, filmStorage, userStorage);
        if (filmStorage.containLike(filmId, userId)) {
            throw new ValidationException(MESSAGE_OF_FOUND_LIKE);
        }
        filmStorage.addLike(filmId, userId);
        log.info("Лайк поставлен");
    }

    public void deleteLike(long filmId, long userId) {
        LikesValidator.validateLikes(filmId, userId, filmStorage, userStorage);
        if (!filmStorage.containLike(filmId, userId)) {
            throw new ValidationException(MESSAGE_OF_NOT_FOUND_LIKE);
        }
        filmStorage.deleteLike(filmId, userId);
        log.info("Лайк удален");
    }

    public Collection<Film> findPopularFilms(int count) {
        int limit = 10;
        if (count > 0) {
            limit = count;
        }
        return filmStorage.getPopularFilms(limit);
    }

    public void deleteFilmById(long id) {
        filmStorage.deleteFilmById(id);
    }

}
