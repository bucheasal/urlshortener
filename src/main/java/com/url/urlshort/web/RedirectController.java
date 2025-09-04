package com.url.urlshort.web;

import com.url.urlshort.service.UrlService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class RedirectController {

    private final UrlService service;

    public RedirectController(UrlService service) {
        this.service = service;
    }

    @GetMapping("/r/{code}")
    public ResponseEntity<Void> redirect(@PathVariable String code) {
        URI target = service.resolveAndHit(code);
        return ResponseEntity.status(HttpStatus.FOUND) // 302
                .location(target)
                .build();
    }
}