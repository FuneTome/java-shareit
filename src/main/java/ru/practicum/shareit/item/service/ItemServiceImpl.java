package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.Collection;
import java.util.Collections;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public Collection<ItemDto> getAllItems(long ownerId) {
        Collection<Item> items = itemRepository.getAllItems(ownerId);
        return items.stream()
                .map(ItemMapper::toDto)
                .toList();
    }

    @Override
    public ItemDto getItemById(long itemId) {
        if (!itemRepository.checkItemExist(itemId)) {
            throw new NotFoundException("Предмет с id = " + itemId + " не найден");
        }
        Item item = itemRepository.getItemById(itemId);
        return ItemMapper.toDto(item);
    }

    @Override
    public Collection<ItemDto> searchItems(String text) {
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        Collection<Item> items = itemRepository.search(text);
        return items.stream()
                .map(ItemMapper::toDto)
                .toList();
    }

    @Override
    public ItemDto addItem(long ownerId, ItemDto newItem) {
        if (!userRepository.existById(ownerId)) {
            throw new NotFoundException("Юзер с id = " + ownerId + " не найден");
        }
        Item item = ItemMapper.toItem(newItem);
        return ItemMapper.toDto(itemRepository.addItem(ownerId, item));
    }

    @Override
    public ItemDto updateItem(long userId, long itemId, ItemUpdateDto itemUpdate) {
        if (!itemRepository.checkItemExist(itemId)) {
            throw new NotFoundException("Предмет с id = " + itemId + " не найден");
        }
        if (!itemRepository.checkIsOwner(userId, itemId)) {
            throw new ForbiddenException("Юзер с id = " + userId + " не является владельцем данной вещи");
        }
        Item updateItem = ItemMapper.toItem(itemUpdate);
        return ItemMapper.toDto(itemRepository.updateItem(itemId, updateItem));
    }
}
