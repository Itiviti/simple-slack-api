package com.ullink.slack.simpleslackapi.impl;

import java.net.Proxy;
import java.util.concurrent.TimeUnit;

import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.WebSocketContainerProvider;

public class SlackSessionFactory {
    /**
     * Add a new variable because Slack Api change the way to be called. Now it requires auth token and app level token
     * @param authToken
     * @param appLevelToken
     * @return
     */
    public static SlackSession createWebSocketSlackSession(String authToken, String appLevelToken)
    {
    	return new SlackWebSocketSessionImpl(null, authToken, appLevelToken, null, true, true, 0, null);
    }

    /**
     * @deprecated use the authToken and appLevelToken method
     * @param authToken
     */
    public static SlackSession createWebSocketSlackSession(String authToken)
    {
        return new SlackWebSocketSessionImpl(null, authToken, null, null, true, true, 0, null);
    }

    /**
     * Add a new variable because Slack Api change the way to be called. Now it requires auth token and app level token
     * CS427 Issue Link: https://github.com/Itiviti/simple-slack-api/issues/283
     * @param authToken
     * @param appLevelToken
     * @return
     */
    public static SlackSessionFactoryBuilder getSlackSessionBuilder(String authToken, String appLevelToken) {
        return new SlackSessionFactoryBuilder(authToken, appLevelToken);
    }

    /**
     * @deprecated use the authToken and appLevelToken method
     * @param authToken
     */
    public static SlackSessionFactoryBuilder getSlackSessionBuilder(String authToken) {
        return new SlackSessionFactoryBuilder(authToken);
    }

    public static class SlackSessionFactoryBuilder {

        private String authToken;
        /**
         * Add new variable -appLevelToken
         */
        private String appLevelToken;
        private String slackBaseApi;
        private Proxy.Type proxyType;
        private String proxyAddress;
        private int proxyPort;
        private String proxyUser;
        private String proxyPassword;
        private int heartbeat;
        private TimeUnit unit;
        private WebSocketContainerProvider provider;
        private boolean autoreconnection;
        private boolean rateLimitSupport = true;

        /**
         * Add new variable -appLevelToken
         * CS427 Issue Link: https://github.com/Itiviti/simple-slack-api/issues/283
         * @param authToken
         * @param appLevelToken
         */
        private SlackSessionFactoryBuilder(String authToken, String appLevelToken) {
            this.authToken = authToken;
            this.appLevelToken = appLevelToken;
        }

        /**
         * @deprecated use the authToken and appLevelToken constructor
         * @param authToken
         */
        private SlackSessionFactoryBuilder(String authToken) {
            this.authToken = authToken;
            this.appLevelToken = appLevelToken;
        }

        public SlackSessionFactoryBuilder withBaseApiUrl(String slackBaseApi) {
        	this.slackBaseApi = slackBaseApi;
        	return this;
        }
        
        public SlackSessionFactoryBuilder withProxy(Proxy.Type proxyType, String proxyAddress, int proxyPort) {
            this.proxyType = proxyType;
            this.proxyAddress = proxyAddress;
            this.proxyPort = proxyPort;
            return this;
        }

        public SlackSessionFactoryBuilder withProxy(Proxy.Type proxyType, String proxyAddress, int proxyPort, String proxyUser, String proxyPassword) {
            this.proxyType = proxyType;
            this.proxyAddress = proxyAddress;
            this.proxyPort = proxyPort;
            this.proxyUser = proxyUser;
            this.proxyPassword = proxyPassword;
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

        public SlackSessionFactoryBuilder withRateLimitSupport(boolean rateLimitSupport) {
            this.rateLimitSupport = rateLimitSupport;
            return this;
        }

        public SlackSession build() {
            return new SlackWebSocketSessionImpl(provider, authToken, appLevelToken, slackBaseApi, proxyType, proxyAddress, proxyPort, proxyUser, proxyPassword, autoreconnection, rateLimitSupport, heartbeat, unit);
        }
    }
}
