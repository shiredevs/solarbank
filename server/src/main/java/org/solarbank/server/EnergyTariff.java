package org.solarbank.server;

import lombok.Data;

@Data
public class EnergyTariff {
    private String currencyCode;
    private Double amount;
}
