package ru.practicum.shareit.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.comment.dto.AnswerCommentDTO;
import ru.practicum.shareit.comment.dto.CommentDTO;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDateBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = ItemController.class)
class ItemControllerTest {
    @MockBean
    private ItemService itemService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @SneakyThrows
    @Test
    void should_GetUserItems_ReturnListUserItems_IfItemsAdd() {
        Long id = 1L;
        Integer from = 0;
        Integer size = 2;
        ItemDateBookingDto itemDateBookingDto = new ItemDateBookingDto();
        itemDateBookingDto.setId(1L);
        itemDateBookingDto.setName("drill");
        itemDateBookingDto.setDescription("Something for repair");
        itemDateBookingDto.setAvailable(true);
        itemDateBookingDto.setLastBooking(null);
        itemDateBookingDto.setNextBooking(null);

        when(itemService.getUserItems(id, from, size)).thenReturn(List.of(itemDateBookingDto));

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", id)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(itemDateBookingDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(itemDateBookingDto.getName())))
                .andExpect(jsonPath("$[0].description", is(itemDateBookingDto.getDescription())))
                .andExpect(jsonPath("$[0].available", is(itemDateBookingDto.getAvailable())))
                .andExpect(jsonPath("$[0].lastBooking", is(itemDateBookingDto.getLastBooking())))
                .andExpect(jsonPath("$[0].nextBooking", is(itemDateBookingDto.getLastBooking())))
                .andExpect(jsonPath("$[0].comments", is(itemDateBookingDto.getComments())));

        verify(itemService).getUserItems(id, from, size);
    }

    @SneakyThrows
    @Test
    void should_GetUserItems_ReturnEmptyListUserItems_IfItemsNotAdd() {
        Long id = 1L;
        Integer from = 0;
        Integer size = 2;

        when(itemService.getUserItems(id, from, size)).thenReturn(List.of());

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", id)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(itemService).getUserItems(id, from, size);
    }

    @SneakyThrows
    @Test
    void should_GetItem_ReturnItemById_IfItemFind() {
        Long userId = 1L;
        Long itemId = 1L;
        ItemDateBookingDto itemDateBookingDto = new ItemDateBookingDto();
        itemDateBookingDto.setId(1L);
        itemDateBookingDto.setName("drill");
        itemDateBookingDto.setDescription("Something for repair");
        itemDateBookingDto.setAvailable(true);
        itemDateBookingDto.setLastBooking(null);
        itemDateBookingDto.setNextBooking(null);

        when(itemService.getItem(userId, itemId)).thenReturn(itemDateBookingDto);

        mockMvc.perform(get("/items/{itemId}", itemId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDateBookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDateBookingDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDateBookingDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDateBookingDto.getAvailable())))
                .andExpect(jsonPath("$.lastBooking", is(itemDateBookingDto.getLastBooking())))
                .andExpect(jsonPath("$.nextBooking", is(itemDateBookingDto.getNextBooking())))
                .andExpect(jsonPath("$.comments", is(itemDateBookingDto.getComments())));

        verify(itemService).getItem(userId, itemId);
    }

    @SneakyThrows
    @Test
    void shouldNot_GetItem_ReturnItemById_IfItemNotFind() {
        Long userId = 1L;
        Long itemId = 1L;

        when(itemService.getItem(userId, itemId)).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/items/{itemId}", itemId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isNotFound());

        verify(itemService).getItem(userId, itemId);
    }

    @SneakyThrows
    @Test
    void should_CreateItem_CreateNewItem() {
        Long userId = 1L;
        ItemDto createItem = new ItemDto();
        createItem.setName("drill");
        createItem.setDescription("Something for repair");
        createItem.setAvailable(true);
        ItemDto returnItem = new ItemDto();
        returnItem.setId(1L);
        returnItem.setName("drill");
        returnItem.setDescription("Something for repair");
        returnItem.setAvailable(true);
        returnItem.setRequestId(2L);

        when(itemService.createItem(userId, createItem)).thenReturn(returnItem);

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(createItem))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(returnItem.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(returnItem.getName())))
                .andExpect(jsonPath("$.description", is(returnItem.getDescription())))
                .andExpect(jsonPath("$.available", is(returnItem.getAvailable())))
                .andExpect(jsonPath("$.requestId", is(returnItem.getRequestId()), Long.class));

        verify(itemService).createItem(userId, createItem);
    }

    @SneakyThrows
    @Test
    void should_UpdateItem_UpdateItemIfAllFieldCorrect() {
        Long userId = 1L;
        Long itemId = 1L;
        ItemDto updateField = new ItemDto();
        updateField.setName("drill");
        ItemDto returnItem = new ItemDto();
        returnItem.setId(itemId);
        returnItem.setName("drill");
        returnItem.setDescription("Something for repair");
        returnItem.setAvailable(true);
        returnItem.setRequestId(2L);

        when(itemService.updateItem(userId, updateField, itemId)).thenReturn(returnItem);

        mockMvc.perform(patch("/items/{itemId}", itemId)
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(updateField))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(returnItem.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(returnItem.getName())))
                .andExpect(jsonPath("$.description", is(returnItem.getDescription())))
                .andExpect(jsonPath("$.available", is(returnItem.getAvailable())))
                .andExpect(jsonPath("$.requestId", is(returnItem.getRequestId()), Long.class));

        verify(itemService).updateItem(userId, updateField, itemId);
    }

    @SneakyThrows
    @Test
    void should_SearchItems_ReturnListItemSearch() {
        Long userId = 1L;
        Long itemId = 1L;
        Integer from = 0;
        Integer size = 2;
        String text = "repair";
        ItemDto createItem = new ItemDto();
        createItem.setId(itemId);
        createItem.setName("drill");
        createItem.setDescription("Something for repair");
        createItem.setAvailable(true);

        when(itemService.itemSearch(userId, text, from, size)).thenReturn(List.of(createItem));

        mockMvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", userId)
                        .param("text", text)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(createItem.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(createItem.getName())))
                .andExpect(jsonPath("$[0].description", is(createItem.getDescription())))
                .andExpect(jsonPath("$[0].available", is(createItem.getAvailable())))
                .andExpect(jsonPath("$[0].requestId", is(createItem.getRequestId())));

        verify(itemService).itemSearch(userId, text, from, size);
    }

    @SneakyThrows
    @Test
    void createComment() {
        Long userId = 1L;
        Long itemId = 1L;
        Long commentId = 1L;
        LocalDateTime dateTime = LocalDateTime.now();
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setText("comment");
        AnswerCommentDTO comment = new AnswerCommentDTO();
        comment.setId(commentId);
        comment.setText("comment");
        comment.setAuthorName("Artem");
        comment.setCreated(dateTime);

        when(itemService.createComment(userId, itemId, commentDTO)).thenReturn(comment);

        mockMvc.perform(post("/items/{itemId}/comment", itemId)
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(commentDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(comment.getId()), Long.class))
                .andExpect(jsonPath("$.text", is(comment.getText())))
                .andExpect(jsonPath("$.authorName", is(comment.getAuthorName())));
        //.andExpect(jsonPath("$.created", is(comment.getCreated())));
    }

    @SneakyThrows
    @Test
    void shouldNot_SearchItems_ReturnListItemSearch_IfFromIsBelowZero() {
        Long userId = 1L;
        Integer from = -1;
        Integer size = 2;
        String text = "repair";

        mockMvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", userId)
                        .param("text", text)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isInternalServerError());

        verify(itemService, never()).itemSearch(userId, text, from, size);
    }

    @SneakyThrows
    @Test
    void shouldNot_SearchItems_ReturnListItemSearch_IfSizeIsMore100() {
        Long userId = 1L;
        Integer from = 1;
        Integer size = 101;
        String text = "repair";

        mockMvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", userId)
                        .param("text", text)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isInternalServerError());

        verify(itemService, never()).itemSearch(userId, text, from, size);
    }

    @SneakyThrows
    @Test
    void shouldNot_GetUserItems_ReturnListUserItems_IfFromBelowZero() {
        Long id = 1L;
        Integer from = -1;
        Integer size = 2;

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", id)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isInternalServerError());

        verify(itemService, never()).getUserItems(id, from, size);
    }

    @SneakyThrows
    @Test
    void shouldNot_GetUserItems_ReturnListUserItems_IfSizeMore100() {
        Long id = 1L;
        Integer from = 0;
        Integer size = 101;

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", id)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isInternalServerError());

        verify(itemService, never()).getUserItems(id, from, size);
    }
}