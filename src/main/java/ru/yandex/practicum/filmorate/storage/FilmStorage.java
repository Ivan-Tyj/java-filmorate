package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {

    Film create(Film film);

    Film update(Film film);

    Film findById(long id);

    Collection<Film> findAll();

    long getFilmsSize();

    void deleteAll();

}
