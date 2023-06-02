package ru.practicum.shareit.request.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestAnswerDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestGetAnswerDTO;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = ItemRequestController.class)
class ItemRequestControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ItemRequestService itemRequestService;

    @SneakyThrows
    @Test
    void should_CreateRequest_CreateItemRequest() {
        Long userId = 1L;
        Long requestId = 1L;
        LocalDateTime dateTime = LocalDateTime.of(2023, 5, 21, 18, 41, 1);

        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("Something");

        ItemRequestAnswerDto answerDto = new ItemRequestAnswerDto();
        answerDto.setId(requestId);
        answerDto.setDescription("Something");
        answerDto.setCreated(dateTime);

        when(itemRequestService.createRequest(userId, itemRequestDto)).thenReturn(answerDto);

        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(itemRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(answerDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(answerDto.getDescription())));
        //.andExpect(jsonPath("$.created", is(answerDto.getCreated().toString())))

        verify(itemRequestService).createRequest(userId, itemRequestDto);
    }

//    @SneakyThrows
//    @Test
//    void shouldNot_CreateRequest_IfFieldIsEmpty() {
//        Long userId = 1L;
//
//        ItemRequestDto itemRequestDto = new ItemRequestDto();
//        itemRequestDto.setDescription("");
//
//        mockMvc.perform(post("/requests")
//                        .header("X-Sharer-User-Id", userId)
//                        .content(objectMapper.writeValueAsString(itemRequestDto))
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
//
//        verify(itemRequestService, never()).createRequest(userId, itemRequestDto);
//    }

    @SneakyThrows
    @Test
    void should_GetOwnerRequest_ReturnListRequest() {
        Long userId = 1L;
        Long requestId = 1L;
        LocalDateTime dateTime = LocalDateTime.of(2023, 5, 21, 18, 41, 1);

        ItemRequestGetAnswerDTO getAnswerDTO = new ItemRequestGetAnswerDTO();
        getAnswerDTO.setId(requestId);
        getAnswerDTO.setDescription("Something");
        getAnswerDTO.setCreated(dateTime);

        when(itemRequestService.getOwnerRequest(userId)).thenReturn(List.of(getAnswerDTO));

        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(getAnswerDTO.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(getAnswerDTO.getDescription())))
                .andExpect(jsonPath("$[0].items", hasSize(0)));

        verify(itemRequestService).getOwnerRequest(userId);
    }

    @SneakyThrows
    @Test
    void should_GetOwnerRequest_ReturnEmptyListRequest_IfRequestNotAdd() {
        Long userId = 1L;

        when(itemRequestService.getOwnerRequest(userId)).thenReturn(List.of());

        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(itemRequestService).getOwnerRequest(userId);
    }

    @SneakyThrows
    @Test
    void should_getAllRequests_ReturnListRequest() {
        Long userId = 1L;
        Long requestId = 1L;
        Integer from = 0;
        Integer size = 1;
        LocalDateTime dateTime = LocalDateTime.of(2023, 5, 21, 18, 41, 1);

        ItemRequestGetAnswerDTO getAnswerDTO = new ItemRequestGetAnswerDTO();
        getAnswerDTO.setId(requestId);
        getAnswerDTO.setDescription("Something");
        getAnswerDTO.setCreated(dateTime);

        when(itemRequestService.getAllRequests(userId, from, size)).thenReturn(List.of(getAnswerDTO));

        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", userId)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(getAnswerDTO.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(getAnswerDTO.getDescription())))
                .andExpect(jsonPath("$[0].items", hasSize(0)));

        verify(itemRequestService).getAllRequests(userId, from, size);
    }

//    @SneakyThrows
//    @Test
//    void should_getAllRequests_ReturnInternalServerError_IfFromBelowZero() {
//        Long userId = 1L;
//        Integer from = -1;
//        Integer size = 1;
//
//        mockMvc.perform(get("/requests/all")
//                        .header("X-Sharer-User-Id", userId)
//                        .param("from", String.valueOf(from))
//                        .param("size", String.valueOf(size)))
//                .andExpect(status().isInternalServerError());
//
//        verify(itemRequestService, never()).getAllRequests(userId, from, size);
//    }

//    @SneakyThrows
//    @Test
//    void should_getAllRequests_ReturnInternalServerError_IfSizeBelowZero() {
//        Long userId = 1L;
//        Integer from = 0;
//        Integer size = -1;
//
//        mockMvc.perform(get("/requests/all")
//                        .header("X-Sharer-User-Id", userId)
//                        .param("from", String.valueOf(from))
//                        .param("size", String.valueOf(size)))
//                .andExpect(status().isInternalServerError());
//
//        verify(itemRequestService, never()).getAllRequests(userId, from, size);
//    }

//    @SneakyThrows
//    @Test
//    void should_getAllRequests_ReturnInternalServerError_IfSizeMore100() {
//        Long userId = 1L;
//        Integer from = 0;
//        Integer size = 101;
//
//        mockMvc.perform(get("/requests/all")
//                        .header("X-Sharer-User-Id", userId)
//                        .param("from", String.valueOf(from))
//                        .param("size", String.valueOf(size)))
//                .andExpect(status().isInternalServerError());
//
//        verify(itemRequestService, never()).getAllRequests(userId, from, size);
//    }

    @SneakyThrows
    @Test
    void should_GetById_ReturnRequest() {
        Long userId = 1L;
        Long requestId = 1L;

        LocalDateTime dateTime = LocalDateTime.of(2023, 5, 21, 18, 41, 1);

        ItemRequestGetAnswerDTO getAnswerDTO = new ItemRequestGetAnswerDTO();
        getAnswerDTO.setId(requestId);
        getAnswerDTO.setDescription("Something");
        getAnswerDTO.setCreated(dateTime);

        when(itemRequestService.getById(userId, requestId)).thenReturn(getAnswerDTO);

        mockMvc.perform(get("/requests/{requestId}", requestId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(getAnswerDTO.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(getAnswerDTO.getDescription())))
                .andExpect(jsonPath("$.items", hasSize(0)));

        verify(itemRequestService).getById(userId, requestId);
    }

    @SneakyThrows
    @Test
    void should_GetById_ReturnNotFoundException_IfItemRequestNotFound() {
        Long userId = 1L;
        Long requestId = 1L;

        when(itemRequestService.getById(userId, requestId)).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/requests/{requestId}", requestId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isNotFound());

        verify(itemRequestService).getById(userId, requestId);
    }


}
