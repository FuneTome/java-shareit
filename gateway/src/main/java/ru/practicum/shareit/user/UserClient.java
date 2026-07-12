package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;

@Service
public class UserClient extends BaseClient {
    private static final String API_PREFIX = "/users";

    @Autowired
    public UserClient(RestTemplateBuilder builder, @Value("http://localhost:9090") String serverUrl) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> getUser(Long id) {
        return get(API_PREFIX + "/" + id);
    }

    public ResponseEntity<Object> addUser(Object userDto) {
        return post(API_PREFIX, userDto);
    }

    public ResponseEntity<Object> updateUser(Object update, Long id) {
        return patch(API_PREFIX+ "/" + id, update);
    }

    public ResponseEntity<Object> deleteUser(Long id) {
        return delete(API_PREFIX+ "/" + id);
    }
}
