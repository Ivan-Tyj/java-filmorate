package ru.yandex.practicum.filmorate.dal.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

@Repository
public class RatMpaRepository extends BaseRepository<Mpa> {

    private static final String FIND_BY_ID_QUERY = "SELECT * FROM rating_mpa WHERE id = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM rating_mpa ORDER BY id";

    public RatMpaRepository(JdbcTemplate jdbc, RowMapper<Mpa> ratingMpaRowMapper) {
        super(jdbc, ratingMpaRowMapper);
    }

    public Optional<Mpa> findById(Long id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }

    public List<Mpa> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

}
