package ru.practicum.exploreWithMe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@ComponentScan(basePackages = {"ru.practicum.exploreWithMe", "ru.practicum.exploreWithMe.client"})
public class EwmApp {
    @Bean
    public RestTemplate restTemplate() {
         return new RestTemplate();
    }

    public static void main(String[] args) {
        SpringApplication.run(EwmApp.class, args);
    }
}
