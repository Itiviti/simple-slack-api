package com.ullink.slack.simpleslackapi.impl;

import java.net.Proxy;
import java.util.concurrent.TimeUnit;

import com.ullink.slack.simpleslackapi.SlackSession;

public class SlackSessionFactory
{
    public static SlackSession createWebSocketSlackSession(String authToken)
    {
    	return new SlackWebSocketSessionImpl(authToken, true, 0, null);
    }

    public static SlackSession createWebSocketSlackSession(final String authToken, Proxy.Type proxyType, String proxyAddress, int proxyPort)
    {
        return new SlackWebSocketSessionImpl(authToken, proxyType, proxyAddress, proxyPort, true,0,null);
    }

    public static SlackSession createWebSocketSlackSession(final String authToken, int heartbeat, TimeUnit unit, Proxy.Type proxyType, String proxyAddress, int proxyPort)
    {
        return new SlackWebSocketSessionImpl(authToken, proxyType, proxyAddress, proxyPort, true, heartbeat, unit);
    }

    public static SlackSession createWebSocketSlackSession(String authToken, int heartbeat, TimeUnit unit) {
        return new SlackWebSocketSessionImpl(authToken, null, null, 0,true, heartbeat, unit);
    }
}
