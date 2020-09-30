package com.ing.assignment.constant;

public final class TwitterStreamConstants {

    private TwitterStreamConstants(){
       /*Utility class should not have public constructor*/
    }

    public static final String AUTHORIZATION_URL = "https://api.twitter.com/oauth/authorize";
    public static final String ACCESS_TOKEN_URL = "https://api.twitter.com/oauth/access_token";
    public static final String REQUEST_TOKEN_URL = "https://api.twitter.com/oauth/request_token";
    public static final String ENDPOINT_TWITTER = "https://stream.twitter.com/1.1/statuses/filter.json?track=";
}
