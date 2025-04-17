package com.example.twitterapp.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class TweetDto {
    private String id;
    @NotBlank
    private String content;
    @NotBlank
    private String author;
    private LocalDateTime createdAt;
    private List<String> hashtags;
}
