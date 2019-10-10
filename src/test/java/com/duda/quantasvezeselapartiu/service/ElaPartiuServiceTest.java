package com.duda.quantasvezeselapartiu.service;

import com.duda.quantasvezeselapartiu.client.RouteClient;
import com.duda.quantasvezeselapartiu.client.SpotifyClient;
import com.duda.quantasvezeselapartiu.exception.NotFoundException;
import com.duda.quantasvezeselapartiu.helper.SpotifyMusicHelper;
import com.duda.quantasvezeselapartiu.model.request.ElaPartiuRequestBuilder;
import com.duda.quantasvezeselapartiu.model.response.QuantasVezesResponse;
import com.duda.quantasvezeselapartiu.model.response.RouteResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class ElaPartiuServiceTest {

    @Spy
    @InjectMocks
    private ElaPartiuService elaPartiuService;

    @Mock
    private RouteClient routeClient;

    @Mock
    private SpotifyClient spotifyClient;

    @Test
    public void whenHappyPath_shouldReturnQuantasVezesWithProperDurationAndMusic(){
        doReturn(Mono.just(RouteResponse.builder().duration(10000d).build()))
                .when(routeClient)
                .getRouteDuration(any());

        doReturn(Mono.just(SpotifyMusicHelper.create()))
                .when(spotifyClient)
                .getSpotifyElaPartiu();

        Mono<QuantasVezesResponse> quantasVezes = elaPartiuService.quantasVezes(
                ElaPartiuRequestBuilder.builder()
                        .from("Sao Paulo")
                        .to("Porto Alegre")
                        .build()
        );

        StepVerifier.create(quantasVezes)
                .expectNextMatches(
                        response -> response.getMusica().equals("Ela Partiu") && response.getVezes().equals(39.22d)
                )
                .verifyComplete();
    }

    @Test
    public void whenErrorAtRouteClient_shouldBringError(){
        ElaPartiuRequestBuilder request = ElaPartiuRequestBuilder.builder()
                .from("Sao Paulo")
                .to("Porto Alegre")
                .build();

        doReturn(Mono.error(new NotFoundException(request)))
                .when(routeClient)
                .getRouteDuration(any());

        doReturn(Mono.just(SpotifyMusicHelper.create()))
                .when(spotifyClient)
                .getSpotifyElaPartiu();

        Mono<QuantasVezesResponse> quantasVezes = elaPartiuService.quantasVezes(request);

        StepVerifier.create(quantasVezes)
                .verifyErrorMessage("404 NOT_FOUND \"No route found from Sao Paulo to Porto Alegre\"");
    }

}