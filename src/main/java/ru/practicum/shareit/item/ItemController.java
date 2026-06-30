package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentDtoOut;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoOut;
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
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @GetMapping
    public Collection<ItemDtoOut> getItems(@RequestHeader(USER_ID_HEADER) long ownerId) {
        return itemService.getAllItems(ownerId);
    }

    @GetMapping("/{itemId}")
    public ItemDtoOut getItem(@PathVariable long itemId) {
        return itemService.getItemById(itemId);
    }

    @GetMapping("/search")
    public Collection<ItemDtoOut> searchItems(@RequestParam("text") String text) {
        return itemService.searchItems(text);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDtoOut addItem(@RequestHeader(USER_ID_HEADER) long ownerId, @Valid @RequestBody ItemDto item) {
        return itemService.addItem(ownerId, item);
    }

    @PatchMapping("/{itemId}")
    public ItemDtoOut updateItem(@RequestHeader(USER_ID_HEADER) long userId, @PathVariable long itemId,
                              @RequestBody ItemDto itemUpdate) {
        return itemService.updateItem(userId, itemId, itemUpdate);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDtoOut createComment(@RequestHeader(USER_ID_HEADER) long authorId,
                                       @PathVariable long itemId,
                                       @Valid @RequestBody CommentDto comment) {
        return itemService.createComment(authorId, itemId, comment);
    }
}
