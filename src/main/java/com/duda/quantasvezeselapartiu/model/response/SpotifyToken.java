package com.duda.quantasvezeselapartiu.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SpotifyToken {
    @JsonProperty("access_token")
    private String token;
}
