package com.ullink.slack.simpleslackapi.impl;

import com.ullink.slack.simpleslackapi.SlackUser;

class SlackUserImpl implements SlackUser
{

    String id;
    String userName;
    String realName;
    boolean isDeleted;

    SlackUserImpl(String id, String userName, String realName, boolean isDeleted)
    {
        this.id = id;
        this.userName = userName;
        this.realName = realName;
        this.isDeleted = isDeleted;
    }

    @Override
    public String getId()
    {
        return id;
    }

    @Override
    public String getUserName()
    {
        return userName;
    }

    @Override
    public String getRealName()
    {
        return realName;
    }

    @Override
    public boolean isDeleted()
    {
        return isDeleted;
    }
}
