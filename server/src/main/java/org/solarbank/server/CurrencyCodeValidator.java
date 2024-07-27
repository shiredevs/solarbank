package org.solarbank.server;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import javax.money.CurrencyUnit;
import javax.money.Monetary;


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