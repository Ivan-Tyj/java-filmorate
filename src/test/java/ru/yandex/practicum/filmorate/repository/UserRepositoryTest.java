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
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.service.RatingService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@JdbcTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Import({FilmRepository.class, UserRepository.class, GenreRepository.class, RatMpaRepository.class,
        FilmService.class, UserService.class, FilmController.class, UserController.class,
        FilmRowMapper.class, UserRowMapper.class, RatingMpaRowMapper.class, GenreRowMapper.class,
        RatingService.class, GenreService.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserRepositoryTest {

    private final UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void beforeAdd() {
        testUser = new User(
                "test@email",
                "testLogin",
                LocalDate.of(2000, 1, 1)
        );
        userRepository.create(testUser);
    }

    @Test
    public void testFindUserById() {
        Optional<User> userOptional = Optional.of(userRepository.findById(1));
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1L)
                );
    }

    @Test
    public void testFindAllUsers() {
        Collection<User> users = userRepository.findAll();
        assertFalse(users.isEmpty());
    }

    @Test
    public void testFilmUpdate() {
        testUser.setEmail("newTest@email");
        testUser.setLogin("newTestLogin");

        userRepository.update(testUser);

        User newUser = userRepository.findById(testUser.getId());

        assertEquals("newTest@email", newUser.getEmail());
        assertEquals("newTestLogin", newUser.getLogin());
    }


}
