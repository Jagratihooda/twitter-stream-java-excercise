package com.ing.assignment.service;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ing.assignment.configuration.TwitterAuthenticationConfiguration;
import com.ing.assignment.model.Tweet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Jagrati
 * Tweet Retrieve Service Implementation
 */
@Component
public class TweetStreamProcessServiceImpl implements TweetStreamProcessService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TweetStreamProcessServiceImpl.class);

    @Value("${twitter.search.string}")
    private String searchString;

    private static final int MAX_INCOMING_MSG_COUNT = 100;

    /**
     * Max interval of time
     */
    private static final long MAX_TIME_INTERVAL = 30000;

    @Autowired
    private TwitterAuthenticationConfiguration authenticationConfig;

    private static final String ENDPOINT_TWITTER = "https://stream.twitter.com/1.1/statuses/filter.json?track=";

    private static final Gson gson = new GsonBuilder().setLenient().create();

    @Override
    public void processTweets() throws IOException {
        List<Tweet> tweetList = fetchTweetList(authenticationConfig.getTwitterConfig().getAuthorizedHttpRequestFactory());

        Map<String, List<Tweet>> groupedAndSortedTweets = tweetList.stream().
                sorted(Comparator.comparing(Tweet::getCreated_at))
                .collect(Collectors.groupingBy(p -> p.getUser().getName()));

        for (List<Tweet> userTweetList : groupedAndSortedTweets.values()) {
            for(Tweet tweet: userTweetList) {
                LOGGER.info(tweet.toString());
            }
        }

    }
    private List<Tweet> fetchTweetList(HttpRequestFactory httpRequestFactory) throws IOException{
        List<Tweet> tweetList = new ArrayList<>();

        HttpRequest request = httpRequestFactory.buildGetRequest(
                new GenericUrl(ENDPOINT_TWITTER.concat(searchString)));
        HttpResponse response = request.execute();
        InputStream in = response.getContent();

        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line = reader.readLine();

        int countTweets = 0;
        long startTime = System.currentTimeMillis();

        while (line != null && countTweets < MAX_INCOMING_MSG_COUNT &&
                (System.currentTimeMillis() - startTime < MAX_TIME_INTERVAL)) {
            // Parse tweet and add to the list
          tweetList.add(gson.fromJson(line, Tweet.class));

            line = reader.readLine();
            countTweets++;
        }
        return tweetList;
    }

}
