package com.ullink.slack.simpleslackapi;

public interface SlackReply
{
    boolean isOk();
    long getReplyTo();
    String getTimestamp();
}
