package com.example.twitterapp;

import com.example.twitterapp.api.TwitterApi;
import com.example.twitterapp.config.AppConfig;
import com.example.twitterapp.model.Tweet;
import com.example.twitterapp.repository.TweetRepository;
import com.example.twitterapp.util.JsonUtil;
import com.example.twitterapp.util.NullUtil;
import com.example.twitterapp.util.TwitterTestUtils;
import com.fasterxml.jackson.databind.JsonNode;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@SpringBootTest
@ContextConfiguration(classes = AppConfig.class)
class TwitterAppApplicationTests {
    private final TwitterApi twitterApi;
    private final TweetRepository tweetRepository;
    private final JsonUtil jsonUtil;
    private final TwitterTestUtils twitterTestUtils;

    @Autowired
    TwitterAppApplicationTests(TwitterApi twitterApi, TweetRepository tweetRepository, JsonUtil jsonUtil, TwitterTestUtils twitterTestUtils) {
        this.twitterApi = twitterApi;
        this.tweetRepository = tweetRepository;
        this.jsonUtil = jsonUtil;
        this.twitterTestUtils = twitterTestUtils;
    }

    @Test
    void should_create_tweet_and_verify_in_database() {
        Tweet createdTweet = twitterTestUtils.createAndFetchTweet();

        Tweet retrieveSavedTweet = twitterTestUtils.findTweetInDatabase(createdTweet.getId());

        assertThat(retrieveSavedTweet)
                .isEqualTo(createdTweet);
    }

    @Test
    void should_delete_tweet_and_verify_in_database() {
        Tweet createdTweet = twitterTestUtils.createAndFetchTweet();

        Response deleteTweetResponse = twitterApi.deleteTweet(createdTweet.getId())
                .then()
                .statusCode(NO_CONTENT.value())
                .extract()
                .response();

        Optional<Tweet> deletedTweet = tweetRepository.findById(createdTweet.getId());

        assertThat(deletedTweet)
                .isNotPresent();
    }

    @Test
    void should_update_tweet_and_verify_in_database() {
        Tweet createdTweet = twitterTestUtils.createAndFetchTweet();

        String newContent = jsonUtil.createContent();

        Response updateTweetResponse = twitterApi.updateTweet(createdTweet.getId(), newContent)
                .then()
                .statusCode(OK.value())
                .body("content", equalTo(newContent))
                .extract()
                .response();

        Tweet updatedTweet = twitterTestUtils.findTweetInDatabase(createdTweet.getId());

        assertThat(updatedTweet.getContent())
                .isEqualTo(newContent);
    }

    @Test
    void should_get_all_tweets_and_verify_in_database() {
        Response allTweetsResponse = twitterApi.getAllTweets()
                .then()
                .statusCode(OK.value())
                .extract()
                .response();

        List<Tweet> allTweetsExpected = tweetRepository.findAll();
        List<Tweet> allTweetsActual = allTweetsResponse.jsonPath().getList("$", Tweet.class);

        assertThat(allTweetsActual)
                .hasSameSizeAs(allTweetsExpected)
                .containsExactlyInAnyOrderElementsOf(allTweetsExpected);
    }

    @Test
    void should_search_tweets_by_keyword_and_verify() {
        Tweet createdTweet = twitterTestUtils.createAndFetchTweet();
        String keyword = NullUtil.getNonNull(() -> createdTweet.getContent().split(" ")[0])
                .orElseThrow();

        Map<String, String> params = Map.of("keyword", keyword);

        Response searchedTweets = twitterApi.searchTweets(params)
                .then()
                .statusCode(OK.value())
                .extract()
                .response();

        List<Tweet> tweets = searchedTweets.jsonPath().getList("$", Tweet.class);

        assertThat(tweets)
                .allMatch(tweet -> tweet.getContent().toLowerCase().contains(keyword.toLowerCase()),
                        "Expected all tweets to contain the keyword: " + keyword);

    }

    @ParameterizedTest(name = "should_retrieve_most_popular_hashtags_by_limit_{0}")
    @ValueSource(ints = 10)
    void should_retrieve_most_popular_hashtags_and_verify(Integer limit) {
        Response popularHashtagsResponse = twitterApi.getPopularHashtags().then()
                .statusCode(OK.value())
                .extract()
                .response();

        Map<String, Long> popularHashtagsExpected = twitterTestUtils.getPopularHashtagsFromDatabase(limit);

        Map<String, Long> popularHashtagsActual = twitterTestUtils.getPopularHashtagsFromResponse(popularHashtagsResponse);

        assertThat(popularHashtagsActual)
                .hasSameSizeAs(popularHashtagsExpected)
                .isEqualTo(popularHashtagsExpected);
    }

    @ParameterizedTest(name = "should_get_top_authors_and_verify_by_limit_{0}")
    @ValueSource(ints = 5)
    void should_get_top_authors_and_verify(Integer limit) {
        Map<String, Integer> params = Map.of("limit", limit);
        Response getTopAuthorsResponse = twitterApi.getTopAuthors(params)
                .then()
                .statusCode(OK.value())
                .body("every { it.containsKey('_id') }", is(true))
                .body("every { it.containsKey('tweetCount') }", is(true))
                .extract()
                .response();

        List<JsonNode> topAuthors = getTopAuthorsResponse.jsonPath().getList("$", JsonNode.class);

        assertThat(twitterTestUtils.isSortedInDescendingOrder(topAuthors, "tweetCount"))
                .isTrue();

    }

    @Test
    void should_get_tweets_after_or_equal_date_and_verify() {
        String fromDate = LocalDateTime.now()
                .minusYears(1)
                .toString();

        Map<String, String> params = Map.of("date", fromDate);
        Response tweetsAfterDateResponse = twitterApi.getTweetsAfterDate(params)
                .then()
                .statusCode(OK.value())
                .extract()
                .response();

        List<Tweet> tweetsAfterDate = NullUtil.getNonNull(() -> tweetsAfterDateResponse.jsonPath().getList("$", Tweet.class)).orElseThrow();

        assertThat(tweetsAfterDate)
                .allMatch(tweet -> {
                    LocalDateTime tweetDate = tweet.getCreatedAt();
                    LocalDateTime fromDateParsed = LocalDateTime.parse(fromDate);
                    return tweetDate.isAfter(fromDateParsed) || tweetDate.isEqual(fromDateParsed);
                });

    }

    @Test
    void should_get_count_tweets_per_author_and_verify() {
        Response countTweetsPerAuthorResponse = twitterApi.countTweetsPerAuthor()
                .then()
                .statusCode(OK.value())
                .body("every { it.containsKey('author') }", is(true))
                .body("every { it.containsKey('tweetCount') }", is(true))
                .extract()
                .response();

        List<JsonNode> countTweetsPerAuthor = countTweetsPerAuthorResponse.jsonPath().getList("$", JsonNode.class);

        assertThat(countTweetsPerAuthor)
                .allMatch(jsonNode -> jsonNode.get("tweetCount").asInt() >= 0);
    }

    @Test
    void should_search_tweets_by_hashtag_and_verify() {
        Map<String, String> params = Map.of("hashtag", "#Java");

        Response searchByHashtagResponse = twitterApi.searchByHashtag(params)
                .then()
                .statusCode(OK.value())
                .extract()
                .response();

        List<Tweet> searchByHashtagTweets = searchByHashtagResponse.jsonPath().getList("$", Tweet.class);

        assertThat(searchByHashtagTweets)
                .allMatch(tweet -> tweet.getHashtags().contains("#Java"));
    }

}
