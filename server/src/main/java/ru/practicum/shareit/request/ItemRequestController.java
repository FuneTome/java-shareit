package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.Collection;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequestMapping(path = "/requests")
@AllArgsConstructor
public class ItemRequestController {
    private final ItemRequestService requestService;
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemRequestDtoOut createRequest(@RequestBody ItemRequestDto request,
                                           @RequestHeader(USER_ID_HEADER) long requestorId) {
        return requestService.createRequest(request, requestorId);
    }

    @GetMapping
    public Collection<ItemRequestDtoOut> getUserRequests(@RequestHeader(USER_ID_HEADER) long requestorId) {
        return requestService.getUserRequests(requestorId);
    }

    @GetMapping("/all")
    public Collection<ItemRequestDtoOut> getAllRequests(@RequestHeader(USER_ID_HEADER) long userId) {
        return requestService.getAllRequests(userId);
    }

    @GetMapping("{requestId}")
    public ItemRequestDtoOut getRequest(@PathVariable long requestId) {
        return requestService.getRequest(requestId);
    }
}
