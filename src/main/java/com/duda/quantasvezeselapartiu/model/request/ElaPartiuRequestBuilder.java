package com.duda.quantasvezeselapartiu.model.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ElaPartiuRequestBuilder {
    private String from;
    private String to;
}
