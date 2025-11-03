package ru.yandex.practicum.filmorate.validator;

import ru.yandex.practicum.filmorate.dal.UserStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

public final class LikesValidator {

    private static final String MESSAGE_OF_NOT_FOUND_FILM = "Фильм не найден";
    private static final String MESSAGE_OF_NOT_FOUND_USER = "Пользователь не найден";

    private static void validateLikes(Long filmId,
                                      Long userId,
                                      FilmStorage filmStorage,
                                      UserStorage userStorage) {

        if (!filmStorage.containFilm(filmId)) {
            throw new NotFoundException(MESSAGE_OF_NOT_FOUND_FILM);
        }
        if (!userStorage.containUser(userId)) {
            throw new NotFoundException(MESSAGE_OF_NOT_FOUND_USER);
        }
    }
}
