package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDateBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    List<ItemDateBookingDto> getUserItems(Long userId);

    ItemDateBookingDto getItem(Long userId, Long id);

    ItemDto createItem(Long userId, ItemDto itemDto);

    ItemDto updateItem(Long userId, ItemDto itemDto, Long itemId);

    List<ItemDto> itemSearch(Long userId, String text);

    Item itemForBooking(Long id);
}
