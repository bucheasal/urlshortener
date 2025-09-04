package com.url.urlshort.dto;

import java.time.Instant;


public record ShortenResponseDto (
        String code,
        String shortUrl,
        String originalUrl,
        Instant createdAt,
        Instant expiresAt,
        long hitCount
) {}
