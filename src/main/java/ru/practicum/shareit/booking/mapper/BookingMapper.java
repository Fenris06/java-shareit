package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.dto.BookingAnswerDTO;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingForItemDTO;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemBookingDTO;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserBookingDTO;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

public class BookingMapper {
    public static Booking fromDto(BookingDto bookingDto, Item item, User user) {
        Booking booking = new Booking();
        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());
        booking.setItem(item);
        booking.setBooker(user);
        return booking;
    }

    public static BookingAnswerDTO toDto(Booking booking) {
        BookingAnswerDTO answerDTO = new BookingAnswerDTO();
        answerDTO.setId(booking.getId());
        answerDTO.setStart(booking.getStart());
        answerDTO.setEnd(booking.getEnd());
        answerDTO.setStatus(booking.getStatus());
        UserBookingDTO userBookingDTO = new UserBookingDTO();
        userBookingDTO.setId(booking.getBooker().getId());
        answerDTO.setBooker(userBookingDTO);
        ItemBookingDTO itemBookingDTO = new ItemBookingDTO();
        itemBookingDTO.setId(booking.getItem().getId());
        itemBookingDTO.setName(booking.getItem().getName());
        answerDTO.setItem(itemBookingDTO);
        return answerDTO;
    }

    public static List<BookingForItemDTO> toBookingForItemDTO(List<Booking> bookings) {
        List<BookingForItemDTO> bookingForItemDTOS = new ArrayList<>();
        for (Booking booking : bookings) {
            BookingForItemDTO bookingForItemDTO = new BookingForItemDTO();
            bookingForItemDTO.setId(booking.getId());
            bookingForItemDTO.setStart(booking.getStart());
            bookingForItemDTO.setEnd(booking.getEnd());
            bookingForItemDTOS.add(bookingForItemDTO);
        }
        return bookingForItemDTOS;
    }
}