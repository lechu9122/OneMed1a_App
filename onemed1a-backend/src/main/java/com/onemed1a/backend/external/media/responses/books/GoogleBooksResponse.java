package com.onemed1a.backend.external.media.responses.books;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GoogleBooksResponse {
    private String id;
    private VolumeInfo volumeInfo;
}
