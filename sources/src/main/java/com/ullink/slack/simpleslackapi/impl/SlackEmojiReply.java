package com.ullink.slack.simpleslackapi.impl;

import com.ullink.slack.simpleslackapi.replies.EmojiSlackReply;

import java.util.Map;

public class SlackEmojiReply extends SlackReplyImpl implements EmojiSlackReply {

    private String timestamp;
    private Map<String, String>  emoji;

    public SlackEmojiReply(boolean ok, String error, Map<String, String>  emoji, String timestamp) {
        super(ok, error);
        this.timestamp = timestamp;
        this.emoji = emoji;
    }

    @Override
    public String getTimestamp() {
        return timestamp;
    }

    @Override
    public Map<String, String> getEmojis() {
        return emoji;
    }
}
