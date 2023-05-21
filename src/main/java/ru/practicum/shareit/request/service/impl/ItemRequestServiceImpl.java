package ru.practicum.shareit.request.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestAnswerDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestGetAnswerDTO;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository repository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    @Transactional
    public ItemRequestAnswerDto createRequest(Long userId, ItemRequestDto requestDto) {
        checkUser(userId);
        LocalDateTime dateTime = LocalDateTime.now();
        ItemRequest itemRequest = ItemRequestMapper.fromDTO(userId, requestDto, dateTime);
        return ItemRequestMapper.toDTO(repository.save(itemRequest));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemRequestGetAnswerDTO> getOwnerRequest(Long userId) {
        checkUser(userId);
        List<ItemRequest> itemRequests = repository.findByRequestorIdOrderByCreatedDesc(userId);
        List<Item> items = findItemsForRequests(itemRequests);
        return setItemsToRequests(itemRequests, items);

    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemRequestGetAnswerDTO> getAllRequests(Long userId, Integer from, Integer size) {
        checkUser(userId);
        List<ItemRequest> itemAllRequests = getAllList(userId, from, size);
        List<Item> items = findItemsForRequests(itemAllRequests);
        return setItemsToRequests(itemAllRequests, items);
    }

    @Override
    @Transactional(readOnly = true)
    public ItemRequestGetAnswerDTO getById(Long userId, Long requestId) {
        checkUser(userId);
        ItemRequest itemRequest = repository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("itemRequest not found"));
        List<ItemDto> itemDTOs = itemRepository.findByRequestIn(List.of(requestId))
                .stream().
                map(ItemMapper::itemToDTO).
                collect(Collectors.toList());
        ItemRequestGetAnswerDTO getAnswerDTO = ItemRequestMapper.toGetDto(itemRequest);
        getAnswerDTO.getItems().addAll(itemDTOs);
        return getAnswerDTO;
    }

    private void checkUser(Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
    }

    private List<Item> findItemsForRequests(List<ItemRequest> itemRequests) {
        List<Long> requestIds = itemRequests.stream().map(ItemRequest::getId).collect(Collectors.toList());
        return itemRepository.findByRequestIn(requestIds);
    }

    private List<ItemRequestGetAnswerDTO> setItemsToRequests(List<ItemRequest> itemRequests, List<Item> items) {
        List<ItemRequestGetAnswerDTO> getAnswerDTO = itemRequests
                .stream().
                map(ItemRequestMapper::toGetDto)
                .collect(Collectors.toList());
        for (ItemRequestGetAnswerDTO request : getAnswerDTO) {
            for (Item item : items) {
                if (Objects.equals(request.getId(), item.getRequest())) {
                    ItemDto newItem = ItemMapper.itemToDTO(item);
                    request.getItems().add(newItem);
                }
            }
        }
        return getAnswerDTO;
    }

    private List<ItemRequest> getAllList(Long userId, Integer from, Integer size) {
        PageRequest page = PageRequest.of(from / size, size);
        return repository.findByRequestorIdNotOrderByCreatedDesc(userId, page);
    }
}
