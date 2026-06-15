package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collection;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private final String USER_ID_HEADER = "X-Sharer-User-Id";

    @GetMapping
    public Collection<ItemDto> getItems(@RequestHeader(USER_ID_HEADER) long ownerId) {
        return itemService.getAllItems(ownerId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItem(@PathVariable long itemId) {
        return itemService.getItemById(itemId);
    }

    @GetMapping("/search")
    public Collection<ItemDto> searchItems(@RequestParam("text") String text) {
        return itemService.searchItems(text);
    }

    @PostMapping
    public ItemDto addItem(@RequestHeader(USER_ID_HEADER) long ownerId, @Valid @RequestBody ItemDto item) {
        return itemService.addItem(ownerId, item);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader(USER_ID_HEADER) long userId, @PathVariable long itemId,
                              @RequestBody ItemUpdateDto itemUpdate) {
        return itemService.updateItem(userId, itemId, itemUpdate);
    }
}
