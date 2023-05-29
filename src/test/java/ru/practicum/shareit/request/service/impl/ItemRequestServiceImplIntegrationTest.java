package ru.practicum.shareit.request.service.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestAnswerDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestGetAnswerDTO;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

@SpringBootTest
class ItemRequestServiceImplIntegrationTest {
    @Autowired
    private ItemRequestServiceImpl service;
    @Autowired
    private ItemRequestRepository itemRequestRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;

    @AfterEach
    private void afterEach() {
        itemRequestRepository.deleteAll();
        itemRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void should_Create_CreateRequest_AndGetOwnerRequest_ReturnListRequests() {
        User firstUser = new User();
        firstUser.setName("Artem");
        firstUser.setEmail("Sergei@yandex.ru");
        User testFirst = userRepository.save(firstUser);

        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("something for java test");

        ItemRequestAnswerDto itemRequestAnswerDto = service.createRequest(testFirst.getId(), itemRequestDto);

        Item item = new Item();
        item.setName("drill");
        item.setDescription("Something for repair");
        item.setAvailable(true);
        item.setOwner(testFirst.getId());
        item.setRequest(itemRequestAnswerDto.getId());

        ItemDto createItem = ItemMapper.itemToDTO(itemRepository.save(item));

        ItemRequestGetAnswerDTO request = new ItemRequestGetAnswerDTO();
        request.setId(itemRequestAnswerDto.getId());
        request.setDescription(itemRequestAnswerDto.getDescription());
        request.setCreated(itemRequestAnswerDto.getCreated());
        request.getItems().add(createItem);

        List<ItemRequestGetAnswerDTO> requests = List.of(request);

        List<ItemRequestGetAnswerDTO> test = service.getOwnerRequest(testFirst.getId());

        assertEquals(test.size(), 1);
        assertEquals(test.get(0).getId(), requests.get(0).getId());
        assertEquals(test.get(0).getItems(), requests.get(0).getItems());
    }
}