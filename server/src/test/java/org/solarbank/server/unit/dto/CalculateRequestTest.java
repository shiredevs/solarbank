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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CalculateRequestTest {
    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidCalculateRequest() {
        Location location = new Location();
        location.setLongitude(100.0);
        location.setLatitude(50.0);

        PanelSize panelSize = new PanelSize();
        panelSize.setWidth(1.0);
        panelSize.setHeight(2.0);

        EnergyTariff energyTariff = new EnergyTariff();
        energyTariff.setCurrencyCode("USD");
        energyTariff.setAmount(50.0);

        CalculateRequest calculateRequest = new CalculateRequest();
        calculateRequest.setLocation(location);
        calculateRequest.setPanelSize(panelSize);
        calculateRequest.setEnergyTariff(energyTariff);
        calculateRequest.setPanelEfficiency(0.01);

        Set<ConstraintViolation<CalculateRequest>> violations = validator.validate(calculateRequest);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testNullPanelEfficiency() {
        CalculateRequest calculateRequest = new CalculateRequest();
        calculateRequest.setPanelEfficiency(null);

        Set<ConstraintViolation<CalculateRequest>> violations = validator.validate(calculateRequest);
        assertEquals(1, violations.size());
        assertEquals(ValidationMessage.PANEL_EFF_NULL.getMessage(), violations.iterator().next().getMessage());
    }

    @Test
    public void testPositiveViolationPanelEfficiency() {
        CalculateRequest calculateRequest = new CalculateRequest();
        calculateRequest.setPanelEfficiency(-0.1);

        Set<ConstraintViolation<CalculateRequest>> violations = validator.validate(calculateRequest);
        assertEquals(1, violations.size());
        assertEquals(ValidationMessage.PANEL_EFF_POSITIVE.getMessage(), violations.iterator().next().getMessage());
    }

    @Test
    public void testMaxViolationPanelEfficiency() {
        CalculateRequest calculateRequest = new CalculateRequest();
        calculateRequest.setPanelEfficiency(1.01);

        Set<ConstraintViolation<CalculateRequest>> violations = validator.validate(calculateRequest);
        assertEquals(1, violations.size());
        assertEquals(ValidationMessage.PANEL_EFF_MAX.getMessage(), violations.iterator().next().getMessage());
    }

    @Test
    public void testInvalidLocation() {
        Location location = new Location();
        location.setLongitude(null);
        location.setLatitude(50.0);

        PanelSize panelSize = new PanelSize();
        panelSize.setWidth(1.0);
        panelSize.setHeight(2.0);

        EnergyTariff energyTariff = new EnergyTariff();
        energyTariff.setCurrencyCode("USD");
        energyTariff.setAmount(50.0);

        CalculateRequest calculateRequest = new CalculateRequest();
        calculateRequest.setLocation(location);
        calculateRequest.setPanelSize(panelSize);
        calculateRequest.setEnergyTariff(energyTariff);
        calculateRequest.setPanelEfficiency(0.01);

        Set<ConstraintViolation<CalculateRequest>> violations = validator.validate(calculateRequest);
        assertEquals(1, violations.size());
    }

    @Test
    public void testInvalidPanelSize() {
        Location location = new Location();
        location.setLongitude(100.0);
        location.setLatitude(50.0);

        PanelSize panelSize = new PanelSize();
        panelSize.setWidth(null);
        panelSize.setHeight(2.0);

        EnergyTariff energyTariff = new EnergyTariff();
        energyTariff.setCurrencyCode("USD");
        energyTariff.setAmount(50.0);

        CalculateRequest calculateRequest = new CalculateRequest();
        calculateRequest.setLocation(location);
        calculateRequest.setPanelSize(panelSize);
        calculateRequest.setEnergyTariff(energyTariff);
        calculateRequest.setPanelEfficiency(0.01);

        Set<ConstraintViolation<CalculateRequest>> violations = validator.validate(calculateRequest);
        assertEquals(1, violations.size());
    }

    @Test
    public void testInvalidEnergyTariff() {
        Location location = new Location();
        location.setLongitude(100.0);
        location.setLatitude(50.0);

        PanelSize panelSize = new PanelSize();
        panelSize.setWidth(1.0);
        panelSize.setHeight(2.0);

        EnergyTariff energyTariff = new EnergyTariff();
        energyTariff.setCurrencyCode("AAA");
        energyTariff.setAmount(50.0);

        CalculateRequest calculateRequest = new CalculateRequest();
        calculateRequest.setLocation(location);
        calculateRequest.setPanelSize(panelSize);
        calculateRequest.setEnergyTariff(energyTariff);
        calculateRequest.setPanelEfficiency(0.01);

        Set<ConstraintViolation<CalculateRequest>> violations = validator.validate(calculateRequest);
        assertEquals(1, violations.size());
    }
}