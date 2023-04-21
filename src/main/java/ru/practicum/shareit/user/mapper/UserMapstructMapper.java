package ru.practicum.shareit.user.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.model.User;
@Mapper
public interface UserMapstructMapper {

    User userFromDTO(UserDTO userDTO);

    UserDTO userToDTO(User user);
}
