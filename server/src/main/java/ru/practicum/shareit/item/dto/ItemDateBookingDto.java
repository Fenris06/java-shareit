package ru.practicum.shareit.item.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.booking.dto.BookingForItemDTO;
import ru.practicum.shareit.comment.dto.AnswerCommentDTO;


import java.util.ArrayList;
import java.util.List;

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
    private final List<AnswerCommentDTO> comments = new ArrayList<>();
}
