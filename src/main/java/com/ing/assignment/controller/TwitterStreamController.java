package com.ing.assignment.controller;
import com.ing.assignment.service.TweetStreamProcessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;

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

	@Value("${twitter.search.string}")
	private String searchString;

	@GetMapping(path = "read-tweets")
	public void readTweets() throws IOException {
		LOGGER.info("Tweet end point called");
		tweetProcessService.processTweets();
	}
}
