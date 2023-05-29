package ru.practicum.shareit.booking.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingAnswerDTO;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;

import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemBookingDTO;
import ru.practicum.shareit.user.dto.UserBookingDTO;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = BookingController.class)
class BookingControllerTest {
    @MockBean
    private BookingService bookingService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @SneakyThrows
    @Test
    void should_CreateBooking_CreateNewBooking() {
        Long userId = 1L;
        Long itemId = 1L;
        Long bookingId = 1L;
        LocalDateTime start = LocalDateTime.of(2023, 5, 21, 18, 41, 1);
        LocalDateTime end = LocalDateTime.of(2023, 5, 21, 18, 42, 2);

        ItemBookingDTO item = new ItemBookingDTO();
        item.setId(itemId);
        item.setName("name");

        UserBookingDTO user = new UserBookingDTO();
        user.setId(userId);

        BookingDto booking = new BookingDto();
        booking.setItemId(itemId);
        booking.setStart(start);
        booking.setEnd(end);

        BookingAnswerDTO newBooking = new BookingAnswerDTO();
        newBooking.setId(bookingId);
        newBooking.setStart(start);
        newBooking.setEnd(end);
        newBooking.setStatus(BookingStatus.APPROVED);
        newBooking.setItem(item);
        newBooking.setBooker(user);

        when(bookingService.createBooking(userId, booking)).thenReturn(newBooking);

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(booking))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(newBooking.getId()), Long.class))
                .andExpect(jsonPath("$.start", is(newBooking.getStart().toString())))
                .andExpect(jsonPath("$.end", is(newBooking.getEnd().toString())))
                .andExpect(jsonPath("$.status", is(newBooking.getStatus().toString())))
                .andExpect(jsonPath("$.item", is(newBooking.getItem()), ItemBookingDTO.class))
                .andExpect(jsonPath("$.booker", is(newBooking.getBooker()), UserBookingDTO.class));

        verify(bookingService).createBooking(userId, booking);
    }

    @SneakyThrows
    @Test
    void should_UpdateBookingStatusCreateException_ifBookingNotFound() {
        Long userId = 1L;
        Long bookingId = 1L;
        boolean status = true;

        when(bookingService.updateBookingStatus(userId, bookingId, status)).thenThrow(NotFoundException.class);

        mockMvc.perform(patch("/bookings/{bookingId}", bookingId)
                        .header("X-Sharer-User-Id", userId)
                        .param("approved", String.valueOf(status))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(bookingService).updateBookingStatus(userId, bookingId, status);
    }

    @SneakyThrows
    @Test
    void should_UpdateBookingStatus_UpdateBookingStatus() {
        Long userId = 1L;
        Long itemId = 1L;
        Long bookingId = 1L;
        LocalDateTime start = LocalDateTime.of(2023, 5, 21, 18, 41, 1);
        LocalDateTime end = LocalDateTime.of(2023, 5, 21, 18, 42, 2);

        boolean status = true;

        ItemBookingDTO item = new ItemBookingDTO();
        item.setId(itemId);
        item.setName("name");

        UserBookingDTO user = new UserBookingDTO();
        user.setId(userId);

        BookingAnswerDTO newBooking = new BookingAnswerDTO();
        newBooking.setId(bookingId);
        newBooking.setStart(start);
        newBooking.setEnd(end);
        newBooking.setStatus(BookingStatus.APPROVED);
        newBooking.setItem(item);
        newBooking.setBooker(user);

        when(bookingService.updateBookingStatus(userId, bookingId, status)).thenReturn(newBooking);

        mockMvc.perform(patch("/bookings/{bookingId}", bookingId)
                        .header("X-Sharer-User-Id", userId)
                        .param("approved", String.valueOf(status))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(newBooking.getId()), Long.class))
                .andExpect(jsonPath("$.start", is(newBooking.getStart().toString())))
                .andExpect(jsonPath("$.end", is(newBooking.getEnd().toString())))
                .andExpect(jsonPath("$.status", is(newBooking.getStatus().toString())))
                .andExpect(jsonPath("$.item", is(newBooking.getItem()), ItemBookingDTO.class))
                .andExpect(jsonPath("$.booker", is(newBooking.getBooker()), UserBookingDTO.class));

        verify(bookingService).updateBookingStatus(userId, bookingId, status);
    }

    @SneakyThrows
    @Test
    void should_getBooking_ReturnBookingById() {
        Long userId = 1L;
        Long itemId = 1L;
        Long bookingId = 1L;
        LocalDateTime start = LocalDateTime.of(2023, 5, 21, 18, 41, 1);
        LocalDateTime end = LocalDateTime.of(2023, 5, 21, 18, 42, 2);

        ItemBookingDTO item = new ItemBookingDTO();
        item.setId(itemId);
        item.setName("name");

        UserBookingDTO user = new UserBookingDTO();
        user.setId(userId);

        BookingAnswerDTO newBooking = new BookingAnswerDTO();
        newBooking.setId(bookingId);
        newBooking.setStart(start);
        newBooking.setEnd(end);
        newBooking.setStatus(BookingStatus.APPROVED);
        newBooking.setItem(item);
        newBooking.setBooker(user);

        when(bookingService.getBookingByUser(userId, bookingId)).thenReturn(newBooking);

        mockMvc.perform(get("/bookings/{bookingId}", bookingId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(newBooking.getId()), Long.class))
                .andExpect(jsonPath("$.start", is(newBooking.getStart().toString())))
                .andExpect(jsonPath("$.end", is(newBooking.getEnd().toString())))
                .andExpect(jsonPath("$.status", is(newBooking.getStatus().toString())))
                .andExpect(jsonPath("$.item", is(newBooking.getItem()), ItemBookingDTO.class))
                .andExpect(jsonPath("$.booker", is(newBooking.getBooker()), UserBookingDTO.class));

        verify(bookingService).getBookingByUser(userId, bookingId);
    }

    @SneakyThrows
    @Test
    void should_getBooking_ReturnNotFoundException_IfBookingNotFound() {
        Long userId = 1L;
        Long bookingId = 1L;

        when(bookingService.getBookingByUser(userId, bookingId)).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/bookings/{bookingId}", bookingId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isNotFound());

        verify(bookingService).getBookingByUser(userId, bookingId);
    }

    @SneakyThrows
    @Test
    void should_GetAllByUser_ReturnListBooking_IfBookingAdd() {
        Long userId = 1L;
        Long itemId = 1L;
        Long bookingId = 1L;
        String state = "ALL";
        Integer from = 0;
        Integer size = 1;
        LocalDateTime start = LocalDateTime.of(2023, 5, 21, 18, 41, 1);
        LocalDateTime end = LocalDateTime.of(2023, 5, 21, 18, 42, 2);

        ItemBookingDTO item = new ItemBookingDTO();
        item.setId(itemId);
        item.setName("name");

        UserBookingDTO user = new UserBookingDTO();
        user.setId(userId);

        BookingAnswerDTO newBooking = new BookingAnswerDTO();
        newBooking.setId(bookingId);
        newBooking.setStart(start);
        newBooking.setEnd(end);
        newBooking.setStatus(BookingStatus.APPROVED);
        newBooking.setItem(item);
        newBooking.setBooker(user);

        when(bookingService.getAllByUser(userId, state, from, size)).thenReturn(List.of(newBooking));

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", userId)
                        .param("state", state)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(newBooking.getId()), Long.class))
                .andExpect(jsonPath("$[0].start", is(newBooking.getStart().toString())))
                .andExpect(jsonPath("$[0].end", is(newBooking.getEnd().toString())))
                .andExpect(jsonPath("$[0].status", is(newBooking.getStatus().toString())))
                .andExpect(jsonPath("$[0].item", is(newBooking.getItem()), ItemBookingDTO.class))
                .andExpect(jsonPath("$[0].booker", is(newBooking.getBooker()), UserBookingDTO.class));

        verify(bookingService).getAllByUser(userId, state, from, size);
    }

    @SneakyThrows
    @Test
    void should_GetAllByUser_ReturnInternalServerError_IfFromBelowZero() {
        Long userId = 1L;
        String state = "ALL";
        Integer from = -1;
        Integer size = 1;

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", userId)
                        .param("state", state)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isInternalServerError());


        verify(bookingService, never()).getAllByUser(userId, state, from, size);
    }

    @SneakyThrows
    @Test
    void should_GetAllByUser_ReturnInternalServerError_IfSizMore100() {
        Long userId = 1L;
        String state = "ALL";
        Integer from = 0;
        Integer size = 101;

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", userId)
                        .param("state", state)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isInternalServerError());


        verify(bookingService, never()).getAllByUser(userId, state, from, size);
    }

    @SneakyThrows
    @Test
    void should_GetAllByUser_ReturnInternalServerError_IfSizeBelowZero() {
        Long userId = 1L;
        String state = "ALL";
        Integer from = 0;
        Integer size = -1;

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", userId)
                        .param("state", state)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isInternalServerError());


        verify(bookingService, never()).getAllByUser(userId, state, from, size);
    }

    @SneakyThrows
    @Test
    void should_GetAllByUser_ReturnEmptyListBooking_IfBookingNotAdd() {
        Long userId = 1L;
        String state = "ALL";
        Integer from = 0;
        Integer size = 1;

        when(bookingService.getAllByUser(userId, state, from, size)).thenReturn(List.of());

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", userId)
                        .param("state", state)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(bookingService).getAllByUser(userId, state, from, size);
    }

    @SneakyThrows
    @Test
    void should_GetAllByOwner_ReturnListBooking() {
        Long userId = 1L;
        Long itemId = 1L;
        Long bookingId = 1L;
        String state = "ALL";
        Integer from = 0;
        Integer size = 1;
        LocalDateTime start = LocalDateTime.of(2023, 5, 21, 18, 41, 1);
        LocalDateTime end = LocalDateTime.of(2023, 5, 21, 18, 42, 2);

        ItemBookingDTO item = new ItemBookingDTO();
        item.setId(itemId);
        item.setName("name");

        UserBookingDTO user = new UserBookingDTO();
        user.setId(userId);

        BookingAnswerDTO newBooking = new BookingAnswerDTO();
        newBooking.setId(bookingId);
        newBooking.setStart(start);
        newBooking.setEnd(end);
        newBooking.setStatus(BookingStatus.APPROVED);
        newBooking.setItem(item);
        newBooking.setBooker(user);

        when(bookingService.getAllByOwner(userId, state, from, size)).thenReturn(List.of(newBooking));

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", userId)
                        .param("state", state)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(newBooking.getId()), Long.class))
                .andExpect(jsonPath("$[0].start", is(newBooking.getStart().toString())))
                .andExpect(jsonPath("$[0].end", is(newBooking.getEnd().toString())))
                .andExpect(jsonPath("$[0].status", is(newBooking.getStatus().toString())))
                .andExpect(jsonPath("$[0].item", is(newBooking.getItem()), ItemBookingDTO.class))
                .andExpect(jsonPath("$[0].booker", is(newBooking.getBooker()), UserBookingDTO.class));

        verify(bookingService).getAllByOwner(userId, state, from, size);
    }

    @SneakyThrows
    @Test
    void should_GetAllByOwner_ReturnEmptyListBooking_IfBookingNotAdd() {
        Long userId = 1L;
        String state = "ALL";
        Integer from = 0;
        Integer size = 1;

        when(bookingService.getAllByOwner(userId, state, from, size)).thenReturn(List.of());

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", userId)
                        .param("state", state)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(bookingService).getAllByOwner(userId, state, from, size);
    }

    @SneakyThrows
    @Test
    void should_GetAllByOwner_ReturnInternalServerError_IfFromBelowZero() {
        Long userId = 1L;
        String state = "ALL";
        Integer from = -1;
        Integer size = 1;


        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", userId)
                        .param("state", state)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isInternalServerError());

        verify(bookingService, never()).getAllByOwner(userId, state, from, size);
    }

    @SneakyThrows
    @Test
    void should_GetAllByOwner_ReturnInternalServerError_IfSizeBelowZero() {
        Long userId = 1L;
        String state = "ALL";
        Integer from = 0;
        Integer size = -1;

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", userId)
                        .param("state", state)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isInternalServerError());

        verify(bookingService, never()).getAllByOwner(userId, state, from, size);
    }

    @SneakyThrows
    @Test
    void should_GetAllByOwner_ReturnInternalServerError_IfSizeMore100() {
        Long userId = 1L;
        String state = "ALL";
        Integer from = 0;
        Integer size = 101;

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", userId)
                        .param("state", state)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isInternalServerError());

        verify(bookingService, never()).getAllByOwner(userId, state, from, size);
    }
}