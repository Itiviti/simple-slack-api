package com.ullink.slack.simpleslackapi.impl;

import com.ullink.slack.simpleslackapi.SlackTeam;

public class SlackTeamImpl implements SlackTeam
{
    final String id;
    final String name;
    final String domain;

    public SlackTeamImpl(String id, String name, String domain)
    {
        this.id = id;
        this.name = name;
        this.domain = domain;
    }

    @Override
    public String getId()
    {
        return id;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public String getDomain()
    {
        return domain;
    }
}
