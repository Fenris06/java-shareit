package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDTO> getUsers() {
       return userService.getUsers();
    }

    @GetMapping("/{id}")
    public UserDTO getUser(@PathVariable("id") Long id) {
        return userService.getUser(id);
    }

    @PostMapping
    public UserDTO create(@Valid @RequestBody  UserDTO userDTO) {
        log.debug("received POST /users with body {}", userDTO);
        return userService.createUser(userDTO);
    }

    @PatchMapping("/{id}")
    public UserDTO update(@Valid @RequestBody UserDTO userDTO, @PathVariable("id") Long id) {
        return userService.updateUser(userDTO, id);
    }

    @DeleteMapping("/{id}")
    public Long delete(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return id;
    }
}
