package com.example.twitterapp.service;

import com.example.twitterapp.model.Tweet;
import org.bson.Document;

import java.time.LocalDateTime;
import java.util.List;

public interface TweetService {
    List<Tweet> getAllTweets();

    Tweet createTweet(Tweet tweet);

    void deleteTweet(String id);

    Tweet updateTweet(String id, String newContent);

    List<Document> getTopAuthors(Integer limit);

    List<Tweet> searchTweetsByKeyword(String keyword);

    List<Tweet> getTweetsAfterDate(LocalDateTime dateTime);

    List<Document> countTweetsPerAuthor();

    List<Tweet> searchTweetsByHashtag(String hashtag);

    List<Document> getMostPopularHashtags();


}
