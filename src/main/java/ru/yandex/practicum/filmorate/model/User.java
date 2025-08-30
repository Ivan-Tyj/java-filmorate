package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public final class User {
    private long id;
    private final String email;
    private final String login;
    private String name;
    private final LocalDate birthday;
    private Set<Long> friends = new HashSet<>();
}
