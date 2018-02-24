package com.ullink.slack.simpleslackapi.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SlackChatConfiguration {
    private boolean asUser;
    private Avatar avatar = Avatar.DEFAULT;
    private String userName;
    private String avatarDescription;

    public static SlackChatConfiguration getConfiguration() {
        return new SlackChatConfiguration();
    }

    public SlackChatConfiguration asUser() {
        asUser = true;
        avatar = Avatar.DEFAULT;
        avatarDescription = null;
        return this;
    }

    public SlackChatConfiguration withIcon(String iconURL) {
        asUser = false;
        avatar = Avatar.ICON_URL;
        avatarDescription = iconURL;
        return this;
    }

    public SlackChatConfiguration withName(String name) {
        asUser = false;
        userName = name;
        return this;
    }

    public SlackChatConfiguration withEmoji(String emoji) {
        asUser = false;
        avatar = Avatar.EMOJI;
        avatarDescription = emoji;
        return this;
    }

    public enum Avatar {
        DEFAULT, EMOJI, ICON_URL
    }

}
