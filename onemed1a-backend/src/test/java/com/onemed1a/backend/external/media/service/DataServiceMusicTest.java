package com.onemed1a.backend.external.media.service;

import com.onemed1a.backend.external.media.responses.music.*;
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

class DataServiceMusicTest {

    private DataService dataService;
    private MediaDataRepository repository;
    private RestTemplate restTemplate;

    @BeforeEach
    void setup() {
        repository = mock(MediaDataRepository.class);
        restTemplate = mock(RestTemplate.class);

        // DataService should have a constructor that accepts (MediaDataRepository, RestTemplate)
        dataService = new DataService(repository, restTemplate);

        // Provide non-null config for @Value fields
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
    void shouldFetchAndSaveMusicAlbums() {

        when(restTemplate.exchange(
                eq("http://fake.example/token"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(SpotifyTokenResponse.class)
        )).thenReturn(new ResponseEntity<>(
                new SpotifyTokenResponse("token_code", "Bearer", 3600),
                HttpStatus.OK
        ));

        SpotifyAlbum album = new SpotifyAlbum();
        album.setId("album1");
        album.setName("Test Album");
        album.setReleaseDate("2023-05-05");

        SpotifyImage img = new SpotifyImage();
        img.setUrl("/album.jpg");
        album.setImages(List.of(img));

        SpotifyAlbumsPage albums = new SpotifyAlbumsPage();
        albums.setItems(List.of(album));

        SpotifyResponseDTO dto = new SpotifyResponseDTO();
        dto.setAlbums(albums);

        when(restTemplate.exchange(
                any(URI.class),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(SpotifyResponseDTO.class)
        )).thenReturn(new ResponseEntity<>(dto, HttpStatus.OK));

        var result = dataService.getMusicMediaItems().getBody();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("Test Album");
        assertThat(result.get(0).getPosterUrl()).isEqualTo("/album.jpg");

        verify(repository).saveAll(result);
    }
}
