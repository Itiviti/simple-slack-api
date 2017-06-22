package com.ullink.slack.simpleslackapi.replies;

import java.util.Map;

public class EmojiSlackReply extends SlackReplyImpl implements SlackReply {

    private String timestamp;
    private Map<String, String>  emoji;

    public EmojiSlackReply(boolean ok, String error, Map<String, String>  emoji, String timestamp) {
        super(ok, error);
        this.timestamp = timestamp;
        this.emoji = emoji;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public Map<String, String> getEmojis() {
        return emoji;
    }
}
