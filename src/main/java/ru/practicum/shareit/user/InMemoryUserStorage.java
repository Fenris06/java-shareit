package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;

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
        users.remove(id);
    }

    @Override
    public void addUserItems(Long userId, Long itemId) {
        if (usersItems.containsKey(userId)) {
            Set<Long> itemIds = usersItems.get(userId);
            itemIds.add(userId);
            usersItems.replace(userId, itemIds);
        } else {
            Set<Long> itemIds = new HashSet<>();
            itemIds.add(itemId);
            usersItems.put(userId, itemIds);
        }
    }

    @Override
    public Set<Long> getUserItemsId(Long userId) {
        if (usersItems.containsKey(userId)) {
            return usersItems.get(userId);
        } else {
            throw new NotFoundException("User don't have any items");
        }
    }


}
