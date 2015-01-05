package com.ullink.slack.simpleslackapi;

public interface SlackUser
{
    String getId();

    String getUserName();

    String getRealName();

    boolean isDeleted();

    String getUserMail();
}
