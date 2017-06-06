package com.ullink.slack.simpleslackapi;

import com.ullink.slack.simpleslackapi.events.SlackEvent;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;

/**
 * 
 * @deprecated use {@link SlackMessagePosted}
 *
 */
@Deprecated
public interface SlackMessage extends SlackEvent {

    String getMessageContent();

    SlackUser getSender();

    SlackBot getBot();

    SlackChannel getChannel();

    String getTimeStamp();
}
