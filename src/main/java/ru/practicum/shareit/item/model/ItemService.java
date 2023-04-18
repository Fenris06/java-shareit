package ru.practicum.shareit.item.model;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NoArgumentException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemStorage itemStorage;
    private final UserService userService;

    public List<ItemDto> getUserItems(Long userId) {
        userService.checkUser(userId);
        List<ItemDto> userItems = new ArrayList<>();
        List<Long> userItemIds = userService.getUserItemsId(userId);
        for (Long id : userItemIds) {
            ItemDto itemDto = getItem(id);
            userItems.add(itemDto);
        }
        return userItems;
    }

    public ItemDto getItem(Long id) {
        return ItemMapper.itemToDTO(itemStorage.getItem(id));
    }

    public ItemDto createItem(Long userId, ItemDto itemDto) {
        userService.checkUser(userId);
        checkItemFields(itemDto);
        Item item = ItemMapper.itemFromDTO(itemDto);
        item.setOwner(userId);
        Item createItem = itemStorage.createItem(item);
        userService.addUserItemId(userId, createItem.getId());
        return ItemMapper.itemToDTO(createItem);
    }

    public ItemDto updateItem(Long userId, ItemDto itemDto, Long itemId) {
        userService.checkUser(userId);
        Item item = ItemMapper.itemFromDTO(itemDto);
        checkUserItems(userId, itemId);
        return ItemMapper.itemToDTO(updateItemFields(item, itemId));
    }

    public List<ItemDto> itemSearch(Long userId, String text) {
        userService.checkUser(userId);
        List<ItemDto> itemSearch = new ArrayList<>();
        List<Item> items = itemStorage.getItems();
        if (text.isEmpty()) {
            return itemSearch;
        }
        String search = text.toLowerCase();
        for (Item item : items) {
            if (item.getAvailable()) {
                if (item.getName().toLowerCase().contains(search)
                        || item.getDescription().toLowerCase().contains(search)) {
                    itemSearch.add(ItemMapper.itemToDTO(item));
                }
            }
        }
        return itemSearch;
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

    private Item updateItemFields(Item item, Long itemId) {
        Item updateItem = itemStorage.getItem(itemId);
        if (item.getName() != null && !item.getName().isEmpty()) {
            updateItem.setName(item.getName());
        }
        if (item.getDescription() != null && !item.getDescription().isEmpty()) {
            updateItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            updateItem.setAvailable(item.getAvailable());
        }
        return itemStorage.updateItem(updateItem);
    }

    private void checkUserItems(Long userId, Long itemId) {
        List<Long> userItemsIds = userService.getUserItemsId(userId);
        if (!userItemsIds.contains(itemId)) {
            throw new NotFoundException("User doesn't have this item item");
        }
    }

}
