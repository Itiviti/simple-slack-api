package com.ullink.slack.simpleslackapi;

import com.google.gson.annotations.SerializedName;

/**
 * SlackStatus is a class that represents the status a user in his profile
 *
 * Example: {
 *     "text": out of office
 *     "emoji": 🌴
 * }
 */
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
     *
     * CS427 Issue link: https://github.com/Itiviti/simple-slack-api /issues/196
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
     *
     * CS427 Issue link: https://github.com/Itiviti/simple-slack-api /issues/196
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
     *
     * CS427 Issue link: https://github.com/Itiviti/simple-slack-api /issues/196
     */
    public String getText()
    {
        return text;
    }

    /**
     * Returns the slack status emoji of a user profile
     *
     * @return void
     *
     * CS427 Issue link: https://github.com/Itiviti/simple-slack-api /issues/196
     */
    public String getEmoji()
    {
        return emoji;
    }
}
