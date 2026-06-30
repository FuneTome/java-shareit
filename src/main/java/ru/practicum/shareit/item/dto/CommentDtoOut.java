package ru.practicum.shareit.item.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDtoOut {
    private Long id;
    private String text;
    private ItemDtoOut item;
    private String author;
    private LocalDateTime creatingDate;
}
