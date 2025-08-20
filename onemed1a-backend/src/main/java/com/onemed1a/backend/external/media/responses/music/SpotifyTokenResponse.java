package com.onemed1a.backend.external.media.responses.music;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SpotifyTokenResponse {
    @JsonProperty("access_token") private String accessToken;
    @JsonProperty("token_type")   private String tokenType;
    @JsonProperty("expires_in")   private int expiresIn;
}

