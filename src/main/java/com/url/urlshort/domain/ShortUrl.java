package com.url.urlshort.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Getter
@Table(name = "short_urls", uniqueConstraints = {
        @UniqueConstraint(name = "uk_short_code", columnNames = "code")})
@NoArgsConstructor
public class ShortUrl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //코드 저장 필드
    @Column(length = 64, nullable = false)
    private String code;

    //원본
    @Column(name = "originalUrl", length = 2048, nullable = false)
    private String originalUrl;

    //생성 시각
    @Column(name = "createdAt", nullable = false, updatable = false)
    private Instant createdAt;

    //유효 기간 만료 시각
    @Column(name = "expiresAt")
    private Instant expiresAt;
    //클릭수
    @Column(name = "hitCount", nullable = false)
    private Long hitCount = 0L;

    //마지막 접근
    @Column(name = "lastAccessedAt")
    private Instant lastAccessedAt;

    @Builder
    public ShortUrl(String code, String originalUrl, Instant expiresAt) {
        if(code==null||code.isBlank()){
            throw new IllegalArgumentException("코드가 필요합니다");
        }
        if (originalUrl == null || originalUrl.isBlank()) {
            throw new IllegalArgumentException("원본이 필요합니다");
        }
        this.code = code.trim();
        this.originalUrl = originalUrl.trim();
        this.expiresAt = expiresAt;
    }

    //insert 직전
    @PrePersist
    void prePersist(){
        if (createdAt == null) {
            createdAt = Instant.now();
        }
    }

    public void accessCount(Instant now) {
        this.hitCount++;
        this.lastAccessedAt = now;
    }

    public boolean isExpired(Instant now) {
        return expiresAt != null && expiresAt.isBefore(now);
    }
}
