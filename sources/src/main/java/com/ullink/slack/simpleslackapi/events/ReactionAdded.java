package com.ullink.slack.simpleslackapi.events;

import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackUser;


public interface ReactionAdded extends SlackEvent {

    String getEmojiName();
    SlackChannel getChannel();
    SlackUser getUser();
    String getMessageID();
    String getFileID();
    String getFileCommentID();

}
