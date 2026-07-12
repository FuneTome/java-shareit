package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;

@Service
public class RequestClient extends BaseClient {
    private static final String API_PREFIX = "/requests";

    @Autowired
    public RequestClient(RestTemplateBuilder builder, @Value("http://localhost:9090") String serverUrl) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> createRequest(Object reqDto, Long requestorId) {
        return post(API_PREFIX, requestorId, reqDto);
    }

    public ResponseEntity<Object> getUserRequests(Long requestorId) {
        return get(API_PREFIX, requestorId);
    }

    public ResponseEntity<Object> getAllRequests() {
        return get(API_PREFIX + "/all");
    }

    public ResponseEntity<Object> getRequest(Long requestId) {
        return get(API_PREFIX + "/" + requestId);
    }
}
