package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.Collection;
import java.util.Set;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final InMemoryUserStorage inMemoryUserStorage;

    @GetMapping
    public Collection<User> findAll() {
        return inMemoryUserStorage.findAll();
    }

    @GetMapping("/users/{id}")
    public User findById(@PathVariable("id") int id) {
        return inMemoryUserStorage.findById(id);
    }

    @PostMapping
    public User create(@RequestBody User user) {
        return inMemoryUserStorage.create(user);
    }

    @PutMapping
    public User update(@RequestBody User user) {
        return inMemoryUserStorage.update(user);
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public Set<Long> addFriend(@PathVariable("id") long id,
                               @PathVariable("friendId") long friendId) {
        return userService.addFriend(id, friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public Set<Long> deleteFriend(@PathVariable("id") long id,
                                  @PathVariable("friendId") long friendId) {
        return userService.deleteFriend(id, friendId);
    }

    @GetMapping("/users/{id}/friends")
    public Collection<User> findAllFriends(@PathVariable("id") long id) {
        return userService.findAllFriends(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public Collection<User> findCommonFriends(@PathVariable("id") long id,
                                              @PathVariable("otherId") long otherId) {
        return userService.findCommonFriends(id, otherId);
    }

    @DeleteMapping
    public void delete(@RequestBody User user) {
        inMemoryUserStorage.delete(user);
    }
}
