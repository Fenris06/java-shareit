package ru.practicum.shareit.item.storage;



import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {
    List<Item> getItems();

    Item getItem(Long id);

    Item createItem(Item item);

    Item updateItem(Item item);

    void deleteItem(Long id);
}

