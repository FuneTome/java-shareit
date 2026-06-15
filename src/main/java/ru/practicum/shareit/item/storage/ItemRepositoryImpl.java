package ru.practicum.shareit.item.storage;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Repository
public class ItemRepositoryImpl implements ItemRepository {
    private final List<Item> items = new ArrayList<>();
    private Long id = 1L;

    @Override
    public Collection<Item> getAllItems(long ownerId) {
        return items.stream()
                .filter(item -> item.getOwner() == ownerId)
                .toList();
    }

    @Override
    public Item getItemById(long itemId) {
        return items.stream()
                .filter(item -> item.getId() == itemId)
                .findFirst()
                .orElse(null);
    }

    @Override
    public Collection<Item> search(String text) {
        return items.stream()
                .filter(item -> (item.getName().toLowerCase().contains(text.toLowerCase())
                        || item.getDescription().toLowerCase().contains(text.toLowerCase()))
                        && item.getAvailable())
                .toList();
    }

    @Override
    public Item addItem(long ownerId, Item newItem) {
        newItem.setId(id++);
        newItem.setOwner(ownerId);
        items.add(newItem);
        return newItem;
    }

    @Override
    public Item updateItem(long itemId, Item itemUpdate) {
        Item oldItem = getItemById(itemId);
        if (itemUpdate.getName() != null) {
            oldItem.setName(itemUpdate.getName());
        }
        if (itemUpdate.getDescription() != null) {
            oldItem.setDescription(itemUpdate.getDescription());
        }
        if (itemUpdate.getAvailable() != null) {
            oldItem.setAvailable(itemUpdate.getAvailable());
        }
        return oldItem;
    }

    public boolean checkItemExist(long itemId) {
        return items.stream().anyMatch(item -> item.getId() == itemId);
    }
}
