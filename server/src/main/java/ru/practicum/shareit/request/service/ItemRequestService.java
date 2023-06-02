package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestAnswerDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestGetAnswerDTO;

import java.util.List;

public interface ItemRequestService {
    ItemRequestAnswerDto createRequest(Long userId, ItemRequestDto requestDto);

    List<ItemRequestGetAnswerDTO> getOwnerRequest(Long userId);

    List<ItemRequestGetAnswerDTO> getAllRequests(Long userId, Integer from, Integer size);

    ItemRequestGetAnswerDTO getById(Long userId, Long requestId);
}
