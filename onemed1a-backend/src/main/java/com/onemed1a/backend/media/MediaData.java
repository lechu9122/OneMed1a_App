package com.onemed1a.backend.media;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "media_data")
public class MediaData {

    public enum MediaType {
        MOVIE, TV, MUSIC, BOOKS
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID mediaId;

    @Column(name = "external_media_id", nullable = false, updatable = false)
    private String externalMediaId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 16)
    private MediaType type; // MOVIE, TV, MUSIC, BOOKS

    @Column(name = "title", nullable = false, columnDefinition = "TEXT")
    private String title;

    @Column(name = "release_year")
    private String releaseDate;

//     Stores genres in a simple join table: media_genres(media_id, genre)
    @ElementCollection
    @Column(name = "genre", nullable = false)
    private List<String> genres = new ArrayList<>();

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "poster_url", columnDefinition = "TEXT")
    private String posterUrl;

    @Column(name = "backdrop_url", columnDefinition = "TEXT")
    private String backdropUrl;

//    // Store raw provider payload or extra fields as JSON (Postgres jsonb)
//    @Column(name = "metadata_json", columnDefinition = "jsonb")
//    private String metadataJson;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) createdAt = Instant.now();
    }
}
