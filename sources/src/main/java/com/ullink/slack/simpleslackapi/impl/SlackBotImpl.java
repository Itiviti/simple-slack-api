package com.ullink.slack.simpleslackapi.impl;

import com.ullink.slack.simpleslackapi.SlackBot;

@Deprecated
class SlackBotImpl extends SlackPersonaImpl implements SlackBot
{
    SlackBotImpl(String id, String userName, String firstName, String lastName, String realName, String userMail, boolean deleted, boolean admin, boolean owner, boolean primaryOwner, boolean restricted, boolean ultraRestricted)
    {
        super(id, userName, firstName, lastName, realName, userMail, deleted, admin, owner, primaryOwner, restricted, ultraRestricted, true,null,null,0);
    }

}
