package com.ullink.slack.simpleslackapi.replies;

import com.ullink.slack.simpleslackapi.SlackChannel;

public interface SlackChannelReply extends ParsedSlackReply
{
    SlackChannel getSlackChannel();
}
