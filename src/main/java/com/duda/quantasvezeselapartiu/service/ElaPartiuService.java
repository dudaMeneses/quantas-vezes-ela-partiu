package com.duda.quantasvezeselapartiu.service;

import com.duda.quantasvezeselapartiu.client.GoogleClient;
import com.duda.quantasvezeselapartiu.client.SpotifyClient;
import com.duda.quantasvezeselapartiu.model.request.ElaPartiuRequestBuilder;
import com.duda.quantasvezeselapartiu.model.response.QuantasVezesResponse;
import com.duda.quantasvezeselapartiu.model.response.SpotifyMusic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class ElaPartiuService {

    @Autowired
    private GoogleClient googleClient;

    @Autowired
    private SpotifyClient spotifyClient;

    public Mono<QuantasVezesResponse> quantasVezes(ElaPartiuRequestBuilder request) {
        return Mono.zip(spotifyClient.getSpotifyElaPartiu(), googleClient.getGoogleDirections(request))
            .map(tuple ->
                QuantasVezesResponse.builder()
                        .musica(tuple.getT1().getName())
                        .vezes(
                                new BigDecimal(tuple.getT2().doubleValue() / (tuple.getT1().getDuration() / 1000D))
                                        .setScale(2, RoundingMode.HALF_UP)
                                        .doubleValue()
                        )
                        .build()
            );
    }

}
