package org.solarbank.server.unit.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.solarbank.server.dto.CalculateRequest;
import org.solarbank.server.dto.EnergyTariff;
import org.solarbank.server.dto.Location;
import org.solarbank.server.dto.PanelSize;
import org.solarbank.server.ValidationMessage;

import java.util.Set;

import static org.solarbank.server.CreateMockCalculateRequest.createCalculateRequest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CalculateRequestTest {
    private Validator validator;

    private CalculateRequest calculateRequest = createCalculateRequest();

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidCalculateRequest() {
        Set<ConstraintViolation<CalculateRequest>> violations = validator.validate(calculateRequest);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testNullPanelEfficiency() {
        calculateRequest.setPanelEfficiency(null);

        Set<ConstraintViolation<CalculateRequest>> violations = validator.validate(calculateRequest);
        assertEquals(1, violations.size());
        assertEquals(ValidationMessage.PANEL_EFF_NULL.getMessage(), violations.iterator().next().getMessage());
    }

    @Test
    public void testPositiveViolationPanelEfficiency() {
        calculateRequest.setPanelEfficiency(-0.1);

        Set<ConstraintViolation<CalculateRequest>> violations = validator.validate(calculateRequest);
        assertEquals(1, violations.size());
        assertEquals(ValidationMessage.PANEL_EFF_POSITIVE.getMessage(), violations.iterator().next().getMessage());
    }

    @Test
    public void testMaxViolationPanelEfficiency() {
        calculateRequest.setPanelEfficiency(1.01);

        Set<ConstraintViolation<CalculateRequest>> violations = validator.validate(calculateRequest);
        assertEquals(1, violations.size());
        assertEquals(ValidationMessage.PANEL_EFF_MAX.getMessage(), violations.iterator().next().getMessage());
    }

    @Test
    public void testNullLocation() {
        calculateRequest.setLocation(null);

        Set<ConstraintViolation<CalculateRequest>> violations = validator.validate(calculateRequest);
        assertEquals(1, violations.size());
        assertEquals(ValidationMessage.LOCATION_NULL.getMessage(), violations.iterator().next().getMessage());
    }

    @Test
    public void testInvalidLocation() {
        calculateRequest.getLocation().setLongitude(null);

        Set<ConstraintViolation<CalculateRequest>> violations = validator.validate(calculateRequest);
        assertEquals(1, violations.size());
    }

    @Test
    public void testNullPanelSize() {
        calculateRequest.setPanelSize(null);

        Set<ConstraintViolation<CalculateRequest>> violations = validator.validate(calculateRequest);
        assertEquals(1, violations.size());
        assertEquals(ValidationMessage.PANEL_SIZE_NULL.getMessage(), violations.iterator().next().getMessage());
    }

    @Test
    public void testInvalidPanelSize() {
        calculateRequest.getPanelSize().setWidth(null);

        Set<ConstraintViolation<CalculateRequest>> violations = validator.validate(calculateRequest);
        assertEquals(1, violations.size());
    }

    @Test
    public void testNullEnergyTariff() {
        calculateRequest.setEnergyTariff(null);

        Set<ConstraintViolation<CalculateRequest>> violations = validator.validate(calculateRequest);
        assertEquals(1, violations.size());
        assertEquals(ValidationMessage.ENERGY_TARIFF_NULL.getMessage(), violations.iterator().next().getMessage());
    }

    @Test
    public void testInvalidEnergyTariff() {
        calculateRequest.getEnergyTariff().setCurrencyCode("AAA");

        Set<ConstraintViolation<CalculateRequest>> violations = validator.validate(calculateRequest);
        assertEquals(1, violations.size());
    }
}