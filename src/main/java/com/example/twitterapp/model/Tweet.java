package com.example.twitterapp.model;

import com.example.twitterapp.util.SharedFunctions;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "tweets")
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class Tweet {
    @Id
    private String id = SharedFunctions.generateCustomId();
    private String content;
    private String author;
    private LocalDateTime createdAt;
    private List<String> hashtags;
}
