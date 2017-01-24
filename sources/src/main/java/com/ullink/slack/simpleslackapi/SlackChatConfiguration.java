package com.ullink.slack.simpleslackapi;

public class SlackChatConfiguration
{
    public enum Avatar
    {
        DEFAULT, EMOJI, ICON_URL
    }

    private boolean asUser;
    private Avatar  avatar = Avatar.DEFAULT;
    private String  userName;
    private String  avatarDescription;

    private SlackChatConfiguration()
    {

    }

    public boolean isAsUser()
    {
        return asUser;
    }

    public Avatar getAvatar()
    {
        return avatar;
    }

    public String getUserName()
    {
        return userName;
    }

    public String getAvatarDescription()
    {
        return avatarDescription;
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
