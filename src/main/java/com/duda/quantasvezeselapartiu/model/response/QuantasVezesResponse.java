package com.duda.quantasvezeselapartiu.model.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class QuantasVezesResponse {
    private String musica;
    private Double vezes;
}
