package com.onemed1a.backend.external.media.service;

import com.onemed1a.backend.external.media.responses.books.GoogleBooksResponse;
import com.onemed1a.backend.external.media.responses.books.GoogleBooksResponseDTO;
import com.onemed1a.backend.external.media.responses.books.VolumeInfo;
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

class DataServiceBookTest {

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
    void shouldFetchAndSaveBooks() {
        // Fake Google Books response
        VolumeInfo volumeInfo = new VolumeInfo();
        volumeInfo.setTitle("Clean Code");
        volumeInfo.setPublishedDate("2008");
        volumeInfo.setCategories(List.of("Programming"));
        volumeInfo.setDescription("A Handbook of Agile Software Craftsmanship");

        GoogleBooksResponse book = new GoogleBooksResponse();
        book.setId("book123");
        book.setVolumeInfo(volumeInfo);

        GoogleBooksResponseDTO dto = new GoogleBooksResponseDTO();
        dto.setItems(List.of(book));

        when(restTemplate.exchange(any(URI.class), eq(HttpMethod.GET), any(), eq(GoogleBooksResponseDTO.class)))
                .thenReturn(new ResponseEntity<>(dto, HttpStatus.OK));

        var result = dataService.getBooksMediaItems().getBody();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("Clean Code");
        assertThat(result.get(0).getGenres()).containsExactly("Programming");

        verify(repository).saveAll(result);
    }
}
