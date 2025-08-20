package com.onemed1a.backend.external.media.service;

import com.onemed1a.backend.external.media.responses.tv.TmdbTVResponse;
import com.onemed1a.backend.external.media.responses.tv.TmdbTVResponseDTO;
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

class DataServiceTvTest {

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
    void shouldFetchAndSaveTvShows() {
        // Fake TMDB TV response
        TmdbTVResponse tv = new TmdbTVResponse();
        tv.setId(101);
        tv.setName("Breaking Bad");
        tv.setFirstAirDate("2008-01-20");
        tv.setOverview("A chemistry teacher turns to crime.");
        tv.setPosterPath("/poster.jpg");
        tv.setBackdropPath("/backdrop.jpg");
        tv.setGenreIds(List.of(18, 80)); // Drama, Crime

        TmdbTVResponseDTO dto = new TmdbTVResponseDTO();
        dto.setResults(List.of(tv));

        when(restTemplate.exchange(any(URI.class), eq(HttpMethod.GET), any(), eq(TmdbTVResponseDTO.class)))
                .thenReturn(new ResponseEntity<>(dto, HttpStatus.OK));

        var result = dataService.getTvMediaItems().getBody();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("Breaking Bad");
        assertThat(result.get(0).getGenres()).containsExactly("18", "80");

        verify(repository).saveAll(result);
    }
}
