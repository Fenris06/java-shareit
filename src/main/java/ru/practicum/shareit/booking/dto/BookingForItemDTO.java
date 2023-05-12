package ru.practicum.shareit.booking.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class BookingForItemDTO {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
}
