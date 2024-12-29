package com.example.twitterapp.controller;

import com.example.twitterapp.model.Tweet;
import com.example.twitterapp.service.TweetService;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/tweets")
@RequiredArgsConstructor
public class TweetController {
    private final TweetService tweetService;

    @GetMapping
    public ResponseEntity<List<Tweet>> getAllTweets() {
        return new ResponseEntity<>(tweetService.getAllTweets(), OK);
    }

    @PostMapping
    public ResponseEntity<Tweet> createTweet(@RequestBody Tweet tweet) {
        return new ResponseEntity<>(tweetService.createTweet(tweet), CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTweet(@PathVariable String id) {
        tweetService.deleteTweet(id);
        return new ResponseEntity<>(NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tweet> updateTweet(@PathVariable String id, @RequestBody String newContent) {
        return new ResponseEntity<>(tweetService.updateTweet(id, newContent), OK);

    }

    @GetMapping("/top-authors")
    public ResponseEntity<List<Document>> getTopAuthors(@RequestParam(defaultValue = "5") Integer limit) {
        return new ResponseEntity<>(tweetService.getTopAuthors(limit), OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Tweet>> searchTweets(@RequestParam String keyword) {
        return new ResponseEntity<>(tweetService.searchTweetsByKeyword(keyword), OK);
    }

    @GetMapping("/after-date")
    public ResponseEntity<List<Tweet>> getTweetsAfterDate(@RequestParam String date) {
        LocalDateTime dateTime = LocalDateTime.parse(date);
        return new ResponseEntity<>(tweetService.getTweetsAfterDate(dateTime), OK);
    }

    @GetMapping("/count-by-author")
    public ResponseEntity<List<Document>> countTweetsPerAuthor() {
        return new ResponseEntity<>(tweetService.countTweetsPerAuthor(), OK);
    }

    @GetMapping("/search-by-hashtag")
    public ResponseEntity<List<Tweet>> searchByHashtag(@RequestParam String hashtag) {
        return new ResponseEntity<>(tweetService.searchTweetsByHashtag(hashtag), OK);
    }

    @GetMapping("/popular-hashtags")
    public ResponseEntity<List<Document>> getPopularHashtags() {
        return new ResponseEntity<>(tweetService.getMostPopularHashtags(), OK);
    }
}
