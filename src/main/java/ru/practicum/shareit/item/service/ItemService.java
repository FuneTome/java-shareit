package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.*;

import java.util.Collection;

public interface ItemService {
    Collection<ItemDtoOut> getAllItems(long ownerId);

    ItemDtoOut getItemById(long itemId);

    Collection<ItemDtoOut> searchItems(String text);

    ItemDtoOut addItem(long ownerId, ItemDto item);

    ItemDtoOut updateItem(long userId, long itemId, ItemDto itemUpdate);

    CommentDtoOut createComment(long authorId, long itemId, CommentDto comment);
}
