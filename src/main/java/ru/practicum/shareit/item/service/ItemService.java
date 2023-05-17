package ru.practicum.shareit.item.service;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.comment.dto.AnswerCommentDTO;
import ru.practicum.shareit.comment.dto.CommentDTO;
import ru.practicum.shareit.item.dto.ItemDateBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;


import java.util.List;

public interface ItemService {

    @Transactional(readOnly = true)
    List<ItemDateBookingDto> getUserItems(Long userId);

    @Transactional(readOnly = true)
    ItemDateBookingDto getItem(Long userId, Long id);

    @Transactional
    ItemDto createItem(Long userId, ItemDto itemDto);

    @Transactional
    ItemDto updateItem(Long userId, ItemDto itemDto, Long itemId);

    @Transactional(readOnly = true)
    List<ItemDto> itemSearch(Long userId, String text);

    @Transactional
    AnswerCommentDTO createComment(Long userId, Long itemId, CommentDTO commentDTO);
}
