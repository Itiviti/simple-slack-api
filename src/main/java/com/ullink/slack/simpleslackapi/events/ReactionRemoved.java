package com.ullink.slack.simpleslackapi.events;

import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackUser;

public interface ReactionRemoved extends SlackEvent {

    public String getEmojiName();
    public SlackChannel getChannel();
    public SlackUser getUser();
    public String getMessageID();
    public String getFileID();
    public String getFileCommentID();
  
}
