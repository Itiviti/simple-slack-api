package com.ullink.slack.simpleslackapi.impl;

import java.net.Proxy;
import java.util.concurrent.TimeUnit;

import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.WebSocketContainerProvider;

public class SlackSessionFactory {
    public static SlackSession createWebSocketSlackSession(String authToken)
    {
    	return new SlackWebSocketSessionImpl(null,authToken, true, 0, null);
    }

    public static SlackSessionFactoryBuilder getSlackSessionBuilder(String authToken) {
        return new SlackSessionFactoryBuilder(authToken);
    }

    public static class SlackSessionFactoryBuilder {

        private String authToken;
        private Proxy.Type proxyType;
        private String proxyAddress;
        private int proxyPort;
        private int heartbeat;
        private TimeUnit unit;
        private WebSocketContainerProvider provider;
        private boolean autoreconnection;

        private SlackSessionFactoryBuilder(String authToken) {
            this.authToken = authToken;
        }

        public SlackSessionFactoryBuilder withProxy(Proxy.Type proxyType, String proxyAddress, int proxyPort) {
            this.proxyType = proxyType;
            this.proxyAddress = proxyAddress;
            this.proxyPort = proxyPort;
            return this;
        }

        public SlackSessionFactoryBuilder withConnectionHeartbeat(int heartbeat, TimeUnit unit) {
            this.heartbeat = heartbeat;
            this.unit = unit;
            return this;
        }

        public SlackSessionFactoryBuilder withCustomWebSocketContainer(WebSocketContainerProvider provider) {
            this.provider = provider;
            return this;
        }

        public SlackSessionFactoryBuilder withAutoreconnectOnDisconnection(boolean autoreconnection) {
            this.autoreconnection = autoreconnection;
            return this;
        }


        public SlackSession build() {
            return new SlackWebSocketSessionImpl(provider,authToken, proxyType, proxyAddress, proxyPort, autoreconnection,heartbeat,unit);
        }
    }
}
