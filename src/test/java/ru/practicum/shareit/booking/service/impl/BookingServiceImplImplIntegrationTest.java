package ru.practicum.shareit.booking.service.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingAnswerDTO;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;

import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BookingServiceImplImplIntegrationTest {
    @Autowired
    private BookingServiceImpl bookingService;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private UserRepository userRepository;

    @AfterEach
    private void afterEach() {
        bookingRepository.deleteAll();
        itemRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void createBooking() {
        LocalDateTime start = LocalDateTime.now().plusMinutes(1);
        LocalDateTime end = LocalDateTime.now().plusMinutes(30);
        User firstUser = new User();
        firstUser.setName("Artem");
        firstUser.setEmail("Sersdgei@yandex.ru");
        User owner = userRepository.save(firstUser);

        User secondUser = new User();
        firstUser.setName("Sergey");
        firstUser.setEmail("Ssccdi@yandex.ru");
        User bookingUser = userRepository.save(secondUser);

        Item item = new Item();
        item.setName("drill");
        item.setDescription("Something for repair");
        item.setAvailable(true);
        item.setOwner(owner.getId());
        Item createItem = itemRepository.save(item);

        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(createItem.getId());
        bookingDto.setStart(start);
        bookingDto.setEnd(end);

        Booking booking = BookingMapper.fromDto(bookingDto, createItem, bookingUser);
        booking.setStatus(BookingStatus.WAITING);
        BookingAnswerDTO answerDTO = BookingMapper.toDto(booking);

        BookingAnswerDTO test = bookingService.createBooking(bookingUser.getId(), bookingDto);

        assertEquals(test.getBooker().getId(), answerDTO.getBooker().getId());
        assertEquals(test.getStart(), answerDTO.getStart());
        assertEquals(test.getEnd(), answerDTO.getEnd());
        assertEquals(test.getStatus(), answerDTO.getStatus());
        assertEquals(test.getItem().getId(), answerDTO.getItem().getId());
    }
}