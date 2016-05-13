package com.ullink.slack.simpleslackapi;

import javax.websocket.WebSocketContainer;

public interface WebSocketContainerProvider
{
    WebSocketContainer getWebSocketContainer();
}
