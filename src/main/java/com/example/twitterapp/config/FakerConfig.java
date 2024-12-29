package com.example.twitterapp.config;

import com.github.javafaker.Faker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class FakerConfig {
    @Lazy
    @Bean
    public Faker faker() {
        return Faker.instance();
    }
}
