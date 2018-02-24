package com.ullink.slack.simpleslackapi.entity;

import com.ullink.slack.simpleslackapi.entity.SlackBot;
import com.ullink.slack.simpleslackapi.entity.SlackChannel;
import com.ullink.slack.simpleslackapi.entity.SlackUser;
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
