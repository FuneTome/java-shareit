package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

public class CommentMapper {
    public static Comment toComment(CommentDto dto, User user, Item item) {
        Comment comment = new Comment();
        comment.setText(dto.getText());
        comment.setAuthor(user);
        comment.setItem(item);
        comment.setCreatingDate(LocalDateTime.now());
        return comment;
    }

    public static CommentDtoOut toOut(Comment comment) {
        CommentDtoOut out = new CommentDtoOut();
        out.setId(comment.getId());
        out.setText(comment.getText());
        out.setCreated(comment.getCreatingDate());
        out.setItem(ItemMapper.toOut(comment.getItem()));
        out.setAuthorName(comment.getAuthor().getName());
        return out;
    }
}
