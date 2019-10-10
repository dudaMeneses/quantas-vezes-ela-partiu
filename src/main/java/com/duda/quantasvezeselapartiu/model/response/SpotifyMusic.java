package com.duda.quantasvezeselapartiu.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SpotifyMusic {

    private String name;

    @JsonProperty("duration_ms")
    private Long duration;
}
