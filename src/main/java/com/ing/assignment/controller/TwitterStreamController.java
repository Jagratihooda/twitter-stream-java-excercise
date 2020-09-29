package com.ing.assignment.controller;
import com.ing.assignment.model.Tweet;
import com.ing.assignment.service.TweetStreamProcessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.AuthenticationException;
import java.io.IOException;
import java.util.List;

/**
 * @author Jagrati
 *This rest controller is for Reading and Storing Tweets.
 */
@RestController
@RequestMapping("/tweetstreaming/")
public class TwitterStreamController {
	private static final Logger LOGGER = LoggerFactory.getLogger(TwitterStreamController.class);

	@Autowired
	private TweetStreamProcessService tweetProcessService;

	@GetMapping(path = "process-tweets")
	public String  readTweets() throws IOException, AuthenticationException {
		tweetProcessService.processTweets();
		LOGGER.info("Tweets are dumped on log file");
		return "Tweets are processed";
	}
}
