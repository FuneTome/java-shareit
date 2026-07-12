package ru.practicum.shareit.request.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;

@Component
public class RequestMapper {
    public static ItemRequestDtoOut toOut(Request request) {
        ItemRequestDtoOut out = new ItemRequestDtoOut();
        out.setId(request.getId());
        out.setDescription(request.getDescription());
        out.setCreated(request.getCreated());
        out.setRequestor(UserMapper.toDto(request.getRequestor()));
        Collection<Item> i = request.getItems();
        out.setItems(i.stream()
                .map(ItemMapper::toOut)
                .toList());
        return out;
    }

    public static Request toRequest(ItemRequestDto dto, User requestor) {
        Request request = new Request();
        request.setDescription(dto.getDescription());
        request.setRequestor(requestor);
        return request;
    }
}
