package com.url.urlshort;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling //자동으로 만료된 데이터 삭제
@SpringBootApplication
public class UrlshortApplication {
	public static void main(String[] args) {
		SpringApplication.run(UrlshortApplication.class, args);
	}

}
