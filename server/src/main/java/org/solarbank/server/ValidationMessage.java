package org.solarbank.server;

import lombok.Getter;

@Getter
public enum ValidationMessage {
    LONGITUDE_NULL("longitude must be provided"),
    LONGITUDE_MAX("maximum longitude = 180"),
    LONGITUDE_MIN("minimum longitude = -180"),
    LATITUDE_NULL("latitude must be provided"),
    LATITUDE_MAX("maximum latitude = 90"),
    LATITUDE_MIN("minimum longitude = -90"),
    PANEL_HEIGHT_NULL("panel height must be provided"),
    PANEL_HEIGHT_POSITIVE("Panel height must be a positive number"),
    PANEL_WIDTH_NULL("panel width must be provided"),
    PANEL_WIDTH_POSITIVE("Panel width must be a positive number"),
    PANEL_EFF_NULL("panel efficiency must be provided"),
    PANEL_EFF_POSITIVE("Panel efficiency must be a positive number"),
    PANEL_EFF_MAX("Panel efficiency can not be above %100"),
    CURRENCY_CODE("Invalid currency code"),
    AMOUNT_NULL("energy tariff must be provided"),
    AMOUNT_MIN("amount must be at least 0.01");

    private final String message;

    ValidationMessage(String message) {
        this.message = message;
    }
}