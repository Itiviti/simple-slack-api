package com.ullink.slack.simpleslackapi.impl;

import java.util.concurrent.TimeUnit;
import com.ullink.slack.simpleslackapi.SlackMessageHandle;
import com.ullink.slack.simpleslackapi.SlackReply;

class SlackMessageHandleImpl implements SlackMessageHandle
{

    private long                messageId;
    private volatile SlackReply slackReply;

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
    public SlackReply getSlackReply()
    {
        return slackReply;
    }

    void setSlackReply(SlackReply slackReply)
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
