package com.ing.assignment.controller;
import com.ing.assignment.exception.TwitterAuthenticationException;
import com.ing.assignment.exception.TwitterServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Jagrati
 * The rest controller advice
 */
@ControllerAdvice(assignableTypes = TwitterStreamController.class)
public class TweetStreamControllerAdvice {

	@ExceptionHandler(TwitterServiceException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody
	public String serviceExceptionHandler(final TwitterServiceException ex) {
		return "{ \"message\" : \"" + ex.getMessage() + "\" }";
	}

	@ExceptionHandler(TwitterAuthenticationException.class)
	@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
	@ResponseBody
	public String authenticationExceptionHandler(final TwitterAuthenticationException ex) {
		return "{ \"message\" : \"" + ex.getMessage() + "\" }";
	}
}
