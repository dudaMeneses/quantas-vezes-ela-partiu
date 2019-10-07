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

import static org.springframework.web.reactive.function.BodyInserters.fromFormData;

@Service
public class ElaPartiuService {

    @Autowired
    private SpotifyProperties spotifyProperties;

    @Autowired
    private GoogleMapsProperties googleMapsProperties;

    @Autowired
    private ObjectMapper mapper;

    public Mono<QuantasVezesResponse> quantasVezes(ElaPartiuRequestBuilder request) {
        return Mono.just(
                QuantasVezesResponse.builder()
                        .musica("Tim Maia - Ela Partiu")
                        .vezes(getQuantasVezesElaPartiu(request))
                        .build()
        );
    }

    private double getQuantasVezesElaPartiu(ElaPartiuRequestBuilder request) {
        return getGoogleDirections(request).doubleValue() / getSpotifyElaPartiuDuration();
    }

    private Long getSpotifyElaPartiuDuration() {
        AtomicReference<Long> musicDuration = new AtomicReference<>(0L);

        getSpotifyAccessToken().subscribe(token ->
                WebClient.builder()
                    .baseUrl(spotifyProperties.getTrackUrl())
                    .defaultHeader("Authorization", "Bearer " + token)
                    .build()
                    .get()
                    .retrieve()
                    .bodyToMono(String.class)
                    .map(content -> {
                        try {
                            return mapper.readTree(content);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .map(jsonNode -> jsonNode.get("duration_ms").asLong() / 1000)
                    .subscribe(musicDuration::set)
        );

        return musicDuration.get();
    }

    private Mono<String> getSpotifyAccessToken() {
        return WebClient.builder()
                .baseUrl(spotifyProperties.getTokenUrl())
                .defaultHeader("Authorization", "Basic " + Base64.getEncoder().encodeToString(spotifyProperties.getIdSecret().getBytes()))
                .defaultHeader("Content-Type", "application/x-www-form-urlencoded")
                .build()
                .post()
                .body(fromFormData("grant_type", "client_credentials"))
                .retrieve()
                .bodyToMono(String.class)
                .map(content -> {
                    try {
                        return mapper.readTree(content);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .map(jsonNode -> jsonNode.path("access_token").asText());
    }

    private Long getGoogleDirections(ElaPartiuRequestBuilder request) {
        AtomicReference<Long> travelDuration = new AtomicReference<>(0L);

        WebClient.create(googleMapsProperties.getDirectionsUrl())
            .get()
            .uri(googleMapsProperties.getDirectionsUrl(), request.getFrom(), request.getTo())
            .retrieve()
            .bodyToMono(String.class)
            .map(content -> {
                try {
                    return mapper.readTree(content);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            })
            .map(jsonNode -> jsonNode.path("routes").get(0))
            .map(jsonNode -> jsonNode.path("legs").get(0))
            .map(jsonNode -> jsonNode.path("duration").get("value").longValue())
            .subscribe(travelDuration::set);

        return travelDuration.get();
    }
}
