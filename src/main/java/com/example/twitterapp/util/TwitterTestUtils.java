package com.example.twitterapp.util;

import com.example.twitterapp.api.TwitterApi;
import com.example.twitterapp.entity.Tweet;
import com.example.twitterapp.repository.TweetRepository;
import com.fasterxml.jackson.databind.JsonNode;
import io.restassured.response.Response;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.CREATED;

@Component
@RequiredArgsConstructor
public class TwitterTestUtils {

    private final TwitterApi twitterApi;
    private final TweetRepository tweetRepository;
    private final JsonUtil jsonUtil;

    public Tweet createAndFetchTweet() {
        Tweet tweetBodyRequest = twitterApi.createTweetBody(
                jsonUtil.createContent(),
                jsonUtil.createAuthor(),
                jsonUtil.createLocalDateTime(),
                jsonUtil.createHashtags());

        return twitterApi.createTweet(tweetBodyRequest)
                .then()
                .statusCode(CREATED.value())
                .extract()
                .response()
                .jsonPath()
                .getObject("$", Tweet.class);
    }

    public Tweet findTweetInDatabase(String tweetId) {
        return tweetRepository.findById(tweetId)
                .orElseThrow(() -> new AssertionError("Tweet not found in the database with ID: " + tweetId));
    }


    public Map<String, Long> getPopularHashtagsFromResponse(Response popularHashtagsResponse) {
        return popularHashtagsResponse.jsonPath().getList("$", Document.class)
                .stream()
                .collect(Collectors.toMap(
                        doc -> doc.getString("_id"),
                        doc -> Long.valueOf(doc.getInteger("count"))
                ));
    }


    public Boolean isSortedInDescendingOrder(List<JsonNode> list, String key) {
        for (int i = 1; i < list.size(); i++) {
            Integer currentCount = list.get(i - 1).get(key).asInt();
            Integer nextCount = list.get(i).get(key).asInt();
            if (currentCount < nextCount) {
                return false;
            }
        }
        return true;
    }

    public Map<String, Long> getPopularHashtagsFromDatabase(Integer limit) {
        return sortAndLimitHashtags(limit, getHashtagCounts());
    }

    private Map<String, Long> getHashtagCounts() {
        return tweetRepository.findAll().stream()
                .flatMap(tweet -> tweet.getHashtags().stream())
                .collect(Collectors.groupingBy(
                        hashtag -> hashtag,
                        Collectors.counting())
                );
    }

    private Map<String, Long> sortAndLimitHashtags(Integer limit, Map<String, Long> hashtagCounts) {
        return hashtagCounts.entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry<String, Long>::getValue)
                        .reversed()
                        .thenComparing(Map.Entry::getKey))
                .limit(limit)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }


}
