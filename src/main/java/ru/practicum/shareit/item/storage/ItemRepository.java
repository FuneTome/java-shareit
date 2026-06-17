package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemRepository {
    Collection<Item> getAllItems(long ownerId);

    Item getItemById(long itemId);

    Collection<Item> search(String text);

    Item addItem(long ownerId, Item newItem);

    Item updateItem(long itemId, Item itemUpdate);

    boolean checkItemExist(long itemId);
}
