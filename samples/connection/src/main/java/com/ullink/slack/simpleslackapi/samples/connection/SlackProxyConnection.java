package com.ullink.slack.simpleslackapi.samples.connection;

import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory;

import java.io.IOException;
import java.net.Proxy;

/**
 * This sample code is creating a Slack session which will connect through a proxy and and is connecting to slack.
 * To get some more details on how to get a token, please have a look here : https://api.slack.com/bot-users
 */
public class SlackProxyConnection
{
    public static void main(String[] args) throws IOException
    {
        SlackSession session = SlackSessionFactory.createWebSocketSlackSession("my-bot-auth-token", Proxy.Type.HTTP, "my.proxy.address", 1234);
        session.connect();
    }
}
