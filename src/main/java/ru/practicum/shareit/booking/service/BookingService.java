package ru.practicum.shareit.booking.service;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingAnswerDTO;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {
    @Transactional
    BookingAnswerDTO createBooking(Long userId, BookingDto bookingDto);

    @Transactional
    BookingAnswerDTO updateBookingStatus(Long userId, Long bookingId, Boolean approved);

    @Transactional(readOnly = true)
    BookingAnswerDTO getBookingByUser(Long userId, Long bookingId);

    @Transactional(readOnly = true)
    List<BookingAnswerDTO> getAllByUser(Long userId, String state);

    @Transactional(readOnly = true)
    List<BookingAnswerDTO> getAllByOwner(Long userId, String state);
}
