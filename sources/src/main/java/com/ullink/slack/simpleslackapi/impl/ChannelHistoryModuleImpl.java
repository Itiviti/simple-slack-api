package com.ullink.slack.simpleslackapi.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackMessageHandle;
import com.ullink.slack.simpleslackapi.events.ReactionAdded;
import com.ullink.slack.simpleslackapi.events.ReactionRemoved;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;
import com.ullink.slack.simpleslackapi.replies.GenericSlackReply;
import org.threeten.bp.LocalDate;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.temporal.ChronoUnit;
import com.ullink.slack.simpleslackapi.ChannelHistoryModule;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.listeners.ReactionAddedListener;
import com.ullink.slack.simpleslackapi.listeners.ReactionRemovedListener;
import com.ullink.slack.simpleslackapi.listeners.SlackMessagePostedListener;

public class ChannelHistoryModuleImpl implements ChannelHistoryModule {

    private final SlackSession session;
    private static final String FETCH_CHANNEL_HISTORY_COMMAND = "conversations.history";
    private static final String FETCH_GROUP_HISTORY_COMMAND = "conversations.history";
    private static final String FETCH_IM_HISTORY_COMMAND = "conversations.history";
    private static final int DEFAULT_HISTORY_FETCH_SIZE = 1000;

    public ChannelHistoryModuleImpl(SlackSession session) {
        this.session = session;
    }

    @Override
    public SlackMessagePosted fetchMessageFromChannel(String channelId, String messageTimestamp) {
        Map<String, String> params = new HashMap<>();
        params.put("channel", channelId);
        params.put("count", "1");
        params.put("latest", messageTimestamp);
        params.put("inclusive", "true");
        SlackChannel channel = session.findChannelById(channelId);
        List<SlackMessagePosted> retrievedList;
        switch (channel.getType()) {
            case INSTANT_MESSAGING:
                retrievedList = fetchHistoryOfChannel(params,FETCH_IM_HISTORY_COMMAND, MessageSubTypeFilter.USERS_AND_INTERNAL_MESSAGES.getRetainedSubtypes());
                break;
            case PRIVATE_GROUP:
                retrievedList = fetchHistoryOfChannel(params,FETCH_GROUP_HISTORY_COMMAND, MessageSubTypeFilter.USERS_AND_INTERNAL_MESSAGES.getRetainedSubtypes());
                break;
            default:
                retrievedList = fetchHistoryOfChannel(params,FETCH_CHANNEL_HISTORY_COMMAND, MessageSubTypeFilter.USERS_AND_INTERNAL_MESSAGES.getRetainedSubtypes());
                break;
        }
        if (retrievedList.size() > 0) {
            return retrievedList.get(0);
        }
        return null;
    }

    @Override
    public List<SlackMessagePosted> fetchHistoryOfChannel(String channelId) {
        return fetchHistoryOfChannel(channelId, null, -1);
    }

    @Override
    public List<SlackMessagePosted> fetchHistoryOfChannel(String channelId, LocalDate day) {
        return fetchHistoryOfChannel(channelId, day, -1);
    }

    @Override
    public List<SlackMessagePosted> fetchHistoryOfChannel(String channelId, int numberOfMessages) {
        return fetchHistoryOfChannel(channelId, null, numberOfMessages);
    }

    @Override
    public List<SlackMessagePosted> fetchHistoryOfChannel(String channelId, LocalDate day, int numberOfMessages) {
        return fetchHistoryOfChannel(channelId, day, numberOfMessages, MessageSubTypeFilter.USERS_MESSAGES);
    }

    @Override
    public List<SlackMessagePosted> fetchHistoryOfChannel(String channelId, LocalDate day, int numberOfMessages, MessageSubTypeFilter filter) {
        return fetchHistoryOfChannel(channelId, day, numberOfMessages, filter.getRetainedSubtypes());
    }

    @Override
    public List<SlackMessagePosted> fetchHistoryOfChannel(String channelId, LocalDate day, int numberOfMessages, Set<String> allowedSubtypes) {
        Map<String, String> params = new HashMap<>();
        params.put("channel", channelId);
        if (day != null) {
            ZonedDateTime start = ZonedDateTime.of(day.atStartOfDay(), ZoneId.of("UTC"));
            ZonedDateTime end = ZonedDateTime.of(day.atStartOfDay().plusDays(1).minus(1, ChronoUnit.MILLIS), ZoneId.of("UTC"));
            params.put("oldest", convertDateToSlackTimestamp(start));
            params.put("latest", convertDateToSlackTimestamp(end));
        }
        if (numberOfMessages > -1) {
            params.put("count", String.valueOf(numberOfMessages));
        } else {
            params.put("count", String.valueOf(DEFAULT_HISTORY_FETCH_SIZE));
        }
        SlackChannel channel = session.findChannelById(channelId);
        switch (channel.getType()) {
            case INSTANT_MESSAGING:
                return fetchHistoryOfChannel(params,FETCH_IM_HISTORY_COMMAND, allowedSubtypes);
            case PRIVATE_GROUP:
                return fetchHistoryOfChannel(params,FETCH_GROUP_HISTORY_COMMAND, allowedSubtypes);
            default:
                return fetchHistoryOfChannel(params,FETCH_CHANNEL_HISTORY_COMMAND, allowedSubtypes);
        }
    }

    private List<SlackMessagePosted> fetchHistoryOfChannel(Map<String, String> params, String command, Set<String> retainedMessageSubtypes) {
        SlackMessageHandle<GenericSlackReply> handle = session.postGenericSlackCommand(params, command);
        GenericSlackReply replyEv = handle.getReply();
        String answer = replyEv.getPlainAnswer();
        JsonParser parser = new JsonParser();
        JsonObject jsonObject = parser.parse(answer).getAsJsonObject();
        JsonArray events = GsonHelper.getJsonArrayOrNull(jsonObject.get("messages"));
        List<SlackMessagePosted> messages = new ArrayList<>();
        if (events != null) {
            for (JsonElement eventJson : events) {
                JsonObject event = eventJson.getAsJsonObject();
                String subtype = GsonHelper.getStringOrNull(event.get("subtype"));
                if (subtype == null || retainedMessageSubtypes.contains(subtype)) {
                    messages.add((SlackMessagePosted) SlackJSONMessageParser.decode(session, event));
                }
            }
        }
        return messages;
    }

    @Override
    public List<SlackMessagePosted> fetchUpdatingHistoryOfChannel(String channelId) {
        return fetchUpdatingHistoryOfChannel(channelId, null, -1);
    }

    @Override
    public List<SlackMessagePosted> fetchUpdatingHistoryOfChannel(String channelId, LocalDate day) {
        return fetchUpdatingHistoryOfChannel(channelId, day, -1);
    }

    @Override
    public List<SlackMessagePosted> fetchUpdatingHistoryOfChannel(String channelId, int numberOfMessages) {
        return fetchUpdatingHistoryOfChannel(channelId, null, numberOfMessages);
    }

    @Override
    public List<SlackMessagePosted> fetchUpdatingHistoryOfChannel(String channelId, LocalDate day, int numberOfMessages) {
        List<SlackMessagePosted> messages = fetchHistoryOfChannel(channelId, day, numberOfMessages);
        session.addReactionAddedListener(new ChannelHistoryReactionAddedListener(messages));
        session.addReactionRemovedListener(new ChannelHistoryReactionRemovedListener(messages));
        session.addMessagePostedListener(new ChannelHistoryMessagePostedListener(messages));
        return messages;
    }

    public static class ChannelHistoryReactionAddedListener implements ReactionAddedListener {

        List<SlackMessagePosted> messages;

        public ChannelHistoryReactionAddedListener(List<SlackMessagePosted> initialMessages) {
            messages = initialMessages;
        }

        @Override
        public void onEvent(ReactionAdded event, SlackSession session) {
            String emojiName = event.getEmojiName();
            for (SlackMessagePosted message : messages) {
                for (String reaction : message.getReactions().keySet()) {
                    if (emojiName.equals(reaction)) {
                        int count = message.getReactions().get(emojiName);
                        message.getReactions().put(emojiName, ++count);
                        return;
                    }
                }
                message.getReactions().put(emojiName, 1);
            }
        }
    }

    public static class ChannelHistoryReactionRemovedListener implements ReactionRemovedListener {

        List<SlackMessagePosted> messages;

        public ChannelHistoryReactionRemovedListener(List<SlackMessagePosted> initialMessages) {
            messages = initialMessages;
        }

        @Override
        public void onEvent(ReactionRemoved event, SlackSession session) {
            String emojiName = event.getEmojiName();
            for (SlackMessagePosted message : messages) {
                for (String reaction : message.getReactions().keySet()) {
                    if (emojiName.equals(reaction)) {
                        int count = message.getReactions().get(emojiName);
                        if (count == 1) {
                            message.getReactions().remove(emojiName);
                        } else {
                            message.getReactions().put(emojiName, --count);
                        }
                        return;
                    }
                }
            }
        }
    }

    public static class ChannelHistoryMessagePostedListener implements SlackMessagePostedListener {

        List<SlackMessagePosted> messages;

        public ChannelHistoryMessagePostedListener(List<SlackMessagePosted> initialMessages) {
            messages = initialMessages;
        }

        @Override
        public void onEvent(SlackMessagePosted event, SlackSession session) {
            messages.add(event);
        }
    }

    private String convertDateToSlackTimestamp(ZonedDateTime date) {
        return (date.toInstant().toEpochMilli() / 1000) + ".123456";
    }

}
