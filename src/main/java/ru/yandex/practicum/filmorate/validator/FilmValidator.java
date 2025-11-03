package ru.yandex.practicum.filmorate.validator;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

@Slf4j
public final class FilmValidator {

    private static final int MAX_LENGTH_DESCRIPTION = 200;
    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    private static final String MESSAGE_OF_VALID_NAME = "Название не может быть пустым";
    private static final String MESSAGE_OF_VALID_DESCRIPTION = "Максимальная длина описания превышена";
    private static final String MESSAGE_OF_VALID_RELEASE_DATE = "Дата релиза должна быть указана";
    private static final String MESSAGE_OF_VALID_RELEASE_DATE_INCORRECT =
            "Дата релиза должна быть позже чем " + MIN_RELEASE_DATE;
    private static final String MESSAGE_OF_VALID_DURATION =
            "Продолжительность фильма должна быть положительным числом";

    public static void validateFilms(Film film) {
        if (film.getName() == null || film.getName().isEmpty() || film.getName().isBlank()) {
            log.error("Передано некорректное наименование фильма - {}", film.getName());
            throw new ValidationException(MESSAGE_OF_VALID_NAME);
        }
        if (film.getDescription().length() > MAX_LENGTH_DESCRIPTION) {
            log.error("Превышена максимальная длина описания: {}", film.getDescription().length());
            throw new ValidationException(MESSAGE_OF_VALID_DESCRIPTION);
        }
        if (film.getReleaseDate() == null) {
            log.error("Не указана дата релиза фильма");
            throw new ValidationException(MESSAGE_OF_VALID_RELEASE_DATE);
        }
        if (film.getReleaseDate().isBefore(MIN_RELEASE_DATE)) {
            log.error("Некорректная дата релиза - {}", film.getReleaseDate());
            throw new ValidationException(MESSAGE_OF_VALID_RELEASE_DATE_INCORRECT);
        }
        if (film.getDuration() <= 0) {
            log.error("Некорректная продолжительность фильма, сек - {}", film.getDuration());
            throw new ValidationException(MESSAGE_OF_VALID_DURATION);
        }
    }
}
