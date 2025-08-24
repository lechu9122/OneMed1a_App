package com.onemed1a.backend.search.controller;

import com.onemed1a.backend.search.dto.SearchRequest;
import com.onemed1a.backend.search.dto.SearchResultItem;
import com.onemed1a.backend.search.dto.SuggestResultItem;
import com.onemed1a.backend.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/media")
public class SearchController {

    private final SearchService service;

    /** Full results with filters; supports ?page=&size=&sort=title,asc */
    @GetMapping("/search")
    public Page<SearchResultItem> search(@ModelAttribute SearchRequest req, Pageable pageable) {
        return service.search(req, pageable);
    }

    /** Lightweight autocomplete (title prefix), default 5 items */
    @GetMapping("/suggest")
    public List<SuggestResultItem> suggest(@RequestParam String q,
                                           @RequestParam(defaultValue = "5") int limit) {
        return service.suggest(q, limit);
    }
}