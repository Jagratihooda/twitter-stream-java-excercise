package com.ing.assignment.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ing.assignment.constant.TweetKeyConstants;

import java.util.Date;

/**
 * @author Jagrati
 * Model class Author
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Author {

    @JsonProperty(TweetKeyConstants.USER_ID)
    private String userId;

    @JsonProperty(TweetKeyConstants.NAME)
    private String name;

    @JsonProperty(TweetKeyConstants.SCREEN_NAME)
    private String screenName;

    @JsonProperty(TweetKeyConstants.CREATED_AT)
    private Date creationDate;

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getScreenName() {
        return screenName;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    @Override
    public String toString(){
        return "Author: [[" +
                TweetKeyConstants.USER_ID + ":: " + userId + "," +
                TweetKeyConstants.NAME + ":: " + name + "," +
                TweetKeyConstants.SCREEN_NAME + ":: " + screenName + "," +
                TweetKeyConstants.CREATED_AT + ":: " + creationDate +
                "]]";
    }
}
