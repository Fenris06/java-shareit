package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

@Mapper
public interface ItemMapstructMapper {

    Item itemFromDTO(ItemDto itemDto);

    ItemDto itemToDTO(Item item);
}
