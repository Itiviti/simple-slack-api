package com.ullink.slack.simpleslackapi.impl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.assertj.core.api.Assertions;
import org.junit.Test;

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
        SlackJSONSessionStatusParser parser = new SlackJSONSessionStatusParser(strBuilder.toString());
        parser.parse();
        Assertions.assertThat(parser.getChannels()).containsOnlyKeys("CHANNELID1","CHANNELID2","CHANNELID3","GROUPID1","DIM01");
        Assertions.assertThat(parser.getUsers()).containsOnlyKeys("USERID1","USERID2","USERID3","USERID4","BOTID1","BOTID2");
        Assertions.assertThat(parser.getWebSocketURL()).isEqualTo("wss://mywebsocketurl");
    }
}
