package com.ing.assignment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Jagrati
 */
@SpringBootApplication
public class TwitterStreamApplication {

    public static void main(String[] args) {
        SpringApplication.run(TwitterStreamApplication.class, args);
    }
}
