package ru.yandex.practicum.filmorate.dal.mapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.RatingMpa;

import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
@Component
public class RatingMpaRowMapper implements RowMapper<RatingMpa> {

    @Override
    public RatingMpa mapRow(ResultSet rs, int rowNum) throws SQLException {
        RatingMpa ratingMpa = new RatingMpa();

        try {
            ratingMpa.setId(rs.getInt("ratingMpa_id"));
        } catch (SQLException e) {
            ratingMpa.setId(rs.getInt("id"));
        }
        try {
            ratingMpa.setName(rs.getString("ratingMpa_name"));
        } catch (SQLException e) {
            ratingMpa.setName(rs.getString("name"));
        }

        return ratingMpa;
    }
}
