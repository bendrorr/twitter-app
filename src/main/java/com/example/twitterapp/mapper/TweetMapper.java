package com.example.twitterapp.mapper;

import com.example.twitterapp.dto.TweetDto;
import com.example.twitterapp.entity.Tweet;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TweetMapper {

    public static TweetDto toDto(Tweet tweet) {
        return TweetDto.builder()
                .id(tweet.getId())
                .content(tweet.getContent())
                .author(tweet.getAuthor())
                .createdAt(tweet.getCreatedAt())
                .hashtags(tweet.getHashtags())
                .build();
    }


    public static Tweet toEntity(TweetDto tweetDto) {
        return Tweet.builder()
                .id(UUID.randomUUID().toString())
                .content(tweetDto.getContent())
                .author(tweetDto.getAuthor())
                .createdAt(LocalDateTime.now())
                .hashtags(tweetDto.getHashtags())
                .build();
    }
}
