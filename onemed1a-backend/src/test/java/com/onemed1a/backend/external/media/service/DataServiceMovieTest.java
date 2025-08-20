package com.onemed1a.backend.external.media.service;

import com.onemed1a.backend.external.media.responses.movies.TmdbMovieResponse;
import com.onemed1a.backend.external.media.responses.movies.TmdbMovieResponseDTO;
import com.onemed1a.backend.media.MediaData;
import com.onemed1a.backend.media.MediaDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class DataServiceMovieTest {

    private DataService dataService;
    private MediaDataRepository repository;
    private RestTemplate restTemplate;

    @BeforeEach
    void setup() {
        repository = mock(MediaDataRepository.class);
        restTemplate = mock(RestTemplate.class);

        dataService = new DataService(repository, restTemplate);

        ReflectionTestUtils.setField(dataService, "movieApiUrl", "http://fake.example/movies");
        ReflectionTestUtils.setField(dataService, "tvApiUrl", "http://fake.example/tv");
        ReflectionTestUtils.setField(dataService, "spotifySearchUrl", "http://fake.example/search");
        ReflectionTestUtils.setField(dataService, "spotifyTokenUrl", "http://fake.example/token");
        ReflectionTestUtils.setField(dataService, "spotifyClientId", "cid");
        ReflectionTestUtils.setField(dataService, "spotifyClientSecret", "secret");
        ReflectionTestUtils.setField(dataService, "googleBooksUri", "http://fake.example/books");
        ReflectionTestUtils.setField(dataService, "googleBooksApiKey", "key");

        when(repository.saveAll(anyList())).thenAnswer(inv -> inv.getArgument(0));
    }


    @Test
    void shouldFetchAndSaveMovies() {
        // Fake TMDB response
        TmdbMovieResponse movie = new TmdbMovieResponse();
        movie.setId(1);
        movie.setTitle("Inception");
        movie.setReleaseDate("2010-07-16");
        movie.setOverview("Dream heist thriller");
        movie.setPosterPath("/poster.jpg");
        movie.setBackdropPath("/backdrop.jpg");
        movie.setGenreIds(List.of(28, 878)); // Action, Sci-Fi

        TmdbMovieResponseDTO dto = new TmdbMovieResponseDTO();
        dto.setResults(List.of(movie));

        when(restTemplate.exchange(any(URI.class), eq(HttpMethod.GET), any(), eq(TmdbMovieResponseDTO.class)))
                .thenReturn(new ResponseEntity<>(dto, HttpStatus.OK));

        var result = dataService.getMovieMediaItems().getBody();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("Inception");
        assertThat(result.get(0).getGenres()).containsExactly("28", "878");

        verify(repository).saveAll(result);
    }
}
