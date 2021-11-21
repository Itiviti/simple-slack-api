package com.ullink.slack.simpleslackapi.utils;

import com.ullink.slack.simpleslackapi.SlackStatus;
import org.junit.Test;
import static org.junit.Assert.*;

public class StatusTest {
    @Test
    public void testStatus() {
        SlackStatus actualSlackStatus = (new SlackStatus()).setEmoji("test_emoji").setText("test_status");
        assertEquals("test_status", actualSlackStatus.getText());
        assertEquals("test_emoji", actualSlackStatus.getEmoji());
    }
}
