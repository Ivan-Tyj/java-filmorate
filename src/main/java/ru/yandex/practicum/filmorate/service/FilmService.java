package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final FilmStorage filmStorage;

    public void addLike(long filmId, long userId) {
        Set<Long> likes = filmStorage.findById(filmId).getLikes();
        log.debug("Список лайков получен, размер - {}", likes.size());
        likes.add(userId);
        log.debug("Лайк поставлен, количество лайков = {}", likes.size());
        filmStorage.findById(filmId).setLikes(likes);
        log.debug("Список лайков фильма обновлен, количество лайков = {}", likes.size());
    }

    public void deleteLike(long filmId, long userId) {
        Set<Long> likes = filmStorage.findById(filmId).getLikes();
        log.debug("Список лайков получен, размер - {}", likes.size());
        likes.remove(userId);
        log.debug("Лайк удален, количество лайков = {}", likes.size());
        filmStorage.findById(filmId).setLikes(likes);
        log.debug("Список лайков фильма обновлен, количество лайков = {}", likes.size());
    }

    public Collection<Film> findPopularFilms(int count) {
        return filmStorage.findAll().stream()
                .sorted((f1, f2) ->
                        Integer.compare(f2.getLikes().size(),
                                f1.getLikes().size()))
                .limit(count > 0 ? count : 10)
                .collect(Collectors.toList());
    }
}
