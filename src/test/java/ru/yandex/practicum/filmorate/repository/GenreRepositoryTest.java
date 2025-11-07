package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.dal.mapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.dal.mapper.GenreRowMapper;
import ru.yandex.practicum.filmorate.dal.mapper.RatingMpaRowMapper;
import ru.yandex.practicum.filmorate.dal.mapper.UserRowMapper;
import ru.yandex.practicum.filmorate.dal.repository.FilmRepository;
import ru.yandex.practicum.filmorate.dal.repository.GenreRepository;
import ru.yandex.practicum.filmorate.dal.repository.RatMpaRepository;
import ru.yandex.practicum.filmorate.dal.repository.UserRepository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genres;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.service.RatingService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Import({FilmRepository.class, UserRepository.class, GenreRepository.class, RatMpaRepository.class,
        FilmService.class, UserService.class, FilmController.class, UserController.class,
        FilmRowMapper.class, UserRowMapper.class, RatingMpaRowMapper.class, GenreRowMapper.class,
        RatingService.class, GenreService.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreRepositoryTest {

    @Autowired
    private GenreRepository genreRepository;

    private Film testFilm;

    @BeforeEach
    void beforeAdd() {
        testFilm = new Film(
                "testFilm",
                "testDescription",
                LocalDate.of(2000, 1, 1),
                90
        );
    }

    @Test
    void findAllGenresTest() {
        List<Genres> genres = genreRepository.findAll();
        assertNotNull(genres);
        assertFalse(genres.isEmpty());
    }

    @Test
    void findByIdGenreTest() {
        Optional<Genres> genreOptional = genreRepository.findOne(1L);
        assertThat(genreOptional)
                .isPresent()
                .hasValueSatisfying(genres ->
                        assertThat(genres).hasFieldOrPropertyWithValue("id", 1L)
                );
    }

    @Test
    void findAllGenresForFilmTest() {
        List<Genres> genres = genreRepository.findGenresFilmById(testFilm.getId());
        assertNotNull(genres);
        assertTrue(genres.isEmpty());
    }
}
