package ru.yandex.practicum.filmorate.validator;


import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

public final class LikesValidator {

    private static final String MESSAGE_OF_NOT_FOUND_FILM = "Фильм не найден";
    private static final String MESSAGE_OF_NOT_FOUND_USER = "Пользователь не найден";

    public static void validateLikes(Long filmId,
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
