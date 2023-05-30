package ru.practicum.shareit.user.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;


import ru.practicum.shareit.exception.NoArgumentException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDTO;


import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;

    @Test
    void should_createUser() {
        Long userId = 1L;
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userId);
        userDTO.setName("Artem");
        userDTO.setEmail("Artem@yandex.ru");

        User user = new User();
        user.setId(userId);
        user.setName("Artem");
        user.setEmail("Artem@yandex.ru");

        when(userRepository.save(any())).thenReturn(user);

        UserDTO testUser = userService.createUser(userDTO);

        assertEquals(testUser, userDTO);
        verify(userRepository).save(any());
    }

    @Test
    void shouldNot_createUser_ifEmailNotCheck() {
        Long userId = 1L;
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userId);
        userDTO.setName("Artem");
        userDTO.setEmail(null);

        NoArgumentException exception = assertThrows(NoArgumentException.class, () -> userService.createUser(userDTO));
        assertEquals("Email not set", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldNot_createUser_ifNameNotCheck() {
        Long userId = 1L;
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userId);
        userDTO.setName(null);
        userDTO.setEmail("Artem@yandex.ru");

        NoArgumentException exception = assertThrows(NoArgumentException.class, () -> userService.createUser(userDTO));

        assertEquals("Name not set", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void should_GetUsers_ReturnListUserDTO() {
        Long userId = 1L;
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userId);
        userDTO.setName("Artem");
        userDTO.setEmail("Artem@yandex.ru");

        User user = new User();
        user.setId(userId);
        user.setName("Artem");
        user.setEmail("Artem@yandex.ru");

        List<UserDTO> testList = List.of(userDTO);

        when(userRepository.findAll()).thenReturn(List.of(user));

        List<UserDTO> userDTOS = userService.getUsers();

        assertEquals(testList, userDTOS);
    }

    @Test
    void should_GetUser_ReturnUser_IfUserFound() {
        Long userId = 1L;
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userId);
        userDTO.setName("Artem");
        userDTO.setEmail("Artem@yandex.ru");

        User user = new User();
        user.setId(userId);
        user.setName("Artem");
        user.setEmail("Artem@yandex.ru");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        UserDTO testUser = userService.getUser(userId);

        assertEquals(testUser, userDTO);
    }

    @Test
    void should_GetUser_ReturnNotFoundException_IfUserNotFound() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> userService.getUser(userId));

        assertEquals("user not found", exception.getMessage());
    }

    @Test
    void should_updateUser_UpdateIfAllFieldsCorrect() {
        Long userId = 1L;
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userId);
        userDTO.setName("Ivan");
        userDTO.setEmail("Ivan@yandex.ru");

        User user = new User();
        user.setId(userId);
        user.setName("Artem");
        user.setEmail("Artem@yandex.ru");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenReturn(user);

        UserDTO testUser = userService.updateUser(userDTO, userId);

        assertEquals(testUser, userDTO);
        verify(userRepository).save(any());
    }

    @Test
    void should_updateUser_UpdateIfNameCorrect() {
        Long userId = 1L;
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userId);
        userDTO.setName(null);
        userDTO.setEmail("Ivan@yandex.ru");

        UserDTO update = new UserDTO();
        update.setId(userId);
        update.setName("Artem");
        update.setEmail("Ivan@yandex.ru");

        User user = new User();
        user.setId(userId);
        user.setName("Artem");
        user.setEmail("Artem@yandex.ru");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenReturn(user);

        UserDTO testUser = userService.updateUser(userDTO, userId);

        assertEquals(testUser, update);
        verify(userRepository).save(any());
    }

    @Test
    void should_updateUser_UpdateIfEmailCorrect() {
        Long userId = 1L;
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userId);
        userDTO.setName("Ivan");
        userDTO.setEmail(null);

        UserDTO update = new UserDTO();
        update.setId(userId);
        update.setName("Ivan");
        update.setEmail("Artem@yandex.ru");

        User user = new User();
        user.setId(userId);
        user.setName("Artem");
        user.setEmail("Artem@yandex.ru");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenReturn(user);

        UserDTO testUser = userService.updateUser(userDTO, userId);

        assertEquals(testUser, update);
        verify(userRepository).save(any());
    }

    @Test
    void shouldNot_UpdateUser_UpdateIfAllFieldsNull() {
        Long userId = 1L;
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userId);
        userDTO.setName(null);
        userDTO.setEmail(null);

        NoArgumentException e = assertThrows(NoArgumentException.class, () -> userService.updateUser(userDTO, userId));

        assertEquals("All fields are empty", e.getMessage());
    }

    @Test
    void deleteUser() {
        Long userId = 1L;
        userService.deleteUser(userId);
        verify(userRepository).deleteById(userId);
    }
}
