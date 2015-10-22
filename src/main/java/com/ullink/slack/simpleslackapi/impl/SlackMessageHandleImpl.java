package com.ullink.slack.simpleslackapi.impl;

import com.ullink.slack.simpleslackapi.SlackMessageHandle;
import com.ullink.slack.simpleslackapi.events.SlackReplyEvent;
import java.util.concurrent.TimeUnit;

class SlackMessageHandleImpl implements SlackMessageHandle
{

    private long                messageId;
    private volatile SlackReplyEvent slackReply;

    public SlackMessageHandleImpl(long messageId)
    {
        this.messageId = messageId;
    }

    @Override
    public long getMessageId()
    {
        return messageId;
    }

    @Override
    public SlackReplyEvent getSlackReply()
    {
        return slackReply;
    }

    @Override
    public void setSlackReply(SlackReplyEvent slackReply)
    {
        this.slackReply = slackReply;
    }

    @Override
    public boolean isAcked()
    {
        return false;
    }

    @Override
    public void waitForReply(long timeout, TimeUnit unit)
    {
        long nanoStart = System.nanoTime();
        while ((System.nanoTime() - nanoStart) < unit.toNanos(timeout) && slackReply == null)
        {
            try
            {
                Thread.sleep(1);
            }
            catch (InterruptedException e)
            {
                return;
            }
        }
    }

}
