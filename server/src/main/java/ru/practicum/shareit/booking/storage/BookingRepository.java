package ru.practicum.shareit.booking.storage;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import ru.practicum.shareit.booking.model.Booking;

import ru.practicum.shareit.booking.model.BookingStatus;


import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;


public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByBooker_IdOrderByStartDesc(Long id, Pageable pageable);

    List<Booking> findByItem_OwnerOrderByStartDesc(Long owner, Pageable pageable);

    List<Booking> findByBooker_IdAndStartAfterOrderByStartDesc(Long id, LocalDateTime start, Pageable pageable);

    List<Booking> findByBooker_IdAndEndBeforeOrderByStartDesc(Long id, LocalDateTime end, Pageable pageable);

    List<Booking> findByBooker_IdAndStartBeforeAndEndAfterOrderByStartDesc(Long id,
                                                                           LocalDateTime start,
                                                                           LocalDateTime end, Pageable pageable);

    List<Booking> findByBooker_IdAndStatusOrderByStartDesc(Long id, BookingStatus status, Pageable pageable);

    List<Booking> findByItem_OwnerAndStartAfterOrderByStartDesc(Long owner, LocalDateTime start, Pageable pageable);

    List<Booking> findByItem_OwnerAndEndBeforeOrderByStartDesc(Long owner, LocalDateTime end, Pageable pageable);

    List<Booking> findByItem_OwnerAndStartBeforeAndEndAfterOrderByStartDesc(Long owner,
                                                                            LocalDateTime start,
                                                                            LocalDateTime end, Pageable pageable);

    List<Booking> findByItem_OwnerAndStatusOrderByStartDesc(Long owner, BookingStatus status, Pageable pageable);

    List<Booking> findByItem_IdAndStatusOrderByStartAsc(Long id, BookingStatus status);

    List<Booking> findByItem_IdInAndStatusOrderByStartAsc(Collection<Long> ids, BookingStatus status);

    List<Booking> findByBooker_IdAndItem_IdAndEndBefore(Long id, Long id1, LocalDateTime end);
}