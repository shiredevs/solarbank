package org.solarbank.server.integration;

import org.junit.jupiter.api.Test;
import org.solarbank.server.configuration.ApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ServerApplicationIT extends IntegrationTestBase {

	@Autowired
	private ApplicationProperties applicationProperties;

	@Test
	public void applicationStarts_validConfigProvided_applicationPropertiesInitialised() {
		assertNotNull(applicationProperties);
		assertNotNull(applicationProperties.getCors());
	}
}
