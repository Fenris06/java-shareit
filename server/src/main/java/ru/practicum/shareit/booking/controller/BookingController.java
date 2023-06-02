package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingAnswerDTO;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;


import java.util.List;


@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService service;

    @PostMapping
    public BookingAnswerDTO createBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                          @RequestBody BookingDto bookingDto) {
        return service.createBooking(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingAnswerDTO updateBookingStatus(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                @PathVariable("bookingId") Long bookingId,
                                                @RequestParam("approved") Boolean approved) {
        return service.updateBookingStatus(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingAnswerDTO getBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                       @PathVariable("bookingId") Long bookingId) {
        return service.getBookingByUser(userId, bookingId);
    }

    @GetMapping
    public List<BookingAnswerDTO> getAllByUser(@RequestHeader("X-Sharer-User-Id") Long userId,
                                               @RequestParam(name = "state") String state,
                                               @RequestParam(name = "from") Integer from,
                                               @RequestParam(name = "size") Integer size) {
        return service.getAllByUser(userId, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingAnswerDTO> getAllByOwner(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                @RequestParam(name = "state") String state,
                                                @RequestParam(name = "from") Integer from,
                                                @RequestParam(name = "size") Integer size) {
        return service.getAllByOwner(userId, state, from, size);
    }
}
