package com.ullink.slack.simpleslackapi.impl;

import com.ullink.slack.simpleslackapi.SlackUser;

class SlackUserImpl implements SlackUser
{

    String  id;
    String  userName;
    String  realName;
    String  userMail;
    boolean isDeleted;

    SlackUserImpl(String id, String userName, String realName, String userMail, boolean isDeleted)
    {
        this.id = id;
        this.userName = userName;
        this.realName = realName;
        this.isDeleted = isDeleted;
        this.userMail = userMail;
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

    @Override
    public String getUserMail()
    {
        return userMail;
    }
}
