package com.onemed1a.backend.search.dto;

import com.onemed1a.backend.media.MediaData;
import lombok.*;

import java.util.List;
import java.util.UUID;

/** Lightweight result for the results page */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SearchResultItem {
    private UUID id;                  // MediaData.mediaId
    private String title;
    private MediaData.MediaType type;
    private String releaseDate;       // keep as stored (e.g., "2023")
    private List<String> genres;
}