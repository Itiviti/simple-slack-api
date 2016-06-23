package com.ullink.slack.simpleslackapi.impl;

import java.util.concurrent.TimeUnit;
import com.ullink.slack.simpleslackapi.SlackMessageHandle;
import com.ullink.slack.simpleslackapi.replies.SlackReply;

class SlackMessageHandleImpl<T extends SlackReply> implements SlackMessageHandle<T>
{

    private static final long WAIT_TIME_IN_MILLISECOND = 1L;
    private long                messageId;
    private volatile T          slackReply;

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
    public T getReply()
    {
        return slackReply;
    }

    void setReply(T slackReply)
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
                Thread.sleep(WAIT_TIME_IN_MILLISECOND);
            }
            catch (InterruptedException e)
            {
                return;
            }
        }
    }
}
