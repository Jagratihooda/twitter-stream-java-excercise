package com.ing.assignment.model;
import com.ing.assignment.constant.TwitterStreamConstants;
import lombok.Builder;
import lombok.Data;

/**
 * @author Jagrati
 * Model class Author
 */
@Data
@Builder
public class Author {
    private long id;
    private String created_at;
    private String name;
    private String screen_name;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("\n").append(id).append(TwitterStreamConstants.TWEET_DELIMITER)
                .append(created_at).append(TwitterStreamConstants.TWEET_DELIMITER)
                .append(name).append(TwitterStreamConstants.TWEET_DELIMITER)
                .append(screen_name);
        return builder.toString();
    }
}
