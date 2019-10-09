package com.duda.quantasvezeselapartiu.model.response;

import lombok.Builder;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

@Getter
@Builder
public class RouteResponse {
    private Double duration;
    private String[] messages;
}
