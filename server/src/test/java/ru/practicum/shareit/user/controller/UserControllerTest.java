package ru.practicum.shareit.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDTO;

import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(value = UserController.class)
class UserControllerTest {
    @MockBean
    UserService userService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @SneakyThrows
    @Test
    void shouldGetUsers_WhenTwoUsersFound_ReturnListUsers() {
        UserDTO user1 = new UserDTO();
        user1.setId(1L);
        user1.setName("Artem");
        user1.setEmail("Artem@yandex.ru");

        UserDTO user2 = new UserDTO();
        user2.setId(2L);
        user2.setName("Ivan");
        user2.setEmail("Ivan@yandex.ru");

        when(userService.getUsers()).thenReturn(List.of(user1, user2));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(user1.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(user1.getName())))
                .andExpect(jsonPath("$[0].email", is((user1.getEmail()))))
                .andExpect(jsonPath("$[1].id", is(user2.getId()), Long.class))
                .andExpect(jsonPath("$[1].name", is(user2.getName())))
                .andExpect(jsonPath("$[1].email", is((user2.getEmail()))));

        verify(userService).getUsers();
    }

    @SneakyThrows
    @Test
    void shouldNotGetUsers_WhenUsersNotFound_ReturnListUsersIsEmpty() {
        when(userService.getUsers()).thenReturn(List.of());

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(userService).getUsers();
    }

    @SneakyThrows
    @Test
    void should_GetUser_Return_User_By_Id() {
        UserDTO user = new UserDTO();
        user.setId(1L);
        user.setName("Artem");
        user.setEmail("Artem@yandex.ru");
        Long userId = 1L;

        when(userService.getUser(userId)).thenReturn(user);

        mockMvc.perform(get("/users/{id}", userId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(user.getName())))
                .andExpect(jsonPath("$.email", is((user.getEmail()))));

        verify(userService).getUser(userId);
    }

    @SneakyThrows
    @Test
    void should_GetUser_Return_NotFoundException_IfUserNOtFoUnd() {
        UserDTO user = new UserDTO();
        user.setId(1L);
        user.setName("Artem");
        user.setEmail("Artem@yandex.ru");

        when(userService.getUser(user.getId())).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/users/{id}", user.getId()))
                .andExpect(status().isNotFound());

        verify(userService).getUser(user.getId());
    }

    @SneakyThrows
    @Test
    void should_Create_GreatNewUser_IfAllFieldsAreCorrect() {
        UserDTO user = new UserDTO();
        user.setId(1L);
        user.setName("Artem");
        user.setEmail("Artem@yandex.ru");

        when(userService.createUser(any())).thenReturn(user);

        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(user.getName())))
                .andExpect(jsonPath("$.email", is((user.getEmail()))));

        verify(userService).createUser(user);
    }

//    @SneakyThrows
//    @Test
//    void shouldNot_Create_GreatNewUser_IfEmailFieldIsNotCorrect() {
//        UserDTO user = new UserDTO();
//        user.setId(1L);
//        user.setName("Artem");
//        user.setEmail("Artemyandex.ru");
//
//        mockMvc.perform(post("/users")
//                        .content(objectMapper.writeValueAsString(user))
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
//
//        verify(userService, never()).createUser(user);
//    }

    @SneakyThrows
    @Test
    void should_Update_UpdateUserField_IfFieldIsCorrect() {
        Long id = 1L;
        UserDTO updateField = new UserDTO();
        updateField.setName("Ivan");
        UserDTO user = new UserDTO();
        user.setId(1L);
        user.setName("Ivan");
        user.setEmail("Artem@yandex.ru");

        when(userService.updateUser(updateField, id)).thenReturn(user);

        mockMvc.perform((patch("/users/{id}", id))
                        .content(objectMapper.writeValueAsString(updateField))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(user.getName())))
                .andExpect(jsonPath("$.email", is((user.getEmail()))));

        verify(userService).updateUser(updateField, id);
    }

//    @SneakyThrows
//    @Test
//    void shouldNot_Update_UpdateUserField_IfFieldEmailIsNotCorrect() {
//        Long id = 1L;
//        UserDTO updateField = new UserDTO();
//        updateField.setEmail("Artemyandex.ru");
//
//        mockMvc.perform((patch("/users/{id}", id))
//                        .content(objectMapper.writeValueAsString(updateField))
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
//
//        verify(userService, never()).updateUser(updateField, id);
//    }

    @SneakyThrows
    @Test
    void should_Delete_DeleteUserById() {
        Long id = 1L;

        mockMvc.perform(delete("/users/{id}", id))
                .andExpect(status().isOk());

        verify(userService).deleteUser(id);
    }
}
