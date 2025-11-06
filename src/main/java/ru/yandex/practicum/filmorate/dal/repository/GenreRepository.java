package ru.yandex.practicum.filmorate.dal.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genres;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public class GenreRepository extends BaseRepository<Genres> {

    private static final String FIND_ONE_GENRE_BY_ID_QUERY = "SELECT * FROM genres WHERE id = ?";
    private static final String FIND_ALL_GENRES_QUERY = "SELECT * FROM genres";
    private static final String FIND_GENRES_BY_FILM_QUERY =
            "SELECT g.* FROM genres AS g " +
                    "JOIN film_genres AS fg ON g.id = fg.genre_id WHERE fg.film_id = ?";
    private static final String INSERT_GENRES_TO_FILM_QUERY =
            "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)";
    private static final String DELETE_ALL_GENRES_FILM_QUERY = "DELETE FROM film_genres WHERE film_id = ?";

    public GenreRepository(JdbcTemplate jdbc, RowMapper<Genres> genreRowMapper) {
        super(jdbc, genreRowMapper);
    }

    public Optional<Genres> findOne(Long id) {
        return findOne(FIND_ONE_GENRE_BY_ID_QUERY, id);
    }

    public List<Genres> findAll() {
        return findMany(FIND_ALL_GENRES_QUERY);
    }

    public List<Genres> findGenresFilmById(Long filmId) {
        return findMany(FIND_GENRES_BY_FILM_QUERY, filmId);
    }

    public void insertGenresToFilm(Long filmId, Set<Genres> genres) {
        if (genres != null && !genres.isEmpty()) {
            jdbc.batchUpdate(INSERT_GENRES_TO_FILM_QUERY, genres.stream()
                    .map(genre -> new Object[]{filmId, genre.getId()})
                    .toList());
        }
    }

    public void deleteAllGenresFilm(Long filmId) {
        jdbc.update(DELETE_ALL_GENRES_FILM_QUERY, filmId);
    }
}
