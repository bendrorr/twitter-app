package com.example.twitterapp.controller;

import com.example.twitterapp.dto.TweetDto;
import com.example.twitterapp.service.TweetService;
import jakarta.validation.Valid;
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
    public ResponseEntity<List<TweetDto>> getAllTweets() {
        return new ResponseEntity<>(tweetService.getAllTweets(), OK);
    }

    @PostMapping
    public ResponseEntity<TweetDto> createTweet(@RequestBody @Valid TweetDto tweetDto) {
        return new ResponseEntity<>(tweetService.createTweet(tweetDto), CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTweet(@PathVariable String id) {
        tweetService.deleteTweet(id);
        return new ResponseEntity<>(NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TweetDto> updateTweet(@PathVariable String id, @RequestBody String newContent) {
        return new ResponseEntity<>(tweetService.updateTweet(id, newContent), OK);

    }

    @GetMapping("/top-authors")
    public ResponseEntity<List<Document>> getTopAuthors(@RequestParam(defaultValue = "5") Integer limit) {
        return new ResponseEntity<>(tweetService.getTopAuthors(limit), OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<TweetDto>> searchTweets(@RequestParam String keyword) {
        return new ResponseEntity<>(tweetService.searchTweetsByKeyword(keyword), OK);
    }

    @GetMapping("/after-date")
    public ResponseEntity<List<TweetDto>> getTweetsAfterDate(@RequestParam LocalDateTime date) {
        return new ResponseEntity<>(tweetService.getTweetsAfterDate(date), OK);
    }

    @GetMapping("/count-by-author")
    public ResponseEntity<List<Document>> countTweetsPerAuthor() {
        return new ResponseEntity<>(tweetService.countTweetsPerAuthor(), OK);
    }

    @GetMapping("/search-by-hashtag")
    public ResponseEntity<List<TweetDto>> searchByHashtag(@RequestParam String hashtag) {
        return new ResponseEntity<>(tweetService.searchTweetsByHashtag(hashtag), OK);
    }

    @GetMapping("/popular-hashtags")
    public ResponseEntity<List<Document>> getPopularHashtags() {
        return new ResponseEntity<>(tweetService.getMostPopularHashtags(), OK);
    }
}
