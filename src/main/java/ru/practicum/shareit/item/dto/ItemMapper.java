package ru.practicum.shareit.item.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.dto.BookingDtoOutMini;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
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

    public static ItemDtoOut toOut(Item item) {
        ItemDtoOut out = new ItemDtoOut();
        out.setId(item.getId());
        out.setName(item.getName());
        out.setDescription(item.getDescription());
        out.setAvailable(item.getAvailable());
        return out;
    }

    public static ItemDtoOut toOut(Item item, BookingDtoOut lastBooking,
                                   BookingDtoOut nextBooking,
                                   Collection<CommentDtoOut> comments) {
        ItemDtoOut out = toOut(item);
        out.setComments(comments != null ? new ArrayList<>(comments) : new ArrayList<>());
        out.setLastBooking(lastBooking);
        out.setNextBooking(nextBooking);
        return out;
    }
}
