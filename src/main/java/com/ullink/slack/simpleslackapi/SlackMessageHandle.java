package com.ullink.slack.simpleslackapi;

import java.util.concurrent.TimeUnit;
import com.ullink.slack.simpleslackapi.events.SlackReplyEvent;

public interface SlackMessageHandle
{
    // the id given to the message sent
    long getMessageId();

    // server response
    SlackReplyEvent getSlackReply();

    boolean isAcked();

    void waitForReply(long timeout, TimeUnit unit);
}
