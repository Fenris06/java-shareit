package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestAnswerDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestGetAnswerDTO;
import ru.practicum.shareit.request.service.ItemRequestService;


import java.util.List;

import static ru.practicum.shareit.header.ControllerHeader.X_SHARER_USER_ID;


@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestService service;

    @PostMapping
    public ItemRequestAnswerDto createRequest(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                              @RequestBody ItemRequestDto requestDto) {
        return service.createRequest(userId, requestDto);
    }

    @GetMapping
    public List<ItemRequestGetAnswerDTO> getOwnerRequest(@RequestHeader(X_SHARER_USER_ID) Long userId) {
        return service.getOwnerRequest(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestGetAnswerDTO> getAllRequests(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                                        @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                        @RequestParam(name = "size", defaultValue = "20") Integer size) {
        return service.getAllRequests(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ItemRequestGetAnswerDTO getById(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                           @PathVariable("requestId") Long requestId) {
        return service.getById(userId, requestId);
    }
}
