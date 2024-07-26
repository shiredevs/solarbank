package org.solarbank.server;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CurrencyCodeValidator implements ConstraintValidator<ValidCurrencyCode, String> {
    @Override
    public boolean isValid(String currencyCode, ConstraintValidatorContext context) {
        try {
            CurrencyUnit currencyUnit = Monetary.getCurrency(currencyCode);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
