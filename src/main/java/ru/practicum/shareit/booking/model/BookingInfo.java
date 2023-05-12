package ru.practicum.shareit.booking.model;

import java.time.LocalDateTime;

/**
 * A Projection for the {@link Booking} entity
 */
public interface BookingInfo {
    Long getId();

    LocalDateTime getStart();

    LocalDateTime getEnd();
}