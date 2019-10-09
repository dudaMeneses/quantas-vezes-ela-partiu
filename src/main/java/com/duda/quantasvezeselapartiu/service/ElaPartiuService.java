package com.duda.quantasvezeselapartiu.service;

import com.duda.quantasvezeselapartiu.client.RouteClient;
import com.duda.quantasvezeselapartiu.client.SpotifyClient;
import com.duda.quantasvezeselapartiu.exception.NotFoundException;
import com.duda.quantasvezeselapartiu.model.request.ElaPartiuRequestBuilder;
import com.duda.quantasvezeselapartiu.model.response.QuantasVezesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class ElaPartiuService {

    @Autowired
    private RouteClient googleClient;

    @Autowired
    private SpotifyClient spotifyClient;

    public Mono<QuantasVezesResponse> quantasVezes(ElaPartiuRequestBuilder request) {
        return Mono.zip(
                spotifyClient.getSpotifyElaPartiu(),
                googleClient.getRouteDuration(request),
                (spotifyMusic, route) -> {
                    if(route.getDuration().equals(0d)){
                        throw new NotFoundException(request);
                    }  else {
                        return QuantasVezesResponse.builder()
                                .musica(spotifyMusic.getName())
                                .vezes(new BigDecimal(route.getDuration() / (spotifyMusic.getDuration() / 1000D))
                                        .setScale(2, RoundingMode.HALF_UP)
                                        .doubleValue())
                                .mensagens(route.getMessages())
                                .build();
                    }
                }
        );
    }

}
