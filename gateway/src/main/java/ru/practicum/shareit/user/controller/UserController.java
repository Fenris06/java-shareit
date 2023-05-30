package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.client.UserClient;
import ru.practicum.shareit.user.dto.UserDTO;


import javax.validation.Valid;


/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserClient client;

    @GetMapping
    public ResponseEntity<Object> getUsers() {
        log.debug("received GET /users");
        return client.getUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUser(@PathVariable("id") Long id) {
        log.debug("received GET /users/{}", id);
        return client.getUser(id);
    }

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody UserDTO userDTO) {
        log.debug("received POST /users with body {}", userDTO);
        return client.createUser(userDTO);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@Valid @RequestBody UserDTO userDTO, @PathVariable("id") Long id) {
        log.debug("received PATCH /users/{} and BODY {}", id, userDTO);
        return client.updateUser(id, userDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") Long id) {
        log.debug("received DELETE /users/{}", id);
        return client.delete(id);
    }
}