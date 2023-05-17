package ru.practicum.shareit.item.service.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.storage.ItemRepository;

import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Primary
public class ItemServiceJpa implements ItemService {
    private final ItemRepository repository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ItemDateBookingDto> getUserItems(Long userId) {
        checkUser(userId);
        LocalDateTime dateTime = LocalDateTime.now();
        List<Item> items = repository.findByOwner(userId);
        List<Long> itemIds = items.stream().map(Item::getId).collect(Collectors.toList());
        List<Booking> itemsBooking = bookingRepository.findByItem_IdInAndStatusOrderByStartAsc(itemIds, BookingStatus.APPROVED);
        List<Booking> firstBookings = findFirstBookings(itemsBooking, dateTime);
        List<Booking> lastBookings = findLastBookings(itemsBooking, dateTime);
        List<ItemDateBookingDto> itemWithBooking = setBookingDate(items, firstBookings, lastBookings);
        return findCommentsForBookerItems(itemWithBooking);
    }

    @Override
    @Transactional(readOnly = true)
    public ItemDateBookingDto getItem(Long userId, Long id) {
        checkUser(userId);
        Item item = repository.findById(id).orElseThrow(() -> new NotFoundException("Item not found"));
        List<Booking> itemBookings = bookingRepository.findByItem_IdAndStatusOrderByStartAsc(item.getId(),
                BookingStatus.APPROVED);
        LocalDateTime dateTime = LocalDateTime.now();
        BookingForItemDTO firstBooking = null;
        BookingForItemDTO lastBooking = null;
        if (Objects.equals(item.getOwner(), userId)) {
            firstBooking = findFirstBooking(itemBookings, dateTime);
            lastBooking = findLastBooking(itemBookings, dateTime);
        }
        ItemDateBookingDto itemDateBookingDto = ItemMapper.toItemWithDate(item, firstBooking, lastBooking);
        return findCommentForItem(itemDateBookingDto);
    }

    @Override
    @Transactional
    public ItemDto createItem(Long userId, ItemDto itemDto) {
        checkItemFields(itemDto);
        checkUser(userId);
        Item item = ItemMapper.itemFromDTO(itemDto);
        item.setOwner(userId);
        return ItemMapper.itemToDTO(repository.save(item));
    }

    @Override
    @Transactional
    public ItemDto updateItem(Long userId, ItemDto itemDto, Long itemId) {
        Item item = ItemMapper.itemFromDTO(itemDto);
        return ItemMapper.itemToDTO(updateItemFields(item, itemId, userId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> itemSearch(Long userId, String text) {
        checkUser(userId);
        if (text.isEmpty()) {
            return new ArrayList<>();
        }
        return repository.itemSearch(text).stream().map(ItemMapper::itemToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AnswerCommentDTO createComment(Long userId, Long itemId, CommentDTO commentDTO) {
        checkUser(userId);
        LocalDateTime dateTime = LocalDateTime.now();
        Booking booking = findBookingForComment(userId, itemId, dateTime);
        User user = booking.getBooker();
        Item item = booking.getItem();
        Comment comment = CommentMapper.fromDTO(commentDTO, item, user, dateTime);
        return CommentMapper.toDTO(commentRepository.save(comment));
    }

    private void checkItemFields(ItemDto itemDto) {
        if (itemDto.getName() == null || itemDto.getName().isEmpty()) {
            throw new NoArgumentException("Item name not add");
        }
        if (itemDto.getDescription() == null || itemDto.getDescription().isEmpty()) {
            throw new NoArgumentException("Item description not add");
        }
        if (itemDto.getAvailable() == null) {
            throw new NoArgumentException("Item available not add");
        }
    }

    private Item updateItemFields(Item item, Long itemId, Long userId) {
        Item updateItem = repository.findById(itemId).orElseThrow(() -> new NotFoundException("Item not found"));
        checkOwner(userId, updateItem);
        if (item.getName() != null && !item.getName().isEmpty()) {
            updateItem.setName(item.getName());
        }
        if (item.getDescription() != null && !item.getDescription().isEmpty()) {
            updateItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            updateItem.setAvailable(item.getAvailable());
        }
        return repository.save(updateItem);
    }

    private void checkOwner(Long userId, Item item) {
        if (!Objects.equals(userId, item.getOwner())) {
            throw new NotFoundException("User can't update this item");
        }
    }

    private void checkUser(Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
    }

    private BookingForItemDTO findFirstBooking(List<Booking> bookings, LocalDateTime dateTime) {
        return bookings.stream()
                .filter(booking -> booking.getStart().isBefore(dateTime))
                .max(Comparator.comparing(Booking::getStart)).map(BookingMapper::toBookingForItem)
                .orElse(null);
    }

    private BookingForItemDTO findLastBooking(List<Booking> bookings, LocalDateTime dateTime) {
        return bookings.stream()
                .filter(booking -> booking.getStart().isAfter(dateTime))
                .min(Comparator.comparing(Booking::getStart)).map(BookingMapper::toBookingForItem)
                .orElse(null);
    }

    private List<Booking> findFirstBookings(List<Booking> bookings, LocalDateTime dateTime) {
        Map<Long, List<Booking>> itemBookings = new HashMap<>();
        List<Booking> sortBooking = new ArrayList<>();
        for (Booking booking : bookings) {
            if (itemBookings.containsKey(booking.getItem().getId())) {
                itemBookings.get(booking.getItem().getId()).add(booking);
            } else {
                List<Booking> listBookings = new ArrayList<>();
                listBookings.add(booking);
                itemBookings.put(booking.getItem().getId(), listBookings);
            }
        }
        for (List<Booking> bookings1 : itemBookings.values()) {
            Booking booking = bookings1.stream()
                    .filter(booking1 -> booking1.getStart().isBefore(dateTime))
                    .max(Comparator.comparing(Booking::getStart))
                    .orElse(new Booking());
            sortBooking.add(booking);
        }
        return sortBooking;
    }

    private List<Booking> findLastBookings(List<Booking> bookings, LocalDateTime dateTime) {
        Map<Long, List<Booking>> itemBookings = new HashMap<>();
        List<Booking> sortBooking = new ArrayList<>();
        for (Booking booking : bookings) {
            if (itemBookings.containsKey(booking.getItem().getId())) {
                itemBookings.get(booking.getItem().getId()).add(booking);
            } else {
                List<Booking> listBookings = new ArrayList<>();
                listBookings.add(booking);
                itemBookings.put(booking.getItem().getId(), listBookings);
            }
        }
        for (List<Booking> bookings1 : itemBookings.values()) {
            Booking booking = bookings1.stream()
                    .filter(booking1 -> booking1.getStart().isAfter(dateTime))
                    .min(Comparator.comparing(Booking::getStart))
                    .orElse(new Booking());
            sortBooking.add(booking);
        }
        return sortBooking;
    }

    private List<ItemDateBookingDto> setBookingDate(List<Item> items, List<Booking> firstBookings, List<Booking> lastBookings) {
        BookingForItemDTO firstBooking = null;
        BookingForItemDTO lastBooking = null;
        List<ItemDateBookingDto> itemDate = items.stream()
                .map(item -> ItemMapper.toItemWithDate(item, firstBooking, lastBooking))
                .collect(Collectors.toList());
        for (ItemDateBookingDto item : itemDate) {
            for (Booking booking : firstBookings) {
                if (Objects.equals(item.getId(), booking.getItem().getId())) {
                    BookingForItemDTO itemBooking = BookingMapper.toBookingForItem(booking);
                    item.setLastBooking(itemBooking);
                }
            }
            for (Booking booking : lastBookings) {
                if (Objects.equals(item.getId(), booking.getItem().getId())) {
                    BookingForItemDTO bookingLast = BookingMapper.toBookingForItem(booking);
                    item.setNextBooking(bookingLast);
                }
            }
        }
        return itemDate;
    }

    private Booking findBookingForComment(Long userId, Long itemId, LocalDateTime dateTime) {
        List<Booking> bookings = bookingRepository.findByBooker_IdAndItem_IdAndEndBefore(userId, itemId, dateTime);
        if (bookings.isEmpty()) {
            throw new NoArgumentException("You can't write comment for this item");
        }
        return bookings.get(0);
    }

    private ItemDateBookingDto findCommentForItem(ItemDateBookingDto itemDateBookingDto) {
        List<AnswerCommentDTO> itemComment = commentRepository.findByItem_Id(itemDateBookingDto.getId())
                .stream()
                .map(CommentMapper::toDTO)
                .collect(Collectors.toList());
        if (!itemComment.isEmpty()) {
            itemDateBookingDto.getComments().addAll(itemComment);
        }
        return itemDateBookingDto;
    }

    private List<ItemDateBookingDto> findCommentsForBookerItems(List<ItemDateBookingDto> bookerItems) {
        List<Long> itemIds = bookerItems.stream().map(ItemDateBookingDto::getId).collect(Collectors.toList());
        List<Comment> itemComment = commentRepository.findByItem_IdIn(itemIds);
        if (!itemComment.isEmpty()) {
            for (ItemDateBookingDto item : bookerItems) {
                for (Comment comment : itemComment) {
                    if (Objects.equals(item.getId(), comment.getItem().getId())) {
                        AnswerCommentDTO answerComment = CommentMapper.toDTO(comment);
                        item.getComments().add(answerComment);
                    }
                }
            }
        }
        return bookerItems;
    }
}
