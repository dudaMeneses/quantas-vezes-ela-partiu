package com.duda.quantasvezeselapartiu.client;

import com.duda.quantasvezeselapartiu.configuration.property.RouteProperties;
import com.duda.quantasvezeselapartiu.model.request.ElaPartiuRequestBuilder;
import com.duda.quantasvezeselapartiu.model.response.RouteResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class RouteClient {

    @Autowired
    private RouteProperties routeProperties;

    @Autowired
    private ObjectMapper mapper;

    public Mono<RouteResponse> getRouteDuration(ElaPartiuRequestBuilder request) {
        return WebClient.create(routeProperties.getDirectionsUrl())
                .get()
                .uri(routeProperties.getDirectionsUrl(), request.getFrom(), request.getTo())
                .retrieve()
                .bodyToMono(JsonNode.class)
                .map(jsonNode -> RouteResponse.builder()
                                .duration(jsonNode.path("route").get("time").doubleValue())
                                .build()
                );
    }
}
