package ru.yandex.practicum.filmorate.dal.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.RatingMpa;

import java.util.List;
import java.util.Optional;

@Repository
public class RatMpaRepository extends BaseRepository<RatingMpa> {

    private static final String FIND_BY_ID_QUERY = "SELECT * FROM mpa WHERE id = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM mpa ORDER BY id";

    public RatMpaRepository(JdbcTemplate jdbc, RowMapper<RatingMpa> mapper) {
        super(jdbc, mapper);
    }

    public Optional<RatingMpa> findById(Long id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }

    public List<RatingMpa> findAll() {
        return findMany(FIND_ALL_QUERY);
    }
}
