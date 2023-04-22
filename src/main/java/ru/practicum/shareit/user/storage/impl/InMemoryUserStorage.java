package ru.practicum.shareit.user.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.*;

@Repository
@Slf4j
@Primary
public class InMemoryUserStorage implements UserStorage {
    long id = 1;
    private final Map<Long, User> users = new HashMap<>();
    private final Map<Long, Set<Long>> usersItems = new HashMap<>();

    @Override
    public List<User> getUsers() {
        log.debug("Users list not return)");
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUser(Long id) {
        if (users.containsKey(id)) {
            log.debug("User id {} not return", id);
            return users.get(id);
        } else {
            log.debug("User id {} not found", id);
            throw new NotFoundException("User not found");
        }
    }

    @Override
    public User createUser(User user) {
        user.setId(id++);
        log.debug("User {} not add", user);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        log.debug("User {} not update", user);
        users.replace(user.getId(), user);
        return user;
    }

    @Override
    public void deleteUser(Long id) {
        log.debug("User {} not delete", id);
        users.remove(id);
    }

    @Override
    public void addUserItems(Long userId, Long itemId) {
        if (usersItems.containsKey(userId)) {
            Set<Long> itemIds = usersItems.get(userId);
            itemIds.add(userId);
            log.debug("User {} with Item {} not add", userId, itemId);
            usersItems.replace(userId, itemIds);
        } else {
            Set<Long> itemIds = new HashSet<>();
            itemIds.add(itemId);
            log.debug("User {} with Item {} not add", userId, itemId);
            usersItems.put(userId, itemIds);
        }
    }

    @Override
    public Set<Long> getUserItemsId(Long userId) {
        if (usersItems.containsKey(userId)) {
            log.debug("User {} itemIds not return", userId);
            return usersItems.get(userId);
        } else {
            throw new NotFoundException("User don't have any items");
        }
    }
}
