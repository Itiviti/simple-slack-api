package com.ullink.slack.simpleslackapi.replies;

public interface SlackMessageReply extends ParsedSlackReply
{
    long getReplyTo();
    String getTimestamp();

}
