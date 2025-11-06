package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {

    Film create(Film film);

    Film update(Film film);

    Film findById(long id);

    Collection<Film> findAll();

    long getFilmsSize();

    void deleteFilmById(long id);

    void deleteAll();

    boolean containFilm(long id);

    void addLike(Long filmId, Long userId);

    void deleteLike(Long filmId, Long userId);

    Collection<Film> getPopularFilms(int count);

    boolean containLike(long filmId, long userId);
}
