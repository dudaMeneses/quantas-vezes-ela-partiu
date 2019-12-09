package com.duda.quantasvezeselapartiu.client;

import com.duda.quantasvezeselapartiu.configuration.property.RouteProperties;
import com.duda.quantasvezeselapartiu.exception.NotFoundException;
import com.duda.quantasvezeselapartiu.model.request.ElaPartiuRequestBuilder;
import com.duda.quantasvezeselapartiu.model.response.RouteResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

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
                .flatMap(jsonNode -> getTravelDuration(request, jsonNode))
                .timeout(Duration.ofSeconds(5));
    }

    private Mono<RouteResponse> getTravelDuration(ElaPartiuRequestBuilder request, JsonNode jsonNode) {
        return Mono.just(jsonNode)
                .flatMap(node -> validateErrorMessages(request, node))
                .flatMap(this::getTravelDuration)
                .switchIfEmpty(Mono.error(new NotFoundException(request)));
    }

    private Mono<RouteResponse> getTravelDuration(JsonNode jsonNode) {
        return Mono.justOrEmpty(jsonNode)
                .map(node -> node.path("route"))
                .map(node -> node.get("time"))
                .map(JsonNode::doubleValue)
                .defaultIfEmpty(0D)
                .map(travelTime -> RouteResponse.builder()
                        .duration(travelTime)
                        .build());
    }

    private Mono<JsonNode> validateErrorMessages(ElaPartiuRequestBuilder request, JsonNode jsonNode) {
        return Mono.just(jsonNode)
                .map(node -> node.path("info"))
                .map(node -> node.withArray("messages"))
                .map(JsonNode::elements)
                .flatMap(iterator -> iterator.hasNext() ? Mono.error(new NotFoundException(request)) : Mono.justOrEmpty(jsonNode));
    }

}
