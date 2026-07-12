package ru.practicum.shareit.request.dto;

import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.Collection;

@Data
public class ItemRequestDtoOut {
    private Long id;
    private String description;
    private LocalDateTime created;
    private UserDto requestor;
    private Collection<ItemDtoOut> items;
}
