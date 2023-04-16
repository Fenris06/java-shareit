package ru.practicum.shareit.user;

import java.util.List;

public interface UserStorage {
    List<User> getUsers();

    User getUser(Long id);

    User createUser(User user);

    User updateUser(User user);

    void deleteUser(Long id);


}
