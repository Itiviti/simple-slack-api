package com.ullink.slack.simpleslackapi.impl;

import com.ullink.slack.simpleslackapi.SlackPresence;
import com.ullink.slack.simpleslackapi.SlackStatus;
import org.junit.Test;

import java.io.IOException;

import static com.ullink.slack.simpleslackapi.TestUtils.getFile;
import static com.ullink.slack.simpleslackapi.TestUtils.gson;
import static org.junit.Assert.*;

public class SlackPersonaImplTest {

  /**
   * CS427 Issue link: https://github.com/Itiviti/simple-slack-api /issues/196
   */
  @Test
  public void testJsonSerialize() throws IOException {
    String statusText = "test_status";
    String statusEmoji = "test_emoji";
    SlackStatus testSlackStatus = (new SlackStatus())
        .setEmoji(statusEmoji)
        .setText(statusText);
    SlackPersonaImpl persona = SlackPersonaImpl.builder()
        .admin(false)
        .bot(true)
        .owner(false)
        .primaryOwner(false)
        .timeZone("CEST")
        .timeZoneLabel("Central European Summer Test")
        .userName("test")
        .id("test123")
        .profile(SlackProfileImpl.builder().displayName("test test").email("test@test.com").phone("1234").status(testSlackStatus).presence(SlackPresence.ACTIVE).build())
        .build();
    String json = gson().toJson(persona);
    assertNotNull(json);
    assertEquals(getFile("/persona.json"), json);
  }

  @Test
  public void testJsonDeserialize() throws IOException {
    String json = getFile("/persona.json");
    SlackPersonaImpl persona = gson().fromJson(json, SlackPersonaImpl.class);
    assertEquals("test", persona.getUserName());
    assertEquals("CEST", persona.getTimeZone());
    assertEquals("test123", persona.getId());
    assertNotNull(persona.getProfile());
    assertEquals("test@test.com", persona.getUserMail());
    assertEquals(SlackPresence.ACTIVE, persona.getPresence());

    SlackStatus expectedSlackStatus = persona.getStatus();
    assertEquals("test_status", expectedSlackStatus.getText());
    assertEquals("test_emoji", expectedSlackStatus.getEmoji());
  }

  @Test
  public void testJsonDeserializeUnknownField() throws IOException {
    String json = getFile("/persona.unknown-field.json");
    SlackPersonaImpl persona = gson().fromJson(json, SlackPersonaImpl.class);
    assertEquals("test", persona.getUserName());
    assertEquals("CEST", persona.getTimeZone());
    assertEquals("test123", persona.getId());
    assertNotNull(persona.getProfile());
    assertEquals("test@test.com", persona.getUserMail());
    assertEquals(SlackPresence.UNKNOWN, persona.getPresence());

    SlackStatus expectedSlackStatus = persona.getStatus();
    assertEquals("test_status", expectedSlackStatus.getText());
    assertEquals("test_emoji", expectedSlackStatus.getEmoji());
  }

  @Test
  public void testJsonDeserializeUnknownPresence() throws IOException {
    String json = getFile("/persona.unknown-presence.json");
    SlackPersonaImpl persona = gson().fromJson(json, SlackPersonaImpl.class);
    assertEquals(SlackPresence.UNKNOWN, persona.getPresence());
  }

}