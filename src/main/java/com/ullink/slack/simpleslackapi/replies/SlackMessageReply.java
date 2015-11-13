package com.ullink.slack.simpleslackapi.replies;

public interface SlackMessageReply extends SlackReply
{
    long getReplyTo();
    String getTimestamp();

}
