package com.ullink.slack.simpleslackapi;

import com.ullink.slack.simpleslackapi.events.SlackMessageEvent;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;

/**
 * 
 * @deprecated use {@link SlackMessagePosted}
 *
 */
@Deprecated
public interface SlackMessage extends SlackMessageEvent
{

    String getMessageContent();

    SlackUser getSender();

    SlackBot getBot();

    SlackChannel getChannel();

}
