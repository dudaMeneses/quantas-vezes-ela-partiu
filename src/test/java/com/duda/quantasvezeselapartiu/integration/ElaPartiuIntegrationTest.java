package com.duda.quantasvezeselapartiu.integration;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ElaPartiuIntegrationTest {

    @Autowired
    private WebTestClient client;

    @Test
    public void whenFromPortoAlegreToFlorianopolis_shouldBeSeventyThreeTimes(){
        client.get()
                .uri("/v1/ela-partiu?from={from}&to={to}", "Porto Alegre", "Florianopolis")
                .exchange()
                .expectStatus().isOk()
                .expectBody().json("{\"musica\":\"Ela Partiu\",\"vezes\": 67.4}");
    }

    @Test
    public void whenFromCityDoesNotExist_shouldBeErrorMessage(){
        client.get()
                .uri("/v1/ela-partiu?from={from}&to={to}", "Unexistent City From Hauhauahau", "Florianopolis")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void whenToCityDoesNotExist_shouldBeErrorMessage(){
        client.get()
                .uri("/v1/ela-partiu?from={from}&to={to}", "Porto Alegre", "Unexistent City From Hauhauahau")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void whenNoFrom_shouldBeBadRequest(){
        client.get()
                .uri("/v1/ela-partiu?to={to}",  "Florianopolis")
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody().jsonPath("$.message", "Query Parameter 'from' is required");
    }

    @Test
    public void whenNoTo_shouldBeBadRequest(){
        client.get()
                .uri("/v1/ela-partiu?from={from}",  "Florianopolis")
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody().jsonPath("$.message", "Query Parameter 'to' is required");
    }
}
