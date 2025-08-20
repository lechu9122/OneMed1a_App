package com.onemed1a.backend.external.media.controller;

import com.onemed1a.backend.external.media.service.DataService;
import com.onemed1a.backend.media.MediaData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Simple test controller to trigger external media loads (movies, TV, books, music)
 * and to inspect what has been stored locally. Intended for manual verification and debugging.
 *
 * <p><strong>Notes:</strong>
 * <ul>
 *   <li>Endpoints under <code>/api/v1/externalMediaData</code> call out to external APIs via {@link DataService}.</li>
 *   <li>All endpoints log their start/end and number of items affected/returned.</li>
 *   <li>Exceptions are logged and surfaced as HTTP 502 Bad Gateway.</li>
 * </ul>
 * </p>
 */
@RestController
@RequestMapping("/api/v1/externalMediaData")
public class DataController {

    private static final Logger log = LoggerFactory.getLogger(DataController.class);

    private final DataService dataService;

    public DataController(DataService dataService) {
        this.dataService = dataService;
    }

    /**
     * Triggers a fetch of movie items from TMDB and persists them.
     *
     * @return 200 with saved items, or 204 if an error occurred (for quick manual testing)
     */
    @GetMapping("/movies")
    public ResponseEntity<List<MediaData>> getMovieMediaItems() {
        log.info("GET /movies - starting TMDB movies import");
        try {
            ResponseEntity<List<MediaData>> response = dataService.getMovieMediaItems();
            log.info("GET /movies - imported {} items", sizeOf(response.getBody()));
            return response;
        } catch (Exception e) {
            log.error("GET /movies - failed to import movies", e);
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
        }
    }

    /**
     * Triggers a fetch of TV items from TMDB and persists them.
     *
     * @return 200 with saved items, or 204 if an error occurred
     */
    @GetMapping("/tv")
    public ResponseEntity<List<MediaData>> getTvMediaItems() {
        log.info("GET /tv - starting TMDB TV import");
        try {
            ResponseEntity<List<MediaData>> response = dataService.getTvMediaItems();
            log.info("GET /tv - imported {} items", sizeOf(response.getBody()));
            return response;
        } catch (Exception e) {
            log.error("GET /tv - failed to import TV shows", e);
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
        }
    }

    /**
     * Triggers a fetch of book items from Google Books and persists them.
     *
     * @return 200 with saved items, or 204 if an error occurred
     */
    @GetMapping("/books")
    public ResponseEntity<List<MediaData>> getBookMediaItems() {
        log.info("GET /books - starting Google Books import");
        try {
            ResponseEntity<List<MediaData>> response = dataService.getBooksMediaItems();
            log.info("GET /books - imported {} items", sizeOf(response.getBody()));
            return response;
        } catch (Exception e) {
            log.error("GET /books - failed to import books", e);
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
        }
    }

    /**
     * Triggers a fetch of music albums from Spotify and persists them.
     *
     * @return 200 with saved items, or 204 if an error occurred
     */
    @GetMapping("/music")
    public ResponseEntity<List<MediaData>> getMusicMediaItems() {
        log.info("GET /music - starting Spotify albums import");
        try {
            ResponseEntity<List<MediaData>> response = dataService.getMusicMediaItems();
            log.info("GET /music - imported {} items", sizeOf(response.getBody()));
            return response;
        } catch (Exception e) {
            log.error("GET /music - failed to import music", e);
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
        }
    }

    /**
     * Returns all media currently stored in the database (regardless of source).
     *
     * @return 200 with all stored media items
     */
    @GetMapping
    public ResponseEntity<List<MediaData>> getMediaItems() {
        log.info("GET / - fetching all stored media items");
        List<MediaData> all = dataService.getAllMediaItems();
        log.info("GET / - returned {} items", all.size());
        return ResponseEntity.ok(all);
    }

    // -------------------------
    // Helpers
    // -------------------------

    private static int sizeOf(List<?> list) {
        return list == null ? 0 : list.size();
    }
}
