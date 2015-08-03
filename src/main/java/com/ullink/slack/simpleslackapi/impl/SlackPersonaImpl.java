package com.ullink.slack.simpleslackapi.impl;

import com.ullink.slack.simpleslackapi.SlackPersona;

class SlackPersonaImpl implements SlackPersona
{
    final String  id;
    final String  userName;
    final String  realName;
    final String  userMail;
    final boolean deleted;
    final boolean admin;
    final boolean owner;
    final boolean primaryOwner;
    final boolean restricted;
    final boolean ultraRestricted;
    final boolean bot;

    SlackPersonaImpl(String id, String userName, String realName, String userMail, boolean deleted, boolean admin, boolean owner, boolean primaryOwner, boolean restricted, boolean ultraRestricted, boolean bot)
    {
        this.id = id;
        this.userName = userName;
        this.realName = realName;
        this.userMail = userMail;
        this.deleted = deleted;
        this.admin = admin;
        this.owner = owner;
        this.primaryOwner = primaryOwner;
        this.restricted = restricted;
        this.ultraRestricted = ultraRestricted;
        this.bot = bot;
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
    public boolean isDeleted()
    {
        return deleted;
    }

    @Override
    public boolean isAdmin()
    {
        return admin;
    }

    @Override
    public boolean isOwner()
    {
        return owner;
    }

    @Override
    public boolean isPrimaryOwner()
    {
        return primaryOwner;
    }

    @Override
    public boolean isRestricted()
    {
        return restricted;
    }

    @Override
    public boolean isUltraRestricted()
    {
        return ultraRestricted;
    }

    @Override
    public boolean isBot()
    {
        return bot;
    }

    @Override
    public String getUserMail()
    {
        return userMail;
    }

    @Override
    public String getRealName()
    {
        return realName;
    }

}
