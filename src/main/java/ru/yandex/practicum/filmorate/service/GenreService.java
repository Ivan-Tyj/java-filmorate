package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.repository.GenreRepository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genres;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreRepository genreRepository;

    public List<Genres> findAllGenres() {
        return genreRepository.findAll();
    }

    public Genres getOneGenreById(Long id) {
        return genreRepository.findOne(id)
                .orElseThrow(() -> new NotFoundException("Жанр с ID = " + id + " - не найден"));
    }
}
