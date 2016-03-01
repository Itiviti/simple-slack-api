package com.ullink.slack.simpleslackapi.impl;

public class SlackChatConfiguration
{
    static enum Avatar
    {
        DEFAULT, EMOJI, ICON_URL;
    }

    protected boolean asUser;
    protected Avatar  avatar = Avatar.DEFAULT;
    protected String  userName;
    protected String  avatarDescription;

    private SlackChatConfiguration()
    {

    }

    public SlackChatConfiguration asUser()
    {
        asUser = true;
        avatar = Avatar.DEFAULT;
        avatarDescription = null;
        return this;
    }

    public SlackChatConfiguration withIcon(String iconURL)
    {
        asUser = false;
        avatar = Avatar.ICON_URL;
        avatarDescription = iconURL;
        return this;
    }

    public SlackChatConfiguration withName(String name)
    {
        asUser = false;
        userName = name;
        return this;
    }

    public SlackChatConfiguration withEmoji(String emoji)
    {
        asUser = false;
        avatar = Avatar.EMOJI;
        avatarDescription = emoji;
        return this;
    }

    public static SlackChatConfiguration getConfiguration()
    {
        return new SlackChatConfiguration();
    }

}
