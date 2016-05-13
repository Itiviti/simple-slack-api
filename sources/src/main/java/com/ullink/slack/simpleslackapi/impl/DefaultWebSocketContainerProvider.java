package com.ullink.slack.simpleslackapi.impl;

import com.ullink.slack.simpleslackapi.WebSocketContainerProvider;
import org.glassfish.tyrus.client.ClientManager;
import org.glassfish.tyrus.client.ClientProperties;

import javax.websocket.WebSocketContainer;

public class DefaultWebSocketContainerProvider implements WebSocketContainerProvider
{

    private String proxyAddress;
    private int proxyPort;

    DefaultWebSocketContainerProvider(String proxyAdress, int proxyPort) {
        this.proxyAddress = proxyAdress;
        this.proxyPort = proxyPort;
    }

    @Override
    public WebSocketContainer getWebSocketContainer()
    {
        ClientManager clientManager = ClientManager.createClient();
        if (proxyAddress != null)
        {
            clientManager.getProperties().put(ClientProperties.PROXY_URI, "http://" + proxyAddress + ":" + proxyPort);
        }
        return clientManager;
    }
}
