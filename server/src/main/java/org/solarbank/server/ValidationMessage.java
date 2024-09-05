package org.solarbank.server;

public class ValidationMessage {
    public static final String LONGITUDE_NULL = "longitude must be provided";
    public static final String LONGITUDE_MAX = "maximum longitude = 180";
    public static final String LONGITUDE_MIN = "minimum longitude = -180";
    public static final String LATITUDE_NULL = "latitude must be provided";
    public static final String LATITUDE_MAX = "maximum latitude = 90";
    public static final String LATITUDE_MIN = "minimum longitude = -90";
    public static final String PANEL_HEIGHT_NULL = "panel height must be provided";
    public static final String PANEL_HEIGHT_POSITIVE = "Panel height must be a positive number";
    public static final String PANEL_WIDTH_NULL = "panel width must be provided";
    public static final String PANEL_WIDTH_POSITIVE = "Panel width must be a positive number";
    public static final String PANEL_EFF_NULL = "panel efficiency must be provided";
    public static final String PANEL_EFF_POSITIVE = "Panel efficiency must be a positive number";
    public static final String PANEL_EFF_MAX = "Panel efficiency can not be above 100%";
    public static final String CURRENCY_CODE = "Invalid currency code";
    public static final String AMOUNT_NULL = "energy tariff must be provided";
    public static final String AMOUNT_MIN = "amount must be at least 0.01";
    public static final String LOCATION_NULL = "Location must be provided";
    public static final String PANEL_SIZE_NULL = "panel size must be provided";
    public static final String ENERGY_TARIFF_NULL = "Energy tariff must be provided";
    public static final String REQUEST_NULL = "Invalid or empty request body, please check your request meets the api specification";
}