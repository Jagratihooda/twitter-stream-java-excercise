package com.ing.assignment.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ing.assignment.constant.TweetKeyConstants;

import java.util.Date;

/**
 * @author Jagrati
 * Model class Tweet
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Tweet {

    @JsonProperty(TweetKeyConstants.AUTHOR)
    private Author author;

    @JsonProperty(TweetKeyConstants.TEXT)
    private String messageText;

    @JsonProperty(TweetKeyConstants.MESSAGE_ID)
    private String messageId;

    @JsonProperty(TweetKeyConstants.CREATED_AT)
    private Date creationDate;

    public Author getAuthor() {
        return author;
    }

    public String getMessageText() {
        return messageText;
    }

    public String getMessageId() {
        return messageId;
    }

    public Date getCreationDate() {
        return creationDate;
    }


    @Override
    public String toString(){
        return "Tweet: [[" +
                TweetKeyConstants.AUTHOR + ": " + author.toString() + "," +
                TweetKeyConstants.MESSAGE_ID + ": " + messageId + "," +
                TweetKeyConstants.CREATED_AT + ": " + creationDate + "," +
                TweetKeyConstants.TEXT + ": " + messageText +
                "]]";
    }
}
