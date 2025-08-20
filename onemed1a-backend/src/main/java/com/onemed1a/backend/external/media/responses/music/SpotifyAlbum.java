
package com.onemed1a.backend.external.media.responses.music;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter @Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SpotifyAlbum {
    private String id;
    private String name;
    private String albumType;              // e.g., "album", "single", "compilation"
    private String releaseDate;            // "yyyy", "yyyy-MM", or "yyyy-MM-dd"
    private String releaseDatePrecision;   // "year" | "month" | "day"
    private int totalTracks;
    private List<String> availableMarkets;
    private Map<String, String> externalUrls; // { "spotify": "https://..." }
    private String href;
    private String uri;
    private List<SpotifyImage> images;
    private List<SpotifyArtist> artists;
}
