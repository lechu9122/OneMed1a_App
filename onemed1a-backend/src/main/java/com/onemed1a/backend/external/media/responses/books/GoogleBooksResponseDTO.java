package com.onemed1a.backend.external.media.responses.books;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GoogleBooksResponseDTO {

    private Integer totalItems;
    private List<GoogleBooksResponse> items;

}
