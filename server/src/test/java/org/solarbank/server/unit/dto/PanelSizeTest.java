package org.solarbank.server.unit.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.solarbank.server.dto.PanelSize;
import org.solarbank.server.validation.ValidationMessage;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PanelSizeTest {
    private Validator validator;
    private PanelSize panelSize;

    @BeforeEach
    public void setUp() {
        panelSize = new PanelSize();

        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    public void testValidPanelSize() {
        panelSize.setHeight(2.0);
        panelSize.setWidth(3.0);

        Set<ConstraintViolation<PanelSize>> violations = validator.validate(panelSize);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testNullHeight() {
        panelSize.setHeight(null);
        panelSize.setWidth(3.0);

        Set<ConstraintViolation<PanelSize>> violations = validator.validate(panelSize);
        assertEquals(1, violations.size());
        assertEquals(ValidationMessage.PANEL_HEIGHT_NULL, violations.iterator().next().getMessage());
    }

    @Test
    public void testPositiveViolationHeight() {
        panelSize.setHeight(-1.0);
        panelSize.setWidth(3.0);

        Set<ConstraintViolation<PanelSize>> violations = validator.validate(panelSize);
        assertEquals(1, violations.size());
        assertEquals(ValidationMessage.PANEL_HEIGHT_POSITIVE, violations.iterator().next().getMessage());
    }

    @Test
    public void testNullWidth() {
        panelSize.setHeight(2.0);
        panelSize.setWidth(null);

        Set<ConstraintViolation<PanelSize>> violations = validator.validate(panelSize);
        assertEquals(1, violations.size());
        assertEquals(ValidationMessage.PANEL_WIDTH_NULL, violations.iterator().next().getMessage());
    }

    @Test
    public void testPositiveViolationWidth() {
        panelSize.setHeight(2.0);
        panelSize.setWidth(-3.0);

        Set<ConstraintViolation<PanelSize>> violations = validator.validate(panelSize);
        assertEquals(1, violations.size());
        assertEquals(ValidationMessage.PANEL_WIDTH_POSITIVE, violations.iterator().next().getMessage());
    }
}