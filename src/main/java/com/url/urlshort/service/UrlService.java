package com.url.urlshort.service;

import com.url.urlshort.config.AppProperties;
import com.url.urlshort.domain.ShortUrl;
import com.url.urlshort.dto.ShortenRequestDto;
import com.url.urlshort.dto.ShortenResponseDto;
import com.url.urlshort.exception.GoneException;
import com.url.urlshort.exception.NotFoundException;
import com.url.urlshort.repository.ShortUrlRepository;
import com.url.urlshort.util.CodeGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.time.Duration;
import java.time.Instant;

@Service
@Transactional
public class UrlService {
    private static final int DEFAULT_CODE_LENGTH = 7;

    private final ShortUrlRepository repository;
    private final AppProperties properties;

    public UrlService(ShortUrlRepository repository, AppProperties properties) {
        this.repository = repository;
        this.properties = properties;
    }

    //  단축 url 생성
    public ShortenResponseDto create(ShortenRequestDto requestDto) {
        String normalized = CodeGenerator.normalizeUrl(requestDto.url());

        //만료 시각 계산
        Instant expiresAt = null;
        if (requestDto.expiresInDays() != null && requestDto.expiresInDays() > 0) {
            expiresAt = Instant.now().plus(Duration.ofDays(requestDto.expiresInDays()));
        }

        //code 생성 ㄷㄱㅈ
        String code;
        if (requestDto.alias() != null && !requestDto.alias().isBlank()) {
            if (!CodeGenerator.validAlias(requestDto.alias())) {
                throw new IllegalArgumentException("Alias는 정규식에 맞춰야한다");
            }
            code = requestDto.alias().trim();

            //이미 중복되는 코드 존재시
            if (repository.existsByCode(code)) {
                throw new IllegalArgumentException("Alias already in use: " + code);
            }
        } //지정하는 코드 없으면 무작위 생성
        else {
            code = generateUniqueCode();
        }

        //엔티티 생성
        ShortUrl so = ShortUrl.builder().code(code).originalUrl(normalized).expiresAt(expiresAt).build();
        so = repository.save(so);

        return new ShortenResponseDto(
                so.getCode(),
                buildShortUrl(so.getCode()),
                so.getOriginalUrl(),
                so.getCreatedAt(),
                so.getExpiresAt(),
                so.getHitCount()
        );
    }


    //단축 코드 상세
    @Transactional(readOnly = true)
    public ShortUrl getEntity(String code) {
        return repository.findByCode(code).orElseThrow(() -> new NotFoundException("No URL from code: " + code));
    }

    //리다이렉트 대상 url 반환 + 조회수/ 마지막 접근
    public URI resolveAndHit(String code) {
        ShortUrl so = getEntity(code);
        Instant now = Instant.now();
        if (so.isExpired(now)) {
            throw new GoneException("Short URL expired");
        }
        so.accessCount(now);
        return URI.create(so.getOriginalUrl());
    }

    //만료된 url 데이터에서 삭제
    public long delExpired() {
        return repository.deleteByExpiresAtBefore(Instant.now());
    }

    // 무작위 코드 생성
    private String generateUniqueCode() {
        int len = DEFAULT_CODE_LENGTH;
        //7자리 8번, 없으면 8자리 8번
        for (int tr = 0; tr < 2; tr++) {
            for (int i = 0; i < 8; i++) {
                String tmp = CodeGenerator.randomCode(len);
                if (!repository.existsByCode(tmp)) return tmp;
            }
            len++;
        }
        throw new IllegalStateException("Failed to generate unique code");
    }

    private String buildShortUrl(String code) {
        String base = properties.getBaseUrl();
        if (base == null || base.isBlank()) {
            base = "http://localhost:8080";
        }
        return base.endsWith("/") ? base + "r/" + code : base + "/r/" + code;
    }
}
