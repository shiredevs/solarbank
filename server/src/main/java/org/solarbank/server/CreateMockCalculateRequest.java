package org.solarbank.server;

import org.solarbank.server.dto.CalculateRequest;
import org.solarbank.server.dto.EnergyTariff;
import org.solarbank.server.dto.Location;
import org.solarbank.server.dto.PanelSize;

public class CreateMockCalculateRequest {
    public static CalculateRequest createCalculateRequest() {
        Location location = new Location();
        location.setLatitude(90.0);
        location.setLongitude(45.0);

        PanelSize panelSize = new PanelSize();
        panelSize.setHeight(2.0);
        panelSize.setWidth(3.0);

        Double panelEfficiency = 0.15;

        EnergyTariff energyTariff = new EnergyTariff();
        energyTariff.setAmount(0.01);
        energyTariff.setCurrencyCode("USD");

        CalculateRequest calculateRequest = new CalculateRequest();
        calculateRequest.setLocation(location);
        calculateRequest.setPanelSize(panelSize);
        calculateRequest.setPanelEfficiency(panelEfficiency);
        calculateRequest.setEnergyTariff(energyTariff);

        return calculateRequest;
    }
}