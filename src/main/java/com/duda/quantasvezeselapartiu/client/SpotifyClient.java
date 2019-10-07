package com.duda.quantasvezeselapartiu.client;

import com.duda.quantasvezeselapartiu.property.SpotifyProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Base64;
import java.util.concurrent.atomic.AtomicReference;

import static org.springframework.web.reactive.function.BodyInserters.fromFormData;

@Component
public class SpotifyClient {

    @Autowired
    private SpotifyProperties spotifyProperties;

    @Autowired
    private ObjectMapper mapper;

    public Long getSpotifyElaPartiuDuration() {
        AtomicReference<Long> musicDuration = new AtomicReference<>(255L);

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
}
