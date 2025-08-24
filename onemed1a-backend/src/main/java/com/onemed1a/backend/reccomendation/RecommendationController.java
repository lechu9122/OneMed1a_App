package com.onemed1a.backend.reccomendation;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.onemed1a.backend.media.MediaData;

@RestController
@RequestMapping("/openai")
public class RecommendationController {
    private final OpenAIService openAIService;

    public RecommendationController(OpenAIService openAIService) {
        this.openAIService = openAIService;
    }

    @PostMapping("/recommendation")
    public List<MediaData> getRecommendation(@RequestBody RecommendationRequest payload) {
        String mediaType = payload.getMediaType();
        String mediaName = payload.getMediaName();
        return openAIService.getRecommendation(mediaType, mediaName);
    }
}
