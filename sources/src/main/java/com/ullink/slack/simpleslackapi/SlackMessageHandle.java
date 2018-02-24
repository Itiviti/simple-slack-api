package com.ullink.slack.simpleslackapi;

import com.ullink.slack.simpleslackapi.replies.SlackReply;

import java.util.concurrent.TimeUnit;

public class SlackMessageHandle<T extends SlackReply> {

    private static final long WAIT_TIME_IN_MILLISECOND = 1L;
    private long messageId;
    private volatile T slackReply;

    public SlackMessageHandle(long messageId) {
        this.messageId = messageId;
    }

    public long getMessageId() {
        return messageId;
    }

    public T getReply() {
        return slackReply;
    }

    public void setReply(T slackReply) {
        this.slackReply = slackReply;
    }

    public boolean isAcked() {
        return false;
    }

    public void waitForReply(long timeout, TimeUnit unit) {
        long nanoStart = System.nanoTime();
        while ((System.nanoTime() - nanoStart) < unit.toNanos(timeout) && slackReply == null) {
            try {
                Thread.sleep(WAIT_TIME_IN_MILLISECOND);
            } catch (InterruptedException e) {
                return;
            }
        }
    }
}
