package com.onemed1a.backend.media.recommendation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onemed1a.backend.media.MediaDataRepository;
import com.onemed1a.backend.reccomendation.OpenAIService;
import com.onemed1a.backend.reccomendation.RecommendationController;
import com.onemed1a.backend.reccomendation.RecommendationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest 
class RecommendationControllerTest {

    private MediaDataRepository mediaDataRepository;
    private OpenAIService openAIService;
    private RecommendationController controller;
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${open.api.key}") // API key in application.properties
    private String apiKey;

    @BeforeEach
    void setup() {
        // Mock repository
        mediaDataRepository = mock(MediaDataRepository.class);
        when(mediaDataRepository.findByTitleAndType(anyString(), any()))
                .thenAnswer(invocation -> Optional.empty());
        when(mediaDataRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Real service with mocked repository
        openAIService = new OpenAIService(apiKey, mediaDataRepository);

        // Controller with real service
        controller = new RecommendationController(openAIService);

        // Setup MockMvc
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void testGetRecommendationEndpoint_realApi() throws Exception {
        RecommendationRequest request = new RecommendationRequest();
        request.setMediaType("MOVIE");
        request.setMediaName("Inception");

        mockMvc.perform(post("/openai/recommendation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").exists())
                .andExpect(jsonPath("$[0].description").exists())
                .andExpect(jsonPath("$[0].genres").exists())
                .andExpect(jsonPath("$[0].releaseDate").exists())
                .andExpect(jsonPath("$[0].type").value("MOVIE"));
    }
}
