package com.ullink.slack.simpleslackapi;

import java.util.concurrent.TimeUnit;

public interface SlackMessageHandle
{
    // the id given to the message sent
    long getMessageId();

    // server response
    SlackReply getSlackReply();

    boolean isAcked();

    void waitForReply(long timeout, TimeUnit unit);
}
