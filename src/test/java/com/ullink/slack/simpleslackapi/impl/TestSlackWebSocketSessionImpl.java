package com.ullink.slack.simpleslackapi.impl;

import mockit.Invocation;
import mockit.Mock;
import mockit.MockUp;
import mockit.integration.junit4.JMockit;
import org.glassfish.tyrus.client.ClientManager;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.websocket.CloseReason;
import javax.websocket.Extension;
import javax.websocket.MessageHandler;
import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Proxy;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.security.Principal;
import java.security.cert.Certificate;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RunWith(JMockit.class)
public class TestSlackWebSocketSessionImpl
{
    public static class MockHttpsURLConnection extends HttpsURLConnection
    {
        protected MockHttpsURLConnection(URL url)
        {
            super(url);
        }

        @Override
        public String getCipherSuite()
        {
            return null;
        }

        @Override
        public Certificate[] getLocalCertificates()
        {
            return new Certificate[0];
        }

        @Override
        public Certificate[] getServerCertificates() throws SSLPeerUnverifiedException
        {
            return new Certificate[0];
        }

        @Override
        public void disconnect()
        {

        }

        @Override
        public boolean usingProxy()
        {
            return false;
        }

        @Override
        public void connect() throws IOException
        {

        }

        @Override
        public InputStream getInputStream() throws IOException
        {
            return new ByteArrayInputStream("{\"users\":[],\"bots\":[],\"channels\":[],\"groups\":[],\"url\":\"wss://testurl\"}".getBytes());
        }
    }

    public class MockSession implements Session
    {

        @Override
        public WebSocketContainer getContainer()
        {
            return null;
        }

        @Override
        public void addMessageHandler(MessageHandler handler) throws IllegalStateException
        {

        }

        @Override
        public <T> void addMessageHandler(Class<T> clazz, MessageHandler.Whole<T> handler)
        {

        }

        @Override
        public <T> void addMessageHandler(Class<T> clazz, MessageHandler.Partial<T> handler)
        {

        }

        @Override
        public Set<MessageHandler> getMessageHandlers()
        {
            return null;
        }

        @Override
        public void removeMessageHandler(MessageHandler handler)
        {

        }

        @Override
        public String getProtocolVersion()
        {
            return null;
        }

        @Override
        public String getNegotiatedSubprotocol()
        {
            return null;
        }

        @Override
        public List<Extension> getNegotiatedExtensions()
        {
            return null;
        }

        @Override
        public boolean isSecure()
        {
            return false;
        }

        @Override
        public boolean isOpen()
        {
            return false;
        }

        @Override
        public long getMaxIdleTimeout()
        {
            return 0;
        }

        @Override
        public void setMaxIdleTimeout(long milliseconds)
        {

        }

        @Override
        public void setMaxBinaryMessageBufferSize(int length)
        {

        }

        @Override
        public int getMaxBinaryMessageBufferSize()
        {
            return 0;
        }

        @Override
        public void setMaxTextMessageBufferSize(int length)
        {

        }

        @Override
        public int getMaxTextMessageBufferSize()
        {
            return 0;
        }

        @Override
        public RemoteEndpoint.Async getAsyncRemote()
        {
            return null;
        }

        @Override
        public RemoteEndpoint.Basic getBasicRemote()
        {
            return null;
        }

        @Override
        public String getId()
        {
            return null;
        }

        @Override
        public void close() throws IOException
        {

        }

        @Override
        public void close(CloseReason closeReason) throws IOException
        {

        }

        @Override
        public URI getRequestURI()
        {
            return null;
        }

        @Override
        public Map<String, List<String>> getRequestParameterMap()
        {
            return null;
        }

        @Override
        public String getQueryString()
        {
            return null;
        }

        @Override
        public Map<String, String> getPathParameters()
        {
            return null;
        }

        @Override
        public Map<String, Object> getUserProperties()
        {
            return null;
        }

        @Override
        public Principal getUserPrincipal()
        {
            return null;
        }

        @Override
        public Set<Session> getOpenSessions()
        {
            return null;
        }
    }


    public class MockClientManager extends MockUp<ClientManager>
    {

    }

    @Test
    public void testConnectionThroughProxy()
    {
        new MockUp<URL>()
        {

            @Mock(invocations = 1)
            public URLConnection openConnection(Invocation invocation, Proxy proxy) throws IOException
            {
                URL url = invocation.getInvokedInstance();
                return new MockHttpsURLConnection(url);
            }

        };
        new MockUp<ClientManager>()
        {
            @Mock(invocations = 1)
            public Session connectToServer(Object obj, URI path)
            {
                return new MockSession();
            }
        };
        SlackWebSocketSessionImpl wsSession = new SlackWebSocketSessionImpl("testAuthToken", Proxy.Type.HTTP, "http://my.proxy.address", 5555);
        wsSession.connect();
    }

    @Test
    public void testDirectConnection()
    {
        new MockUp<URL>()
        {
            @Mock(invocations = 1)
            public URLConnection openConnection(Invocation invocation) throws IOException
            {
                URL url = invocation.getInvokedInstance();
                return new MockHttpsURLConnection(url);
            }

        };
        new MockUp<ClientManager>()
        {
            @Mock(invocations = 1)
            public Session connectToServer(Object obj, URI path)
            {
                return new MockSession();
            }
        };
        SlackWebSocketSessionImpl wsSession = new SlackWebSocketSessionImpl("testAuthToken");
        wsSession.connect();
    }

}
