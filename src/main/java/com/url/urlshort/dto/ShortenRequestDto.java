package com.url.urlshort.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

public record ShortenRequestDto (
    @NotBlank @Size(max =2048) @URL String url,
    @Positive Integer expiresInDays,
    @Size(min=3,max = 30) String alias
) {}
