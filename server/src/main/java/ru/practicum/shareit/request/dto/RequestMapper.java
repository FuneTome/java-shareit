package ru.practicum.shareit.request.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class RequestMapper {
    public static ItemRequestDtoOut toOut(Request request) {
        ItemRequestDtoOut out = new ItemRequestDtoOut();
        out.setId(request.getId());
        out.setDescription(request.getDescription());
        out.setCreated(request.getCreated());
        out.setRequestor(UserMapper.toDto(request.getRequestor()));
        List<ItemDtoOut> itemDto = Optional.ofNullable(request.getItems())
                .orElse(Collections.emptyList())
                .stream()
                .map(ItemMapper::toOut)
                .collect(Collectors.toList());
        out.setItems(itemDto);
        return out;
    }

    public static Request toRequest(ItemRequestDto dto, User requestor) {
        Request request = new Request();
        request.setDescription(dto.getDescription());
        request.setRequestor(requestor);
        return request;
    }
}
