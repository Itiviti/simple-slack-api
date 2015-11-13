package com.ullink.slack.simpleslackapi;

import com.ullink.slack.simpleslackapi.replies.SlackReply;

import java.util.concurrent.TimeUnit;

public interface SlackMessageHandle<T extends SlackReply>
{
    // the id given to the message sent
    long getMessageId();

    // server response
    T getReply();

    boolean isAcked();

    void waitForReply(long timeout, TimeUnit unit);
}
