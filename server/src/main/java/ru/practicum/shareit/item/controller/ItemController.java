package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.AnswerCommentDTO;
import ru.practicum.shareit.comment.dto.CommentDTO;

import ru.practicum.shareit.item.dto.ItemDateBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;


import java.util.List;

import static ru.practicum.shareit.header.ControllerHeader.X_SHARER_USER_ID;


@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public List<ItemDateBookingDto> getUserItems(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                                 @RequestParam(name = "from") Integer from,
                                                 @RequestParam(name = "size") Integer size) {
        log.debug("received GET /items with HEADER {}", userId);
        return itemService.getUserItems(userId, from, size);
    }

    @GetMapping("/{itemId}")
    public ItemDateBookingDto getItem(@RequestHeader(X_SHARER_USER_ID) Long userId, @PathVariable("itemId") Long id) {
        log.debug("received GET /items/{}", id);
        return itemService.getItem(userId, id);
    }

    @PostMapping
    public ItemDto createItem(@RequestHeader(X_SHARER_USER_ID) Long userId, @RequestBody ItemDto itemDto) {
        log.debug("received POST /items/ with HEADER {} and BODY {}", userId, itemDto);
        return itemService.createItem(userId, itemDto);
    }

    @PatchMapping("/{id}")
    public ItemDto updateItem(@RequestHeader(X_SHARER_USER_ID) Long userId,
                              @RequestBody ItemDto itemDto,
                              @PathVariable("id") Long id) {
        log.debug("received PATCH /items/{} with HEADER {} and BODY {}", id, userId, itemDto);
        return itemService.updateItem(userId, itemDto, id);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                     @RequestParam String text,
                                     @RequestParam(name = "from") Integer from,
                                     @RequestParam(name = "size") Integer size) {
        log.debug("received GET /search with HEADER {} abd PARAM {}", userId, text);
        return itemService.itemSearch(userId, text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public AnswerCommentDTO createComment(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                          @PathVariable("itemId") Long itemId,
                                          @RequestBody CommentDTO commentDTO) {
        return itemService.createComment(userId, itemId, commentDTO);
    }
}
