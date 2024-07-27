package org.solarbank.server.unit.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.solarbank.server.dto.Location;
import org.solarbank.server.ValidationMessage;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LocationTest {
    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidLocation() {
        Location location = new Location();
        location.setLongitude(100.0);
        location.setLatitude(50.0);

        Set<ConstraintViolation<Location>> violations = validator.validate(location);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testNullLongitude() {
        Location location = new Location();
        location.setLongitude(null);
        location.setLatitude(50.0);

        Set<ConstraintViolation<Location>> violations = validator.validate(location);
        assertEquals(1, violations.size());
        assertEquals(ValidationMessage.LONGITUDE_NULL.getMessage(), violations.iterator().next().getMessage());
    }

    @Test
    public void testMaxViolationLongitude() {
        Location location = new Location();
        location.setLongitude(190.0);
        location.setLatitude(50.0);

        Set<ConstraintViolation<Location>> violations = validator.validate(location);
        assertEquals(1, violations.size());
        assertEquals(ValidationMessage.LONGITUDE_MAX.getMessage(), violations.iterator().next().getMessage());
    }

    @Test
    public void testMinViolationLongitude() {
        Location location = new Location();
        location.setLongitude(-190.0);
        location.setLatitude(50.0);

        Set<ConstraintViolation<Location>> violations = validator.validate(location);
        assertEquals(1, violations.size());
        assertEquals(ValidationMessage.LONGITUDE_MIN.getMessage(), violations.iterator().next().getMessage());
    }

    @Test
    public void testNullLatitude() {
        Location location = new Location();
        location.setLongitude(100.0);
        location.setLatitude(null);

        Set<ConstraintViolation<Location>> violations = validator.validate(location);
        assertEquals(1, violations.size());
        assertEquals(ValidationMessage.LATITUDE_NULL.getMessage(), violations.iterator().next().getMessage());
    }

    @Test
    public void testMaxViolationLatitude() {
        Location location = new Location();
        location.setLongitude(100.0);
        location.setLatitude(92.0);

        Set<ConstraintViolation<Location>> violations = validator.validate(location);
        assertEquals(1, violations.size());
        assertEquals(ValidationMessage.LATITUDE_MAX.getMessage(), violations.iterator().next().getMessage());
    }

    @Test
    public void testMinViolationLatitude() {
        Location location = new Location();
        location.setLongitude(100.0);
        location.setLatitude(-92.0);

        Set<ConstraintViolation<Location>> violations = validator.validate(location);
        assertEquals(1, violations.size());
        assertEquals(ValidationMessage.LATITUDE_MIN.getMessage(), violations.iterator().next().getMessage());
    }
}