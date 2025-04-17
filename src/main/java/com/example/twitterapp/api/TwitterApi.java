package com.example.twitterapp.api;

import com.example.twitterapp.entity.Tweet;
import com.example.twitterapp.util.RestAssuredUtils;
import io.restassured.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class TwitterApi {
    private final String BASE_URL = "http://localhost:8080/api/tweets";
    private final RestAssuredUtils restAssuredUtils;

    public Response getAllTweets() {
        return restAssuredUtils.get(BASE_URL, null, getHeaders());
    }

    public Response createTweet(Tweet tweet) {
        return restAssuredUtils.post(BASE_URL, null, null, getHeaders(), tweet);
    }

    public Response deleteTweet(String id) {
        return restAssuredUtils.delete(BASE_URL + "/" + id, null, getHeaders());

    }

    public Response updateTweet(String id, String newContent) {
        return restAssuredUtils.put(BASE_URL + "/" + id, null, null, getHeaders(), newContent);

    }

    public Response searchTweets(Map<String, ?> params) {
        return restAssuredUtils.get(BASE_URL + "/search", params, getHeaders());
    }

    public Response getPopularHashtags() {
        return restAssuredUtils.get(BASE_URL + "/popular-hashtags", null, getHeaders());
    }

    public Response getTopAuthors(Map<String, ?> params) {
        return restAssuredUtils.get(BASE_URL + "/top-authors", params, getHeaders());
    }

    public Response getTweetsAfterDate(Map<String, ?> params) {
        return restAssuredUtils.get(BASE_URL + "/after-date", params, getHeaders());
    }

    public Response countTweetsPerAuthor() {
        return restAssuredUtils.get(BASE_URL + "/count-by-author", null, getHeaders());
    }

    public Response searchByHashtag(Map<String, ?> params) {
        return restAssuredUtils.get(BASE_URL + "/search-by-hashtag", params, getHeaders());
    }

    public Tweet createTweetBody(String content, String author, LocalDateTime createdAt, List<String> hashtags) {
        return Tweet.builder()
                .content(content)
                .author(author)
                .hashtags(hashtags)
                .createdAt(createdAt)
                .build();

    }

    private Map<String, String> getHeaders() {
        return Map.of(HttpHeaders.CONTENT_TYPE, "application/json");
    }
}
