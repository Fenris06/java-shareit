package ru.practicum.shareit.booking.validation;

import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.exception.NoArgumentException;

public class BookingValidation {

    public static void checkBookingTime(BookItemRequestDto bookItemRequestDto) {
        if (bookItemRequestDto.getEnd().isBefore(bookItemRequestDto.getStart())) {
            throw new NoArgumentException("End time not add or end time is before start time");
        }
        if (bookItemRequestDto.getStart().isEqual(bookItemRequestDto.getEnd())) {
            throw new NoArgumentException("Start time can't be equal end time");
        }
    }

    public static BookingState checkBookingState(String stateParam) {
        return BookingState.from(stateParam)
                .orElseThrow(() -> new NoArgumentException("Unknown state: " + stateParam));
    }
}
