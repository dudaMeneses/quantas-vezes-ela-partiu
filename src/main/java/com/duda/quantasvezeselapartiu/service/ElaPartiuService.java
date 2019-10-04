package com.duda.quantasvezeselapartiu.service;

import com.duda.quantasvezeselapartiu.component.GoogleMapsProperties;
import com.duda.quantasvezeselapartiu.component.SpotifyProperties;
import com.duda.quantasvezeselapartiu.model.request.ElaPartiuRequestBuilder;
import com.duda.quantasvezeselapartiu.model.response.QuantasVezesResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Base64;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class ElaPartiuService {

    @Autowired
    private SpotifyProperties spotifyProperties;

    @Autowired
    private GoogleMapsProperties googleMapsProperties;

    public Mono<QuantasVezesResponse> quantasVezes(ElaPartiuRequestBuilder request) throws IOException {
        return Mono.just(
                QuantasVezesResponse.builder()
                        .musica("Tim Maia - Ela Partiu")
                        .vezes(getQuantasVezesElaPartiu(request))
                        .build()
        );
    }

    private double getQuantasVezesElaPartiu(ElaPartiuRequestBuilder request) throws IOException {
        return (double) getGoogleDirections(request) / getSpotifyElaPartiuDuration();
    }

    private Long getSpotifyElaPartiuDuration() throws IOException {
        String elaPartiuJson = WebClient.create(spotifyProperties.getTrackUrl())
                .get()
                .header("Authorization", "Bearer " + getSpotifyAccessToken())
                .retrieve()
                .bodyToMono(String.class)
                .toProcessor().peek();

        return new ObjectMapper().readTree(elaPartiuJson).path("duration_ms").asLong() / 1000;
    }

    private String getSpotifyAccessToken() throws IOException {
        String spotifyToken = WebClient.create(spotifyProperties.getTokenUrl())
                .post()
                .header("Authorization", "Basic " + Base64.getEncoder().encodeToString(spotifyProperties.getIdSecret().getBytes()))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .retrieve()
                .bodyToMono(String.class)
                .toProcessor().peek();

        return new ObjectMapper().readTree(spotifyToken)
                .path("access_token").asText();
    }

    private Long getGoogleDirections(ElaPartiuRequestBuilder request) throws IOException {
        String json = WebClient.create(googleMapsProperties.getDirectionsUrl())
                .get()
                .uri(googleMapsProperties.getDirectionsUrl(), request.getFrom(), request.getTo())
                .retrieve()
                .bodyToMono(String.class)
                .toProcessor().peek();

        return new ObjectMapper().readTree(json)
                .path("routes").get(0)
                .path("legs").get(0)
                .path("duration").get("value").longValue();
    }
}
