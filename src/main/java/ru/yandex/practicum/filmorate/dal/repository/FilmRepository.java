package ru.yandex.practicum.filmorate.dal.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.mapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genres;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;

@Repository("filmRepository")
@Primary
@Slf4j
public class FilmRepository extends BaseRepository<Film> implements FilmStorage {

    private static final String INSERT_FILM_QUERY =
            "INSERT INTO films (name, description, release_date, duration, rating_mpa_id) VALUES (?, ?, ?, ?, ?)";
    private static final String VALIDATE_RATING_MPA_QUERY = "SELECT COUNT(*) FROM rating_mpa WHERE id = ?";
    private static final String VALIDATE_GENRE_QUERY = "SELECT COUNT(*) FROM genres WHERE id = ?";
    private static final String UPDATE_FILM_QUERY =
            "UPDATE films " +
                    "SET name = ?, description = ?, release_date = ?, duration = ?, rating_mpa_id = ? WHERE id = ?";
    private static final String FIND_BY_ID_FILM_QUERY =
            "SELECT f.*, rm.id AS rating_id, rm.name AS rating_name " +
                    "FROM films AS f JOIN rating_mpa AS rm ON f.rating_mpa_id = rm.id WHERE f.id = ?";
    private static final String FIND_ALL_FILMS_QUERY =
            "SELECT f.*, rm.id AS rating_id, rm.name AS rating_name " +
                    "FROM films AS f JOIN rating_mpa AS rm ON f.rating_mpa_id = rm.id";
    private static final String DELETE_ONE_FILM_QUERY = "DELETE FROM films WHERE id = ?";
    private static final String DELETE_ALL_FILMS_QUERY = "DELETE FROM films";
    private static final String COUNT_FILM_BY_ID_QUERY = "SELECT COUNT(*) FROM films WHERE id = ?";
    private static final String COUNT_CHECK_LIKE_QUERY =
            "SELECT COUNT(*) FROM likes WHERE film_id = ? AND user_id = ?";
    private static final String INSERT_LIKE_QUERY =
            "INSERT INTO likes (film_id, user_id) VALUES (?, ?)";
    private static final String DELETE_LIKE_QUERY =
            "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
    private static final String CONTAIN_MPA_QUERY = "SELECT COUNT(*) FROM rating_mpa WHERE id = ?";
    private static final String GET_POPULAR_FILMS_QUERY = """
                SELECT f.*, rm.id AS rating_id, rm.name AS rating_name
                FROM films AS f
                JOIN rating_mpa AS rm ON f.rating_mpa_id = rm.id
                LEFT JOIN (
                    SELECT film_id, COUNT(user_id) as likes_count
                    FROM likes AS l
                    GROUP BY film_id
                ) l ON f.id = l.film_id
                ORDER BY l.likes_count DESC NULLS LAST, f.id ASC
                LIMIT ?
            """;

    private final GenreRepository genreRepository;
    private final RatMpaRepository ratMpaRepository;

    public FilmRepository(JdbcTemplate jdbc, FilmRowMapper filmRowMapper,
                          GenreRepository genreRepository, RatMpaRepository ratMpaRepository) {
        super(jdbc, filmRowMapper);
        this.genreRepository = genreRepository;
        this.ratMpaRepository = ratMpaRepository;
    }

    @Override
    public Film create(Film film) {
        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            validateAllGenres(film.getGenres());
        }

        Long ratingMpa = (film.getMpa() != null) ? film.getMpa().getId() : 1;

        if (film.getMpa() != null) {
            validateMpaContain(film.getMpa().getId());
        }

        long id = insert(INSERT_FILM_QUERY, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), ratingMpa);
        film.setId(id);
        saveGenresForFilm(film);
        Mpa mpa = ratMpaRepository.findById(ratingMpa).orElseThrow(
                () -> new NotFoundException("Рейтинг не найден"));
        film.setMpa(mpa);
        return film;
    }


    private void saveGenresForFilm(Film film) {
        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            List<Genres> sortGenres = new ArrayList<>(film.getGenres());
            sortGenres.sort(Comparator.comparing(Genres::getId));
            genreRepository.insertGenresToFilm(film.getId(), new HashSet<>(sortGenres));
        }
    }

    private void validateMpaContain(Long mpaId) {
        Integer count = jdbc.queryForObject(CONTAIN_MPA_QUERY, Integer.class, mpaId);
        if (count == 0) {
            throw new NotFoundException("MPA рейтинг с ID = " + mpaId + " - не найден");
        }
    }

    @Override
    public Film update(Film film) {
        Long ratingMpa = (film.getMpa() != null) ? film.getMpa().getId() : 1;
        update(UPDATE_FILM_QUERY, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), ratingMpa, film.getId());
        genreRepository.deleteAllGenresFilm(film.getId());
        saveGenresForFilm(film);
        return film;
    }

    @Override
    public Film findById(long id) {
        Film film = findOne(FIND_BY_ID_FILM_QUERY, id)
                .orElseThrow(() -> new NotFoundException("Фильм с ID = " + id + " не найден"));
        if (film != null) {
            film.setGenres(new HashSet<>(genreRepository.findGenresFilmById(film.getId())));
        }
        return film;
    }

    @Override
    public Collection<Film> findAll() {
        List<Film> films = findMany(FIND_ALL_FILMS_QUERY);
        for (Film film : films) {
            if (film != null) {
                film.setGenres(new HashSet<>(genreRepository.findGenresFilmById(film.getId())));
            }
        }
        return films;
    }

    @Override
    public long getFilmsSize() {
        return findAll().size();
    }

    @Override
    public void deleteFilmById(long id) {
        jdbc.update(DELETE_ONE_FILM_QUERY, id);
    }

    @Override
    public void deleteAll() {
        jdbc.update(DELETE_ALL_FILMS_QUERY);
    }

    @Override
    public boolean containFilm(long id) {
        Integer count = jdbc.queryForObject(COUNT_FILM_BY_ID_QUERY, Integer.class, id);
        return count > 0;
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        jdbc.update(INSERT_LIKE_QUERY, filmId, userId);
    }

    @Override
    public void deleteLike(Long filmId, Long userId) {
        jdbc.update(DELETE_LIKE_QUERY, filmId, userId);
    }

    @Override
    public Collection<Film> getPopularFilms(int count) {
        List<Film> films = findMany(GET_POPULAR_FILMS_QUERY, count);
        for (Film film : films) {
            film.setGenres(new HashSet<>(genreRepository.findGenresFilmById(film.getId())));
        }
        return films;
    }

    @Override
    public boolean containLike(long filmId, long userId) {
        Integer count = jdbc.queryForObject(COUNT_CHECK_LIKE_QUERY, Integer.class, filmId, userId);
        return count > 0;
    }

    private void validateRatingMpa(Long ratingMpaId) {
        Integer count = jdbc.queryForObject(VALIDATE_RATING_MPA_QUERY, Integer.class, ratingMpaId);
        if (count == 0) {
            throw new NotFoundException("Рейтинг MPA с ID = " + ratingMpaId + " - не найден");
        }
    }

    private void validateAllGenres(Set<Genres> genres) {
        for (Genres genre : genres) {
            if (genre != null && genre.getId() != null) {
                Integer count = jdbc.queryForObject(VALIDATE_GENRE_QUERY, Integer.class, genre.getId());
                if (count == null || count == 0) {
                    throw new NotFoundException("Жанр с ID = " + genre.getId() + " - не найден");
                }
            }
        }
    }
}
