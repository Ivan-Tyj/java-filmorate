package ru.yandex.practicum.filmorate.storage;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();

    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    @Getter
    private int maxIdFilms = 0;


    public long getFilmsSize() {
        return films.size();
    }

    @Override
    public void deleteFilmById(long id) {
        throw new RuntimeException("Not Implements");
    }

    @Override
    public Collection<Film> findAll() {
        return films.values();
    }

    public Film findById(long id) {
        Optional<Film> film = Optional.ofNullable(films.get(id));
        return film.orElseThrow();
    }

    @Override
    public Film create(Film film) {
        film.setId((long) ++maxIdFilms);
        log.debug("Id фильма - {}", film.getId());
        log.debug("Общее количество фильмов - {}", films.size());
        films.put(film.getId(), film);
        log.debug("Новый фильм добавлен ,общее количество фильмов - {}", films.size());
        return film;
    }

    @Override
    public Film update(Film film) {
        films.put(film.getId(), film);
        log.debug("Фильм обновлен");
        return film;
    }

    @Override
    public void deleteAll() {
        log.debug("Общее количество фильмов - {}", films.size());
        films.clear();
        maxIdFilms = 0;
        log.debug("Список фильмов пуст");
    }

    @Override
    public boolean containFilm(long id) {
        return films.containsKey(id);
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        throw new RuntimeException("Not Implements");
    }

    @Override
    public void deleteLike(Long filmId, Long userId) {
        throw new RuntimeException("Not Implements");
    }

    @Override
    public Collection<Film> getPopularFilms(int count) {
        throw new RuntimeException("Not Implements");
    }

    @Override
    public boolean containLike(long filmId, long userId) {
        throw new RuntimeException("Not Implements");
    }
}
