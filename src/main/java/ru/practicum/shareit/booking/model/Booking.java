package ru.practicum.shareit.booking.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Booking {
   private Long id;
   private LocalDateTime start;
   private LocalDateTime end;
   private Long itemId;
   private Long bookerId;
   private String status;
}
