package ru.yandex.practicum.filmorate.dal.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.RatingMpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

@Component
public class FilmRowMapper implements RowMapper<Film> {

    private final RatingMpaRowMapper ratingMpaRowMapper;

    public FilmRowMapper(RatingMpaRowMapper ratingMpaRowMapper) {
        this.ratingMpaRowMapper = ratingMpaRowMapper;
    }


    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(rs.getInt("id"));
        film.setName(rs.getString("name"));
        film.setDescription(rs.getString("description"));
        java.sql.Date releaseDate = rs.getDate("releaseDate");
        if (releaseDate != null) {
            film.setReleaseDate(releaseDate.toLocalDate());
        }
        film.setDuration(rs.getInt("duration"));
        film.setGenre(new HashSet<>());
        RatingMpa ratingMpa = ratingMpaRowMapper.mapRow(rs, rowNum);
        film.setRatingMPA(ratingMpa);

        return film;
    }
}
