package com.ullink.slack.simpleslackapi;

import org.junit.Test;

import java.io.IOException;

import static com.ullink.slack.simpleslackapi.TestUtils.getFile;
import static com.ullink.slack.simpleslackapi.TestUtils.gson;
import static org.junit.Assert.*;

public class SlackChannelTest {

  @Test
  public void testJsonSerialize() throws IOException {
    SlackChannel channel = SlackChannel.builder()
        .id("test")
        .name("test_name")
        .topic("test_topic")
        .purpose("test_purpose")
        .direct(true)
        .build();
    String json = gson().toJson(channel);
    assertNotNull(json);
    assertEquals(getFile("/channel.json"), json);
  }


  @Test
  public void testJsonDeserialize() throws IOException {
    String json = getFile("/channel.json");
    SlackChannel object = gson().fromJson(json, SlackChannel.class);
    assertEquals("test", object.getId());
    assertEquals("test_name", object.getName());
    assertEquals("test_topic", object.getTopic());
    assertEquals("test_purpose", object.getPurpose());
    assertTrue(object.isDirect());
    assertFalse(object.isArchived());
    assertTrue(object.getMembers().isEmpty());
  }

}