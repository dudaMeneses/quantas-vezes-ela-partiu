package com.duda.quantasvezeselapartiu.configuration.property;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Getter
@Component
@ConfigurationProperties("spotify")
@PropertySource(value="classpath:external.yml")
public class SpotifyProperties {
    @Value("${id}:${secret}")
    private String idSecret;

    @Value("${token.api}")
    private String tokenUrl;

    @Value("${track.api}")
    private String trackUrl;
}
