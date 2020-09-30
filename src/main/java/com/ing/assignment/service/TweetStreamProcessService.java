package com.ing.assignment.service;
import com.ing.assignment.model.Tweet;

import java.io.IOException;
import java.util.List;

/**
 * @author Jagrati
 * Tweet Retrieve Service
 */
public interface TweetStreamProcessService {

	/**
	 * This method read and stores tweets on a log file
	 */
	List<Tweet> processTweets() throws IOException;

	}
