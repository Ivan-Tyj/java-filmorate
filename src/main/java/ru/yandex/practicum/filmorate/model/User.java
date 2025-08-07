package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.LocalDate;

@Data
public final class User {
    private int id;
    private final String email;
    private final String login;
    private String name;
    private final LocalDate birthday;
}
