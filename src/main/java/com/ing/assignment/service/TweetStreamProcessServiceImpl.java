package com.ing.assignment.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.ing.assignment.configuration.TwitterStreamConfiguration;
import com.ing.assignment.constant.TwitterStreamConstants;
import com.ing.assignment.exception.TwitterServiceException;
import com.ing.assignment.model.Tweet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.naming.AuthenticationException;
import java.io.*;
import java.rmi.ServerException;
import java.text.SimpleDateFormat;
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
    private String searchParam;
    private static final String DATE_FORMATTER = "EEE MMM dd HH:mm:ss Z yyyy";
    private static final int MAX_INCOMING_MSG_COUNT = 100;
    private static final long MAX_TIME_INTERVAL = 30000;

    @Autowired
    private TwitterStreamConfiguration twitterStreamConfig;

    /**
     * This method is used to fetch tweets and logs them on a log file.
     * @throws IOException, AuthenticationException
     * @return tweetList
     */
    @Override
    public  List<Tweet> processTweets() throws TwitterServiceException, IOException, AuthenticationException {
        List<Tweet> tweetList = new ArrayList<>();

        for (List<Tweet> usrTweets : getGroupedAndSortedTweets().values()) {
            usrTweets.sort(Comparator.comparing(Tweet::getCreationDate));
                for(Tweet tweet: usrTweets) {
                    LOGGER.info(tweet.toString());
                    tweetList.add(tweet);
                }
        }
        LOGGER.info("Tweet Processing Completed");
        return tweetList;
    }

    /**
     * This method is used to fetch grouped and sorted tweets.
     * @throws AuthenticationException, IOException
     * @return tweetList
     */
    private Map<String, List<Tweet>> getGroupedAndSortedTweets() throws TwitterServiceException, IOException, AuthenticationException {
          return fetchTweetList().stream()
                  .sorted(Comparator.comparing(p -> p.getAuthor().getCreationDate()))
                  .collect(Collectors.groupingBy(p -> p.getAuthor().getUserId()));
    }

    /**
     * This method is used to fetch tweets and logs them on a log file.
     * @throws IOException, AuthenticationException
     * @return tweetList
     */
    private List<Tweet> fetchTweetList() throws IOException, TwitterServiceException, AuthenticationException {
        LOGGER.info("Fetching data from Twitter Api");
        List<Tweet> tweetList = new ArrayList<>();
        HttpRequest request = twitterStreamConfig.getHttpRequestFactory().buildGetRequest(
                new GenericUrl(TwitterStreamConstants.ENDPOINT_TWITTER.concat(searchParam)));

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(request.execute().getContent()))) {
            String line = reader.readLine();
            ObjectMapper mapper = new ObjectMapper();
            mapper.setDateFormat(new SimpleDateFormat(DATE_FORMATTER, Locale.ENGLISH));
            int countTweets = 0;
            long readStartTime = System.currentTimeMillis();
            while (line != null && (System.currentTimeMillis() - readStartTime < MAX_TIME_INTERVAL)
                  && countTweets < MAX_INCOMING_MSG_COUNT) {
                  tweetList.add(mapper.readValue(line, Tweet.class));
                  line = reader.readLine();
                  countTweets++;
            }
        } catch (IOException e) {
            throw new ServerException("Exception Occurred while reading the tweets", e);
        }
        return tweetList;
    }

}
