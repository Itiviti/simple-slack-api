package com.ullink.slack.simpleslackapi.replies;

import java.util.Map;

public interface EmojiSlackReply extends SlackReply
{
    String getTimestamp();

    Map<String,String> getEmojis();
}
