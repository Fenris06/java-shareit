package ru.practicum.shareit.item.service;


import ru.practicum.shareit.comment.dto.AnswerCommentDTO;
import ru.practicum.shareit.comment.dto.CommentDTO;
import ru.practicum.shareit.item.dto.ItemDateBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;


import java.util.List;

public interface ItemService {
    List<ItemDateBookingDto> getUserItems(Long userId);

    ItemDateBookingDto getItem(Long userId, Long id);

    ItemDto createItem(Long userId, ItemDto itemDto);

    ItemDto updateItem(Long userId, ItemDto itemDto, Long itemId);

    List<ItemDto> itemSearch(Long userId, String text);

    AnswerCommentDTO createComment(Long userId, Long itemId, CommentDTO commentDTO);
}
