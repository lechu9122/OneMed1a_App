package com.onemed1a.backend.external.media.responses.music;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import lombok.*;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SpotifyResponseDTO {
    private SpotifyAlbumsPage albums;
}
