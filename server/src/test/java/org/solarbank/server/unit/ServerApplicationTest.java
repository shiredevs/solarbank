package org.solarbank.server.unit;

import org.junit.jupiter.api.Test;
import org.solarbank.server.ServerApplication;

class ServerApplicationTest {

    @Test
    public void executeApplication() {
        System.setProperty("spring.profiles.active", "test");

        ServerApplication.main(new String[] {});
    }
}
