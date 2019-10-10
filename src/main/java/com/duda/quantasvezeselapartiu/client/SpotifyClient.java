package com.duda.quantasvezeselapartiu.client;

import com.duda.quantasvezeselapartiu.configuration.property.SpotifyProperties;
import com.duda.quantasvezeselapartiu.exception.SpotifyCredentialException;
import com.duda.quantasvezeselapartiu.model.response.SpotifyMusic;
import com.duda.quantasvezeselapartiu.model.response.SpotifyToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Base64;

import static org.springframework.web.reactive.function.BodyInserters.fromFormData;

@Component
public class SpotifyClient {

    @Autowired
    private SpotifyProperties spotifyProperties;

    public Mono<SpotifyMusic> getSpotifyElaPartiu() {
        return getSpotifyAccessToken().flatMap(token ->
                WebClient.builder()
                        .baseUrl(spotifyProperties.getTrackUrl())
                        .defaultHeader("Authorization", "Bearer " + token.getToken())
                        .build()
                        .get()
                        .retrieve()
                        .bodyToMono(SpotifyMusic.class)
        );
    }

    private Mono<SpotifyToken> getSpotifyAccessToken() {
        return WebClient.builder()
                .baseUrl(spotifyProperties.getTokenUrl())
                .defaultHeader("Authorization", "Basic " + Base64.getEncoder().encodeToString(spotifyProperties.getIdSecret().getBytes()))
                .defaultHeader("Content-Type", "application/x-www-form-urlencoded")
                .build()
                .post()
                .body(fromFormData("grant_type", "client_credentials"))
                .retrieve()
                .bodyToMono(SpotifyToken.class)
                .switchIfEmpty(Mono.error(new SpotifyCredentialException()));
    }
}
