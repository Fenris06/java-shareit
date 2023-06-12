package ru.practicum.shareit.user.uservalidation;

import ru.practicum.shareit.exception.NoArgumentException;
import ru.practicum.shareit.user.dto.UserDTO;

public class UserValidation {

    public static void checkUserFields(UserDTO userDTO) {
        if (userDTO.getName() == null || userDTO.getName().isEmpty()) {
            throw new NoArgumentException("Name not set");
        }
        if (userDTO.getEmail() == null || userDTO.getEmail().isEmpty()) {
            throw new NoArgumentException("Email not set");
        }
    }

    public static void checkUserUpdateFields(UserDTO userDTO) {
        if (userDTO.getName() == null && userDTO.getEmail() == null) {
            throw new NoArgumentException("All fields are empty");
        }
    }
}
