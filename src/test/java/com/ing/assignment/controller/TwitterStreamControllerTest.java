package com.ing.assignment.controller;

import com.ing.assignment.model.Tweet;
import com.ing.assignment.service.TweetStreamProcessService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;

import org.mockito.junit.MockitoJUnitRunner;
import javax.naming.AuthenticationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class TwitterStreamControllerTest {
    @InjectMocks
    private TwitterStreamController twitterStreamController;
    @Mock
    private TweetStreamProcessService tweetStreamProcessService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void readTweetsTest() throws IOException, AuthenticationException {
        List<Tweet> tweetList = new ArrayList<>();
        Mockito.when(tweetStreamProcessService.processTweets()).thenReturn((tweetList));
        assertEquals(tweetList, twitterStreamController.readTweets());

    }

}

