package ru.practicum.shareit.booking;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContentAssert;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingJsonTest {
    private final JacksonTester<BookingDto> jsonTester;
    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void serializationTest() throws Exception {
        BookingDto dto = new BookingDto();
        dto.setItemId(1L);
        dto.setStartDate(LocalDateTime.now().plusDays(1));
        dto.setEndDate(LocalDateTime.now().plusDays(2));

        JsonContentAssert content = jsonTester.write(dto).assertThat();
        content.extractingJsonPathNumberValue("$.itemId").isEqualTo(1);
        content.extractingJsonPathStringValue("$.start").matches("\\d{4}-\\d{2}-\\d{2}T.*");
        content.extractingJsonPathStringValue("$.end").matches("\\d{4}-\\d{2}-\\d{2}T.*");
    }

    @Test
    void deserializationTest() throws Exception {
        String json = "{\"itemId\":1,\"start\":\"2026-07-15T10:00:00\",\"end\":\"2026-07-16T10:00:00\"}";
        BookingDto dto = jsonTester.parseObject(json);
        assertThat(dto.getItemId()).isEqualTo(1L);
        assertThat(dto.getStartDate()).isNotNull();
        assertThat(dto.getEndDate()).isNotNull();
    }

    @Test
    void validDto_shouldHaveNoViolations() {
        BookingDto dto = new BookingDto();
        dto.setItemId(1L);
        dto.setStartDate(LocalDateTime.now().plusDays(1));
        dto.setEndDate(LocalDateTime.now().plusDays(2));

        Set<ConstraintViolation<BookingDto>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void itemIdIsNull_shouldHaveViolation() {
        BookingDto dto = new BookingDto();
        dto.setItemId(null);
        dto.setStartDate(LocalDateTime.now().plusDays(1));
        dto.setEndDate(LocalDateTime.now().plusDays(2));

        Set<ConstraintViolation<BookingDto>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertEquals("itemId", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    void startIsNull_shouldHaveViolation() {
        BookingDto dto = new BookingDto();
        dto.setItemId(1L);
        dto.setStartDate(null);
        dto.setEndDate(LocalDateTime.now().plusDays(2));

        Set<ConstraintViolation<BookingDto>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertEquals("startDate", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    void endIsNull_shouldHaveViolation() {
        BookingDto dto = new BookingDto();
        dto.setItemId(1L);
        dto.setStartDate(LocalDateTime.now().plusDays(1));
        dto.setEndDate(null);

        Set<ConstraintViolation<BookingDto>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertEquals("endDate", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    void startInPast_shouldHaveViolation() {
        BookingDto dto = new BookingDto();
        dto.setItemId(1L);
        dto.setStartDate(LocalDateTime.now().minusDays(1));
        dto.setEndDate(LocalDateTime.now().plusDays(1));

        Set<ConstraintViolation<BookingDto>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertEquals("startDate", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    void endInPast_shouldHaveViolation() {
        BookingDto dto = new BookingDto();
        dto.setItemId(1L);
        dto.setStartDate(LocalDateTime.now().plusDays(1));
        dto.setEndDate(LocalDateTime.now().minusDays(1));

        Set<ConstraintViolation<BookingDto>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertEquals("endDate", violations.iterator().next().getPropertyPath().toString());
    }
}