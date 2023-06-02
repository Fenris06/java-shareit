package ru.practicum.shareit.request.mapper;

import ru.practicum.shareit.request.dto.ItemRequestAnswerDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestGetAnswerDTO;
import ru.practicum.shareit.request.model.ItemRequest;

import java.time.LocalDateTime;


public class ItemRequestMapper {
    public static ItemRequest fromDTO(Long userId, ItemRequestDto itemRequestDto, LocalDateTime dateTime) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription(itemRequestDto.getDescription());
        itemRequest.setRequestorId(userId);
        itemRequest.setCreated(dateTime);
        return itemRequest;
    }

    public static ItemRequestAnswerDto toDTO(ItemRequest itemRequest) {
        ItemRequestAnswerDto itemRequestAnswerDto = new ItemRequestAnswerDto();
        itemRequestAnswerDto.setId(itemRequest.getId());
        itemRequestAnswerDto.setDescription(itemRequest.getDescription());
        itemRequestAnswerDto.setCreated(itemRequest.getCreated());
        return itemRequestAnswerDto;
    }

    public static ItemRequestGetAnswerDTO toGetDto(ItemRequest itemRequest) {
        ItemRequestGetAnswerDTO getAnswerDTO = new ItemRequestGetAnswerDTO();
        getAnswerDTO.setId(itemRequest.getId());
        getAnswerDTO.setDescription(itemRequest.getDescription());
        getAnswerDTO.setCreated(itemRequest.getCreated());
        return getAnswerDTO;
    }
}