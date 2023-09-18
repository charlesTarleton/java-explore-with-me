package ru.practicum.exploreWithMe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("stats-service.stats-client.src.main.java.ru.practicum.exploreWithMe.client")
public class EwmApp {
    public static void main(String[] args) {
        SpringApplication.run(EwmApp.class, args);
    }
}
