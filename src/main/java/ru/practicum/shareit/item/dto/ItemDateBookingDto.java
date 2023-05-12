package ru.practicum.shareit.item.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.booking.dto.BookingForItemDTO;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ItemDateBookingDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private BookingForItemDTO lastBooking;
    private BookingForItemDTO nextBooking;
}
