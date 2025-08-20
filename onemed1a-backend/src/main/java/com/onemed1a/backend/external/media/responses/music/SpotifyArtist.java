package com.onemed1a.backend.external.media.responses.music;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SpotifyArtist {
    private String id;
    private String name;
    private String href;
    private String uri;
    // external_urls can be added if you need it
}
