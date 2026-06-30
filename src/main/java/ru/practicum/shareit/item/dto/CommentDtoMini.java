package ru.practicum.shareit.item.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDtoMini {
    private Long id;
    private String text;
    private String author;
    private LocalDateTime creatingDate;
}
