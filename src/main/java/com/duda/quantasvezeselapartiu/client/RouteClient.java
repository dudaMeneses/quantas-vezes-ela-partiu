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
                .map(jsonNode -> {
                    List<String> messages = getMessageValues(jsonNode);
                    if(messages.isEmpty()){
                        return RouteResponse.builder()
                                .duration(jsonNode.path("route").get("time").doubleValue())
                                .build();
                    } else {
                        throw new NotFoundException(request);
                    }
                })
                .doOnError(Mono::error);
    }

    private List<String> getMessageValues(JsonNode jsonNode) {
        List<String> messages = new ArrayList<>();

        Iterable messagesNodeIterable = () -> jsonNode.path("info").withArray("messages").elements();
        StreamSupport.stream(messagesNodeIterable.spliterator(), false)
            .forEach(message -> messages.add(message.toString()));

        return messages;
    }
}
