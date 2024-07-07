package org.solarbank.server;

public class UserInputDto {
    private Location location;
    private PanelSize panelSize;
    private double panelEfficiency;
    private EnergyTariff energyTariff;

    // Getters and setters
    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public PanelSize getPanelSize() {
        return panelSize;
    }

    public void setPanelSize(PanelSize panelSize) {
        this.panelSize = panelSize;
    }

    public double getPanelEfficiency() {
        return panelEfficiency;
    }

    public void setPanelEfficiency(double panelEfficiency) {
        this.panelEfficiency = panelEfficiency;
    }

    public EnergyTariff getEnergyTariff() {
        return energyTariff;
    }

    public void setEnergyTariff(EnergyTariff energyTariff) {
        this.energyTariff = energyTariff;
    }
}
