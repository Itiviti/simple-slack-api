package com.ullink.slack.simpleslackapi;

import com.ullink.slack.simpleslackapi.events.SlackReplyEvent;
import java.util.concurrent.TimeUnit;

public interface SlackMessageHandle
{
    // the id given to the message sent
    long getMessageId();

    // server response
    SlackReplyEvent getSlackReply();
    
    void setSlackReply(SlackReplyEvent replyEvent);

    boolean isAcked();

    void waitForReply(long timeout, TimeUnit unit);
}
