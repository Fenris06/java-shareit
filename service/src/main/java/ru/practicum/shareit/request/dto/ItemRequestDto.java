package ru.practicum.shareit.request.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@EqualsAndHashCode
@ToString
public class ItemRequestDto {
    @NotNull(message = "Description can't be null")
    @NotEmpty(message = "Description can't be empty")
    private String description;
}
