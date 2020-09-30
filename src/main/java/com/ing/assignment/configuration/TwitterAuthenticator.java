package com.ing.assignment.configuration;

import com.google.api.client.auth.oauth.*;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.ing.assignment.constant.TwitterStreamConstants;
import com.ing.assignment.exception.TwitterAuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.AuthenticationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/*
 * This class is used to perform Authorization
 *  while connecting to Twitter
 *
 * */
public class TwitterAuthenticator {

    private static final Logger LOGGER = LoggerFactory.getLogger(TwitterAuthenticator.class);

    private final String consumerKey;
    private final String consumerSecret;
    private HttpRequestFactory requestFactory;

    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

    public TwitterAuthenticator(final String consumerKey, final String consumerSecret) {
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
    }

    /**
     * This method initializes an HTTP request factory and return the authenticated HTTP request factory
     *
     * @return requestFactory
     */
    public HttpRequestFactory getHttpRequestFactory() {
        if (requestFactory != null) {
            return requestFactory;
        }
        requestFactory = retrieveHttpRequestFactory();
        return requestFactory;
    }

    private HttpRequestFactory retrieveHttpRequestFactory() {
        LOGGER.info("creating Request Factory");
        OAuthHmacSigner signer = new OAuthHmacSigner();
        signer.clientSharedSecret = consumerSecret;

        OAuthCredentialsResponse credentialsResponse = retrieveTempToken(signer);
        signer.tokenSharedSecret = credentialsResponse.tokenSecret;

        OAuthAuthorizeTemporaryTokenUrl authTempTokenUrl = new OAuthAuthorizeTemporaryTokenUrl(TwitterStreamConstants.AUTHORIZATION_URL);
        authTempTokenUrl.temporaryToken = credentialsResponse.token;

        OAuthCredentialsResponse accessTokenResponse = getAccessTokenResponse(getProvidedPinByUser(authTempTokenUrl), credentialsResponse.token, signer);
        signer.tokenSharedSecret = accessTokenResponse.tokenSecret;

        OAuthParameters authParams = new OAuthParameters();
        authParams.signer = signer;
        authParams.consumerKey = consumerKey;
        authParams.token = accessTokenResponse.token;
        return HTTP_TRANSPORT.createRequestFactory(authParams);
    }

    private OAuthCredentialsResponse getAccessTokenResponse(final String providedPin, final String token, final OAuthHmacSigner signer) {
        OAuthCredentialsResponse accessTokenResponse;
        OAuthGetAccessToken accessToken = new OAuthGetAccessToken(TwitterStreamConstants.ACCESS_TOKEN_URL);
        accessToken.verifier = providedPin;
        accessToken.consumerKey = consumerSecret;
        accessToken.signer = signer;
        accessToken.transport = HTTP_TRANSPORT;
        accessToken.temporaryToken = token;

        try {
            accessTokenResponse = accessToken.execute();
        } catch (IOException e) {
            throw new TwitterAuthenticationException("Can not retrieve access token: " + e.getMessage(), e);
        }
        LOGGER.info("Authorization is successful");
        return accessTokenResponse;
    }

    private String getProvidedPinByUser(final OAuthAuthorizeTemporaryTokenUrl authorizeUrl) {
        LOGGER.info("Reading the key provided by User");
        String providedPin = null;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            LOGGER.info(String.format("Please click on the url to get the pin:%n  %s", authorizeUrl.build()));
            LOGGER.info("\n Please enter the retrieved PIN:");
            providedPin = br.readLine();
        } catch (IOException e) {
            LOGGER.error("Error occured while reading the pin" + e);
        }
        return providedPin;
    }

    private OAuthCredentialsResponse retrieveTempToken(final OAuthHmacSigner signer) {
        LOGGER.info("Retrieving the temp token");
        OAuthGetTemporaryToken tempToken = new OAuthGetTemporaryToken(TwitterStreamConstants.REQUEST_TOKEN_URL);
        tempToken.consumerKey = consumerKey;
        tempToken.signer = signer;
        tempToken.transport = HTTP_TRANSPORT;

        OAuthCredentialsResponse credentialsResponse;
        try {
            credentialsResponse = tempToken.execute();
        } catch (IOException e) {
            throw new TwitterAuthenticationException("Exception occurred while retrieving the temp key: " + e.getMessage(), e);
        }
        return credentialsResponse;
    }

}
