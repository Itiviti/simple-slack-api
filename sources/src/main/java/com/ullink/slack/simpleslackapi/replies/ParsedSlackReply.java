package com.ullink.slack.simpleslackapi.replies;

public interface ParsedSlackReply extends SlackReply
{
    boolean isOk();
    String getErrorMessage();
}
