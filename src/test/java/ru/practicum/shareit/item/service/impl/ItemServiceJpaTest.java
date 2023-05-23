package ru.practicum.shareit.item.service.impl;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.dto.BookingForItemDTO;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.comment.dto.AnswerCommentDTO;

import ru.practicum.shareit.comment.dto.CommentDTO;
import ru.practicum.shareit.comment.mapper.CommentMapper;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.storage.CommentRepository;

import ru.practicum.shareit.exception.NoArgumentException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDateBookingDto;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceJpaTest {
    @InjectMocks
    private ItemServiceJpa itemServiceJpa;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private BookingRepository bookingRepository;

    @Test
    void should_GetUserItems_ReturnListItems() {
        Long userId = 1L;
        Long itemId = 1L;
        Long requestId = 1L;
        Integer from = 0;
        Integer size = 2;
        LocalDateTime first = LocalDateTime.of(2023, 5, 22, 22, 0, 0);
        LocalDateTime second = LocalDateTime.of(2023, 5, 22, 22, 30, 0);
        LocalDateTime third = LocalDateTime.of(2023, 5, 23, 22, 0, 0);
        LocalDateTime fourth = LocalDateTime.of(2023, 5, 23, 22, 30, 0);

        Item item = new Item();
        item.setId(itemId);
        item.setName("drill");
        item.setDescription("Something for repair");
        item.setAvailable(true);
        item.setOwner(userId);
        item.setRequest(requestId);

        Booking lastBooking = new Booking();
        lastBooking.setId(1L);
        lastBooking.setStart(first);
        lastBooking.setEnd(second);
        lastBooking.setItem(item);
        lastBooking.setBooker(new User());
        lastBooking.setStatus(BookingStatus.APPROVED);

        Booking nextBooking = new Booking();
        nextBooking.setId(2L);
        nextBooking.setStart(third);
        nextBooking.setEnd(fourth);
        nextBooking.setItem(item);
        nextBooking.setBooker(new User());

        Comment comment = new Comment();
        comment.setId(1L);
        comment.setText("comment");
        comment.setItem(item);
        comment.setAuthor(new User());
        comment.setCreate(first);

        BookingForItemDTO itemFirstBook = BookingMapper.toBookingForItem(lastBooking);
        BookingForItemDTO itemLastBook = BookingMapper.toBookingForItem(nextBooking);
        AnswerCommentDTO answerCommentDTO = CommentMapper.toDTO(comment);
        ItemDateBookingDto dateBookingDto = ItemMapper.toItemWithDate(item, itemFirstBook, itemLastBook);
        dateBookingDto.getComments().add(answerCommentDTO);
        List<ItemDateBookingDto> items = List.of(dateBookingDto);

        List<Long> longs = List.of(1L);

        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(itemRepository.findByOwner(userId, PageRequest.of(from / size, size))).thenReturn(List.of(item));
        when(bookingRepository.findByItem_IdInAndStatusOrderByStartAsc(any(), any())).thenReturn(List.of(lastBooking, nextBooking));
        when(commentRepository.findByItem_IdIn(longs)).thenReturn(List.of(comment));

        List<ItemDateBookingDto> test = itemServiceJpa.getUserItems(userId, from, size);
        assertEquals(test, items);
        verify(userRepository, times(1)).findById(userId);
        verify(itemRepository, times(1)).findByOwner(userId, PageRequest.of(from / size, size));
        verify(bookingRepository, times(1)).findByItem_IdInAndStatusOrderByStartAsc(anyCollection(), any());
        verify(commentRepository, times(1)).findByItem_IdIn(longs);
    }


    @Test
    void should_GetItem_ReturnItem_IfItemFound() {
        Long userId = 1L;
        Long itemId = 1L;
        Long requestId = 1L;

        LocalDateTime first = LocalDateTime.of(2023, 5, 22, 22, 0, 0);
        LocalDateTime second = LocalDateTime.of(2023, 5, 22, 22, 30, 0);
        LocalDateTime third = LocalDateTime.of(2023, 5, 23, 22, 0, 0);
        LocalDateTime fourth = LocalDateTime.of(2023, 5, 23, 22, 30, 0);

        Item item = new Item();
        item.setId(itemId);
        item.setName("drill");
        item.setDescription("Something for repair");
        item.setAvailable(true);
        item.setOwner(userId);
        item.setRequest(requestId);

        Booking lastBooking = new Booking();
        lastBooking.setId(1L);
        lastBooking.setStart(first);
        lastBooking.setEnd(second);
        lastBooking.setItem(item);
        lastBooking.setBooker(new User());
        lastBooking.setStatus(BookingStatus.APPROVED);

        Booking nextBooking = new Booking();
        nextBooking.setId(2L);
        nextBooking.setStart(third);
        nextBooking.setEnd(fourth);
        nextBooking.setItem(item);
        nextBooking.setBooker(new User());

        Comment comment = new Comment();
        comment.setId(1L);
        comment.setText("comment");
        comment.setItem(item);
        comment.setAuthor(new User());
        comment.setCreate(first);

        BookingForItemDTO itemFirstBook = BookingMapper.toBookingForItem(lastBooking);
        BookingForItemDTO itemLastBook = BookingMapper.toBookingForItem(nextBooking);
        AnswerCommentDTO answerCommentDTO = CommentMapper.toDTO(comment);
        ItemDateBookingDto dateBookingDto = ItemMapper.toItemWithDate(item, itemFirstBook, itemLastBook);
        dateBookingDto.getComments().add(answerCommentDTO);

        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(bookingRepository.findByItem_IdAndStatusOrderByStartAsc(any(), any())).thenReturn(List.of(lastBooking, nextBooking));
        when(commentRepository.findByItem_Id(itemId)).thenReturn(List.of(comment));

        ItemDateBookingDto test = itemServiceJpa.getItem(userId, itemId);

        assertEquals(test, dateBookingDto);
        verify(userRepository, times(1)).findById(userId);
        verify(itemRepository, times(1)).findById(itemId);
        verify(bookingRepository, times(1)).findByItem_IdAndStatusOrderByStartAsc(any(), any());
    }

    @Test
    void should_CreateItem_CreateItem_IfAllFieldsValid() {
        Long userId = 1L;
        Long itemId = 1L;
        Long requestId = 1L;

        Item item = new Item();
        item.setId(itemId);
        item.setName("drill");
        item.setDescription("Something for repair");
        item.setAvailable(true);
        item.setOwner(userId);
        item.setRequest(requestId);

        ItemDto itemDto = ItemMapper.itemToDTO(item);

        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(itemRepository.save(any())).thenReturn(item);

        ItemDto test = itemServiceJpa.createItem(userId, itemDto);
        assertEquals(test, itemDto);
        verify(itemRepository, times(1)).save(any());
    }

    @Test
    void shouldNot_CreateItem_CreateItem_IfUserNotValid() {
        Long userId = 1L;
        Long itemId = 1L;
        Long requestId = 1L;

        Item item = new Item();
        item.setId(itemId);
        item.setName("drill");
        item.setDescription("Something for repair");
        item.setAvailable(true);
        item.setOwner(userId);
        item.setRequest(requestId);

        ItemDto itemDto = ItemMapper.itemToDTO(item);

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        NotFoundException e = assertThrows(NotFoundException.class, () -> itemServiceJpa.createItem(userId, itemDto));

        assertEquals("User not found", e.getMessage());
        verify(itemRepository, never()).save(item);
    }

    @Test
    void shouldNot_CreateItem_CreateItem_IfNameNotValid() {
        Long userId = 1L;
        Long itemId = 1L;
        Long requestId = 1L;

        Item item = new Item();
        item.setId(itemId);
        item.setName("");
        item.setDescription("Something for repair");
        item.setAvailable(true);
        item.setOwner(userId);
        item.setRequest(requestId);

        ItemDto itemDto = ItemMapper.itemToDTO(item);

        NoArgumentException e = assertThrows(NoArgumentException.class, () -> itemServiceJpa.createItem(userId, itemDto));

        assertEquals("Item name not add", e.getMessage());
        verify(itemRepository, never()).save(item);
    }

    @Test
    void shouldNot_CreateItem_CreateItem_IfDescriptionNotValid() {
        Long userId = 1L;
        Long itemId = 1L;
        Long requestId = 1L;

        Item item = new Item();
        item.setId(itemId);
        item.setName("drill");
        item.setDescription("");
        item.setAvailable(true);
        item.setOwner(userId);
        item.setRequest(requestId);

        ItemDto itemDto = ItemMapper.itemToDTO(item);

        NoArgumentException e = assertThrows(NoArgumentException.class, () -> itemServiceJpa.createItem(userId, itemDto));

        assertEquals("Item description not add", e.getMessage());
        verify(itemRepository, never()).save(item);
    }

    @Test
    void shouldNot_CreateItem_CreateItem_IfAvailableNotValid() {
        Long userId = 1L;
        Long itemId = 1L;
        Long requestId = 1L;

        Item item = new Item();
        item.setId(itemId);
        item.setName("drill");
        item.setDescription("Something for repair");
        item.setAvailable(null);
        item.setOwner(userId);
        item.setRequest(requestId);

        ItemDto itemDto = ItemMapper.itemToDTO(item);

        NoArgumentException e = assertThrows(NoArgumentException.class, () -> itemServiceJpa.createItem(userId, itemDto));

        assertEquals("Item available not add", e.getMessage());
        verify(itemRepository, never()).save(item);
    }

    @Test
    void should_UpdateItem_UpdateItemIfAllFieldsAreValid() {
        Long userId = 1L;
        Long itemId = 1L;
        Long requestId = 1L;

        ItemDto itemDto = new ItemDto();
        itemDto.setId(itemId);
        itemDto.setName("screwdriver");
        itemDto.setDescription("new description");
        itemDto.setAvailable(false);
        itemDto.setRequestId(requestId);

        Item item = new Item();
        item.setId(itemId);
        item.setName("drill");
        item.setDescription("Something for repair");
        item.setAvailable(true);
        item.setOwner(userId);
        item.setRequest(requestId);

        Item updateItem = new Item();
        updateItem.setId(itemId);
        updateItem.setName(itemDto.getName());
        updateItem.setDescription(itemDto.getDescription());
        updateItem.setAvailable(itemDto.getAvailable());
        updateItem.setOwner(item.getOwner());
        updateItem.setRequest(itemDto.getRequestId());

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(itemRepository.save(any())).thenReturn(updateItem);
        ItemDto test = itemServiceJpa.updateItem(userId, itemDto, itemId);

        assertEquals(test, itemDto);
        verify(itemRepository, times(1)).save(any());
    }

    @Test
    void shouldNot_UpdateItem_UpdateItemIfOwnerNotWalid() {
        Long userId = 1L;
        Long itemId = 1L;
        Long requestId = 1L;
        Long exceptionUser = 2L;

        ItemDto itemDto = new ItemDto();
        itemDto.setId(itemId);
        itemDto.setName("screwdriver");
        itemDto.setDescription("new description");
        itemDto.setAvailable(false);
        itemDto.setRequestId(requestId);

        Item item = new Item();
        item.setId(itemId);
        item.setName("drill");
        item.setDescription("Something for repair");
        item.setAvailable(true);
        item.setOwner(userId);
        item.setRequest(requestId);

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        NotFoundException e = assertThrows(NotFoundException.class, () -> itemServiceJpa.updateItem(exceptionUser, itemDto, itemId));

        assertEquals("User can't update this item", e.getMessage());
        verify(itemRepository, never()).save(any());
    }

    @Test
    void should_ItemSearch_ReturnListItem_IfItemFound() {
        Long userId = 1L;
        Long itemId = 1L;
        Long requestId = 1L;
        Integer from = 0;
        Integer size = 1;
        String text = "text";

        Item item = new Item();
        item.setId(itemId);
        item.setName("drill");
        item.setDescription("Something for repair");
        item.setAvailable(true);
        item.setOwner(userId);
        item.setRequest(requestId);

        ItemDto itemDto = ItemMapper.itemToDTO(item);

        List<ItemDto> itemDTOs = List.of(itemDto);

        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(itemRepository.itemSearch(text, PageRequest.of(from / size, size))).thenReturn(List.of(item));

        List<ItemDto> test = itemServiceJpa.itemSearch(userId, text, from, size);

        assertEquals(test, itemDTOs);
        verify(itemRepository, times(1)).itemSearch(text, PageRequest.of(from / size, size));
    }

    @Test
    void should_CreateComment_CreateCommentIfFindBooking() {
        Long userId = 1L;
        Long itemId = 1L;
        Long requestId = 1L;

        LocalDateTime first = LocalDateTime.of(2023, 5, 22, 22, 0, 0);
        LocalDateTime second = LocalDateTime.of(2023, 5, 22, 22, 30, 0);
        LocalDateTime third = LocalDateTime.of(2023, 5, 23, 22, 0, 0);
        LocalDateTime fourth = LocalDateTime.of(2023, 5, 23, 22, 30, 0);

        Item item = new Item();
        item.setId(itemId);
        item.setName("drill");
        item.setDescription("Something for repair");
        item.setAvailable(true);
        item.setOwner(userId);
        item.setRequest(requestId);

        Booking lastBooking = new Booking();
        lastBooking.setId(1L);
        lastBooking.setStart(first);
        lastBooking.setEnd(second);
        lastBooking.setItem(item);
        lastBooking.setBooker(new User());
        lastBooking.setStatus(BookingStatus.APPROVED);

        Booking nextBooking = new Booking();
        nextBooking.setId(2L);
        nextBooking.setStart(third);
        nextBooking.setEnd(fourth);
        nextBooking.setItem(item);
        nextBooking.setBooker(new User());

        Comment comment = new Comment();
        comment.setId(1L);
        comment.setText("comment");
        comment.setItem(item);
        comment.setAuthor(new User());
        comment.setCreate(first);

        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setText(comment.getText());

        AnswerCommentDTO answerCommentDTO = CommentMapper.toDTO(comment);

        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(bookingRepository.findByBooker_IdAndItem_IdAndEndBefore(anyLong(), anyLong(), any(LocalDateTime.class))).thenReturn(List.of(lastBooking, nextBooking));
        when(commentRepository.save(any())).thenReturn(comment);

        AnswerCommentDTO test = itemServiceJpa.createComment(userId, itemId, commentDTO);

        assertEquals(test, answerCommentDTO);
        verify(userRepository, times(1)).findById(userId);
        verify(bookingRepository, times(1)).findByBooker_IdAndItem_IdAndEndBefore(anyLong(), anyLong(), any(LocalDateTime.class));
        verify(commentRepository, times(1)).save(any());
    }

    @Test
    void shouldNot_CreateComment_CreateCommentIfNotFindBooking() {
        Long userId = 1L;
        Long itemId = 1L;
        Long requestId = 1L;

        LocalDateTime first = LocalDateTime.of(2023, 5, 22, 22, 0, 0);

        Item item = new Item();
        item.setId(itemId);
        item.setName("drill");
        item.setDescription("Something for repair");
        item.setAvailable(true);
        item.setOwner(userId);
        item.setRequest(requestId);

        Comment comment = new Comment();
        comment.setId(1L);
        comment.setText("comment");
        comment.setItem(item);
        comment.setAuthor(new User());
        comment.setCreate(first);

        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setText(comment.getText());

        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(bookingRepository.findByBooker_IdAndItem_IdAndEndBefore(anyLong(), anyLong(), any(LocalDateTime.class))).thenReturn(List.of());

        NoArgumentException e = assertThrows(NoArgumentException.class, () -> itemServiceJpa.createComment(userId, itemId, commentDTO));

        assertEquals("You can't write comment for this item", e.getMessage());
        verify(commentRepository, never()).save(any());
    }
}