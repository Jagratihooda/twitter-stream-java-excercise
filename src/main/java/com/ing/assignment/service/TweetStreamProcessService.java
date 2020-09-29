package com.ing.assignment.service;
import com.ing.assignment.exception.TwitterServiceException;
import com.ing.assignment.model.Tweet;

import javax.naming.AuthenticationException;
import java.io.IOException;
import java.util.List;

/**
 * @author Jagrati
 * Tweet Retrieve Service
 */
public interface TweetStreamProcessService {

	/**
	 * This method read and stores tweet on a log file
	 */
	List<Tweet>  processTweets() throws TwitterServiceException, IOException, AuthenticationException;

	}
