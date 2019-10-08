package com.duda.quantasvezeselapartiu.client;

import com.duda.quantasvezeselapartiu.model.request.ElaPartiuRequestBuilder;
import com.duda.quantasvezeselapartiu.property.GoogleProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.concurrent.atomic.AtomicReference;

@Component
public class GoogleClient implements JsonReader {

    @Autowired
    private GoogleProperties googleMapsProperties;

    @Autowired
    private ObjectMapper mapper;

    public Long getGoogleDirections(ElaPartiuRequestBuilder request) {
        AtomicReference<Long> travelDuration = new AtomicReference<>(0L);

        WebClient.create(googleMapsProperties.getDirectionsUrl())
                .get()
                .uri(googleMapsProperties.getDirectionsUrl(), request.getFrom(), request.getTo())
                .retrieve()
                .bodyToMono(String.class)
                .map(content -> contentToNode(mapper, content))
                .map(jsonNode -> jsonNode.path("routes").get(0))
                .map(jsonNode -> jsonNode.path("legs").get(0))
                .map(jsonNode -> jsonNode.path("duration").get("value").longValue())
                .subscribe(travelDuration::set);

        return travelDuration.get();
    }
}
