package com.duda.quantasvezeselapartiu.configuration.endpoint;

import com.duda.quantasvezeselapartiu.handler.ElaPartiuHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class ElaPartiuEndpointConfiguration {

    @Bean
    public RouterFunction<ServerResponse> routes(ElaPartiuHandler handler){
        return nest(path("/v1"),
                route(GET("/ela-partiu"), handler::quantasVezes)
        );
    }

}
