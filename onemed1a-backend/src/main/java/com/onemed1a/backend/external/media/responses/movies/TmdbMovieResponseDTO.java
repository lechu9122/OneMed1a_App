package com.onemed1a.backend.external.media.responses.movies;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class TmdbMovieResponseDTO {
    private int page;
    private List<TmdbMovieResponse> results;
}

