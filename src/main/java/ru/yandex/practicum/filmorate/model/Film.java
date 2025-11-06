package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public final class Film {
    private long id;
    private final String name;
    private final String description;
    private final LocalDate releaseDate;
    private final int duration;
    private Set<Long> likes = new HashSet<>();


    public int getLikesSize() {
        return likes.size();
    }

    public void addLike(Long id) {
        likes.add(id);
    }

    public void deleteLike(Long id) {
        likes.remove(id);
    }
}
