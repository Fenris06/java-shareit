package ru.practicum.shareit.booking.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingAnswerDTO;
import ru.practicum.shareit.booking.dto.BookingDto;

import ru.practicum.shareit.booking.mapper.BookingMapper;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.NoArgumentException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;

import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;


import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository repository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public BookingAnswerDTO createBooking(Long userId, BookingDto bookingDto) {
        User user = userRepository.findById(userId).orElseThrow(()-> new NotFoundException("User not found"));
        Item item = itemRepository.findById(bookingDto.getItemId()).orElseThrow(()-> new NotFoundException("Item not found"));
        checkItemUser(item, user);
        checkItemStatus(item);
        Booking booking = BookingMapper.fromDto(bookingDto, item, user);
        checkBookingFields(booking);
        booking.setStatus(BookingStatus.WAITING);
        return BookingMapper.toDto(repository.save(booking));
    }

    @Override
    public BookingAnswerDTO updateBookingStatus(Long userId, Long bookingId, Boolean approved) {
        checkUser(userId);
        Booking booking = getBooking(bookingId);
        checkOwner(booking, userId);
        Booking updateBookingStatus = updateBookingFields(booking, approved);
        return BookingMapper.toDto(repository.save(updateBookingStatus));
    }

    @Override
    public BookingAnswerDTO getBookingByUser(Long userId, Long bookingId) {
        checkUser(userId);
        Booking booking = getBooking(bookingId);
        checkBookingUser(userId, booking);
        return BookingMapper.toDto(booking);
    }

    @Override
    public List<BookingAnswerDTO> getAllByUser(Long userId, String state) {
        checkUser(userId);
        LocalDateTime verification = LocalDateTime.now();
        switch (state) {
            case "ALL":
                return repository.findByBooker_IdOrderByStartDesc(userId)
                        .stream()
                        .map(BookingMapper::toDto)
                        .collect(Collectors.toList());
            case "FUTURE":
                return repository.findByBooker_IdAndStartAfterOrderByStartDesc(userId, verification)
                        .stream()
                        .map(BookingMapper::toDto)
                        .collect(Collectors.toList());
            case "PAST":
                return repository.findByBooker_IdAndEndBeforeOrderByStartDesc(userId, verification)
                        .stream()
                        .map(BookingMapper::toDto)
                        .collect(Collectors.toList());
            case "CURRENT":
                return repository.findByBooker_IdAndStartAfterAndEndBeforeOrderByStartDesc(userId,
                                verification,
                                verification)
                        .stream()
                        .map(BookingMapper::toDto)
                        .collect(Collectors.toList());
            case "WAITING":
                return repository.findByBooker_IdAndStatusOrderByStartDesc(userId, BookingStatus.WAITING)
                        .stream()
                        .map(BookingMapper::toDto)
                        .collect(Collectors.toList());
            case "REJECTED":
                return repository.findByBooker_IdAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED)
                        .stream().
                        map(BookingMapper::toDto)
                        .collect(Collectors.toList());
            default:
                throw new NoArgumentException("Unknown state: UNSUPPORTED_STATUS");
        }
    }

    @Override
    public List<BookingAnswerDTO> getAllByOwner(Long userId, String state) {
        List<Booking> ownerBooking;
        LocalDateTime verification = LocalDateTime.now();
        switch (state) {
            case "ALL": {
                ownerBooking = repository.findByItem_OwnerOrderByStartDesc(userId);
                break;
            }
            case "FUTURE": {
                ownerBooking = repository.findByItem_OwnerAndStartAfterOrderByStartDesc(userId, verification);
                break;
            }
            case "PAST": {
                ownerBooking = repository.findByItem_OwnerAndEndBeforeOrderByStartDesc(userId, verification);
                break;
            }
            case "CURRENT": {
                ownerBooking = repository.findByItem_OwnerAndStartAfterAndEndBeforeOrderByStartDesc(userId,
                        verification,
                        verification);
                break;
            }
            case "WAITING": {
                ownerBooking = repository.findByItem_OwnerAndStatusOrderByStartDesc(userId, BookingStatus.WAITING);
                break;
            }
            case "REJECTED": {
                ownerBooking =repository.findByItem_OwnerAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED);
                break;
            }
            default:
                throw new NoArgumentException("Unknown state: UNSUPPORTED_STATUS");
        }
        checkOwnerList(ownerBooking);
        return ownerBooking.stream().map(BookingMapper::toDto).collect(Collectors.toList());
    }

    private void checkBookingFields(Booking booking) {
        if (booking.getStart() == null || booking.getStart().isBefore(LocalDateTime.now())) {
            throw new NoArgumentException("Start time not add or start time is in the past");
        }
        if (booking.getEnd() == null || booking.getEnd().isBefore(booking.getStart())) {
            throw new NoArgumentException("End time not add or end time is before start time");
        }
        if (booking.getStart().isEqual(booking.getEnd())) {
            throw new NoArgumentException("Start time can't be equal end time");
        }
    }

    private void checkItemStatus(Item item) {
        if (!item.getAvailable()) {
            throw new NoArgumentException("Item not available");
        }
    }

    private void checkOwner(Booking booking, Long userId) {
        if (!Objects.equals(booking.getItem().getOwner(), userId)) {
            throw new NotFoundException("This user can't approved booking status");
        }
    }

    private void checkItemUser(Item item, User user) {
        if (Objects.equals(item.getOwner(), user.getId())) {
            throw new NotFoundException("Item owner can't create booking " + user.getId());
        }
    }

    private void checkOwnerList(List<Booking> list) {
        if (list.isEmpty()) {
            throw new NotFoundException("This user don't have any items");
        }
    }

    private Booking updateBookingFields(Booking booking, Boolean approved) {
        if (booking.getStatus().equals(BookingStatus.APPROVED)) {
            throw new NoArgumentException("You can't change status if it approved");
        }
        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }
        return booking;
    }

    private void checkBookingUser(Long userId, Booking booking) {
        if(Objects.equals(booking.getBooker().getId(), userId)
                || Objects.equals(booking.getItem().getOwner(), userId)) {
        } else {
            throw new NotFoundException("This user can't see this booking");
        }
    }

    public Booking getBooking(Long bookingId) {
        return repository.findById(bookingId).orElseThrow(() -> new NotFoundException("Booking not create"));
    }

    private void checkUser(Long userId) {
        userRepository.findById(userId).orElseThrow(()-> new NotFoundException("User not found"));
    }
}
