package com.duda.quantasvezeselapartiu.configuration.property;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Getter
@Component
@ConfigurationProperties("route")
@PropertySource(value="classpath:external.yml")
public class RouteProperties {

    @Value("${directions.api}")
    private String directionsUrl;

}
