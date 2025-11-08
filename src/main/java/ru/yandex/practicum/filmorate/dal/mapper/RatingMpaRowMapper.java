package ru.yandex.practicum.filmorate.dal.mapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
@Component
public class RatingMpaRowMapper implements RowMapper<Mpa> {

    @Override
    public Mpa mapRow(ResultSet rs, int rowNum) throws SQLException {
        Mpa mpa = new Mpa();

        try {
            mpa.setId(rs.getLong("rating_mpa_id"));
        } catch (SQLException e) {
            mpa.setId(rs.getLong("id"));
        }

        try {
            mpa.setName(rs.getString("rating_name"));
        } catch (SQLException e) {
            mpa.setName(rs.getString("name"));
        }

        return mpa;

    }
}
