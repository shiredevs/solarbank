package org.solarbank.server.unit.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.solarbank.server.dto.PanelSize;
import org.solarbank.server.ValidationMessage;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PanelSizeTest {
    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidPanelSize() {
        PanelSize panelSize = new PanelSize();
        panelSize.setHeight(2.0);
        panelSize.setWidth(3.0);

        Set<ConstraintViolation<PanelSize>> violations = validator.validate(panelSize);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testNullHeight() {
        PanelSize panelSize = new PanelSize();
        panelSize.setHeight(null);
        panelSize.setWidth(3.0);

        Set<ConstraintViolation<PanelSize>> violations = validator.validate(panelSize);
        assertEquals(1, violations.size());
        assertEquals(ValidationMessage.PANEL_HEIGHT_NULL.getMessage(), violations.iterator().next().getMessage());
    }

    @Test
    public void testPositiveViolationHeight() {
        PanelSize panelSize = new PanelSize();
        panelSize.setHeight(-1.0);
        panelSize.setWidth(3.0);

        Set<ConstraintViolation<PanelSize>> violations = validator.validate(panelSize);
        assertEquals(1, violations.size());
        assertEquals(ValidationMessage.PANEL_HEIGHT_POSITIVE.getMessage(), violations.iterator().next().getMessage());
    }

    @Test
    public void testNullWidth() {
        PanelSize panelSize = new PanelSize();
        panelSize.setHeight(2.0);
        panelSize.setWidth(null);

        Set<ConstraintViolation<PanelSize>> violations = validator.validate(panelSize);
        assertEquals(1, violations.size());
        assertEquals(ValidationMessage.PANEL_WIDTH_NULL.getMessage(), violations.iterator().next().getMessage());
    }

    @Test
    public void testPositiveViolationWidth() {
        PanelSize panelSize = new PanelSize();
        panelSize.setHeight(2.0);
        panelSize.setWidth(-3.0);

        Set<ConstraintViolation<PanelSize>> violations = validator.validate(panelSize);
        assertEquals(1, violations.size());
        assertEquals(ValidationMessage.PANEL_WIDTH_POSITIVE.getMessage(), violations.iterator().next().getMessage());
    }
}