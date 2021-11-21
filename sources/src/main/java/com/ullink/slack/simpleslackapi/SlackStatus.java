package com.ullink.slack.simpleslackapi;

import com.google.gson.annotations.SerializedName;

public class SlackStatus {
    @SerializedName("text")
    private String text;
    @SerializedName("emoji")
    private String emoji;

    public SlackStatus setText(String txt)
    {
        text = txt;
        return this;
    }

    public SlackStatus setEmoji(String emoji)
    {
        this.emoji = emoji;
        return this;
    }

    public String getText()
    {
        return text;
    }

    public String getEmoji()
    {
        return emoji;
    }
}
