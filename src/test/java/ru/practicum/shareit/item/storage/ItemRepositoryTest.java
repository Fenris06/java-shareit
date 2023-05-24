package ru.practicum.shareit.item.storage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase
class ItemRepositoryTest {
    Item first;
    Item second;
    Item third;
    @Autowired
    ItemRepository itemRepository;

    @BeforeEach
    public void beforeEach() {
        first = new Item();
        first.setName("drill");
        first.setDescription("Something for repair");
        first.setAvailable(true);
        first.setOwner(1L);
        itemRepository.save(first);

        second = new Item();
        second.setName("screwdriver");
        second.setDescription("Something for repair");
        second.setAvailable(false);
        second.setOwner(2L);
        itemRepository.save(second);

        third = new Item();
        third.setName("hammer");
        third.setDescription("nail tool");
        third.setAvailable(true);
        third.setOwner(3L);
        itemRepository.save(third);
    }

    @AfterEach
    public void afterEach() {
        itemRepository.deleteAll();
    }

    @Test
    void should_itemSearch_SearchListItemAvailableTrue_IfTextEqualsItemName() {
        String text = "drill";
        Integer from = 0;
        Integer size = 2;
        PageRequest page = PageRequest.of(from / size, size);

        List<Item> test = itemRepository.itemSearch(text, page);

        assertEquals(test.size(), 1);
        assertEquals(test.get(0).getName(), first.getName());
    }

    @Test
    void should_itemSearch_SearchListItemAvailableTrue_IfTextEqualsItemDescription() {
        String text = "repa";
        Integer from = 0;
        Integer size = 2;
        PageRequest page = PageRequest.of(from / size, size);


        List<Item> test = itemRepository.itemSearch(text, page);

        assertEquals(test.size(), 1);
        assertEquals(test.get(0).getName(), first.getName());
    }

    @Test
    void should_itemSearch_SearchListItemAvailableTrue_IfTextEqualsItemDescriptionOfTwoItems() {
        String text = "il";
        Integer from = 0;
        Integer size = 2;
        PageRequest page = PageRequest.of(from / size, size);

        List<Item> test = itemRepository.itemSearch(text, page);

        assertEquals(test.size(), 2);
        assertEquals(test.get(0).getName(), first.getName());
        assertEquals(test.get(1).getName(), third.getName());

    }

    @Test
    void should_itemSearch_SearchListItemAvailableTrue_IfTextEqualsItemDescriptionOfTwoItems_ReturnOneItemIfSizeOne() {
        String text = "il";
        Integer from = 0;
        Integer size = 1;
        PageRequest page = PageRequest.of(from / size, size);

        List<Item> test = itemRepository.itemSearch(text, page);

        assertEquals(test.size(), 1);
        assertEquals(test.get(0).getName(), first.getName());
    }

    @Test
    void should_itemSearch_SearchListItemAvailableTrue_ReturnEmptyListIfTextNotFind() {
        String text = "text";
        Integer from = 0;
        Integer size = 2;
        PageRequest page = PageRequest.of(from / size, size);

        List<Item> test = itemRepository.itemSearch(text, page);

        assertEquals(test.size(), 0);
    }
}