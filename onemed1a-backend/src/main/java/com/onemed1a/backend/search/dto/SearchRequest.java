package com.onemed1a.backend.search.dto;

import com.onemed1a.backend.media.MediaData;
import com.onemed1a.backend.usermediastatus.UserMediaStatus;
import lombok.*;

import java.util.UUID;

/** Query params for /media/search bound via @ModelAttribute */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SearchRequest {
    private String q;                         // text in title
    private MediaData.MediaType type;         // MOVIE / TV / BOOK / MUSIC (whatever you use)
    private String genre;                     // exact match on a genre element
    private Integer yearFrom;                 // inclusive (compared against releaseDate string)
    private Integer yearTo;                   // inclusive
    private UUID userId;                      // optional: filter by this user's status
    private UserMediaStatus.Status status;    // COMPLETED / WATCHING / PLAN_TO_WATCH
}