package ru.practicum.shareit.user;


public class UserMapper {

    public static User userFromDTO(UserDTO userDTO) {
        User user = new User();
        user.setId(user.getId());
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        return user;
    }

    public static UserDTO userToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        return userDTO;
    }
}
