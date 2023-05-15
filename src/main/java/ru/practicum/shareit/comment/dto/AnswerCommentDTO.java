package ru.practicum.shareit.comment.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
@Setter
@Getter
@EqualsAndHashCode
@ToString
public class AnswerCommentDTO {
    private Long id;
    private String text;
    private String authorName;
    private LocalDateTime created;
}
