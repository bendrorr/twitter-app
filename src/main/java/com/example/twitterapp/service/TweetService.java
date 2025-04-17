package com.example.twitterapp.service;

import com.example.twitterapp.dto.TweetDto;
import com.example.twitterapp.entity.Tweet;
import org.bson.Document;

import java.time.LocalDateTime;
import java.util.List;

public interface TweetService {
    List<TweetDto> getAllTweets();

    TweetDto createTweet(TweetDto tweetDto);

    void deleteTweet(String id);

    TweetDto updateTweet(String id, String newContent);

    List<Document> getTopAuthors(Integer limit);

    List<TweetDto> searchTweetsByKeyword(String keyword);

    List<TweetDto> getTweetsAfterDate(LocalDateTime dateTime);

    List<Document> countTweetsPerAuthor();

    List<TweetDto> searchTweetsByHashtag(String hashtag);

    List<Document> getMostPopularHashtags();


}
