package com.ullink.slack.simpleslackapi.impl;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.assertj.core.api.Assertions.assertThat;

public class TestSlackJSONSessionStatusParser
{
    @Test
    public void testParsingSessionDescription() throws Exception
    {
        InputStream stream = getClass().getResourceAsStream("/test_json.json");
        InputStreamReader isReader = new InputStreamReader(stream);
        BufferedReader reader = new BufferedReader(isReader);
        StringBuilder strBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            strBuilder.append(line);
        }

      SlackJSONSessionStatusParser parser = new SlackJSONSessionStatusParser(strBuilder.toString(), null);
        parser.parse();

        assertThat(parser.getChannels()).containsOnlyKeys("CHANNELID1", "CHANNELID2", "CHANNELID3", "GROUPID1", "DIM01");
        assertThat(parser.getUsers()).containsOnlyKeys("USERID1","USERID2","USERID3","USERID4","BOTID1","BOTID2");
        assertThat(parser.getWebSocketURL()).isEqualTo("wss://mywebsocketurl");
        assertThat(parser.getUsers().get("USERID1").getTimeZone()).isEqualTo("Europe/Amsterdam");
        assertThat(parser.getUsers().get("USERID1").getTimeZoneLabel()).isEqualTo("Central European Summer Time");
        assertThat(parser.getUsers().get("USERID1").getTimeZoneOffset()).isEqualTo(7200);

        assertThat(parser.getSessionPersona().getId()).isEqualTo("SELF");
        assertThat(parser.getSessionPersona().getUserName()).isEqualTo("myself");

        assertThat(parser.getTeam().getId()).isEqualTo("TEAM");
        assertThat(parser.getTeam().getName()).isEqualTo("Example Team");
        assertThat(parser.getTeam().getDomain()).isEqualTo("example");

        assertThat(parser.getIntegrations().get("INTEGRATION1").getName()).isEqualTo("bot1");
        assertThat(parser.getIntegrations().get("INTEGRATION1").isDeleted()).isEqualTo(false);
        assertThat(parser.getIntegrations().get("INTEGRATION2").getName()).isEqualTo("bot2");
        assertThat(parser.getIntegrations().get("INTEGRATION2").isDeleted()).isEqualTo(true);
    }
}
