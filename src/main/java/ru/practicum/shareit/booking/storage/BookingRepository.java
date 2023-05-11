package ru.practicum.shareit.booking.storage;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;


import java.time.LocalDateTime;
import java.util.List;


public interface BookingRepository extends JpaRepository<Booking, Long> {
     List<Booking> findByBooker_IdOrderByStartDesc(Long id);

     List<Booking> findByItem_OwnerOrderByStartDesc(Long owner);

     List<Booking> findByBooker_IdAndStartAfterOrderByStartDesc(Long id, LocalDateTime start);

     List<Booking> findByBooker_IdAndEndBeforeOrderByStartDesc(Long id, LocalDateTime end);

     List<Booking> findByBooker_IdAndStartAfterAndEndBeforeOrderByStartDesc(Long id,
                                                                            LocalDateTime start,
                                                                            LocalDateTime end);

     List<Booking> findByBooker_IdAndStatusOrderByStartDesc(Long id, BookingStatus status);

     List<Booking> findByItem_OwnerAndStartAfterOrderByStartDesc(Long owner, LocalDateTime start);

     List<Booking> findByItem_OwnerAndEndBeforeOrderByStartDesc(Long owner, LocalDateTime end);

     List<Booking> findByItem_OwnerAndStartAfterAndEndBeforeOrderByStartDesc(Long owner,
                                                                             LocalDateTime start,
                                                                             LocalDateTime end);

     List<Booking> findByItem_OwnerAndStatusOrderByStartDesc(Long owner, BookingStatus status);







}