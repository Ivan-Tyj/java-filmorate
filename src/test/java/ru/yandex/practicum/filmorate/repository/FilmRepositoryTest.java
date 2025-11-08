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
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.service.RatingService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.Collection;
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
public class FilmRepositoryTest {

    private final FilmRepository filmRepository;
    private final UserRepository userRepository;


    private Film testFilm;

    @BeforeEach
    void beforeAdd() {
        testFilm = new Film(
                "testFilm",
                "testDescription",
                LocalDate.of(2000, 1, 1),
                90
        );
        filmRepository.create(testFilm);
    }

    @Test
    void testFindFilmById() {
        Optional<Film> filmOptional = Optional.of(filmRepository.findById(1));
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1L)
                );
    }

    @Test
    void testFindAllFilms() {
        Collection<Film> films = filmRepository.findAll();
        assertFalse(films.isEmpty());
    }

    @Test
    void testFilmUpdate() {
        testFilm.setName("NewName");
        testFilm.setDescription("NewDescription");

        filmRepository.update(testFilm);

        Film newFilm = filmRepository.findById(testFilm.getId());

        assertEquals("NewName", newFilm.getName());
        assertEquals("NewDescription", newFilm.getDescription());
    }

    @Test
    void testGetPopularFilms() {
        Film testFilm2 = new Film("testFilm2", "testDescription",
                LocalDate.of(2000, 1, 1), 90);
        filmRepository.create(testFilm2);

        User user1 = new User("test@email", "testLogin",
                LocalDate.of(2000, 1, 1));
        userRepository.create(user1);

        User user2 = new User("otherTest@email", "otherTestLogin",
                LocalDate.of(2000, 1, 1));
        userRepository.create(user2);

        filmRepository.addLike(testFilm.getId(), user1.getId());
        filmRepository.addLike(testFilm2.getId(), user1.getId());
        filmRepository.addLike(testFilm2.getId(), user2.getId());

        Collection<Film> films = filmRepository.getPopularFilms(10);
        assertFalse(films.isEmpty());
        assertEquals(2, films.size());
        assertEquals(testFilm2.getId(), films.iterator().next().getId());
    }
}
