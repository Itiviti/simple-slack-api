package com.ullink.slack.simpleslackapi.events;

import com.ullink.slack.simpleslackapi.SlackAttachment;
import com.ullink.slack.simpleslackapi.SlackChannel;
import java.util.ArrayList;

public interface SlackMessageUpdated extends SlackMessageEvent {
    SlackChannel getChannel();
    String getMessageTimestamp();
    String getNewMessage();
    ArrayList<SlackAttachment> getAttachments();
}
