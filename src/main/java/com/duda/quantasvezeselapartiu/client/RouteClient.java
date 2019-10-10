package com.duda.quantasvezeselapartiu.client;

import com.duda.quantasvezeselapartiu.configuration.property.RouteProperties;
import com.duda.quantasvezeselapartiu.exception.NotFoundException;
import com.duda.quantasvezeselapartiu.model.request.ElaPartiuRequestBuilder;
import com.duda.quantasvezeselapartiu.model.response.RouteResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

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
        JsonNode messagesNode = jsonNode.path("info").get("messages");

        List<String> messages = new ArrayList<>();

        if(messagesNode.isArray()){
            for(final JsonNode message : messagesNode){
                messages.add(message.textValue());
            }
        }

        return messages;
    }
}
