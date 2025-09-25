package org.solarbank.server.utils;

import org.solarbank.server.dto.CalculateRequest;
import org.solarbank.server.dto.EnergyTariff;
import org.solarbank.server.dto.Location;
import org.solarbank.server.dto.PanelSize;

public class RequestHelper {

    public static CalculateRequest createCalculateRequest() {
        return createCalculateRequest(90.0, 45.0);
    }

    public static CalculateRequest createCalculateRequest(double latitude, double longitude) {
        return createCalculateRequest(
            latitude,
            longitude,
            0.15,
            2.0,
            3.0,
            1.0,
            "USD"
        );
    }

    public static CalculateRequest createCalculateRequest(
        double latitude,
        double longitude,
        double panelEfficiency,
        double panelHeight,
        double panelWidth,
        double energyTariffAmount,
        String energyTariffCurrencyCode
    ) {
        Location location = new Location();
        location.setLatitude(latitude);
        location.setLongitude(longitude);

        PanelSize panelSize = new PanelSize();
        panelSize.setHeight(panelHeight);
        panelSize.setWidth(panelWidth);

        EnergyTariff energyTariff = new EnergyTariff();
        energyTariff.setAmount(energyTariffAmount);
        energyTariff.setCurrencyCode(energyTariffCurrencyCode);

        CalculateRequest calculateRequest = new CalculateRequest();
        calculateRequest.setLocation(location);
        calculateRequest.setPanelSize(panelSize);
        calculateRequest.setPanelEfficiency(panelEfficiency);
        calculateRequest.setEnergyTariff(energyTariff);

        return calculateRequest;
    }
}
