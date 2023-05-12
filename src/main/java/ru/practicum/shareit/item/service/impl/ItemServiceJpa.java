package ru.practicum.shareit.item.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.NoArgumentException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapstructMapper;
import ru.practicum.shareit.item.mapper.ItemMapstructMapperImpl;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.storage.ItemRepository;

import ru.practicum.shareit.user.storage.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Primary
public class ItemServiceJpa implements ItemService {
    private final ItemMapstructMapper mapper = new ItemMapstructMapperImpl();
    private final ItemRepository repository;
    private final UserRepository userRepository;

    @Override
    public List<ItemDto> getUserItems(Long userId) {
        checkUser(userId);
        return repository.findByOwner(userId).stream().map(mapper::itemToDTO).collect(Collectors.toList());
    }

    @Override
    public ItemDto getItem(Long userId, Long id) {
        checkUser(userId);
        return mapper.itemToDTO(repository.findById(id).orElseThrow(()-> new NotFoundException("Item not found")));
    }

    @Override
    public ItemDto createItem(Long userId, ItemDto itemDto) {
        checkItemFields(itemDto);
        checkUser(userId);
        Item item = mapper.itemFromDTO(itemDto);
        item.setOwner(userId);
        return mapper.itemToDTO(repository.save(item));
    }

    @Override
    public ItemDto updateItem(Long userId, ItemDto itemDto, Long itemId) {
        Item item = mapper.itemFromDTO(itemDto);
        return mapper.itemToDTO(updateItemFields(item, itemId, userId));
    }

    @Override
    public List<ItemDto> itemSearch(Long userId, String text) {
        checkUser(userId);
        if (text.isEmpty()) {
            return new ArrayList<>();
        }
        return repository.itemSearch(text).stream().map(mapper::itemToDTO).collect(Collectors.toList());
    }

    @Override
    public Item itemForBooking(Long id) {
        return repository.findById(id).orElseThrow(()-> new NotFoundException("Item not found"));
    }

    private void checkItemFields(ItemDto itemDto) {
        if (itemDto.getName() == null || itemDto.getName().isEmpty()) {
            throw new NoArgumentException("Item name not add");
        }
        if (itemDto.getDescription() == null || itemDto.getDescription().isEmpty()) {
            throw new NoArgumentException("Item description not add");
        }
        if (itemDto.getAvailable() == null) {
            throw new NoArgumentException("Item available not add");
        }
    }

    private Item updateItemFields(Item item, Long itemId, Long userId) {
        Item updateItem = repository.findById(itemId).orElseThrow(()-> new NotFoundException("Item not found"));
        checkOwner(userId, updateItem);
        if (item.getName() != null && !item.getName().isEmpty()) {
            updateItem.setName(item.getName());
        }
        if (item.getDescription() != null && !item.getDescription().isEmpty()) {
            updateItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            updateItem.setAvailable(item.getAvailable());
        }
        return repository.save(updateItem);
    }

    private void checkOwner(Long userId, Item item) {
        if (!Objects.equals(userId, item.getOwner())) {
            throw new NotFoundException("User can't update this item");
        }
    }

    private void checkUser(Long userId) {
        userRepository.findById(userId).orElseThrow(()-> new NotFoundException("User not found"));
    }
}
