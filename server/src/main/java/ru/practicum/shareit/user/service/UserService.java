package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

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

    @Transactional(readOnly = true)
    public List<UserDTO> getUsers() {
        log.debug("Users not covert to DTO");
        return repository.findAll().stream().map(UserMapper::userToDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserDTO getUser(Long id) {
        log.debug("User id {} not convert to DTO", id);
        return UserMapper.userToDTO(repository.findById(id).orElseThrow(() -> new NotFoundException("user not found")));
    }

    @Transactional
    public UserDTO createUser(UserDTO userDTO) {
        User user = UserMapper.userFromDTO(userDTO);
        return UserMapper.userToDTO(repository.save(user));
    }

    @Transactional
    public UserDTO updateUser(UserDTO userDTO, Long id) {
        User user = UserMapper.userFromDTO(userDTO);
        return UserMapper.userToDTO(updateFields(user, id));
    }

    @Transactional
    public void deleteUser(Long id) {
        repository.deleteById(id);
    }

    private User updateFields(User user, Long userId) {
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

