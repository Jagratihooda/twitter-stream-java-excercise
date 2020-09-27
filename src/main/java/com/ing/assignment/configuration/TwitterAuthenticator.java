package com.ing.assignment.configuration;

import com.google.api.client.auth.oauth.*;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.ing.assignment.exception.TwitterAuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Scanner;



public class TwitterAuthenticator {

    private static final Logger logger = LoggerFactory.getLogger(TwitterAuthenticator.class);

    private final String consumerKey;
    private final String consumerSecret;
    private HttpRequestFactory factory;

    private static final HttpTransport TRANSPORT = new NetHttpTransport();
    private static final String AUTHORIZE_URL = "https://api.twitter.com/oauth/authorize";
    private static final String ACCESS_TOKEN_URL = "https://api.twitter.com/oauth/access_token";
    private static final String REQUEST_TOKEN_URL = "https://api.twitter.com/oauth/request_token";


    public TwitterAuthenticator(final String consumerKey, final String consumerSecret) {
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
    }

    public synchronized HttpRequestFactory getAuthorizedHttpRequestFactory() throws TwitterAuthenticationException {
        if (factory != null) {
            return factory;
        }
        factory = createRequestFactory();
        return factory;
    }


    private HttpRequestFactory createRequestFactory() throws TwitterAuthenticationException {
        OAuthHmacSigner signer = new OAuthHmacSigner();
        signer.clientSharedSecret = consumerSecret;

        OAuthCredentialsResponse requestTokenResponse = getTemporaryToken(signer);
        signer.tokenSharedSecret = requestTokenResponse.tokenSecret;


        OAuthAuthorizeTemporaryTokenUrl authorizeUrl = new OAuthAuthorizeTemporaryTokenUrl(AUTHORIZE_URL);
        authorizeUrl.temporaryToken = requestTokenResponse.token;


        String providedPin = retrievePin(authorizeUrl);

        OAuthCredentialsResponse accessTokenResponse = retrieveAccessTokens(providedPin, signer, requestTokenResponse.token);
        signer.tokenSharedSecret = accessTokenResponse.tokenSecret;

        OAuthParameters parameters = new OAuthParameters();
        parameters.consumerKey = consumerKey;
        parameters.token = accessTokenResponse.token;
        parameters.signer = signer;

        return TRANSPORT.createRequestFactory(parameters);
    }


    private OAuthCredentialsResponse getTemporaryToken(final OAuthHmacSigner signer) throws TwitterAuthenticationException {
        OAuthGetTemporaryToken requestToken = new OAuthGetTemporaryToken(REQUEST_TOKEN_URL);
        requestToken.consumerKey = consumerKey;
        requestToken.transport = TRANSPORT;
        requestToken.signer = signer;

        OAuthCredentialsResponse requestTokenResponse;
        try {
            requestTokenResponse = requestToken.execute();
        } catch (IOException e) {
            throw new TwitterAuthenticationException("Unable to aquire temporary token: " + e.getMessage(), e);
        }

        logger.info("Aquired temporary token...");
        return requestTokenResponse;
    }



    private String retrievePin(final OAuthAuthorizeTemporaryTokenUrl authorizeUrl) throws TwitterAuthenticationException {
        String providedPin;
        Scanner scanner = new Scanner(System.in);
        try {
            logger.info("Go to the following link in your browser:\n" + authorizeUrl.build());
            logger.info("\nPlease enter the retrieved PIN:");
            providedPin = scanner.nextLine();
        } finally {
            scanner.close();
        }

        if (providedPin == null) {
            throw new TwitterAuthenticationException("Unable to read entered PIN");
        }

        return providedPin;
    }


    private OAuthCredentialsResponse retrieveAccessTokens(final String providedPin, final OAuthHmacSigner signer, final String token) throws TwitterAuthenticationException {
        OAuthGetAccessToken accessToken = new OAuthGetAccessToken(ACCESS_TOKEN_URL);
        accessToken.verifier = providedPin;
        accessToken.consumerKey = consumerSecret;
        accessToken.signer = signer;
        accessToken.transport = TRANSPORT;
        accessToken.temporaryToken = token;

        OAuthCredentialsResponse accessTokenResponse;
        try {
            accessTokenResponse = accessToken.execute();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new TwitterAuthenticationException("Unable to authorize access: " + e.getMessage(), e);
        }

        logger.info("\nAuthorization was successful");
        return accessTokenResponse;
    }
}
