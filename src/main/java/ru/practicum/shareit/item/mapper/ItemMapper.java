package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.booking.dto.BookingForItemDTO;

import ru.practicum.shareit.item.dto.ItemDateBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

public class ItemMapper {
    public static Item itemFromDTO(ItemDto itemDto) {
        Item item = new Item();
        item.setId(itemDto.getId());
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        return item;
    }

    public static ItemDto itemToDTO(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        return itemDto;
    }

    public static ItemDateBookingDto toItemWithDate(Item item, BookingForItemDTO first, BookingForItemDTO last) {
        ItemDateBookingDto dateBookingDto = new ItemDateBookingDto();
        dateBookingDto.setId(item.getId());
        dateBookingDto.setName(item.getName());
        dateBookingDto.setDescription(item.getDescription());
        dateBookingDto.setAvailable(item.getAvailable());
        dateBookingDto.setLastBooking(first);
        dateBookingDto.setNextBooking(last);
        return dateBookingDto;
    }
}
