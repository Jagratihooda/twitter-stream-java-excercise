package com.ing.assignment.service;

import com.google.api.client.http.*;
import com.google.api.client.json.Json;
import com.google.api.client.testing.http.MockHttpTransport;
import com.google.api.client.testing.http.MockLowLevelHttpRequest;
import com.google.api.client.testing.http.MockLowLevelHttpResponse;
import com.ing.assignment.configuration.TwitterStreamConfiguration;
import com.ing.assignment.exception.TwitterServiceException;
import com.ing.assignment.model.Tweet;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.mockito.Mock;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TweetStreamProcessServiceTest {
    @InjectMocks
    private TweetStreamProcessServiceImpl service;

    @Mock
    private TwitterStreamConfiguration config;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(service, "searchParam", "test");
    }

    @Test
    public void processTweetsTest() throws IOException, TwitterServiceException {
        when(config.getHttpRequestFactory()).thenReturn(getMockedRequestFactory());

        List<Tweet> tweetList = service.processTweets();

        //Checking the tweet list size
        assertEquals(3, tweetList.size());

        //Checking the tweet Sorting and grouping
        assertEquals("678", tweetList.get(0).getMessageId());
        assertEquals("345", tweetList.get(1).getMessageId());
        assertEquals("123", tweetList.get(2).getMessageId());

        assertEquals("8910", tweetList.get(0).getAuthor().getUserId());
        assertEquals("8910", tweetList.get(1).getAuthor().getUserId());
        assertEquals("123456", tweetList.get(2).getAuthor().getUserId());


        assertEquals("Test Text 3", tweetList.get(0).getMessageText());
        assertEquals("Test Text 2", tweetList.get(1).getMessageText());
        assertEquals("Test Text 1", tweetList.get(2).getMessageText());


        assertTrue(tweetList.get(0).getCreationDate().before(tweetList.get(1).getCreationDate()));
        assertTrue(tweetList.get(1).getCreationDate().before(tweetList.get(2).getCreationDate()));
        assertTrue(tweetList.get(0).getAuthor().getCreationDate()
                .equals(tweetList.get(1).getAuthor().getCreationDate()));
    }

    @Test(expected = TwitterServiceException.class)
    public void fetchTweetListExceptionTest()
            throws TwitterServiceException, IOException {
        HttpTransport transport = new MockHttpTransport() {
            @Override
            public LowLevelHttpRequest buildRequest(String method, String url) {
                return new MockLowLevelHttpRequest() {
                    @Override
                    public LowLevelHttpResponse execute() throws IOException {
                        throw new IOException("IO exception Exception Occurred");
                    }
                };
            }
        };
        HttpRequestFactory reqFactory = transport.createRequestFactory();
        when(config.getHttpRequestFactory()).thenReturn(reqFactory);
        service.processTweets();

    }

    private HttpRequestFactory getMockedRequestFactory() {

        HttpTransport transport = new MockHttpTransport() {
            @Override
            public LowLevelHttpRequest buildRequest(String method, String url) {
                return new MockLowLevelHttpRequest() {
                    @Override
                    public LowLevelHttpResponse execute() {
                        MockLowLevelHttpResponse response = new MockLowLevelHttpResponse();
                        response.addHeader("custom_header", "value");
                        response.setStatusCode(200);
                        response.setContentType(Json.MEDIA_TYPE);
                        response.setContent(
                                "{\"id\":\"123\",\"created_at\":\"Wed Sep 30 05:19:26 +0000 2020\",\"text\":\"Test Text 1\",\"user\":{\"id\":\"123456\",\"created_at\":\"Wed Sep 30 01:19:26 +0000 2020\"}}\n" +
                                        "{\"id\":\"345\",\"created_at\":\"Tue Sep 29 15:19:26 +0000 2020\",\"text\":\"Test Text 2\",\"user\":{\"id\":\"8910\",\"created_at\":\"Mon Sep 28 09:19:26 +0000 2020\"}}\n" +
                                        "{\"id\":\"678\",\"created_at\":\"Tue Sep 29 05:20:26 +0000 2020\",\"text\":\"Test Text 3\",\"user\":{\"id\":\"8910\",\"created_at\":\"Mon Sep 28 09:19:26 +0000 2020\"}}");

                        return response;
                    }
                };
            }
        };
        return transport.createRequestFactory();
    }

}

