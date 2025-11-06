package ru.yandex.practicum.filmorate.validator;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.storage.UserStorage;

public class FriendValidator {

    private static final String MESSAGE_OF_NOT_FOUND_USER = "Пользователь не найден";
    private static final String MESSAGE_OF_NOT_FOUND_USER_FRIEND = "Пользователь(друг) не найден";
    private static final String MESSAGE_OF_INVALID_BY_MYSELF = "Id пользователя и друга совпадают";

    public static void validateFriend(Long userId, Long idFriend, UserStorage userStorage) {
        if (!userStorage.containUser(userId)) {
            throw new NotFoundException(MESSAGE_OF_NOT_FOUND_USER);
        }
        if (!userStorage.containUser(idFriend)) {
            throw new NotFoundException(MESSAGE_OF_NOT_FOUND_USER_FRIEND);
        }
        if (userId.equals(idFriend)) {
            throw new ValidationException(MESSAGE_OF_INVALID_BY_MYSELF);
        }
    }
}
