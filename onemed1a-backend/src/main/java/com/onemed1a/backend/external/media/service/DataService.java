package com.onemed1a.backend.external.media.service;

import com.onemed1a.backend.external.media.responses.books.GoogleBooksResponse;
import com.onemed1a.backend.external.media.responses.books.GoogleBooksResponseDTO;
import com.onemed1a.backend.external.media.responses.books.VolumeInfo;
import com.onemed1a.backend.external.media.responses.movies.TmdbMovieResponse;
import com.onemed1a.backend.external.media.responses.movies.TmdbMovieResponseDTO;
import com.onemed1a.backend.external.media.responses.music.SpotifyAlbum;
import com.onemed1a.backend.external.media.responses.music.SpotifyResponseDTO;

import com.onemed1a.backend.external.media.responses.music.SpotifyTokenResponse;
import com.onemed1a.backend.external.media.responses.tv.TmdbTVResponse;
import com.onemed1a.backend.external.media.responses.tv.TmdbTVResponseDTO;
import com.onemed1a.backend.media.MediaData;
import com.onemed1a.backend.media.MediaDataRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Service responsible for loading external media data (Movies, TV, Music, Books)
 * into the local {@link MediaDataRepository}.
 *
 * <p>Integrates with:
 * - TMDB (Movies & TV)
 * - Spotify (Music)
 * - Google Books (Books)</p>
 *
 */
@Service
public class DataService {

    private final RestTemplate restTemplate;
    private final MediaDataRepository mediaDataRepository;

    // ---- TMDB (Movies & TV) Configuration ----
    @Value("${tmdb.movie.url}")
    private String movieApiUrl;

    @Value("${tmdb.tv.url}")
    private String tvApiUrl;

    @Value("${tmdb.bearer.token}")
    private String tmdbBearerToken;

    // ---- Spotify (Music) Configuration ----
    @Value("${spotify.search.url}")
    private String spotifySearchUrl;

    @Value("${spotify.token.url}")
    private String spotifyTokenUrl;

    @Value("${spotify.client.id}")
    private String spotifyClientId;

    @Value("${spotify.client.secret}")
    private String spotifyClientSecret;

    // ---- Google Books (Books) Configuration ----
    @Value("${google.books.uri}")
    private String googleBooksUri;

    @Value("${google.books.api.key}")
    private String googleBooksApiKey;

    public DataService(MediaDataRepository mediaDataRepository, RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.mediaDataRepository = mediaDataRepository;
    }

    /**
     * Fetches movies from TMDB and stores them in the media repository.
     *
     * @return saved list of {@link MediaData} for movies
     */
    public ResponseEntity<List<MediaData>> getMovieMediaItems() {
        HttpEntity<Void> request = new HttpEntity<>(configureTMDBHeader());
        ResponseEntity<TmdbMovieResponseDTO> response =
                restTemplate.exchange(URI.create(movieApiUrl), HttpMethod.GET, request, TmdbMovieResponseDTO.class);

        TmdbMovieResponseDTO body = response.getBody();
        List<MediaData> mediaDataList = new ArrayList<>();

        if (body != null) {
            for (TmdbMovieResponse movie : body.getResults()) {
                MediaData media = MediaData.builder()
                        .externalMediaId(String.valueOf(movie.getId()))
                        .type(MediaData.MediaType.MOVIE)
                        .title(movie.getTitle())
                        .releaseDate(movie.getReleaseDate())
                        .genres(movie.getGenreIds().stream().map(String::valueOf).toList())
                        .description(movie.getOverview())
                        .posterUrl(movie.getPosterPath())
                        .backdropUrl(movie.getBackdropPath())
                        .build();

                mediaDataList.add(media);
            }
        }

        return ResponseEntity.ok(mediaDataRepository.saveAll(mediaDataList));
    }

    /**
     * Fetches TV shows from TMDB and stores them in the media repository.
     *
     * @return saved list of {@link MediaData} for TV shows
     */
    public ResponseEntity<List<MediaData>> getTvMediaItems() {
        HttpEntity<Void> request = new HttpEntity<>(configureTMDBHeader());
        ResponseEntity<TmdbTVResponseDTO> response =
                restTemplate.exchange(URI.create(tvApiUrl), HttpMethod.GET, request, TmdbTVResponseDTO.class);

        TmdbTVResponseDTO body = response.getBody();
        List<MediaData> mediaDataList = new ArrayList<>();

        if (body != null) {
            for (TmdbTVResponse tv : body.getResults()) {
                MediaData media = MediaData.builder()
                        .externalMediaId(String.valueOf(tv.getId()))
                        .type(MediaData.MediaType.TV)
                        .title(tv.getName())
                        .releaseDate(tv.getFirstAirDate())
                        .genres(tv.getGenreIds().stream().map(String::valueOf).toList())
                        .description(tv.getOverview())
                        .posterUrl(tv.getPosterPath())
                        .backdropUrl(tv.getBackdropPath())
                        .build();

                mediaDataList.add(media);
            }
        }

        return ResponseEntity.ok(mediaDataRepository.saveAll(mediaDataList));
    }

    /**
     * Fetches books from Google Books API and stores them in the media repository.
     *
     * @return saved list of {@link MediaData} for books
     */
    public ResponseEntity<List<MediaData>> getBooksMediaItems() {
        URI uri = UriComponentsBuilder.fromUriString(googleBooksUri)
                .queryParam("q", "Money")
                .queryParam("subject:", "fiction")
                .queryParam("printType", "books")
                .queryParam("key", googleBooksApiKey)
                .build(true) // Preserve reserved characters
                .toUri();

        ResponseEntity<GoogleBooksResponseDTO> resp = restTemplate.exchange(
                uri, HttpMethod.GET, new HttpEntity<>(configureDefaultHeader()), GoogleBooksResponseDTO.class);

        GoogleBooksResponseDTO body = resp.getBody();
        List<MediaData> mediaDataList = new ArrayList<>();

        if (body != null && body.getItems() != null) {
            for (GoogleBooksResponse item : body.getItems()) {
                VolumeInfo v = item.getVolumeInfo();

                MediaData media = MediaData.builder()
                        .externalMediaId(item.getId())
                        .type(MediaData.MediaType.BOOKS)
                        .title(v.getTitle())
                        .releaseDate(v.getPublishedDate())
                        .genres(v.getCategories())
                        .description(v.getDescription())
                        .posterUrl(v.getImageLinks() != null ? v.getImageLinks().getThumbnail() : null)
                        .backdropUrl(null)
                        .build();

                mediaDataList.add(media);
            }
        }

        return ResponseEntity.ok(mediaDataRepository.saveAll(mediaDataList));
    }

    /**
     * Fetches music albums from Spotify and stores them in the media repository.
     *
     * @return saved list of {@link MediaData} for music albums
     */
    public ResponseEntity<List<MediaData>> getMusicMediaItems() {
        URI uri = UriComponentsBuilder.fromUriString(spotifySearchUrl)
                .queryParam("q", "hip%20hop%20year%3A2011-2025")
                .queryParam("type", "album")
                .queryParam("market", "NZ")
                .queryParam("limit", 20)
                .queryParam("offset", 0)
                .build()
                .toUri();

        HttpEntity<Void> request = new HttpEntity<>(configureSpotifyHeader());
        ResponseEntity<SpotifyResponseDTO> resp =
                restTemplate.exchange(uri, HttpMethod.GET, request, SpotifyResponseDTO.class);

        SpotifyResponseDTO body = resp.getBody();
        List<MediaData> mediaDataList = new ArrayList<>();

        if (body != null && body.getAlbums() != null) {
            for (SpotifyAlbum album : body.getAlbums().getItems()) {
                MediaData media = MediaData.builder()
                        .externalMediaId(album.getId())
                        .type(MediaData.MediaType.MUSIC)
                        .title(album.getName())
                        .releaseDate(album.getReleaseDate())
                        .genres(List.of()) // Genres not available in current response
                        .description(null) // Spotify does not return album descriptions
                        .posterUrl(album.getImages().getFirst().getUrl())
                        .backdropUrl(null)
                        .build();

                mediaDataList.add(media);
            }
        }

        return ResponseEntity.ok(mediaDataRepository.saveAll(mediaDataList));
    }

    /**
     * Returns all media data items
     *
     * @return list of {@link MediaData} of all media types
     */
    public List<MediaData> getAllMediaItems() {
        return mediaDataRepository.findAll();
    }

    // -------------------------
    //   Header Configurations
    // -------------------------

    /**
     * Creates HTTP headers for TMDB requests (JSON + Bearer token).
     */
    private HttpHeaders configureTMDBHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(tmdbBearerToken);
        return headers;
    }

    /**
     * Creates default HTTP headers (JSON only).
     */
    private HttpHeaders configureDefaultHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }

    /**
     * Creates HTTP headers for Spotify requests.
     * <p>Performs client credentials flow to fetch an access token.</p>
     */
    private HttpHeaders configureSpotifyHeader() {
        // Build request for Spotify token
        HttpHeaders authHeaders = new HttpHeaders();
        authHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        authHeaders.setBasicAuth(spotifyClientId, spotifyClientSecret);

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "client_credentials");

        ResponseEntity<SpotifyTokenResponse> resp = restTemplate.exchange(
                spotifyTokenUrl, HttpMethod.POST, new HttpEntity<>(form, authHeaders), SpotifyTokenResponse.class);

        SpotifyTokenResponse body = resp.getBody();
        String spotifyToken = body != null ? body.getAccessToken() : "";

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(spotifyToken);
        return headers;
    }


}
