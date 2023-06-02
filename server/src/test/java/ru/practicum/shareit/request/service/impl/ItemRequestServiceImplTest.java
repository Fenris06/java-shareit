package ru.practicum.shareit.request.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
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
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceImplTest {
    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private ItemRequestRepository itemRequestRepository;

    @Test
    void should_createRequest_CreateItemRequest_IfUserValid() {
        Long userId = 1L;
        Long requestId = 1L;
        Long itemId = 1L;
        LocalDateTime crate = LocalDateTime.of(2023, 5, 23, 19, 0, 0);

        Item item = new Item();
        item.setId(itemId);
        item.setName("drill");
        item.setDescription("Something for repair");
        item.setAvailable(true);
        item.setOwner(userId);
        item.setRequest(requestId);

        ItemRequest request = new ItemRequest();
        request.setId(requestId);
        request.setDescription("Something");
        request.setRequestorId(userId);
        request.setCreated(crate);

        ItemRequestDto requestDto = new ItemRequestDto();
        requestDto.setDescription("Something");

        ItemRequestAnswerDto answerDto = ItemRequestMapper.toDTO(request);

        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(itemRequestRepository.save(any())).thenReturn(request);

        ItemRequestAnswerDto test = itemRequestService.createRequest(userId, requestDto);
        assertEquals(test, answerDto);
        verify(itemRequestRepository, times(1)).save(any());
    }

    @Test
    void shouldNot_createRequest_CreateItemRequest_IfUserNotValid() {
        Long userId = 1L;

        ItemRequestDto requestDto = new ItemRequestDto();
        requestDto.setDescription("Something");

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        NotFoundException e = assertThrows(NotFoundException.class, () -> itemRequestService.createRequest(userId, requestDto));

        assertEquals("User not found", e.getMessage());
        verify(itemRequestRepository, never()).save(any());
    }

    @Test
    void should_GetOwnerRequest_ReturnListItemRequestWithItems() {
        Long userId = 1L;
        Long requestId = 1L;
        Long itemId = 1L;
        LocalDateTime crate = LocalDateTime.of(2023, 5, 23, 19, 0, 0);

        Item item = new Item();
        item.setId(itemId);
        item.setName("drill");
        item.setDescription("Something for repair");
        item.setAvailable(true);
        item.setOwner(userId);
        item.setRequest(requestId);

        ItemDto itemDto = ItemMapper.itemToDTO(item);

        ItemRequest request = new ItemRequest();
        request.setId(requestId);
        request.setDescription("Something");
        request.setRequestorId(userId);
        request.setCreated(crate);

        ItemRequestGetAnswerDTO answerDto = ItemRequestMapper.toGetDto(request);
        answerDto.getItems().add(itemDto);
        List<ItemRequestGetAnswerDTO> answerDTOS = List.of(answerDto);

        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(itemRequestRepository.findByRequestorIdOrderByCreatedDesc(userId)).thenReturn(List.of(request));
        when(itemRepository.findByRequestIn(List.of(itemId))).thenReturn(List.of(item));

        List<ItemRequestGetAnswerDTO> test = itemRequestService.getOwnerRequest(userId);
        assertEquals(test, answerDTOS);
        verify(userRepository, times(1)).findById(userId);
        verify(itemRequestRepository, times(1)).findByRequestorIdOrderByCreatedDesc(userId);
        verify(itemRepository, times(1)).findByRequestIn(List.of(itemId));
    }

    @Test
    void should_GetAllRequests_ReturnAllRequestWithItems() {
        Long userId = 1L;
        Long requestId = 1L;
        Long itemId = 1L;
        Integer from = 0;
        Integer size = 1;
        LocalDateTime crate = LocalDateTime.of(2023, 5, 23, 19, 0, 0);

        Item item = new Item();
        item.setId(itemId);
        item.setName("drill");
        item.setDescription("Something for repair");
        item.setAvailable(true);
        item.setOwner(userId);
        item.setRequest(requestId);

        ItemDto itemDto = ItemMapper.itemToDTO(item);

        ItemRequest request = new ItemRequest();
        request.setId(requestId);
        request.setDescription("Something");
        request.setRequestorId(userId);
        request.setCreated(crate);

        ItemRequestGetAnswerDTO answerDto = ItemRequestMapper.toGetDto(request);
        answerDto.getItems().add(itemDto);
        List<ItemRequestGetAnswerDTO> answerDTOS = List.of(answerDto);

        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(itemRequestRepository.findByRequestorIdNotOrderByCreatedDesc(userId, PageRequest.of(from / size, size))).thenReturn(List.of(request));
        when(itemRepository.findByRequestIn(List.of(itemId))).thenReturn(List.of(item));

        List<ItemRequestGetAnswerDTO> test = itemRequestService.getAllRequests(userId, from, size);

        assertEquals(test, answerDTOS);
        verify(userRepository, times(1)).findById(userId);
        verify(itemRequestRepository, times(1)).findByRequestorIdNotOrderByCreatedDesc(userId, PageRequest.of(from / size, size));
        verify(itemRepository, times(1)).findByRequestIn(List.of(itemId));
    }

    @Test
    void should_GetById_ReturnIte_RequestWithItems_IfRequestFind() {
        Long userId = 1L;
        Long requestId = 1L;
        Long itemId = 1L;
        LocalDateTime crate = LocalDateTime.of(2023, 5, 23, 19, 0, 0);

        Item item = new Item();
        item.setId(itemId);
        item.setName("drill");
        item.setDescription("Something for repair");
        item.setAvailable(true);
        item.setOwner(userId);
        item.setRequest(requestId);

        ItemDto itemDto = ItemMapper.itemToDTO(item);

        ItemRequest request = new ItemRequest();
        request.setId(requestId);
        request.setDescription("Something");
        request.setRequestorId(userId);
        request.setCreated(crate);

        ItemRequestGetAnswerDTO answerDto = ItemRequestMapper.toGetDto(request);
        answerDto.getItems().add(itemDto);


        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(itemRequestRepository.findById(requestId)).thenReturn(Optional.of(request));
        when(itemRepository.findByRequestIn(List.of(requestId))).thenReturn(List.of(item));

        ItemRequestGetAnswerDTO test = itemRequestService.getById(userId, requestId);

        assertEquals(test, answerDto);
        verify(userRepository, times(1)).findById(userId);
        verify(itemRequestRepository, times(1)).findById(requestId);
        verify(itemRepository, times(1)).findByRequestIn(List.of(requestId));
    }

    @Test
    void shouldNot_GetById_ReturnIte_RequestWithItems_IfRequestNotFind() {
        Long userId = 1L;
        Long requestId = 1L;
        Long itemId = 1L;
        LocalDateTime crate = LocalDateTime.of(2023, 5, 23, 19, 0, 0);

        Item item = new Item();
        item.setId(itemId);
        item.setName("drill");
        item.setDescription("Something for repair");
        item.setAvailable(true);
        item.setOwner(userId);
        item.setRequest(requestId);

        ItemRequest request = new ItemRequest();
        request.setId(requestId);
        request.setDescription("Something");
        request.setRequestorId(userId);
        request.setCreated(crate);

        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(itemRequestRepository.findById(requestId)).thenReturn(Optional.empty());

        NotFoundException e = assertThrows(NotFoundException.class, () -> itemRequestService.getById(userId, requestId));

        assertEquals("itemRequest not found", e.getMessage());
        verify(userRepository, times(1)).findById(userId);
        verify(itemRequestRepository, times(1)).findById(requestId);
        verify(itemRepository, never()).findByRequestIn(List.of(requestId));
    }
}
