package com.onemed1a.backend.reccomendation;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.onemed1a.backend.media.MediaData;
import com.onemed1a.backend.media.MediaData.MediaType;
import com.onemed1a.backend.media.MediaDataRepository;
import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;

@Service
public class OpenAIService {

    private final OpenAIClient client;
    private final MediaDataRepository mediaDataRepository;
    public OpenAIService(@Value("${open.api.key}") String apiKey, 
                         MediaDataRepository mediaDataRepository) {
        this.client = OpenAIOkHttpClient.builder()
                .apiKey(apiKey)
                .build();
        this.mediaDataRepository = mediaDataRepository;
    }

    public List<MediaData> getRecommendation(String mediaType, String mediaName) {
        String prompt = String.format("""
                You are a recommendation engine.

                Task:
                Given a media titled "%s", return 5 similar recommendations of type "%s".

                Rules:
                - Each recommendation must include exactly 5 fields in this order:
                Title | Genre | Description | Release Year | Media Type
                - Use the pipe character (`|`) as a separator between fields.
                - Separate recommendations with a newline.
                - Media Type must be one of: MOVIE, TV, MUSIC, BOOKS.
                - Do not include any commentary or formatting.

                Output format (example for 2 items):
                <title1> | <genre1> | <description1> | <release_year1> | <mediaType1>
                <title2> | <genre2> | <description2> | <release_year2> | <mediaType2>
                ...and so on until 5 items.
                """, mediaName, mediaType);

        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                .addUserMessage(prompt)
                .model("gpt-5-nano") 
                .build();

        ChatCompletion response = client.chat().completions().create(params);
        String res = response.choices().get(0).message().content().orElse("No content available");

        List<MediaData> recommendations = new ArrayList<>();
        String[] items = res.split("\\r?\\n"); // split by newline
        for (String item : items) {
                String[] fields = item.split("\\|");
                
                if (fields.length == 5) {
                        String title = fields[0].trim();
                        String genre = fields[1].trim();
                        String description = fields[2].trim();
                        String releaseYear = fields[3].trim();
                        String type = fields[4].trim().toUpperCase();
                        MediaType mediaTypeEnum;
                        try {
                                mediaTypeEnum = MediaType.valueOf(type);
                        } catch (IllegalArgumentException e) {
                                continue; // Skip this item if the type is invalid
                        }
                        // Check if the media already exists in the repository
                        if (mediaDataRepository.findByTitleAndType(title, mediaTypeEnum).isPresent()) {
                                mediaDataRepository.findByTitleAndType(title, mediaTypeEnum).ifPresent(recommendations::add);
                        }else{
                                MediaData newMedia = MediaData.builder()
                                        .externalMediaId(java.util.UUID.randomUUID().toString()) // Generate a random UUID
                                        .title(title)
                                        .type(mediaTypeEnum)
                                        .genres(List.of(genre))
                                        .description(description)
                                        .releaseDate(releaseYear)
                                        .posterUrl("") // Placeholder, can be updated later
                                        .backdropUrl("") // Placeholder, can be updated later
                                        .createdAt(java.time.Instant.now())
                                        .build();
                                mediaDataRepository.save(newMedia);
                                MediaData add = mediaDataRepository.findByTitleAndType(title, mediaTypeEnum)
                                        .orElse(newMedia); // Ensure we get the saved entity
                                recommendations.add(add);
                        }
                }
        }
        return recommendations;

    }
}
