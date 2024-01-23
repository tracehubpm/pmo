/*
 * Copyright (c) 2023-2024 Tracehub.git
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to read
 * the Software only. Permissions is hereby NOT GRANTED to use, copy, modify,
 * merge, publish, distribute, sublicense, and/or sell copies of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package it;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

/**
 * Keycloak Integration Suite.
 *
 * @since 0.0.0
 */
@DirtiesContext
@SuppressWarnings({
    "JTCOP.RuleCorrectTestName",
    "JTCOP.RuleAllTestsHaveProductionClass",
    "PMD.ProhibitPublicStaticMethods"
})
public interface KeycloakIntegration {

    /**
     * Keycloak Container.
     */
    KeycloakContainer KEYCLOAK = new KeycloakContainer()
        .withRealmImportFile("data/realm.json");

    /**
     * Properties.
     *
     * @param reg DynamicPropertyRegistry
     */
    @DynamicPropertySource
    static void properties(final DynamicPropertyRegistry reg) {
        reg.add(
            "spring.security.oauth2.resourceserver.jwt.issuer-uri",
            () -> "%s/realms/test".formatted(KeycloakIntegration.KEYCLOAK.getAuthServerUrl())
        );
    }

    /**
     * Start container.
     */
    @BeforeAll
    static void start() {
        KeycloakIntegration.KEYCLOAK.start();
    }

    /**
     * Stop container.
     */
    @AfterAll
    static void stop() {
        KeycloakIntegration.KEYCLOAK.stop();
    }
}
