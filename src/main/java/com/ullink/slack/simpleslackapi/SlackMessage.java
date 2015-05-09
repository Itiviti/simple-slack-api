package com.ullink.slack.simpleslackapi;

import java.util.HashMap;
import java.util.Map;
import com.ullink.slack.simpleslackapi.events.SlackMessageEvent;

public interface SlackMessage extends SlackMessageEvent
{

    public static enum SlackMessageSubType
    {
        CHANNEL_JOIN("channel_join"), MESSAGE_CHANGED("message_changed"), MESSAGE_DELETED("message_deleted"), BOT_MESSAGE("bot_message"), OTHER("-");

        private static final Map<String, SlackMessageSubType> CODE_MAP = new HashMap<>();

        static
        {
            for (SlackMessageSubType enumValue : SlackMessageSubType.values())
            {
                CODE_MAP.put(enumValue.getCode(), enumValue);
            }
        }

        String                                                code;

        public static SlackMessageSubType getByCode(String code)
        {
            SlackMessageSubType toReturn = CODE_MAP.get(code);
            if (toReturn == null)
            {
                return OTHER;
            }
            return toReturn;
        }

        SlackMessageSubType(String code)
        {
            this.code = code;
        }

        public String getCode()
        {
            return code;
        }
    }

    String getMessageContent();

    SlackUser getSender();

    SlackBot getBot();

    SlackChannel getChannel();

    SlackMessageSubType getSubType();

}
