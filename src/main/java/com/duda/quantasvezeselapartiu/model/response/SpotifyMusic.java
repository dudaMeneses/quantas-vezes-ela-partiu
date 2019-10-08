package com.duda.quantasvezeselapartiu.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SpotifyMusic {

    private String name;

    @JsonProperty("duration_ms")
    private Long duration;
}