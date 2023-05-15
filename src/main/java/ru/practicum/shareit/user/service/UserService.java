package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import ru.practicum.shareit.exception.NoArgumentException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDTO;

import ru.practicum.shareit.user.mapper.UserMapper;

import ru.practicum.shareit.user.storage.UserRepository;
import ru.practicum.shareit.user.model.User;


import java.util.List;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository repository;

    public List<UserDTO> getUsers() {
        log.debug("Users not covert to DTO");
        return repository.findAll().stream().map(UserMapper::userToDTO).collect(Collectors.toList());
    }

    public UserDTO getUser(Long id) {
        log.debug("User id {} not convert to DTO", id);
        return UserMapper.userToDTO(repository.findById(id).orElseThrow(() -> new NotFoundException("user not found")));
    }

    public UserDTO createUser(UserDTO userDTO) {
        User user = UserMapper.userFromDTO(userDTO);
        checkUserName(user.getName());
        checkUserEmail(user.getEmail());
        return UserMapper.userToDTO(repository.save(user));
    }

    public UserDTO updateUser(UserDTO userDTO, Long id) {
        User user = UserMapper.userFromDTO(userDTO);
        return UserMapper.userToDTO(updateFields(user, id));
    }

    public void deleteUser(Long id) {
        repository.deleteById(id);
    }

    private void checkUserEmail(String email) {
        if (email == null) {
            throw new NoArgumentException("Email not set");
        }
    }

    private void checkUserName(String name) {
        if (name == null) {
            throw new NoArgumentException("Name not set");
        }
    }

    private User updateFields(User user, Long userId) {
        if (user.getName() == null && user.getEmail() == null) {
            throw new NoArgumentException("All fields are empty");
        }
        User updateUser = repository.findById(userId).orElseThrow(() -> new NotFoundException("user not found"));
        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            if (!updateUser.getEmail().equals(user.getEmail())) {
                updateUser.setEmail(user.getEmail());
            }
        }
        if (user.getName() != null && !user.getName().isEmpty()) {
            updateUser.setName(user.getName());
        }
        return repository.save(updateUser);
    }
}

