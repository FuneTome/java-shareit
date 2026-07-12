package ru.practicum.shareit.request.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;
import ru.practicum.shareit.request.dto.RequestMapper;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.Collection;

@Service
@AllArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;

    @Override
    public ItemRequestDtoOut createRequest(ItemRequestDto newRequest, long requestorId) {
        User requestor = checkUserExistAndReturn(requestorId);
        Request request = RequestMapper.toRequest(newRequest, requestor);
        return RequestMapper.toOut(requestRepository.save(request));
    }

    @Override
    public Collection<ItemRequestDtoOut> getUserRequests(long requestorId) {
        checkUserExistAndReturn(requestorId);
        Collection<Request> req = requestRepository.findByRequestor_Id(requestorId);
        return req.stream()
                .map(RequestMapper::toOut)
                .toList();
    }

    @Override
    public Collection<ItemRequestDtoOut> getAllRequests() {
        Collection<Request> req = requestRepository.findAll();
        return req.stream()
                .map(RequestMapper::toOut)
                .toList();
    }

    @Override
    public ItemRequestDtoOut getRequest(long requestId) {
        return RequestMapper.toOut(checkRequestExistAndReturn(requestId));
    }

    private User checkUserExistAndReturn(long userId) {
        return userRepository.findById(userId).orElseThrow(()
                -> new NotFoundException("Юзер с id = " + userId + " не найден"));
    }

    private Request checkRequestExistAndReturn(long requestId) {
        return requestRepository.findById(requestId).orElseThrow(()
                -> new NotFoundException("Запрос с id = " + requestId + " не найден"));
    }
}
