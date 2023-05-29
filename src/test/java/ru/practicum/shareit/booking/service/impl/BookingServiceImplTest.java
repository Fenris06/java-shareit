package ru.practicum.shareit.booking.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.dto.BookingAnswerDTO;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.NoArgumentException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {
    @InjectMocks
    private BookingServiceImpl bookingService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private BookingRepository bookingRepository;

    @Test
    void should_CreateBooking_CreateBookingIfAllFieldsValid() {
        Long userId = 2L;
        Long itemUser = 1L;
        Long itemId = 1L;
        Long requestId = 1L;
        Long bookingId = 1L;
        LocalDateTime first = LocalDateTime.of(2023, 6, 22, 22, 0, 0);
        LocalDateTime second = LocalDateTime.of(2023, 6, 22, 22, 30, 0);

        Item item = new Item();
        item.setId(itemId);
        item.setName("drill");
        item.setDescription("Something for repair");
        item.setAvailable(true);
        item.setOwner(itemUser);
        item.setRequest(requestId);

        User user = new User();
        user.setId(userId);
        user.setName("Artem");
        user.setEmail("Artem@yandex.ru");

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setStart(first);
        booking.setEnd(second);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.WAITING);

        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(bookingId);
        bookingDto.setStart(first);
        bookingDto.setEnd(second);
        bookingDto.setItemId(itemId);

        BookingAnswerDTO answerDTO = BookingMapper.toDto(booking);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(bookingRepository.save(any())).thenReturn(booking);

        BookingAnswerDTO test = bookingService.createBooking(userId, bookingDto);

        assertEquals(test, answerDTO);
        verify(bookingRepository, times(1)).save(any());
    }

    @Test
    void shouldNot_CreateBooking_CreateBookingIfUserIdEqualsItemOwnerId() {
        Long userId = 1L;
        Long itemUser = 1L;
        Long itemId = 1L;
        Long requestId = 1L;
        Long bookingId = 1L;
        LocalDateTime first = LocalDateTime.of(2023, 6, 22, 22, 0, 0);
        LocalDateTime second = LocalDateTime.of(2023, 6, 22, 22, 30, 0);

        Item item = new Item();
        item.setId(itemId);
        item.setName("drill");
        item.setDescription("Something for repair");
        item.setAvailable(true);
        item.setOwner(itemUser);
        item.setRequest(requestId);

        User user = new User();
        user.setId(userId);
        user.setName("Artem");
        user.setEmail("Artem@yandex.ru");

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setStart(first);
        booking.setEnd(second);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.WAITING);

        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(bookingId);
        bookingDto.setStart(first);
        bookingDto.setEnd(second);
        bookingDto.setItemId(itemId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        NotFoundException e = assertThrows(NotFoundException.class, () -> bookingService.createBooking(userId, bookingDto));

        assertEquals("Item owner can't create booking " + user.getId(), e.getMessage());
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void shouldNot_CreateBooking_CreateBookingIfItemStatusNotAvailable() {
        Long userId = 2L;
        Long itemUser = 1L;
        Long itemId = 1L;
        Long requestId = 1L;
        Long bookingId = 1L;
        LocalDateTime first = LocalDateTime.of(2023, 6, 22, 22, 0, 0);
        LocalDateTime second = LocalDateTime.of(2023, 6, 22, 22, 30, 0);

        Item item = new Item();
        item.setId(itemId);
        item.setName("drill");
        item.setDescription("Something for repair");
        item.setAvailable(false);
        item.setOwner(itemUser);
        item.setRequest(requestId);

        User user = new User();
        user.setId(userId);
        user.setName("Artem");
        user.setEmail("Artem@yandex.ru");

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setStart(first);
        booking.setEnd(second);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.WAITING);

        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(bookingId);
        bookingDto.setStart(first);
        bookingDto.setEnd(second);
        bookingDto.setItemId(itemId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        NoArgumentException e = assertThrows(NoArgumentException.class, () -> bookingService.createBooking(userId, bookingDto));

        assertEquals("Item not available", e.getMessage());
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void shouldNot_CreateBooking_CreateBookingIfStartEqualsEnd() {
        Long userId = 2L;
        Long itemUser = 1L;
        Long itemId = 1L;
        Long requestId = 1L;
        Long bookingId = 1L;
        LocalDateTime first = LocalDateTime.of(2023, 6, 22, 22, 0, 0);
        LocalDateTime second = LocalDateTime.of(2023, 6, 22, 22, 0, 0);

        Item item = new Item();
        item.setId(itemId);
        item.setName("drill");
        item.setDescription("Something for repair");
        item.setAvailable(true);
        item.setOwner(itemUser);
        item.setRequest(requestId);

        User user = new User();
        user.setId(userId);
        user.setName("Artem");
        user.setEmail("Artem@yandex.ru");

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setStart(first);
        booking.setEnd(second);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.WAITING);

        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(bookingId);
        bookingDto.setStart(first);
        bookingDto.setEnd(second);
        bookingDto.setItemId(itemId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        NoArgumentException e = assertThrows(NoArgumentException.class, () -> bookingService.createBooking(userId, bookingDto));

        assertEquals("Start time can't be equal end time", e.getMessage());
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void shouldNot_CreateBooking_CreateBookingIfStartItThePast() {
        Long userId = 2L;
        Long itemUser = 1L;
        Long itemId = 1L;
        Long requestId = 1L;
        Long bookingId = 1L;
        LocalDateTime first = LocalDateTime.of(2023, 4, 22, 22, 0, 0);
        LocalDateTime second = LocalDateTime.of(2023, 6, 22, 22, 0, 0);

        Item item = new Item();
        item.setId(itemId);
        item.setName("drill");
        item.setDescription("Something for repair");
        item.setAvailable(true);
        item.setOwner(itemUser);
        item.setRequest(requestId);

        User user = new User();
        user.setId(userId);
        user.setName("Artem");
        user.setEmail("Artem@yandex.ru");

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setStart(first);
        booking.setEnd(second);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.WAITING);

        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(bookingId);
        bookingDto.setStart(first);
        bookingDto.setEnd(second);
        bookingDto.setItemId(itemId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        NoArgumentException e = assertThrows(NoArgumentException.class, () -> bookingService.createBooking(userId, bookingDto));

        assertEquals("Start time not add or start time is in the past", e.getMessage());
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void shouldNot_CreateBooking_CreateBookingIfEndIsNull() {
        Long userId = 2L;
        Long itemUser = 1L;
        Long itemId = 1L;
        Long requestId = 1L;
        Long bookingId = 1L;
        LocalDateTime first = LocalDateTime.of(2023, 6, 22, 22, 0, 0);
        LocalDateTime second = null;

        Item item = new Item();
        item.setId(itemId);
        item.setName("drill");
        item.setDescription("Something for repair");
        item.setAvailable(true);
        item.setOwner(itemUser);
        item.setRequest(requestId);

        User user = new User();
        user.setId(userId);
        user.setName("Artem");
        user.setEmail("Artem@yandex.ru");

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setStart(first);
        booking.setEnd(second);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.WAITING);

        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(bookingId);
        bookingDto.setStart(first);
        bookingDto.setEnd(second);
        bookingDto.setItemId(itemId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        NoArgumentException e = assertThrows(NoArgumentException.class, () -> bookingService.createBooking(userId, bookingDto));

        assertEquals("End time not add or end time is before start time", e.getMessage());
        verify(bookingRepository, never()).save(any());
    }


    @Test
    void should_UpdateBookingStatus_UpdateStatusToAPPROVED_IfStatusWAITINGAndBooleanTrue() {
        Long userId = 1L;
        Long itemUser = 1L;
        Long itemId = 1L;
        Long requestId = 1L;
        Long bookingId = 1L;
        LocalDateTime first = LocalDateTime.of(2023, 6, 22, 22, 0, 0);
        LocalDateTime second = LocalDateTime.of(2023, 6, 22, 22, 30, 0);

        Item item = new Item();
        item.setId(itemId);
        item.setName("drill");
        item.setDescription("Something for repair");
        item.setAvailable(true);
        item.setOwner(itemUser);
        item.setRequest(requestId);

        User user = new User();
        user.setId(userId);
        user.setName("Artem");
        user.setEmail("Artem@yandex.ru");

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setStart(first);
        booking.setEnd(second);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.WAITING);

        Booking updateBooking = new Booking();
        updateBooking.setId(bookingId);
        updateBooking.setStart(first);
        updateBooking.setEnd(second);
        updateBooking.setItem(item);
        updateBooking.setBooker(user);
        updateBooking.setStatus(BookingStatus.APPROVED);

        BookingAnswerDTO answerDTO = BookingMapper.toDto(updateBooking);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any())).thenReturn(updateBooking);

        BookingAnswerDTO test = bookingService.updateBookingStatus(userId, bookingId, true);

        assertEquals(test, answerDTO);
        verify(bookingRepository, times(1)).save(any());
    }

    @Test
    void should_UpdateBookingStatus_UpdateStatusToREJECTED_IfStatusWAITINGAndBooleanFalse() {
        Long userId = 1L;
        Long itemUser = 1L;
        Long itemId = 1L;
        Long requestId = 1L;
        Long bookingId = 1L;
        LocalDateTime first = LocalDateTime.of(2023, 6, 22, 22, 0, 0);
        LocalDateTime second = LocalDateTime.of(2023, 6, 22, 22, 30, 0);

        Item item = new Item();
        item.setId(itemId);
        item.setName("drill");
        item.setDescription("Something for repair");
        item.setAvailable(true);
        item.setOwner(itemUser);
        item.setRequest(requestId);

        User user = new User();
        user.setId(userId);
        user.setName("Artem");
        user.setEmail("Artem@yandex.ru");

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setStart(first);
        booking.setEnd(second);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.WAITING);

        Booking updateBooking = new Booking();
        updateBooking.setId(bookingId);
        updateBooking.setStart(first);
        updateBooking.setEnd(second);
        updateBooking.setItem(item);
        updateBooking.setBooker(user);
        updateBooking.setStatus(BookingStatus.REJECTED);

        BookingAnswerDTO answerDTO = BookingMapper.toDto(updateBooking);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any())).thenReturn(updateBooking);

        BookingAnswerDTO test = bookingService.updateBookingStatus(userId, bookingId, false);

        assertEquals(test, answerDTO);
        verify(bookingRepository, times(1)).save(any());
    }

    @Test
    void shouldNot_UpdateBookingStatus_UpdateStatus_IfStatusAPPROVED() {
        Long userId = 1L;
        Long itemUser = 1L;
        Long itemId = 1L;
        Long requestId = 1L;
        Long bookingId = 1L;
        LocalDateTime first = LocalDateTime.of(2023, 6, 22, 22, 0, 0);
        LocalDateTime second = LocalDateTime.of(2023, 6, 22, 22, 30, 0);

        Item item = new Item();
        item.setId(itemId);
        item.setName("drill");
        item.setDescription("Something for repair");
        item.setAvailable(true);
        item.setOwner(itemUser);
        item.setRequest(requestId);

        User user = new User();
        user.setId(userId);
        user.setName("Artem");
        user.setEmail("Artem@yandex.ru");

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setStart(first);
        booking.setEnd(second);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.APPROVED);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        NoArgumentException e = assertThrows(NoArgumentException.class, () -> bookingService.updateBookingStatus(userId, bookingId, true));

        assertEquals("You can't change status if it approved", e.getMessage());
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void shouldNot_UpdateBookingStatus_UpdateStatus_IfBookingNotFind() {
        Long userId = 1L;
        Long itemUser = 1L;
        Long itemId = 1L;
        Long requestId = 1L;
        Long bookingId = 1L;
        LocalDateTime first = LocalDateTime.of(2023, 6, 22, 22, 0, 0);
        LocalDateTime second = LocalDateTime.of(2023, 6, 22, 22, 30, 0);

        Item item = new Item();
        item.setId(itemId);
        item.setName("drill");
        item.setDescription("Something for repair");
        item.setAvailable(true);
        item.setOwner(itemUser);
        item.setRequest(requestId);

        User user = new User();
        user.setId(userId);
        user.setName("Artem");
        user.setEmail("Artem@yandex.ru");

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setStart(first);
        booking.setEnd(second);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.APPROVED);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());

        NotFoundException e = assertThrows(NotFoundException.class, () -> bookingService.updateBookingStatus(userId, bookingId, true));

        assertEquals("Booking not create", e.getMessage());
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void should_GetBookingByUser_GetBookingIfUserIsOwner() {
        Long userId = 1L;
        Long itemUser = 1L;
        Long itemId = 1L;
        Long requestId = 1L;
        Long bookingId = 1L;
        LocalDateTime first = LocalDateTime.of(2023, 6, 22, 22, 0, 0);
        LocalDateTime second = LocalDateTime.of(2023, 6, 22, 22, 30, 0);

        Item item = new Item();
        item.setId(itemId);
        item.setName("drill");
        item.setDescription("Something for repair");
        item.setAvailable(true);
        item.setOwner(itemUser);
        item.setRequest(requestId);

        User user = new User();
        user.setId(userId);
        user.setName("Artem");
        user.setEmail("Artem@yandex.ru");

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setStart(first);
        booking.setEnd(second);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.APPROVED);

        BookingAnswerDTO answerDTO = BookingMapper.toDto(booking);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        BookingAnswerDTO test = bookingService.getBookingByUser(userId, bookingId);

        assertEquals(test, answerDTO);
        verify(userRepository, times(1)).findById(userId);
        verify(bookingRepository, times(1)).findById(bookingId);
    }

    @Test
    void shouldNot_GetBookingByUser_GetBookingIfUserNotOwner() {
        Long userId = 1L;
        Long notValidUserId = 2L;
        Long itemUser = 1L;
        Long itemId = 1L;
        Long requestId = 1L;
        Long bookingId = 1L;
        LocalDateTime first = LocalDateTime.of(2023, 6, 22, 22, 0, 0);
        LocalDateTime second = LocalDateTime.of(2023, 6, 22, 22, 30, 0);

        Item item = new Item();
        item.setId(itemId);
        item.setName("drill");
        item.setDescription("Something for repair");
        item.setAvailable(true);
        item.setOwner(itemUser);
        item.setRequest(requestId);

        User user = new User();
        user.setId(userId);
        user.setName("Artem");
        user.setEmail("Artem@yandex.ru");

        User notValidUser = new User();
        notValidUser.setId(notValidUserId);
        notValidUser.setName("Ivan");
        notValidUser.setEmail("Ivan@yandex.ru");

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setStart(first);
        booking.setEnd(second);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.APPROVED);

        when(userRepository.findById(notValidUserId)).thenReturn(Optional.of(notValidUser));
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        NotFoundException e = assertThrows(NotFoundException.class, () -> bookingService.getBookingByUser(notValidUserId, bookingId));

        assertEquals("This user can't see this booking", e.getMessage());
        verify(userRepository, times(1)).findById(notValidUserId);
        verify(bookingRepository, times(1)).findById(bookingId);
    }

    @Test
    void should_getAllByUser_GetAllByUser_IfUserValidAndStateALL() {
        Long userId = 1L;
        Long itemUser = 1L;
        Long itemId = 1L;
        Long requestId = 1L;
        Long bookingId = 1L;
        Integer from = 0;
        Integer size = 2;
        String state = "ALL";
        LocalDateTime first = LocalDateTime.of(2023, 6, 22, 22, 0, 0);
        LocalDateTime second = LocalDateTime.of(2023, 6, 22, 22, 30, 0);

        Item item = new Item();
        item.setId(itemId);
        item.setName("drill");
        item.setDescription("Something for repair");
        item.setAvailable(true);
        item.setOwner(itemUser);
        item.setRequest(requestId);

        User user = new User();
        user.setId(userId);
        user.setName("Artem");
        user.setEmail("Artem@yandex.ru");

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setStart(first);
        booking.setEnd(second);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.APPROVED);

        BookingAnswerDTO answerDTO = BookingMapper.toDto(booking);
        List<BookingAnswerDTO> answerDTOS = List.of(answerDTO);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookingRepository.findByBooker_IdOrderByStartDesc(userId, PageRequest.of(from / size, size))).thenReturn(List.of(booking));

        List<BookingAnswerDTO> test = bookingService.getAllByUser(userId, state, from, size);

        assertEquals(test, answerDTOS);
        verify(bookingRepository, times(1)).findByBooker_IdOrderByStartDesc(userId, PageRequest.of(from / size, size));
        verify(bookingRepository, never()).findByBooker_IdAndStartAfterOrderByStartDesc(anyLong(), any(LocalDateTime.class), any(PageRequest.class));
        verify(bookingRepository, never()).findByBooker_IdAndEndBeforeOrderByStartDesc(anyLong(), any(LocalDateTime.class), any(PageRequest.class));
        verify(bookingRepository, never()).findByBooker_IdAndStartBeforeAndEndAfterOrderByStartDesc(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class), any(PageRequest.class));
        verify(bookingRepository, never()).findByBooker_IdAndStatusOrderByStartDesc(anyLong(), any(), any(PageRequest.class));
    }

    @Test
    void should_getAllByUser_GetAllByUser_IfUserValidAndStateFUTURE() {
        Long userId = 1L;
        Long itemUser = 1L;
        Long itemId = 1L;
        Long requestId = 1L;
        Long bookingId = 1L;
        Integer from = 0;
        Integer size = 2;
        String state = "FUTURE";
        LocalDateTime first = LocalDateTime.of(2023, 6, 22, 22, 0, 0);
        LocalDateTime second = LocalDateTime.of(2023, 6, 22, 22, 30, 0);

        Item item = new Item();
        item.setId(itemId);
        item.setName("drill");
        item.setDescription("Something for repair");
        item.setAvailable(true);
        item.setOwner(itemUser);
        item.setRequest(requestId);

        User user = new User();
        user.setId(userId);
        user.setName("Artem");
        user.setEmail("Artem@yandex.ru");

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setStart(first);
        booking.setEnd(second);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.APPROVED);

        BookingAnswerDTO answerDTO = BookingMapper.toDto(booking);
        List<BookingAnswerDTO> answerDTOS = List.of(answerDTO);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookingRepository.findByBooker_IdAndStartAfterOrderByStartDesc(anyLong(), any(LocalDateTime.class), any(PageRequest.class))).thenReturn(List.of(booking));

        List<BookingAnswerDTO> test = bookingService.getAllByUser(userId, state, from, size);

        assertEquals(test, answerDTOS);

        verify(bookingRepository, times(1)).findByBooker_IdAndStartAfterOrderByStartDesc(anyLong(), any(LocalDateTime.class), any(PageRequest.class));
        verify(bookingRepository, never()).findByBooker_IdOrderByStartDesc(userId, PageRequest.of(from / size, size));
        verify(bookingRepository, never()).findByBooker_IdAndEndBeforeOrderByStartDesc(anyLong(), any(LocalDateTime.class), any(PageRequest.class));
        verify(bookingRepository, never()).findByBooker_IdAndStartBeforeAndEndAfterOrderByStartDesc(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class), any(PageRequest.class));
        verify(bookingRepository, never()).findByBooker_IdAndStatusOrderByStartDesc(anyLong(), any(), any(PageRequest.class));
    }

    @Test
    void should_getAllByUser_GetAllByUser_IfUserValidAndStatePAST() {
        Long userId = 1L;
        Long itemUser = 1L;
        Long itemId = 1L;
        Long requestId = 1L;
        Long bookingId = 1L;
        Integer from = 0;
        Integer size = 2;
        String state = "PAST";
        LocalDateTime first = LocalDateTime.of(2023, 6, 22, 22, 0, 0);
        LocalDateTime second = LocalDateTime.of(2023, 6, 22, 22, 30, 0);

        Item item = new Item();
        item.setId(itemId);
        item.setName("drill");
        item.setDescription("Something for repair");
        item.setAvailable(true);
        item.setOwner(itemUser);
        item.setRequest(requestId);

        User user = new User();
        user.setId(userId);
        user.setName("Artem");
        user.setEmail("Artem@yandex.ru");

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setStart(first);
        booking.setEnd(second);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.APPROVED);

        BookingAnswerDTO answerDTO = BookingMapper.toDto(booking);
        List<BookingAnswerDTO> answerDTOS = List.of(answerDTO);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookingRepository.findByBooker_IdAndEndBeforeOrderByStartDesc(anyLong(), any(LocalDateTime.class), any(PageRequest.class))).thenReturn(List.of(booking));

        List<BookingAnswerDTO> test = bookingService.getAllByUser(userId, state, from, size);

        assertEquals(test, answerDTOS);
        verify(bookingRepository, times(1)).findByBooker_IdAndEndBeforeOrderByStartDesc(anyLong(), any(LocalDateTime.class), any(PageRequest.class));
        verify(bookingRepository, never()).findByBooker_IdOrderByStartDesc(userId, PageRequest.of(from / size, size));
        verify(bookingRepository, never()).findByBooker_IdAndStartAfterOrderByStartDesc(anyLong(), any(LocalDateTime.class), any(PageRequest.class));
        verify(bookingRepository, never()).findByBooker_IdAndStartBeforeAndEndAfterOrderByStartDesc(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class), any(PageRequest.class));
        verify(bookingRepository, never()).findByBooker_IdAndStatusOrderByStartDesc(anyLong(), any(), any(PageRequest.class));
    }

    @Test
    void should_getAllByUser_GetAllByUser_IfUserValidAndStateCURRENT() {
        Long userId = 1L;
        Long itemUser = 1L;
        Long itemId = 1L;
        Long requestId = 1L;
        Long bookingId = 1L;
        Integer from = 0;
        Integer size = 2;
        String state = "CURRENT";
        LocalDateTime first = LocalDateTime.of(2023, 6, 22, 22, 0, 0);
        LocalDateTime second = LocalDateTime.of(2023, 6, 22, 22, 30, 0);

        Item item = new Item();
        item.setId(itemId);
        item.setName("drill");
        item.setDescription("Something for repair");
        item.setAvailable(true);
        item.setOwner(itemUser);
        item.setRequest(requestId);

        User user = new User();
        user.setId(userId);
        user.setName("Artem");
        user.setEmail("Artem@yandex.ru");

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setStart(first);
        booking.setEnd(second);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.APPROVED);

        BookingAnswerDTO answerDTO = BookingMapper.toDto(booking);
        List<BookingAnswerDTO> answerDTOS = List.of(answerDTO);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookingRepository.findByBooker_IdAndStartBeforeAndEndAfterOrderByStartDesc(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class), any(PageRequest.class))).thenReturn(List.of(booking));

        List<BookingAnswerDTO> test = bookingService.getAllByUser(userId, state, from, size);

        assertEquals(test, answerDTOS);

        verify(bookingRepository, times(1)).findByBooker_IdAndStartBeforeAndEndAfterOrderByStartDesc(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class), any(PageRequest.class));
        verify(bookingRepository, never()).findByBooker_IdOrderByStartDesc(userId, PageRequest.of(from / size, size));
        verify(bookingRepository, never()).findByBooker_IdAndStartAfterOrderByStartDesc(anyLong(), any(LocalDateTime.class), any(PageRequest.class));
        verify(bookingRepository, never()).findByBooker_IdAndEndBeforeOrderByStartDesc(anyLong(), any(LocalDateTime.class), any(PageRequest.class));
        verify(bookingRepository, never()).findByBooker_IdAndStatusOrderByStartDesc(anyLong(), any(), any(PageRequest.class));
    }

    @Test
    void should_getAllByUser_GetAllByUser_IfUserValidAndStateWAITING() {
        Long userId = 1L;
        Long itemUser = 1L;
        Long itemId = 1L;
        Long requestId = 1L;
        Long bookingId = 1L;
        Integer from = 0;
        Integer size = 2;
        String state = "WAITING";
        LocalDateTime first = LocalDateTime.of(2023, 6, 22, 22, 0, 0);
        LocalDateTime second = LocalDateTime.of(2023, 6, 22, 22, 30, 0);

        Item item = new Item();
        item.setId(itemId);
        item.setName("drill");
        item.setDescription("Something for repair");
        item.setAvailable(true);
        item.setOwner(itemUser);
        item.setRequest(requestId);

        User user = new User();
        user.setId(userId);
        user.setName("Artem");
        user.setEmail("Artem@yandex.ru");

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setStart(first);
        booking.setEnd(second);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.WAITING);

        BookingAnswerDTO answerDTO = BookingMapper.toDto(booking);
        List<BookingAnswerDTO> answerDTOS = List.of(answerDTO);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookingRepository.findByBooker_IdAndStatusOrderByStartDesc(userId, BookingStatus.WAITING, PageRequest.of(from / size, size))).thenReturn(List.of(booking));

        List<BookingAnswerDTO> test = bookingService.getAllByUser(userId, state, from, size);

        assertEquals(test, answerDTOS);
        verify(bookingRepository, times(1)).findByBooker_IdAndStatusOrderByStartDesc(userId, BookingStatus.WAITING, PageRequest.of(from / size, size));
        verify(bookingRepository, never()).findByBooker_IdOrderByStartDesc(userId, PageRequest.of(from / size, size));
        verify(bookingRepository, never()).findByBooker_IdAndStartAfterOrderByStartDesc(anyLong(), any(LocalDateTime.class), any(PageRequest.class));
        verify(bookingRepository, never()).findByBooker_IdAndEndBeforeOrderByStartDesc(anyLong(), any(LocalDateTime.class), any(PageRequest.class));
        verify(bookingRepository, never()).findByBooker_IdAndStartBeforeAndEndAfterOrderByStartDesc(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class), any(PageRequest.class));
    }

    @Test
    void should_getAllByUser_GetAllByUser_IfUserValidAndStateREJECTED() {
        Long userId = 1L;
        Long itemUser = 1L;
        Long itemId = 1L;
        Long requestId = 1L;
        Long bookingId = 1L;
        Integer from = 0;
        Integer size = 2;
        String state = "REJECTED";
        LocalDateTime first = LocalDateTime.of(2023, 6, 22, 22, 0, 0);
        LocalDateTime second = LocalDateTime.of(2023, 6, 22, 22, 30, 0);

        Item item = new Item();
        item.setId(itemId);
        item.setName("drill");
        item.setDescription("Something for repair");
        item.setAvailable(true);
        item.setOwner(itemUser);
        item.setRequest(requestId);

        User user = new User();
        user.setId(userId);
        user.setName("Artem");
        user.setEmail("Artem@yandex.ru");

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setStart(first);
        booking.setEnd(second);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.REJECTED);

        BookingAnswerDTO answerDTO = BookingMapper.toDto(booking);
        List<BookingAnswerDTO> answerDTOS = List.of(answerDTO);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookingRepository.findByBooker_IdAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED, PageRequest.of(from / size, size))).thenReturn(List.of(booking));

        List<BookingAnswerDTO> test = bookingService.getAllByUser(userId, state, from, size);

        assertEquals(test, answerDTOS);
        verify(bookingRepository, times(1)).findByBooker_IdAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED, PageRequest.of(from / size, size));
        verify(bookingRepository, never()).findByBooker_IdOrderByStartDesc(userId, PageRequest.of(from / size, size));
        verify(bookingRepository, never()).findByBooker_IdAndStartAfterOrderByStartDesc(anyLong(), any(LocalDateTime.class), any(PageRequest.class));
        verify(bookingRepository, never()).findByBooker_IdAndEndBeforeOrderByStartDesc(anyLong(), any(LocalDateTime.class), any(PageRequest.class));
        verify(bookingRepository, never()).findByBooker_IdAndStartBeforeAndEndAfterOrderByStartDesc(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class), any(PageRequest.class));
    }

    @Test
    void shouldNot_getAllByUser_GetAllByUser_IfUserValidAndStateNotValid() {
        Long userId = 1L;
        Long itemUser = 1L;
        Long itemId = 1L;
        Long requestId = 1L;
        Long bookingId = 1L;
        Integer from = 0;
        Integer size = 2;
        String state = "NEXT";
        LocalDateTime first = LocalDateTime.of(2023, 6, 22, 22, 0, 0);
        LocalDateTime second = LocalDateTime.of(2023, 6, 22, 22, 30, 0);

        Item item = new Item();
        item.setId(itemId);
        item.setName("drill");
        item.setDescription("Something for repair");
        item.setAvailable(true);
        item.setOwner(itemUser);
        item.setRequest(requestId);

        User user = new User();
        user.setId(userId);
        user.setName("Artem");
        user.setEmail("Artem@yandex.ru");

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setStart(first);
        booking.setEnd(second);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.REJECTED);

        BookingAnswerDTO answerDTO = BookingMapper.toDto(booking);
        List<BookingAnswerDTO> answerDTOS = List.of(answerDTO);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        NoArgumentException e = assertThrows(NoArgumentException.class, () -> bookingService.getAllByUser(userId, state, from, size));

        assertEquals("Unknown state: UNSUPPORTED_STATUS", e.getMessage());
        verify(bookingRepository, never()).findByBooker_IdAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED, PageRequest.of(from / size, size));
        verify(bookingRepository, never()).findByBooker_IdOrderByStartDesc(userId, PageRequest.of(from / size, size));
        verify(bookingRepository, never()).findByBooker_IdAndStartAfterOrderByStartDesc(anyLong(), any(LocalDateTime.class), any(PageRequest.class));
        verify(bookingRepository, never()).findByBooker_IdAndEndBeforeOrderByStartDesc(anyLong(), any(LocalDateTime.class), any(PageRequest.class));
        verify(bookingRepository, never()).findByBooker_IdAndStartBeforeAndEndAfterOrderByStartDesc(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class), any(PageRequest.class));
    }

    @Test
    void should_GetAllByOwner_GetAllByOwner_IfStataALL() {
        Long userId = 1L;
        Long itemUser = 1L;
        Long itemId = 1L;
        Long requestId = 1L;
        Long bookingId = 1L;
        Integer from = 0;
        Integer size = 2;
        String state = "ALL";
        LocalDateTime first = LocalDateTime.of(2023, 6, 22, 22, 0, 0);
        LocalDateTime second = LocalDateTime.of(2023, 6, 22, 22, 30, 0);

        Item item = new Item();
        item.setId(itemId);
        item.setName("drill");
        item.setDescription("Something for repair");
        item.setAvailable(true);
        item.setOwner(itemUser);
        item.setRequest(requestId);

        User user = new User();
        user.setId(userId);
        user.setName("Artem");
        user.setEmail("Artem@yandex.ru");

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setStart(first);
        booking.setEnd(second);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.REJECTED);

        BookingAnswerDTO answerDTO = BookingMapper.toDto(booking);
        List<BookingAnswerDTO> answerDTOS = List.of(answerDTO);

        when(bookingRepository.findByItem_OwnerOrderByStartDesc(userId, PageRequest.of(from / size, size))).thenReturn(List.of(booking));

        List<BookingAnswerDTO> test = bookingService.getAllByOwner(userId, state, from, size);

        assertEquals(test, answerDTOS);
        verify(bookingRepository, times(1)).findByItem_OwnerOrderByStartDesc(userId, PageRequest.of(from / size, size));
        verify(bookingRepository, never()).findByItem_OwnerAndStartAfterOrderByStartDesc(anyLong(), any(LocalDateTime.class), any(PageRequest.class));
        verify(bookingRepository, never()).findByItem_OwnerAndEndBeforeOrderByStartDesc(anyLong(), any(LocalDateTime.class), any(PageRequest.class));
        verify(bookingRepository, never()).findByItem_OwnerAndStartBeforeAndEndAfterOrderByStartDesc(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class), any(PageRequest.class));
        verify(bookingRepository, never()).findByItem_OwnerAndStatusOrderByStartDesc(userId, BookingStatus.WAITING, PageRequest.of(from / size, size));
        verify(bookingRepository, never()).findByItem_OwnerAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED, PageRequest.of(from / size, size));
    }

    @Test
    void should_GetAllByOwner_GetAllByOwner_IfStataFUTURE() {
        Long userId = 1L;
        Long itemUser = 1L;
        Long itemId = 1L;
        Long requestId = 1L;
        Long bookingId = 1L;
        Integer from = 0;
        Integer size = 2;
        String state = "FUTURE";
        LocalDateTime first = LocalDateTime.of(2023, 6, 22, 22, 0, 0);
        LocalDateTime second = LocalDateTime.of(2023, 6, 22, 22, 30, 0);

        Item item = new Item();
        item.setId(itemId);
        item.setName("drill");
        item.setDescription("Something for repair");
        item.setAvailable(true);
        item.setOwner(itemUser);
        item.setRequest(requestId);

        User user = new User();
        user.setId(userId);
        user.setName("Artem");
        user.setEmail("Artem@yandex.ru");

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setStart(first);
        booking.setEnd(second);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.REJECTED);

        BookingAnswerDTO answerDTO = BookingMapper.toDto(booking);
        List<BookingAnswerDTO> answerDTOS = List.of(answerDTO);

        when(bookingRepository.findByItem_OwnerAndStartAfterOrderByStartDesc(anyLong(), any(LocalDateTime.class), any(PageRequest.class))).thenReturn(List.of(booking));

        List<BookingAnswerDTO> test = bookingService.getAllByOwner(userId, state, from, size);

        assertEquals(test, answerDTOS);
        verify(bookingRepository, times(1)).findByItem_OwnerAndStartAfterOrderByStartDesc(anyLong(), any(LocalDateTime.class), any(PageRequest.class));
        verify(bookingRepository, never()).findByItem_OwnerOrderByStartDesc(userId, PageRequest.of(from / size, size));
        verify(bookingRepository, never()).findByItem_OwnerAndEndBeforeOrderByStartDesc(anyLong(), any(LocalDateTime.class), any(PageRequest.class));
        verify(bookingRepository, never()).findByItem_OwnerAndStartBeforeAndEndAfterOrderByStartDesc(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class), any(PageRequest.class));
        verify(bookingRepository, never()).findByItem_OwnerAndStatusOrderByStartDesc(userId, BookingStatus.WAITING, PageRequest.of(from / size, size));
        verify(bookingRepository, never()).findByItem_OwnerAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED, PageRequest.of(from / size, size));
    }

    @Test
    void should_GetAllByOwner_GetAllByOwner_IfStataPAST() {
        Long userId = 1L;
        Long itemUser = 1L;
        Long itemId = 1L;
        Long requestId = 1L;
        Long bookingId = 1L;
        Integer from = 0;
        Integer size = 2;
        String state = "PAST";
        LocalDateTime first = LocalDateTime.of(2023, 6, 22, 22, 0, 0);
        LocalDateTime second = LocalDateTime.of(2023, 6, 22, 22, 30, 0);

        Item item = new Item();
        item.setId(itemId);
        item.setName("drill");
        item.setDescription("Something for repair");
        item.setAvailable(true);
        item.setOwner(itemUser);
        item.setRequest(requestId);

        User user = new User();
        user.setId(userId);
        user.setName("Artem");
        user.setEmail("Artem@yandex.ru");

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setStart(first);
        booking.setEnd(second);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.REJECTED);

        BookingAnswerDTO answerDTO = BookingMapper.toDto(booking);
        List<BookingAnswerDTO> answerDTOS = List.of(answerDTO);

        when(bookingRepository.findByItem_OwnerAndEndBeforeOrderByStartDesc(anyLong(), any(LocalDateTime.class), any(PageRequest.class))).thenReturn(List.of(booking));

        List<BookingAnswerDTO> test = bookingService.getAllByOwner(userId, state, from, size);

        assertEquals(test, answerDTOS);
        verify(bookingRepository, times(1)).findByItem_OwnerAndEndBeforeOrderByStartDesc(anyLong(), any(LocalDateTime.class), any(PageRequest.class));
        verify(bookingRepository, never()).findByItem_OwnerAndStartAfterOrderByStartDesc(anyLong(), any(LocalDateTime.class), any(PageRequest.class));
        verify(bookingRepository, never()).findByItem_OwnerOrderByStartDesc(userId, PageRequest.of(from / size, size));
        verify(bookingRepository, never()).findByItem_OwnerAndStartBeforeAndEndAfterOrderByStartDesc(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class), any(PageRequest.class));
        verify(bookingRepository, never()).findByItem_OwnerAndStatusOrderByStartDesc(userId, BookingStatus.WAITING, PageRequest.of(from / size, size));
        verify(bookingRepository, never()).findByItem_OwnerAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED, PageRequest.of(from / size, size));
    }

    @Test
    void should_GetAllByOwner_GetAllByOwner_IfStataCURRENT() {
        Long userId = 1L;
        Long itemUser = 1L;
        Long itemId = 1L;
        Long requestId = 1L;
        Long bookingId = 1L;
        Integer from = 0;
        Integer size = 2;
        String state = "CURRENT";
        LocalDateTime first = LocalDateTime.of(2023, 6, 22, 22, 0, 0);
        LocalDateTime second = LocalDateTime.of(2023, 6, 22, 22, 30, 0);

        Item item = new Item();
        item.setId(itemId);
        item.setName("drill");
        item.setDescription("Something for repair");
        item.setAvailable(true);
        item.setOwner(itemUser);
        item.setRequest(requestId);

        User user = new User();
        user.setId(userId);
        user.setName("Artem");
        user.setEmail("Artem@yandex.ru");

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setStart(first);
        booking.setEnd(second);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.REJECTED);

        BookingAnswerDTO answerDTO = BookingMapper.toDto(booking);
        List<BookingAnswerDTO> answerDTOS = List.of(answerDTO);

        when(bookingRepository.findByItem_OwnerAndStartBeforeAndEndAfterOrderByStartDesc(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class), any(PageRequest.class))).thenReturn(List.of(booking));

        List<BookingAnswerDTO> test = bookingService.getAllByOwner(userId, state, from, size);

        assertEquals(test, answerDTOS);
        verify(bookingRepository, times(1)).findByItem_OwnerAndStartBeforeAndEndAfterOrderByStartDesc(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class), any(PageRequest.class));
        verify(bookingRepository, never()).findByItem_OwnerAndEndBeforeOrderByStartDesc(anyLong(), any(LocalDateTime.class), any(PageRequest.class));
        verify(bookingRepository, never()).findByItem_OwnerAndStartAfterOrderByStartDesc(anyLong(), any(LocalDateTime.class), any(PageRequest.class));
        verify(bookingRepository, never()).findByItem_OwnerOrderByStartDesc(userId, PageRequest.of(from / size, size));
        verify(bookingRepository, never()).findByItem_OwnerAndStatusOrderByStartDesc(userId, BookingStatus.WAITING, PageRequest.of(from / size, size));
        verify(bookingRepository, never()).findByItem_OwnerAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED, PageRequest.of(from / size, size));
    }

    @Test
    void should_GetAllByOwner_GetAllByOwner_IfStataWAITING() {
        Long userId = 1L;
        Long itemUser = 1L;
        Long itemId = 1L;
        Long requestId = 1L;
        Long bookingId = 1L;
        Integer from = 0;
        Integer size = 2;
        String state = "WAITING";
        LocalDateTime first = LocalDateTime.of(2023, 6, 22, 22, 0, 0);
        LocalDateTime second = LocalDateTime.of(2023, 6, 22, 22, 30, 0);

        Item item = new Item();
        item.setId(itemId);
        item.setName("drill");
        item.setDescription("Something for repair");
        item.setAvailable(true);
        item.setOwner(itemUser);
        item.setRequest(requestId);

        User user = new User();
        user.setId(userId);
        user.setName("Artem");
        user.setEmail("Artem@yandex.ru");

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setStart(first);
        booking.setEnd(second);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.WAITING);

        BookingAnswerDTO answerDTO = BookingMapper.toDto(booking);
        List<BookingAnswerDTO> answerDTOS = List.of(answerDTO);

        when(bookingRepository.findByItem_OwnerAndStatusOrderByStartDesc(userId, BookingStatus.WAITING, PageRequest.of(from / size, size))).thenReturn(List.of(booking));

        List<BookingAnswerDTO> test = bookingService.getAllByOwner(userId, state, from, size);

        assertEquals(test, answerDTOS);
        verify(bookingRepository, times(1)).findByItem_OwnerAndStatusOrderByStartDesc(userId, BookingStatus.WAITING, PageRequest.of(from / size, size));
        verify(bookingRepository, never()).findByItem_OwnerAndStartBeforeAndEndAfterOrderByStartDesc(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class), any(PageRequest.class));
        verify(bookingRepository, never()).findByItem_OwnerAndEndBeforeOrderByStartDesc(anyLong(), any(LocalDateTime.class), any(PageRequest.class));
        verify(bookingRepository, never()).findByItem_OwnerAndStartAfterOrderByStartDesc(anyLong(), any(LocalDateTime.class), any(PageRequest.class));
        verify(bookingRepository, never()).findByItem_OwnerOrderByStartDesc(userId, PageRequest.of(from / size, size));
        verify(bookingRepository, never()).findByItem_OwnerAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED, PageRequest.of(from / size, size));
    }

    @Test
    void should_GetAllByOwner_GetAllByOwner_IfStataREJECTED() {
        Long userId = 1L;
        Long itemUser = 1L;
        Long itemId = 1L;
        Long requestId = 1L;
        Long bookingId = 1L;
        Integer from = 0;
        Integer size = 2;
        String state = "REJECTED";
        LocalDateTime first = LocalDateTime.of(2023, 6, 22, 22, 0, 0);
        LocalDateTime second = LocalDateTime.of(2023, 6, 22, 22, 30, 0);

        Item item = new Item();
        item.setId(itemId);
        item.setName("drill");
        item.setDescription("Something for repair");
        item.setAvailable(true);
        item.setOwner(itemUser);
        item.setRequest(requestId);

        User user = new User();
        user.setId(userId);
        user.setName("Artem");
        user.setEmail("Artem@yandex.ru");

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setStart(first);
        booking.setEnd(second);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.REJECTED);

        BookingAnswerDTO answerDTO = BookingMapper.toDto(booking);
        List<BookingAnswerDTO> answerDTOS = List.of(answerDTO);

        when(bookingRepository.findByItem_OwnerAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED, PageRequest.of(from / size, size))).thenReturn(List.of(booking));

        List<BookingAnswerDTO> test = bookingService.getAllByOwner(userId, state, from, size);

        assertEquals(test, answerDTOS);
        verify(bookingRepository, times(1)).findByItem_OwnerAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED, PageRequest.of(from / size, size));
        verify(bookingRepository, never()).findByItem_OwnerAndStatusOrderByStartDesc(userId, BookingStatus.WAITING, PageRequest.of(from / size, size));
        verify(bookingRepository, never()).findByItem_OwnerAndStartBeforeAndEndAfterOrderByStartDesc(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class), any(PageRequest.class));
        verify(bookingRepository, never()).findByItem_OwnerAndEndBeforeOrderByStartDesc(anyLong(), any(LocalDateTime.class), any(PageRequest.class));
        verify(bookingRepository, never()).findByItem_OwnerAndStartAfterOrderByStartDesc(anyLong(), any(LocalDateTime.class), any(PageRequest.class));
        verify(bookingRepository, never()).findByItem_OwnerOrderByStartDesc(userId, PageRequest.of(from / size, size));
    }

    @Test
    void should_GetAllByOwner_GetAllByOwner_IfStateNotValid() {
        Long userId = 1L;
        Long itemUser = 1L;
        Long itemId = 1L;
        Long requestId = 1L;
        Long bookingId = 1L;
        Integer from = 0;
        Integer size = 2;
        String state = "NEXT";
        LocalDateTime first = LocalDateTime.of(2023, 6, 22, 22, 0, 0);
        LocalDateTime second = LocalDateTime.of(2023, 6, 22, 22, 30, 0);

        Item item = new Item();
        item.setId(itemId);
        item.setName("drill");
        item.setDescription("Something for repair");
        item.setAvailable(true);
        item.setOwner(itemUser);
        item.setRequest(requestId);

        User user = new User();
        user.setId(userId);
        user.setName("Artem");
        user.setEmail("Artem@yandex.ru");

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setStart(first);
        booking.setEnd(second);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.REJECTED);

        BookingAnswerDTO answerDTO = BookingMapper.toDto(booking);
        List<BookingAnswerDTO> answerDTOS = List.of(answerDTO);

        NoArgumentException e = assertThrows(NoArgumentException.class, () -> bookingService.getAllByOwner(userId, state, from, size));

        assertEquals("Unknown state: UNSUPPORTED_STATUS", e.getMessage());
        verify(bookingRepository, never()).findByItem_OwnerAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED, PageRequest.of(from / size, size));
        verify(bookingRepository, never()).findByItem_OwnerAndStatusOrderByStartDesc(userId, BookingStatus.WAITING, PageRequest.of(from / size, size));
        verify(bookingRepository, never()).findByItem_OwnerAndStartBeforeAndEndAfterOrderByStartDesc(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class), any(PageRequest.class));
        verify(bookingRepository, never()).findByItem_OwnerAndEndBeforeOrderByStartDesc(anyLong(), any(LocalDateTime.class), any(PageRequest.class));
        verify(bookingRepository, never()).findByItem_OwnerAndStartAfterOrderByStartDesc(anyLong(), any(LocalDateTime.class), any(PageRequest.class));
        verify(bookingRepository, never()).findByItem_OwnerOrderByStartDesc(userId, PageRequest.of(from / size, size));
    }

    @Test
    void getBooking() {
        Long userId = 1L;
        Long itemUser = 1L;
        Long itemId = 1L;
        Long requestId = 1L;
        Long bookingId = 1L;
        LocalDateTime first = LocalDateTime.of(2023, 6, 22, 22, 0, 0);
        LocalDateTime second = LocalDateTime.of(2023, 6, 22, 22, 30, 0);

        Item item = new Item();
        item.setId(itemId);
        item.setName("drill");
        item.setDescription("Something for repair");
        item.setAvailable(true);
        item.setOwner(itemUser);
        item.setRequest(requestId);

        User user = new User();
        user.setId(userId);
        user.setName("Artem");
        user.setEmail("Artem@yandex.ru");

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setStart(first);
        booking.setEnd(second);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.WAITING);

        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(bookingId);
        bookingDto.setStart(first);
        bookingDto.setEnd(second);
        bookingDto.setItemId(itemId);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        Booking test = bookingService.getBooking(bookingId);
        assertEquals(test, booking);
    }
}