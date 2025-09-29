package org.solarbank.server.client;

import lombok.extern.slf4j.Slf4j;
import org.solarbank.server.configuration.ApplicationProperties;
import org.solarbank.server.dto.Location;
import org.solarbank.server.dto.RadianceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Year;

import static org.springframework.web.util.UriComponentsBuilder.*;

@Slf4j
@Service
public class NasaPowerClient {
    private final ApiClient apiClient;
    private final String baseUrl;

    @Autowired
    public NasaPowerClient(ApiClient apiClient, ApplicationProperties applicationProperties) {
        this.apiClient = apiClient;
        this.baseUrl = applicationProperties
            .getEndpoint()
            .getNasaPowerBaseUrl();
    }

    public RadianceResponse getMeanDailyRadianceFor(Location location) {
        try {
            log.info("Retrieving radiance data for {}", location.toString());

            return apiClient.get(generateUrl(location), RadianceResponse.class);

        } catch (RuntimeException exception) {
            throw new NasaPowerClientException(
                String.format("Request for radiance data failed for %s", location.toString()),
                exception
            );
        }
    }

    private String generateUrl(Location location) {
        int endYear = Year.now().getValue() - 1;
        int startYear = endYear - 19;

        return fromUriString(String.format("%s/temporal/monthly/point", baseUrl))
            .queryParam("start", startYear)
            .queryParam("end", endYear)
            .queryParam("latitude", location.getLatitude())
            .queryParam("longitude", location.getLongitude())
            .queryParam("community", "re")
            .queryParam("parameters", "ALLSKY_SFC_SW_DWN")
            .queryParam("format", "json")
            .queryParam("units", "metric")
            .queryParam("header", true)
            .queryParam("time-standard", "utc")
            .toUriString();
    }
}
