package ru.practicum.shareit.booking.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemBookingDTO;

import ru.practicum.shareit.user.dto.UserBookingDTO;


import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class BookingAnswerDTO {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private BookingStatus status;
    private ItemBookingDTO item;
    private UserBookingDTO booker;
}
