package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DuplicateException;
import ru.practicum.shareit.exception.NoArgumentException;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.storage.UserStorage;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserStorage userStorage;
    private final Set<String> emails = new TreeSet<>();

    public List<UserDTO> getUsers() {
        log.debug("Users not covert to DTO");
        return userStorage.getUsers().stream().map(UserMapper::userToDTO).collect(Collectors.toList());
    }

    public UserDTO getUser(Long id) {
        log.debug("User id {} not convert to DTO", id);
        return UserMapper.userToDTO(userStorage.getUser(id));
    }

    public UserDTO createUser(UserDTO userDTO) {
        User user = UserMapper.userFromDTO(userDTO);
        checkUserName(user.getName());
        checkUserEmail(user.getEmail());
        emails.add(user.getEmail());
        return UserMapper.userToDTO(userStorage.createUser(user));
    }

    public UserDTO updateUser(UserDTO userDTO, Long id) {
        User user = UserMapper.userFromDTO(userDTO);
        return UserMapper.userToDTO(updateFields(user, id));
    }

    public void deleteUser(Long id) {
        User user = userStorage.getUser(id);
        emails.remove(user.getEmail());
        userStorage.deleteUser(id);
    }

    public void checkUser(Long id) {
        userStorage.getUser(id);
    }

    private void checkUserEmail(String email) {
        if (email == null) {
            throw new NoArgumentException("Email not set");
        }
        if (emails.contains(email)) {
            throw new DuplicateException("Email is already use");
        }
    }

    private void checkUserName(String name) {
        if (name == null) {
            throw new NoArgumentException("Name not set");
        }
    }

    public void addUserItemId(Long userId, Long itemId) {
        userStorage.addUserItems(userId, itemId);
    }

    public List<Long> getUserItemsId(Long userId) {
        return new ArrayList<>(userStorage.getUserItemsId(userId));
    }

    private User updateFields(User user, Long userId) {
        if (user.getName() == null && user.getEmail() == null) {
            throw new NoArgumentException("All fields are empty");
        }
        User updateUser = userStorage.getUser(userId);
        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            if (!updateUser.getEmail().equals(user.getEmail())) {
                checkUserEmail(user.getEmail());
                emails.remove(updateUser.getEmail());
                updateUser.setEmail(user.getEmail());
                emails.add(updateUser.getEmail());
            }
        }
        if (user.getName() != null && !user.getName().isEmpty()) {
            updateUser.setName(user.getName());
        }
        return userStorage.updateUser(updateUser);
    }
}

