package ru.practicum.shareit.item.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ItemDto {
    private Long id;
    @NotNull(message = "Item name not add")
    @NotEmpty(message = "Item name not add")
    private String name;
    @NotNull(message = "Item description not add")
    @NotEmpty(message = "Item description not add")
    private String description;
    @NotNull(message = "Item available not add")
    private Boolean available;
    private Long requestId;
}