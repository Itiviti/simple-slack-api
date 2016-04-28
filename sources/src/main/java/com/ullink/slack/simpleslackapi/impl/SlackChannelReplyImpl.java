package com.ullink.slack.simpleslackapi.impl;

import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.replies.SlackChannelReply;

public class SlackChannelReplyImpl extends SlackReplyImpl implements SlackChannelReply
{
    private SlackChannel slackChannel;

    SlackChannelReplyImpl(boolean ok, String error, SlackChannel slackChannel)
    {
        super(ok,error);
        this.slackChannel = slackChannel;
    }

    @Override
    public SlackChannel getSlackChannel()
    {
        return slackChannel;
    }
}
