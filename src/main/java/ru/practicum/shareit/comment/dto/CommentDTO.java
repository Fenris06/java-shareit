package ru.practicum.shareit.comment.dto;

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
public class CommentDTO {
    @NotNull
    @NotEmpty(message = "Text can't be empty")
    String text;
}
