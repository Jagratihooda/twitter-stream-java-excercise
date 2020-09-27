package com.ing.assignment.model;
import com.ing.assignment.constant.TwitterStreamConstants;
import lombok.Builder;
import lombok.Data;
import java.util.Date;

/**
 * @author Jagrati
 * Model class Tweet
 */
@Data
@Builder
public class Tweet {
    private long id;
    private String created_at;
    private String text;
    private Author user;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("\n")
        .append(user.toString()).append(TwitterStreamConstants.AUTHOR_TWEET_DELIMITER)
        .append(id).append(TwitterStreamConstants.TWEET_DELIMITER).
                append(created_at).append(TwitterStreamConstants.TWEET_DELIMITER)
                .append(text);
        return builder.toString();
    }
}
