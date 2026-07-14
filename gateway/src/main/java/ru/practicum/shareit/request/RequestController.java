package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

@RestController
@RequestMapping(path = "/requests")
@AllArgsConstructor
public class RequestController {
    private final RequestClient requestClient;
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createRequest(@Valid @RequestBody ItemRequestDto request,
                                                @RequestHeader(USER_ID_HEADER) long requestorId) {
        return requestClient.createRequest(request, requestorId);
    }

    @GetMapping
    public ResponseEntity<Object> getUserRequests(@RequestHeader(USER_ID_HEADER) long requestorId) {
        return requestClient.getUserRequests(requestorId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequests(@RequestHeader(USER_ID_HEADER) long userId) {
        return requestClient.getAllRequests(userId);
    }

    @GetMapping("{requestId}")
    public ResponseEntity<Object> getRequest(@PathVariable long requestId) {
        return requestClient.getRequest(requestId);
    }
}
