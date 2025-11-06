package ru.yandex.practicum.filmorate.storage;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();

    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    @Getter
    private int maxIdFilms = 0;


    public long getFilmsSize() {
        return films.size();
    }

    public Collection<Film> findAll() {
        return films.values();
    }

    public Film findById(long id) {
        Optional<Film> film = Optional.ofNullable(films.get(id));
        return film.orElseThrow();
    }


    public Film create(Film film) {
        film.setId(++maxIdFilms);
        log.debug("Id фильма - {}", film.getId());
        log.debug("Общее количество фильмов - {}", films.size());
        films.put(film.getId(), film);
        log.debug("Новый фильм добавлен ,общее количество фильмов - {}", films.size());
        return film;
    }

    public Film update(Film film) {
        films.put(film.getId(), film);
        log.debug("Фильм обновлен");
        return film;
    }

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
}
