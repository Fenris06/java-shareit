package ru.practicum.shareit.item.model;



import java.util.List;

public interface ItemStorage {
    List<Item> getItems();

    Item getItem(Long id);

    Item createItem(Item item);

    Item updateItem(Item item);

    void deleteItem(Long id);


}

