package ru.practicum.shareit.booking;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BookingJsonTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
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