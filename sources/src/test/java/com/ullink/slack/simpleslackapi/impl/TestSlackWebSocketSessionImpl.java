package com.ullink.slack.simpleslackapi.impl;

import com.ullink.slack.simpleslackapi.WebSocketContainerProvider;
import mockit.Mocked;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.fail;

public class TestSlackWebSocketSessionImpl {

  @Test(expected = IllegalArgumentException.class)
  public void testSendMessageWithNullChanel(@Mocked WebSocketContainerProvider provider) throws Exception{
    SlackWebSocketSessionImpl webSocketSession = new SlackWebSocketSessionImpl(provider,
        "", false, 42L, TimeUnit.MILLISECONDS);
    try {
      webSocketSession.sendMessage(null, "");
    } catch (NullPointerException e) {
      fail("NullPointerException unexpected here");
    }
  }
}
