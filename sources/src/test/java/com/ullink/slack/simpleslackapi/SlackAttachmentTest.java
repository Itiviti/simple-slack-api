package com.ullink.slack.simpleslackapi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static com.ullink.slack.simpleslackapi.TestUtils.getFile;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class SlackAttachmentTest {

  private Gson gson;

  @Before
  public void setUp() throws IOException {
    GsonBuilder builder = new GsonBuilder();
    builder.registerTypeAdapter(SlackPresence.class, new SlackPresenceSerDes());
    gson = builder.create();
  }

  @Test
  public void testJsonSerialize() throws IOException {
    SlackAttachment attachment = new SlackAttachment("test", "fallback", "text", "pretext");
    attachment.addField("field 1", "value 1", false);
    attachment.addField("field 2", "value 2", true);
    attachment.addAction("action 1", "text 1", "type", "value");
    String json = gson.toJson(attachment);
    assertNotNull(json);
    assertEquals(getFile("/attachment.json"), json);
  }

  @Test
  public void testJsonDeserialize() throws IOException {
    String json = getFile("/attachment.json");
    SlackAttachment attachment = gson.fromJson(json, SlackAttachment.class);
    assertEquals("test", attachment.getTitle());
    assertEquals("fallback", attachment.getFallback());
    assertEquals("text", attachment.getText());
    assertNotNull(attachment.getFields());
    assertFalse(attachment.getFields().isEmpty());
    assertEquals(2, attachment.getFields().size());
    assertEquals("field 1", attachment.getFields().get(0).getTitle());
    assertNotNull(attachment.getActions());
    assertFalse(attachment.getActions().isEmpty());
    assertEquals(1, attachment.getActions().size());
    assertEquals("action 1", attachment.getActions().get(0).getName());
  }


}