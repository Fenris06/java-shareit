package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingAnswerDTO;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Validated
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
                                               @RequestParam(name = "state",
                                                       defaultValue = "ALL",
                                                       required = false) String state,
                                               @RequestParam(name = "from", defaultValue = "0") @Min(0) Integer from,
                                               @RequestParam(name = "size", defaultValue = "20") @Min(1) @Max(100) Integer size) {
        return service.getAllByUser(userId, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingAnswerDTO> getAllByOwner(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                @RequestParam(name = "state",
                                                        defaultValue = "ALL",
                                                        required = false) String state,
                                                @RequestParam(name = "from", defaultValue = "0") @Min(0) Integer from,
                                                @RequestParam(name = "size", defaultValue = "20") @Min(1) @Max(100) Integer size) {
        return service.getAllByOwner(userId, state, from, size);
    }
}
