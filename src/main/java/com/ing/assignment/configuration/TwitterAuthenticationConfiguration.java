package com.ing.assignment.configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TwitterAuthenticationConfiguration {
    @Value("${auth.consumer.key}")
    private String consumerKey;
    @Value("${auth.consumer.secret}")
    private String consumerSecret;

    @Bean
    public TwitterAuthenticator getTwitterConfig() {
        return new TwitterAuthenticator(consumerKey, consumerSecret);
    }

}
