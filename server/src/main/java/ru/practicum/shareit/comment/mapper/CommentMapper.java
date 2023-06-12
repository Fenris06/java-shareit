package ru.practicum.shareit.comment.mapper;

import ru.practicum.shareit.comment.dto.AnswerCommentDTO;
import ru.practicum.shareit.comment.dto.CommentDTO;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

public class CommentMapper {
    public static Comment fromDTO(CommentDTO commentDTO, Item item, User user, LocalDateTime dateTime) {
        Comment comment = new Comment();
        comment.setText(commentDTO.getText());
        comment.setItem(item);
        comment.setAuthor(user);
        comment.setCreate(dateTime);
        return comment;
    }

    public static AnswerCommentDTO toDTO(Comment comment) {
        AnswerCommentDTO commentDTO = new AnswerCommentDTO();
        commentDTO.setId(comment.getId());
        commentDTO.setText(comment.getText());
        commentDTO.setAuthorName(comment.getAuthor().getName());
        commentDTO.setCreated(comment.getCreate());
        return commentDTO;
    }
}
