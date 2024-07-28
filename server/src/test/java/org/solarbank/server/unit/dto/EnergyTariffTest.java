package org.solarbank.server.unit.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.solarbank.server.dto.EnergyTariff;
import org.solarbank.server.ValidationMessage;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EnergyTariffTest {
    private Validator validator;
    private EnergyTariff energyTariff = new EnergyTariff();

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidEnergyTariff() {
        energyTariff.setCurrencyCode("USD");
        energyTariff.setAmount(50.0);

        Set<ConstraintViolation<EnergyTariff>> violations = validator.validate(energyTariff);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testCurrencyNotFound() {
        energyTariff.setCurrencyCode("AAA");
        energyTariff.setAmount(50.0);

        Set<ConstraintViolation<EnergyTariff>> violations = validator.validate(energyTariff);
        assertEquals(1, violations.size());
        assertEquals(ValidationMessage.CURRENCY_CODE.getMessage(), violations.iterator().next().getMessage());
    }

    @Test
    public void testNullAmount() {
        energyTariff.setCurrencyCode("USD");
        energyTariff.setAmount(null);

        Set<ConstraintViolation<EnergyTariff>> violations = validator.validate(energyTariff);
        assertEquals(1, violations.size());
        assertEquals(ValidationMessage.AMOUNT_NULL.getMessage(), violations.iterator().next().getMessage());
    }

    @Test
    public void testMinViolationAmount() {
        energyTariff.setCurrencyCode("USD");
        energyTariff.setAmount(0.0);

        Set<ConstraintViolation<EnergyTariff>> violations = validator.validate(energyTariff);
        assertEquals(1, violations.size());
        assertEquals(ValidationMessage.AMOUNT_MIN.getMessage(), violations.iterator().next().getMessage());
    }
}