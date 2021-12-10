package com.ullink.slack.simpleslackapi.impl;

import com.ullink.slack.simpleslackapi.SlackIntegration;
import com.ullink.slack.simpleslackapi.SlackPresence;
import com.ullink.slack.simpleslackapi.SlackUser;

class SlackIntegrationUser implements SlackIntegration, SlackUser
{
    private final SlackIntegration integration;

    SlackIntegrationUser(SlackIntegration integration) {
        this.integration = integration;
    }

    @Override
    public String getId()
    {
        return integration.getId();
    }

    @Override
    public String getUserName()
    {
        return integration.getName();
    }

    @Override
    public String getRealName()
    {
        return integration.getName();
    }

    @Override
    public String getUserMail()
    {
        return null;
    }

    @Override
    public String getUserSkype()
    {
        return null;
    }

    @Override
    public String getUserPhone()
    {
        return null;
    }

    @Override
    public String getUserTitle()
    {
        return null;
    }
    /**
     * To get the user's status
     * @return The status of users
     * CS427 Issue Link: https://github.com/Itiviti/simple-slack-api/issues/196
     */
    @Override
    public String getStatusText()
    {
        return null;
    }
    /**
     * To get the users statuses's emoji text
     * @return The emoji text of users' statuses
     * CS427 Issue Link: https://github.com/Itiviti/simple-slack-api/issues/196
     */
    @Override
    public String getStatusEmoji()
    {
        return null;
    }

    @Override
    public String getName()
    {
        return integration.getName();
    }

    @Override
    public boolean isDeleted()
    {
        return integration.isDeleted();
    }

    @Override
    public boolean isAdmin()
    {
        return false;
    }

    @Override
    public boolean isOwner()
    {
        return false;
    }

    @Override
    public boolean isPrimaryOwner()
    {
        return false;
    }

    @Override
    public boolean isRestricted()
    {
        return false;
    }

    @Override
    public boolean isUltraRestricted()
    {
        return false;
    }

    @Override
    public boolean isBot()
    {
        return true;
    }

    @Override
    public String getTimeZone()
    {
        return null;
    }

    @Override
    public String getTimeZoneLabel()
    {
        return null;
    }

    @Override
    public Integer getTimeZoneOffset()
    {
        return null;
    }

    @Override
    public SlackPresence getPresence()
    {
        return SlackPresence.UNKNOWN;
    }
}
