package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;

import java.util.Collection;

public interface ItemRequestService {
    ItemRequestDtoOut createRequest(ItemRequestDto request, long requestorId);

    Collection<ItemRequestDtoOut> getUserRequests(long requestorId);

    Collection<ItemRequestDtoOut> getAllRequests(long userId);

    ItemRequestDtoOut getRequest(long requestId);
}
