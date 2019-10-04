package com.duda.quantasvezeselapartiu.controller;

import com.duda.quantasvezeselapartiu.model.request.ElaPartiuRequestBuilder;
import com.duda.quantasvezeselapartiu.model.response.QuantasVezesResponse;
import com.duda.quantasvezeselapartiu.service.ElaPartiuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping(path = "v1/ela-partiu", produces = MediaType.APPLICATION_JSON_VALUE)
public class ElaPartiuController {

    @Autowired
    private ElaPartiuService elaPartiuService;

    @GetMapping
    public Mono<QuantasVezesResponse> quantasVezes(
            @RequestParam("from") String from,
            @RequestParam("to") String to
    ) throws IOException, ExecutionException, InterruptedException {
        return elaPartiuService.quantasVezes(ElaPartiuRequestBuilder.builder().from(from).to(to).build());
    }

}
