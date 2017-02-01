package com.ullink.slack.simpleslackapi.events;

import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackUser;

public interface ReactionRemoved extends SlackEvent {

    String getEmojiName();
    SlackChannel getChannel();
    SlackUser getUser();
    SlackUser getItemUser();
    String getMessageID();
    String getFileID();
    String getFileCommentID();
    String getTimestamp();

}
