package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Set;

public interface UserStorage {

    List<User> getUsers();

    User getUser(Long id);

    User createUser(User user);

    User updateUser(User user);

    void deleteUser(Long id);

    void addUserItems(Long userId, Long itemId);

    Set<Long> getUserItemsId(Long userId);
}
