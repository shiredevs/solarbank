package org.solarbank.server;

import lombok.Data;

@Data
public class UserInputDto {
    private Location location;
    private PanelSize panelSize;
    private Double panelEfficiency;
    private EnergyTariff energyTariff;
}
