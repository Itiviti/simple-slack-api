package com.ullink.slack.simpleslackapi;

import com.google.gson.annotations.SerializedName;

public class SlackStatus {
    @SerializedName("text")
    private String text;
    @SerializedName("emoji")
    private String emoji;

    /**
     * Sets the slack status text of a user profile
     *
     * @param txt is the text of the slack status
     * @return SlackStatus
     */
    public SlackStatus setText(String txt)
    {
        text = txt;
        return this;
    }

    /**
     * Sets the slack status emoji of a user profile
     *
     * @param emoji is the emoji of the slack status
     * @return SlackStatus
     */
    public SlackStatus setEmoji(String emoji)
    {
        this.emoji = emoji;
        return this;
    }

    /**
     * Returns the slack status text of a user profile
     *
     * @return void
     */
    public String getText()
    {
        return text;
    }

    /**
     * Returns the slack status emoji of a user profile
     *
     * @return void
     */
    public String getEmoji()
    {
        return emoji;
    }
}
