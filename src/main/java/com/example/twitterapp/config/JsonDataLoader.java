package com.example.twitterapp.config;

import com.example.twitterapp.model.Tweet;
import com.example.twitterapp.repository.TweetRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;
import java.util.List;

@Configuration
public class JsonDataLoader {

    @Bean
    public CommandLineRunner loadInitialData(TweetRepository tweetRepository, ObjectMapper objectMapper) {
        return args -> {
            if (tweetRepository.count() == 0) {
                InputStream inputStream = getClass().getResourceAsStream("/data.json");
                List<Tweet> persons = objectMapper.readValue(inputStream, new TypeReference<>() {
                });
                tweetRepository.saveAll(persons);
                System.out.println("Data loaded successfully from JSON file.");
            }
        };
    }
}
