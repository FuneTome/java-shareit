package ru.practicum.shareit.item.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.dto.BookingDtoOutMini;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

@Component
public class ItemMapper {
    public static Item toItem(ItemDto dto) {
        Item item = new Item();
        item.setId(dto.getId());
        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        item.setAvailable(dto.getAvailable());
        item.setOwner(dto.getOwner());
        return item;
    }

    public static ItemDto toDto(Item item) {
        ItemDto dto = new ItemDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setAvailable(item.getAvailable());
        dto.setOwner(item.getOwner());
        return dto;
    }

    public static ItemDtoOut toOut(Item item) {
        ItemDtoOut out = new ItemDtoOut();
        out.setId(item.getId());
        out.setName(item.getName());
        out.setDescription(item.getDescription());
        out.setAvailable(item.getAvailable());
        return out;
    }

    public static ItemDtoOut toOut(Item item, BookingDtoOutMini lastBooking,
                                   BookingDtoOutMini nextBooking,
                                   Collection<CommentDtoMini> comments) {
        ItemDtoOut out = new ItemDtoOut();
        out.setId(item.getId());
        out.setName(item.getName());
        out.setDescription(item.getDescription());
        out.setAvailable(item.getAvailable());
        out.setComments(comments);
        out.setLastBooking(lastBooking);
        out.setNextBooking(nextBooking);
        return out;
    }
}
