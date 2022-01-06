package com.ullink.slack.simpleslackapi.impl;

import com.google.gson.annotations.SerializedName;
import com.ullink.slack.simpleslackapi.SlackPersona;
import com.ullink.slack.simpleslackapi.SlackPresence;
import com.ullink.slack.simpleslackapi.SlackUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SlackPersonaImpl implements SlackPersona, SlackUser {

    private String  id;
    @SerializedName("name")
    private String  userName;
    @SerializedName("skype")
    private boolean deleted;
    @SerializedName("is_admin")
    private boolean admin;
    @SerializedName("is_owner")
    private boolean owner;
    @SerializedName("is_primary_owner")
    private boolean primaryOwner;
    @SerializedName("is_restricted")
    private boolean restricted;
    @SerializedName("is_ultra_restricted")
    private boolean ultraRestricted;
    @SerializedName("is_bot")
    private boolean bot;
    @SerializedName("tz")
    private String timeZone;
    @SerializedName("tz_label")
    private String timeZoneLabel;
    @SerializedName("tz_offset")
    private Integer timeZoneOffset;

    private SlackProfileImpl profile;


    @Override
    public String getRealName() {
        return profile.getRealName();
    }

    @Override
    public String getUserMail() {
        return profile.getEmail();
    }

    @Override
    public String getUserSkype() {
        return profile.getSkype();
    }

    @Override
    public String getUserPhone() {
        return profile.getPhone();
    }

    @Override
    public String getUserTitle() {
        return profile.getTitle();
    }
    /**
     * To get the user's status
     * CS427 Issue Link: https://github.com/Itiviti/simple-slack-api/issues/279
     * @return The status of users
     */
    @Override
    public String getStatusText()
    {
        return profile.getStatusText();
    }
    /**
     * To get the users status's emoji text
     * CS427 Issue Link: https://github.com/Itiviti/simple-slack-api/issues/279
     * @return The emoji text of users' statuses
     */
    @Override
    public String getStatusEmoji()
    {
        return profile.getStatusEmoji();
    }

    @Override
    public SlackPresence getPresence() {
        return profile.getPresence();
    }

}
