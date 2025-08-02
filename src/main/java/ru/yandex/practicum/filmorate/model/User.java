package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class User {
    private int id;
    @NonNull
    private final String email;
    private final String login;
    @NonNull
    private String name;
    private final LocalDate birthday;
}
