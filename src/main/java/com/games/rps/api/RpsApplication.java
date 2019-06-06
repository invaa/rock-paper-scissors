package com.games.rps.api;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableAsync
@EnableSwagger2
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RpsApplication {
    public static void main(String[] args) {
        SpringApplication.run(RpsApplication.class, args);
    }
}
