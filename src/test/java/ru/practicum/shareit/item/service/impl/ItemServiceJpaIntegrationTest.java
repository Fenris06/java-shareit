package ru.practicum.shareit.item.service.impl;

import org.junit.jupiter.api.AfterEach;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingForItemDTO;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;


import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.comment.dto.AnswerCommentDTO;
import ru.practicum.shareit.comment.mapper.CommentMapper;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.storage.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDateBookingDto;

import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;

import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.storage.UserRepository;


import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest()
class ItemServiceJpaIntegrationTest {
    @Autowired
    private ItemServiceJpa serviceJpa;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserService userService;

    @AfterEach
    private void afterEach() {
        commentRepository.deleteAll();
        bookingRepository.deleteAll();
        itemRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void getUserItems() {
        LocalDateTime first = LocalDateTime.of(2023, 5, 22, 22, 0, 0);
        LocalDateTime second = LocalDateTime.of(2023, 5, 22, 22, 30, 0);
        LocalDateTime third = LocalDateTime.of(2023, 6, 23, 22, 0, 0);
        LocalDateTime fourth = LocalDateTime.of(2023, 6, 23, 22, 30, 0);

        User firstUser = new User();
        firstUser.setName("Artem");
        firstUser.setEmail("Arrtem@yandex.ru");
        User testFirst = userRepository.save(firstUser);

        User secondUser = new User();
        secondUser.setName("Ivan");
        secondUser.setEmail("Irvan@yandex.ru");
        User testSecond = userRepository.save(secondUser);

        User thirdUser = new User();
        testFirst.setName("Maksim");
        thirdUser.setEmail("Marksim@yandex.ru");
        User testThird = userRepository.save(thirdUser);

        Item item = new Item();
        item.setName("drill");
        item.setDescription("Something for repair");
        item.setAvailable(true);
        item.setOwner(testFirst.getId());
        Item createItem = itemRepository.save(item);

        Booking lastBooking = new Booking();
        lastBooking.setStart(first);
        lastBooking.setEnd(second);
        lastBooking.setItem(createItem);
        lastBooking.setBooker(testSecond);
        lastBooking.setStatus(BookingStatus.APPROVED);
        bookingRepository.save(lastBooking);

        Booking nextBooking = new Booking();
        nextBooking.setStart(third);
        nextBooking.setEnd(fourth);
        nextBooking.setItem(createItem);
        nextBooking.setBooker(testThird);
        nextBooking.setStatus(BookingStatus.APPROVED);
        bookingRepository.save(nextBooking);

        Comment comment = new Comment();
        comment.setText("comment");
        comment.setItem(item);
        comment.setAuthor(testSecond);
        comment.setCreate(first);
        Comment itemComment = commentRepository.save(comment);

        BookingForItemDTO last = BookingMapper.toBookingForItem(lastBooking);
        BookingForItemDTO next = BookingMapper.toBookingForItem(nextBooking);
        AnswerCommentDTO answerCommentDTO = CommentMapper.toDTO(itemComment);

        ItemDateBookingDto dateBookingDto = ItemMapper.toItemWithDate(createItem, last, next);
        dateBookingDto.getComments().add(answerCommentDTO);

        List<ItemDateBookingDto> items = List.of(dateBookingDto);

        List<ItemDateBookingDto> test = serviceJpa.getUserItems(testFirst.getId(), 0, 2);

        assertEquals(test.size(), 1);
        assertEquals(test, items);
    }


}