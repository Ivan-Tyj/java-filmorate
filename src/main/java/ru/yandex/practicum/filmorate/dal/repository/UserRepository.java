package ru.yandex.practicum.filmorate.dal.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.mapper.UserRowMapper;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository("userRepository")
@Primary
@Slf4j
public class UserRepository extends BaseRepository<User> implements UserStorage {

    private static final String FIND_ALL_USERS_QUERY = "SELECT * FROM users";
    private static final String FIND_USER_BY_ID_QUERY = "SELECT * FROM users WHERE id = ?";
    private static final String INSERT_QUERY =
            "INSERT INTO users (email, login, name, birthday) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_QUERY =
            "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?";
    private static final String GET_FRIENDS_LIST_QUERY =
            "SELECT friend_id FROM friends WHERE user_id = ?";
    private static final String FRIENDSHIP_CHECK_QUERY =
            "SELECT COUNT(*) FROM friends WHERE user_id = ? AND friend_id = ?";
    private static final String INSERT_FRIENDSHIP_QUERY =
            "INSERT INTO friends (user_id, friend_id) VALUES (?, ?)";
    private static final String DELETE_FRIENDSHIP_QUERY =
            "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";
    private static final String GER_QUERY =
            "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";
    private static final String DELETE_USER_QUERY = "DELETE FROM users WHERE id = ?";
    private static final String CONTAIN_USER_QUERY = "SELECT COUNT(*) FROM users WHERE id = ?";
    private static final String GET_COMMON_FRIENDS_QUERY = """
            WITH user_friends AS (
                SELECT friend_id as user_id FROM friends WHERE user_id = ?
                UNION
                SELECT user_id as user_id FROM friends WHERE friend_id = ?
            ),
                another_friends AS (
                SELECT friend_id as user_id FROM friends WHERE user_id = ?
                UNION
                SELECT user_id as user_id FROM friends WHERE friend_id = ?
            )
            SELECT u.*
            FROM users AS u
            JOIN user_friends AS uf ON u.id = uf.user_id
            JOIN another_friends AS af ON u.id = af.user_id""";
    private static final String DELETE_ALL_USERS_QUERY = "DELETE FROM users";

    public UserRepository(JdbcTemplate jdbc, UserRowMapper userRowMapper) {
        super(jdbc, userRowMapper);
    }

    @Override
    public Collection<User> findAll() {
        return findMany(FIND_ALL_USERS_QUERY);
    }

    @Override
    public User findById(long id) {
        return findOne(FIND_USER_BY_ID_QUERY, id)
                .orElseThrow(() -> new NotFoundException("Позьзователь с id = " + id + "не найден"));
    }

    @Override
    public User create(User user) {
        long id = insert(
                INSERT_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday());
        user.setId(id);
        return user;
    }

    @Override
    public User update(User user) {
        update(UPDATE_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
        return user;
    }

    @Override
    public List<User> getFriendList(Long userId) {
        List<Long> friendIds = jdbc.query(GET_FRIENDS_LIST_QUERY,
                (rs, rowNum) -> rs.getLong("friend_id"), userId);
        return friendIds.stream()
                .map(this::findById)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        if (userId.equals(friendId)) {
            log.error("Пользователь попытался добавить в друзья самого себя");
            throw new ValidationException("Попытка добавить в друзья самого себя");
        }
        if (checkFriendship(userId, friendId) == 1) {
            log.warn("Пользователи с ID: {} и {} - уже друзья", userId, friendId);
            return;
        }
        if (checkFriendship(userId, friendId) == 2) {
            log.warn("Пользователь с ID = {}, направил заявку в друзья пользователю с ID = {}", userId, friendId);
            return;
        }
        jdbc.update(INSERT_FRIENDSHIP_QUERY, userId, friendId);
    }

    private Integer checkFriendship(Long userId, Long friendId) {
        int countUser = jdbc.queryForObject(FRIENDSHIP_CHECK_QUERY, Integer.class, userId, friendId);
        int countFriend = jdbc.queryForObject(FRIENDSHIP_CHECK_QUERY, Integer.class, friendId, userId);
        if (countUser > 0 && countFriend > 0) {
            return 1;
        }
        if (countUser > 0 && countFriend == 0) {
            return 2;
        }
        return 0;
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {
        jdbc.update(DELETE_FRIENDSHIP_QUERY, userId, friendId);
    }

    @Override
    public void deleteUser(Long userId) {
        jdbc.update(DELETE_USER_QUERY, userId);
        log.info("Пользователь с ID = {} удален", userId);
    }

    @Override
    public boolean containUser(Long userId) {
        Integer count = jdbc.queryForObject(CONTAIN_USER_QUERY, Integer.class, userId);
        return count > 0;
    }

    @Override
    public List<User> findCommonFriends(Long userId, Long anotherUserId) {
        return findMany(GET_COMMON_FRIENDS_QUERY, userId, userId, anotherUserId, anotherUserId);
    }

    @Override
    public void deleteAll() {
        jdbc.update(DELETE_ALL_USERS_QUERY);
    }
}