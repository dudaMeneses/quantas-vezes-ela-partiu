package com.duda.quantasvezeselapartiu.handler;

import com.duda.quantasvezeselapartiu.model.request.ElaPartiuRequestBuilder;
import com.duda.quantasvezeselapartiu.model.response.QuantasVezesResponse;
import com.duda.quantasvezeselapartiu.service.ElaPartiuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.BodyInserters.fromPublisher;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
public class ElaPartiuHandler {

    @Autowired
    private ElaPartiuService elaPartiuService;

    public Mono<ServerResponse> quantasVezesMono(ServerRequest request){
        Mono<QuantasVezesResponse> response = elaPartiuService.quantasVezes(
                ElaPartiuRequestBuilder.builder()
                        .from(request.queryParam("from").orElseThrow())
                        .to(request.queryParam("to").orElseThrow())
                        .build()
        );

        return ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(fromPublisher(response, QuantasVezesResponse.class));
    }

}
