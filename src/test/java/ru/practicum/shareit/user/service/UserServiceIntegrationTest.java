package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDTO;

import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest()
class UserServiceIntegrationTest {
    UserDTO firstUser;
    UserDTO secondUser;
    UserDTO testFirst;
    UserDTO testSecond;
    @Autowired
    private UserRepository repository;
    @Autowired
    private UserService userService;

    @BeforeEach
    private void beforeEach() {
        firstUser = new UserDTO();
        firstUser.setName("Artem");
        firstUser.setEmail("Artem@yandex.ru");
        testFirst = userService.createUser(firstUser);

        secondUser = new UserDTO();
        secondUser.setName("Ivan");
        secondUser.setEmail("Ivan@yandex.ru");
        testSecond = userService.createUser(secondUser);

    }

    @AfterEach
    private void afterEach() {
        repository.deleteAll();
    }


    @Test
    void should_GetUsers_returnListUsers() {
        List<UserDTO> test = userService.getUsers();

        assertEquals(test.size(), 2);
        assertEquals(test.get(0).getName(), testFirst.getName());
        assertEquals(test.get(1).getName(), testSecond.getName());
    }

    @Test
    void should_GetUser_ReturnUser_IfUserFind() {
        UserDTO test = userService.getUser(testFirst.getId());

        assertEquals(test.getName(), testFirst.getName());
        assertEquals(test.getEmail(), testFirst.getEmail());
    }

    @Test
    void should_CreateUser_CreateNewUser() {
        UserDTO newUser = new UserDTO();
        newUser.setName("Maksim");
        newUser.setEmail("Maksim@yandex.ru");

        UserDTO test = userService.createUser(newUser);

        assertEquals(test.getName(), newUser.getName());
        assertEquals(test.getEmail(), newUser.getEmail());
    }

    @Test
    void shouldNot_CreateUser_WithSameEmails() {
        UserDTO newUser = new UserDTO();
        newUser.setName("Maksim");
        newUser.setEmail("Artem@yandex.ru");

        assertThrows(DataIntegrityViolationException.class, () -> userService.createUser(newUser));
    }

    @Test
    void should_UpdateUser_UpdateUser_IfFiledIsValid() {
        UserDTO update = new UserDTO();
        update.setEmail("Thor@yandex.ru");

        UserDTO test = userService.updateUser(update, testFirst.getId());

        assertEquals(test.getName(), testFirst.getName());
        assertEquals(test.getEmail(), update.getEmail());
    }

    @Test
    void should_DeleteUser_DeleteUserById() {
        userService.deleteUser(testFirst.getId());

        assertThrows(NotFoundException.class, () -> userService.getUser(testFirst.getId()));
    }
}