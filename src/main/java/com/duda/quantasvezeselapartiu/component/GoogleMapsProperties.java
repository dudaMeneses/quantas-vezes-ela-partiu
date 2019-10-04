package com.duda.quantasvezeselapartiu.component;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Getter
@Component
@ConfigurationProperties("google")
@PropertySource(value="classpath:external.yml")
public class GoogleMapsProperties {

    @Value("${directions.api}")
    private String directionsUrl;

}
