package ru.practicum.shareit.item.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.Collections;

@Component
public class ItemMapper {

    public static Item toItem(ItemDto dto) {
        Item item = new Item();
        item.setId(dto.getId());
        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        item.setAvailable(dto.getAvailable());
        return item;
    }

    public static ItemDtoOut toOut(Item item) {
        ItemDtoOut out = new ItemDtoOut();
        out.setId(item.getId());
        out.setName(item.getName());
        out.setDescription(item.getDescription());
        out.setAvailable(item.getAvailable());
        if (item.getRequest() != null) {
            out.setRequestId(item.getRequest().getId());
        } else {
            out.setRequestId(null);
        }
        return out;
    }

    public static ItemDtoOut toOut(Item item, BookingDtoOut lastBooking,
                                   BookingDtoOut nextBooking,
                                   Collection<CommentDtoOut> comments) {
        ItemDtoOut out = toOut(item);
        out.setComments(comments != null ? comments : Collections.emptyList());
        out.setLastBooking(lastBooking);
        out.setNextBooking(nextBooking);
        return out;
    }
}