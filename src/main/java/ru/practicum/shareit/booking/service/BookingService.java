package ru.practicum.shareit.booking.service;


import ru.practicum.shareit.booking.dto.BookingAnswerDTO;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {
    BookingAnswerDTO createBooking(Long userId, BookingDto bookingDto);

    BookingAnswerDTO updateBookingStatus(Long userId, Long bookingId, Boolean approved);

    BookingAnswerDTO getBookingByUser(Long userId, Long bookingId);

    List<BookingAnswerDTO> getAllByUser(Long userId, String state, Integer from, Integer size);

    List<BookingAnswerDTO> getAllByOwner(Long userId, String state, Integer from, Integer size);
}
