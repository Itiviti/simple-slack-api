package com.ullink.slack.simpleslackapi.samples.connection;

import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.WebSocketContainerProvider;
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory;

import javax.websocket.ClientEndpointConfig;
import javax.websocket.DeploymentException;
import javax.websocket.Endpoint;
import javax.websocket.Extension;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import java.io.IOException;
import java.net.Proxy;
import java.net.URI;
import java.util.Set;
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
                            @Override
                            public long getDefaultAsyncSendTimeout()
                            {
                                return 0;
                            }

                            @Override
                            public void setAsyncSendTimeout(long timeoutmillis)
                            {

                            }

                            @Override
                            public Session connectToServer(Object annotatedEndpointInstance, URI path) throws DeploymentException, IOException
                            {
                                return null;
                            }

                            @Override
                            public Session connectToServer(Class<?> annotatedEndpointClass, URI path) throws DeploymentException, IOException
                            {
                                return null;
                            }

                            @Override
                            public Session connectToServer(Endpoint endpointInstance, ClientEndpointConfig cec, URI path) throws DeploymentException, IOException
                            {
                                return null;
                            }

                            @Override
                            public Session connectToServer(Class<? extends Endpoint> endpointClass, ClientEndpointConfig cec, URI path) throws DeploymentException, IOException
                            {
                                return null;
                            }

                            @Override
                            public long getDefaultMaxSessionIdleTimeout()
                            {
                                return 0;
                            }

                            @Override
                            public void setDefaultMaxSessionIdleTimeout(long timeout)
                            {

                            }

                            @Override
                            public int getDefaultMaxBinaryMessageBufferSize()
                            {
                                return 0;
                            }

                            @Override
                            public void setDefaultMaxBinaryMessageBufferSize(int max)
                            {

                            }

                            @Override
                            public int getDefaultMaxTextMessageBufferSize()
                            {
                                return 0;
                            }

                            @Override
                            public void setDefaultMaxTextMessageBufferSize(int max)
                            {

                            }

                            @Override
                            public Set<Extension> getInstalledExtensions()
                            {
                                return null;
                            }
                        };
                    }
                }).build();
        session.connect();
    }
}
