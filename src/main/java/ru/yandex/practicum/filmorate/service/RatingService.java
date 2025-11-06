package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.repository.RatMpaRepository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RatingService {
    private final RatMpaRepository repository;

    public List<Mpa> findAllRatingMpa() {
        return repository.findAll();
    }

    public Mpa getRatingMpaById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Рейтинг MPA с ID = " + id + " - не найден"));
    }
}
