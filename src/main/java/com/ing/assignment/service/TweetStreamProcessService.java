package com.ing.assignment.service;
import java.io.IOException;

/**
 * @author Jagrati
 * Tweet Retrieve Service
 */
public interface TweetStreamProcessService {

	/**
	 * This method read and stores tweet on a log file
	 */
	void processTweets() throws IOException;


	}
