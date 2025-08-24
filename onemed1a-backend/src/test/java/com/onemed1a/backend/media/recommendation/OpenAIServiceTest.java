package com.onemed1a.backend.media.recommendation;

import com.onemed1a.backend.media.MediaData;
import com.onemed1a.backend.media.MediaDataRepository;
import com.onemed1a.backend.reccomendation.OpenAIService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@SpringBootTest
class OpenAIServiceTest {

    private MediaDataRepository mediaDataRepository;
    private OpenAIService openAIService;

    @Value("${open.api.key}") // make sure your real key is in application.properties
    private String apiKey;

    @BeforeEach
    void setup() {
        mediaDataRepository = mock(MediaDataRepository.class);
        openAIService = new OpenAIService(apiKey, mediaDataRepository);

        // Mock DB repository behavior
        when(mediaDataRepository.findByTitleAndType(anyString(), any()))
                .thenAnswer(invocation -> Optional.empty());
        when(mediaDataRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void testGetRecommendation_realApi_returnsValidList() {
        String mediaName = "Inception";
        String mediaType = "MOVIE";

        List<MediaData> recommendations = openAIService.getRecommendation(mediaType, mediaName);

        assertNotNull(recommendations, "Recommendations should not be null");
        assertFalse(recommendations.isEmpty(), "Recommendations should not be empty");

        // Optional: Check fields are non-empty
        for (MediaData media : recommendations) {
            assertNotNull(media.getTitle());
            assertNotNull(media.getDescription());
            assertNotNull(media.getGenres());
            assertNotNull(media.getReleaseDate());
            assertNotNull(media.getType());
        }
    }
}

