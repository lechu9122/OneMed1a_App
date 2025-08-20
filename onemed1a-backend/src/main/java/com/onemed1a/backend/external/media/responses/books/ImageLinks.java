package com.onemed1a.backend.external.media.responses.books;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ImageLinks {
    private String smallThumbnail;
    private String thumbnail;   // commonly used as poster
}
