package org.solarbank.server.unit.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.solarbank.server.dto.Location;
import org.solarbank.server.validation.ValidationMessage;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LocationTest {
    private Validator validator;
    private Location location;

    @BeforeEach
    public void setUp() {
        location = new Location();

        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    public void testValidLocation() {
        location.setLongitude(100.0);
        location.setLatitude(50.0);

        Set<ConstraintViolation<Location>> violations = validator.validate(location);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testNullLongitude() {
        location.setLongitude(null);
        location.setLatitude(50.0);

        Set<ConstraintViolation<Location>> violations = validator.validate(location);
        assertEquals(1, violations.size());
        assertEquals(ValidationMessage.LONGITUDE_NULL, violations.iterator().next().getMessage());
    }

    @Test
    public void testMaxViolationLongitude() {
        location.setLongitude(190.0);
        location.setLatitude(50.0);

        Set<ConstraintViolation<Location>> violations = validator.validate(location);
        assertEquals(1, violations.size());
        assertEquals(ValidationMessage.LONGITUDE_MAX, violations.iterator().next().getMessage());
    }

    @Test
    public void testMinViolationLongitude() {
        location.setLongitude(-190.0);
        location.setLatitude(50.0);

        Set<ConstraintViolation<Location>> violations = validator.validate(location);
        assertEquals(1, violations.size());
        assertEquals(ValidationMessage.LONGITUDE_MIN, violations.iterator().next().getMessage());
    }

    @Test
    public void testNullLatitude() {
        location.setLongitude(100.0);
        location.setLatitude(null);

        Set<ConstraintViolation<Location>> violations = validator.validate(location);
        assertEquals(1, violations.size());
        assertEquals(ValidationMessage.LATITUDE_NULL, violations.iterator().next().getMessage());
    }

    @Test
    public void testMaxViolationLatitude() {
        location.setLongitude(100.0);
        location.setLatitude(92.0);

        Set<ConstraintViolation<Location>> violations = validator.validate(location);
        assertEquals(1, violations.size());
        assertEquals(ValidationMessage.LATITUDE_MAX, violations.iterator().next().getMessage());
    }

    @Test
    public void testMinViolationLatitude() {
        location.setLongitude(100.0);
        location.setLatitude(-92.0);

        Set<ConstraintViolation<Location>> violations = validator.validate(location);
        assertEquals(1, violations.size());
        assertEquals(ValidationMessage.LATITUDE_MIN, violations.iterator().next().getMessage());
    }
}