package com.url.urlshort.web;

import com.url.urlshort.config.AppProperties;
import com.url.urlshort.domain.ShortUrl;
import com.url.urlshort.dto.ShortenRequestDto;
import com.url.urlshort.dto.ShortenResponseDto;
import com.url.urlshort.service.UrlService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/urls")
public class UrlApiController {

    private final UrlService service;
    private final AppProperties appProperties;

    public UrlApiController(UrlService service, AppProperties appProperties) {
        this.service = service;
        this.appProperties = appProperties;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ShortenResponseDto create(@Valid @RequestBody ShortenRequestDto requestDto) {
        return service.create(requestDto);
    }

    @GetMapping("/{code}")
    public ShortenResponseDto get(@PathVariable String code) {
        ShortUrl so = service.getEntity(code);
        return new ShortenResponseDto(
                so.getCode(), shortUrlFor(so.getCode())
                , so.getOriginalUrl()
                , so.getCreatedAt()
                , so.getExpiresAt()
                , so.getHitCount()
        );
    }

    private String shortUrlFor(String code) {
        String base = appProperties.getBaseUrl();
        if (base == null || base.isBlank()) {
            base ="http://localhost:8080";
        }
        return base.endsWith("/") ? base + "r/" + code : base + "/r/" + code;
    }
}
