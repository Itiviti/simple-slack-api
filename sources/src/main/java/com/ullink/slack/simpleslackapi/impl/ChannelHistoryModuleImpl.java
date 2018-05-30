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
    private static final String FETCH_CHANNEL_HISTORY_COMMAND = "channels.history";
    private static final String FETCH_GROUP_HISTORY_COMMAND = "groups.history";
    private static final String FETCH_IM_HISTORY_COMMAND = "im.history";
    private static final int DEFAULT_HISTORY_FETCH_SIZE = 1000;

    public ChannelHistoryModuleImpl(SlackSession session) {
        this.session = session;
    }

    @Override
    public SlackMessagePosted fetchMessageFromChannel(String channelId, String messageTimestamp) {
        List<SlackMessagePosted> history;
        history = fetchHistoryOfChannel(channelId, messageTimestamp, 1);
        if (history != null) {
            return history.get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<SlackMessagePosted> fetchHistoryOfChannel(String channelId) {
        return fetchHistoryOfChannel(channelId, (LocalDate) null);
    }

    @Override
    public List<SlackMessagePosted> fetchHistoryOfChannel(String channelId, String messageTimestamp) {
        return fetchHistoryOfChannel(channelId, messageTimestamp, -1);
    }

    @Override
    public List<SlackMessagePosted> fetchHistoryOfChannel(String channelId, String messageTimestamp, int numberOfMessages) {
        return fetchHistoryOfChannel(channelId, messageTimestamp, null, numberOfMessages);
    }

    @Override
    public List<SlackMessagePosted> fetchHistoryOfChannel(String channelId, String latest, String oldest, int numberOfMessages) {
        return fetchHistoryOfChannel(channelId, latest, oldest, numberOfMessages, null);
    }

    @Override
    public List<SlackMessagePosted> fetchHistoryOfChannel(String channelId, String latest, String oldest, int numberOfMessages, Set<String> allowedSubtypes) {
        return fetchHistoryOfChannel(channelId, latest, oldest, numberOfMessages, allowedSubtypes);
    }

    @Override
    public List<SlackMessagePosted> fetchHistoryOfChannel(String channelId, String latest, String oldest, int numberOfMessages, Set<String> allowedSubtypes, boolean listenForUpdates) {
        Map<String, String> params = new HashMap<>();
        params.put("channel", channelId);
        if (numberOfMessages > -1) {
            params.put("count", String.valueOf(numberOfMessages));
        } else {
            params.put("count", String.valueOf(DEFAULT_HISTORY_FETCH_SIZE));
        }

        if (oldest != null) {
            params.put("oldest", oldest);
        }

        if (latest != null) {
            params.put("latest", latest);
        }

        if (allowedSubtypes == null) {
            allowedSubtypes = MessageSubTypeFilter.USERS_AND_INTERNAL_MESSAGES.getRetainedSubtypes();
        }
        params.put("inclusive", "true");
        SlackChannel channel = session.findChannelById(channelId);
        List<SlackMessagePosted> retrievedList;
        switch (channel.getType()) {
            case INSTANT_MESSAGING:
                retrievedList = fetchRawHistoryOfChannel(params,FETCH_IM_HISTORY_COMMAND, allowedSubtypes);
                break;
            case PRIVATE_GROUP:
                retrievedList = fetchRawHistoryOfChannel(params,FETCH_GROUP_HISTORY_COMMAND, allowedSubtypes);
                break;
            default:
                retrievedList = fetchRawHistoryOfChannel(params,FETCH_CHANNEL_HISTORY_COMMAND, allowedSubtypes);
                break;
        }
        if (retrievedList != null && retrievedList.size()>0) {
            if (listenForUpdates) {
                session.addReactionAddedListener(new ChannelHistoryReactionAddedListener(retrievedList));
                session.addReactionRemovedListener(new ChannelHistoryReactionRemovedListener(retrievedList));
                session.addMessagePostedListener(new ChannelHistoryMessagePostedListener(retrievedList));
            }
            return retrievedList;
        }
        return null;
    }

    @Override
    public List<SlackMessagePosted> fetchHistoryOfChannel(String channelId, LocalDate day) {
        return fetchHistoryOfChannel(channelId, day, -1);
    }

    @Override
    public List<SlackMessagePosted> fetchHistoryOfChannel(String channelId, int numberOfMessages) {
        return fetchHistoryOfChannel(channelId, (LocalDate) null, numberOfMessages);
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
    public List<SlackMessagePosted> fetchHistoryOfChannel(String channelId, int numberOfMessages, Set<String> allowedSubtypes) {
        return fetchHistoryOfChannel(channelId, null, null, numberOfMessages, allowedSubtypes);
    }

    @Override
    public List<SlackMessagePosted> fetchHistoryOfChannel(String channelId, LocalDate day, int numberOfMessages, Set<String> allowedSubtypes) {
        if (day != null) {
            return fetchHistoryOfChannel(channelId, numberOfMessages, allowedSubtypes);
        } else {
            return fetchHistoryOfChannel(channelId,
                    convertDateToSlackTimestamp(ZonedDateTime.of(day.atStartOfDay(), ZoneId.of("UTC"))),
                    convertDateToSlackTimestamp(ZonedDateTime.of(day.atStartOfDay().plusDays(1).minus(1, ChronoUnit.MILLIS), ZoneId.of("UTC"))),
                    numberOfMessages,
                    allowedSubtypes);

        }
    }

    private List<SlackMessagePosted> fetchRawHistoryOfChannel(Map<String, String> params, String command, Set<String> retainedMessageSubtypes) {
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

    public static class ChannelHistoryReactionAddedListener implements ReactionAddedListener {

        List<SlackMessagePosted> messages = new ArrayList<>();

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
    };

    public static class ChannelHistoryReactionRemovedListener implements ReactionRemovedListener {

        List<SlackMessagePosted> messages = new ArrayList<>();

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

        List<SlackMessagePosted> messages = new ArrayList<>();

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
