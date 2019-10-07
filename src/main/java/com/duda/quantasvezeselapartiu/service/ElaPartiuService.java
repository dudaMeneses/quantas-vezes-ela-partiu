package com.duda.quantasvezeselapartiu.service;

import com.duda.quantasvezeselapartiu.client.GoogleClient;
import com.duda.quantasvezeselapartiu.client.SpotifyClient;
import com.duda.quantasvezeselapartiu.model.request.ElaPartiuRequestBuilder;
import com.duda.quantasvezeselapartiu.model.response.QuantasVezesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ElaPartiuService {

    @Autowired
    private GoogleClient googleClient;

    @Autowired
    private SpotifyClient spotifyClient;

    public Mono<QuantasVezesResponse> quantasVezes(ElaPartiuRequestBuilder request) {
        return Mono.just(
                QuantasVezesResponse.builder()
                        .musica("Tim Maia - Ela Partiu")
                        .vezes(getQuantasVezesElaPartiu(request))
                        .build()
        );
    }

    private double getQuantasVezesElaPartiu(ElaPartiuRequestBuilder request) {
        return googleClient.getGoogleDirections(request).doubleValue() / spotifyClient.getSpotifyElaPartiuDuration();
    }

}
