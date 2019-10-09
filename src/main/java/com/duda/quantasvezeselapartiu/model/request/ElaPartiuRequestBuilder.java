package com.duda.quantasvezeselapartiu.model.request;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@Builder
public class ElaPartiuRequestBuilder {

    @NotNull
    private String from;

    @NotNull
    private String to;
}
