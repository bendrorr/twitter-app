package com.example.twitterapp.service.impl;

import com.example.twitterapp.model.Tweet;
import com.example.twitterapp.repository.TweetRepository;
import com.example.twitterapp.service.TweetService;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TweetServiceImpl implements TweetService {
    private final TweetRepository tweetRepository;
    private final MongoTemplate mongoTemplate;


    @Override
    public List<Tweet> getAllTweets() {
        return tweetRepository.findAll();
    }

    @Override
    public Tweet createTweet(Tweet tweet) {
        return tweetRepository.save(tweet);
    }

    @Override
    public void deleteTweet(String id) {
        tweetRepository.deleteById(id);
    }

    @Override
    public Tweet updateTweet(String id, String newContent) {
        Tweet existingTweet = tweetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tweet not found with id: " + id));
        existingTweet.setContent(newContent);
        return tweetRepository.save(existingTweet);
    }

    @Override
    public List<Document> getTopAuthors(Integer limit) {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.group("author").count().as("tweetCount"),
                Aggregation.sort(Sort.by(Sort.Order.desc("tweetCount"), Sort.Order.asc("_id"))),
                Aggregation.limit(limit)
        );
        return mongoTemplate.aggregate(aggregation, "tweets", Document.class).getMappedResults();
    }

    @Override
    public List<Tweet> searchTweetsByKeyword(String keyword) {
        Query query = new Query()
                .addCriteria(Criteria.where("content").regex(keyword, "i"));
        return mongoTemplate.find(query, Tweet.class);
    }

    @Override
    public List<Tweet> getTweetsAfterDate(LocalDateTime dateTime) {
        Query query = new Query()
                .addCriteria(Criteria.where("createdAt").gte(dateTime));
        return mongoTemplate.find(query, Tweet.class);
    }

    @Override
    public List<Document> countTweetsPerAuthor() {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.group("author").count().as("tweetCount"),
                Aggregation.project("tweetCount").and("author").previousOperation()
        );
        return mongoTemplate.aggregate(aggregation, "tweets", Document.class).getMappedResults();
    }

    @Override
    public List<Tweet> searchTweetsByHashtag(String hashtag) {
        Query query = new Query()
                .addCriteria(Criteria.where("hashtags").is(hashtag));
        return mongoTemplate.find(query, Tweet.class);
    }

    @Override
    public List<Document> getMostPopularHashtags() {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.unwind("hashtags"),
                Aggregation.group("hashtags").count().as("count"),
                Aggregation.sort(Sort.by(Sort.Order.desc("count"), Sort.Order.asc("_id"))),
                Aggregation.limit(10)

        );
        return mongoTemplate.aggregate(aggregation, "tweets", Document.class).getMappedResults();
    }


}
