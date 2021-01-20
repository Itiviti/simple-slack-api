package com.ullink.slack.simpleslackapi.impl;

import com.ullink.slack.simpleslackapi.SlackPresence;
import org.junit.Test;

import java.io.IOException;

import static com.ullink.slack.simpleslackapi.TestUtils.getFile;
import static com.ullink.slack.simpleslackapi.TestUtils.gson;
import static org.junit.Assert.*;

public class SlackPersonaImplTest {

  @Test
  public void testJsonSerialize() throws IOException {
    SlackPersonaImpl persona = SlackPersonaImpl.builder()
        .admin(false)
        .bot(true)
        .owner(false)
        .primaryOwner(false)
        .timeZone("CEST")
        .timeZoneLabel("Central European Summer Test")
        .userName("test")
        .id("test123")
        .profile(SlackProfileImpl.builder().displayName("test test").email("test@test.com").phone("1234").presence(SlackPresence.ACTIVE).build())
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
  }

  @Test
  public void testJsonDeserializeUnknownPresence() throws IOException {
    String json = getFile("/persona.unknown-presence.json");
    SlackPersonaImpl persona = gson().fromJson(json, SlackPersonaImpl.class);
    assertEquals(SlackPresence.UNKNOWN, persona.getPresence());
  }

}