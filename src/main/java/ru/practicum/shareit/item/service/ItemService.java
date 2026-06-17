package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import java.util.Collection;

public interface ItemService {
    Collection<ItemDto> getAllItems(long ownerId);

    ItemDto getItemById(long itemId);

    Collection<ItemDto> searchItems(String text);

    ItemDto addItem(long ownerId, ItemDto item);

    ItemDto updateItem(long userId, long itemId, ItemUpdateDto itemUpdate);
}
