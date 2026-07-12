package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(RestTemplateBuilder builder, @Value("http://localhost:9090") String serverUrl) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> addItem(Long userId, Object itemDto) {
        return post(API_PREFIX, userId, itemDto);
    }

    public ResponseEntity<Object> updateItem(Long userId, Long itemId, Object itemDto) {
        return patch(API_PREFIX + "/" + itemId, userId, itemDto);
    }

    public ResponseEntity<Object> getItem(Long userId, Long itemId) {
        return get(API_PREFIX + "/" + itemId, userId, null);
    }

    public ResponseEntity<Object> getItems(Long userId) {
        return get(API_PREFIX, userId, null);
    }

    public ResponseEntity<Object> searchItems(String text) {
        return get(API_PREFIX + "/search?text=" + text, null, null);
    }
}
