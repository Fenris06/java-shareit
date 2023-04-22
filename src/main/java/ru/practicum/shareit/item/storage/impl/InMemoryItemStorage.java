package ru.practicum.shareit.item.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;

import java.util.*;

@Repository
@Slf4j
public class InMemoryItemStorage implements ItemStorage {
    private long id = 1;
    private final Map<Long, Item> items = new HashMap<>();

    @Override
    public List<Item> getItems() {
        return new ArrayList<>(items.values());
    }

    @Override
    public Item getItem(Long id) {
        if (items.get(id) != null) {
            log.debug("Item id {} not return", id);
            return items.get(id);
        } else {
            log.debug("Item id {} not found", id);
            throw new NotFoundException("Item not found");
        }
    }

    @Override
    public Item createItem(Item item) {
        item.setId(id++);
        log.debug("Item id {} not add", item.getId());
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item updateItem(Item item) {
        log.debug("Item id {} not update", item.getId());
        items.replace(item.getId(), item);
        return item;
    }

    @Override
    public void deleteItem(Long id) {
        log.debug("Item id {} not delete", id);
        items.remove(id);
    }
}
