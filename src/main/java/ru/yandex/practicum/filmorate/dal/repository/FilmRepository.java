package ru.yandex.practicum.filmorate.dal.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.dal.mapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;
import java.util.List;

public class FilmRepository extends BaseRepository<Film> implements FilmStorage {

    private static final String INSERT_FILM_QUERY =
            "INSERT INTO films (name, description, release_date, duration, mpa_id) VALUES (?, ?, ?, ?, ?)";

    private final GenreRepository genreRepository;

    public FilmRepository(JdbcTemplate jdbc, FilmRowMapper filmRowMapper, GenreRepository genreRepository) {
        super(jdbc, filmRowMapper);
        this.genreRepository = genreRepository;
    }

    @Override
    public Film create(Film film) {
        return null;
    }

    @Override
    public Film update(Film film) {
        return null;
    }

    @Override
    public Film findById(long id) {
        return null;
    }

    @Override
    public Collection<Film> findAll() {
        return List.of();
    }

    @Override
    public long getFilmsSize() {
        return 0;
    }

    @Override
    public void deleteAll() {

    }

    @Override
    public boolean containFilm(long id) {
        return false;
    }
}
