package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.CommentDTO;
import ru.practicum.shareit.item.controller.cient.ItemClient;
import ru.practicum.shareit.item.dto.ItemDto;


import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;


@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient client;

    @GetMapping
    public ResponseEntity<Object> getUserItems(@RequestHeader("X-Sharer-User-Id") Long userId,
                                               @RequestParam(name = "from", defaultValue = "0") @Min(0) Integer from,
                                               @RequestParam(name = "size", defaultValue = "20") @Min(1) @Max(100) Integer size) {
        log.debug("received GET /items with HEADER {}", userId);
        return client.getUserItems(userId, from, size);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable("itemId") Long id) {
        log.debug("received GET /items/{}", id);
        return client.getItem(userId, id);
    }

    @PostMapping
    public ResponseEntity<Object> createItem(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody ItemDto itemDto) {
        log.debug("received POST /items/ with HEADER {} and BODY {}", userId, itemDto);
        return client.createItem(userId, itemDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @RequestBody ItemDto itemDto,
                                             @PathVariable("id") Long id) {
        log.debug("received PATCH /items/{} with HEADER {} and BODY {}", id, userId, itemDto);
        return client.updateItem(userId, itemDto, id);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(@RequestHeader("X-Sharer-User-Id") Long userId,
                                              @RequestParam String text,
                                              @RequestParam(name = "from", defaultValue = "0") @Min(0) Integer from,
                                              @RequestParam(name = "size", defaultValue = "20") @Min(1) @Max(100) Integer size) {
        log.debug("received GET /search with HEADER {} abd PARAM {}", userId, text);
        return client.searchItems(userId, text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                @PathVariable("itemId") Long itemId,
                                                @RequestBody @Valid CommentDTO commentDTO) {
        return client.createComment(userId, itemId, commentDTO);
    }
}
