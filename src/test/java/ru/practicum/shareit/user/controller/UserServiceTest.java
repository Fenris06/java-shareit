package ru.practicum.shareit.user.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDTO;

import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    UserMapper userMapper;
    @Mock
    UserRepository userRepository;
    @InjectMocks
     UserService userService;

    @Test
    void should_createUser() {
//        Long userId = 1L;
//        UserDTO userDTO = new UserDTO();
//        userDTO.setId(userId);
//        userDTO.setName("Artem");
//        userDTO.setEmail("Artem@yandex.ru");
//        User user = new User();
//        user.setId(userId);
//        user.setName("Artem");
//        user.setEmail("Artem@yandex.ru");
//
//     when(userRepository.save(user)).thenReturn(user);
//
//
//     UserDTO testUser = userService.createUser(userDTO);
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

        NotFoundException exception = assertThrows(NotFoundException.class, ()-> userService.getUser(userId));

        assertEquals("user not found", exception.getMessage());
    }



    @Test
    void updateUser() {
    }

    @Test
    void deleteUser() {
        Long userId = 1L;
        userService.deleteUser(userId);
        verify(userRepository).deleteById(userId);
    }
}