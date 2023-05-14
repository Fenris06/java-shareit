package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.booking.dto.BookingForItemDTO;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemDateBookingDto;
import ru.practicum.shareit.item.model.Item;

public class ItemDateMapper {
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
