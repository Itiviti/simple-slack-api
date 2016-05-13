package com.ullink.slack.simpleslackapi.samples.connection;

import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.WebSocketContainerProvider;
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory;

import javax.websocket.WebSocketContainer;
import java.io.IOException;
import java.net.Proxy;
import java.util.concurrent.TimeUnit;

/**
 * This sample code is creating a Slack session which will connect through a proxy and and is connecting to slack.
 * To get some more details on how to get a token, please have a look here : https://api.slack.com/bot-users
 */
public class SlackCustomConnection
{
    public static void main(String[] args) throws IOException
    {
        SlackSession session = SlackSessionFactory.getSlackSessionBuilder("my-bot-auth-token")
                                                  .withProxy(Proxy.Type.HTTP, "my.proxy.address", 1234)
                                              .withAutoreconnectOnDisconnection(false)
                .withConnectionHeartbeat(10, TimeUnit.SECONDS)
                .withCustomWebSocketContainer(new WebSocketContainerProvider()
                {
                    @Override
                    public WebSocketContainer getWebSocketContainer()
                    {
                        return new WebSocketContainer()
                        {
                        };
                    }
                })
                                                  .build();
        session.connect();
    }
}
