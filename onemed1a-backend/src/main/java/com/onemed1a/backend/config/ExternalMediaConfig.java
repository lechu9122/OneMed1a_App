package com.onemed1a.backend.config;

import com.onemed1a.backend.external.media.service.DataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Bootstraps external media data on application startup by invoking {@link DataService}
 * loaders. Useful for seeding the local database for manual testing / demo environments.
 *
 */
@Configuration
public class ExternalMediaConfig {

    private static final Logger log = LoggerFactory.getLogger(ExternalMediaConfig.class);

    /**
     * Runs once at startup and triggers imports from TMDB, Spotify, and Google Books.
     * Each step is wrapped so one failure doesn't prevent the others.
     */
    @Bean
    CommandLineRunner preloadMedia(DataService dataService) {
        return args -> {
            log.info("External media bootstrap: starting");

            runSafely("movies", dataService::getMovieMediaItems);
            runSafely("tv", dataService::getTvMediaItems);
            runSafely("music", dataService::getMusicMediaItems);
            runSafely("books", dataService::getBooksMediaItems);

            log.info("External media bootstrap: finished");
        };
    }

    /**
     * Small helper to execute a loader and log outcome without failing the whole startup.
     */
    private void runSafely(String label, Runnable task) {
        try {
            task.run();
            log.info("Loaded external media: {}", label);
        } catch (Exception ex) {
            log.warn("Failed to load external media: {}", label, ex);
        }
    }
}
