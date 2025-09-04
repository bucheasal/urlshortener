package com.url.urlshort.repository;

import com.url.urlshort.domain.ShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface ShortUrlRepository extends JpaRepository<ShortUrl, Long> {
    Optional<ShortUrl> findByCode(String code);

    boolean existsByCode(String code);

    long deleteByExpiresAtBefore(Instant now);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query("update ShortUrl s set s.hitCount = s.hitCount +1, s.lastAccessedAt =:now where s.code = :code")
    int increaseHitCount(@Param("code") String code, @Param("now") Instant now);
}
