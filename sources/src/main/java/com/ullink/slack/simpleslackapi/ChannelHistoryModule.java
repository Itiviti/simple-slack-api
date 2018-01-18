package com.ullink.slack.simpleslackapi;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;
import org.threeten.bp.LocalDate;

public interface ChannelHistoryModule {

    enum MessageSubTypeFilter {

        USERS_AND_INTERNAL_MESSAGES() {
            @Override public Set<String> getRetainedSubtypes()
            {
                return USERS_AND_INTERNAL_MESSAGES_CODE_SET;
            }
        },
        USERS_MESSAGES() {
            @Override public Set<String> getRetainedSubtypes()
            {
                return USERS_MESSAGES_CODE_SET;
            }
        };
        abstract public Set<String> getRetainedSubtypes();

        Set<String> USERS_AND_INTERNAL_MESSAGES_CODE_SET = getUsersAndInternalMessagesSubTypesCode();
        Set<String> USERS_MESSAGES_CODE_SET = getUsersMessagesSubTypesCode();

        private Set<String> getUsersAndInternalMessagesSubTypesCode() {
            Set<String> toReturn = new HashSet<>();
            for (SlackMessagePosted.MessageSubType subType : SlackMessagePosted.MessageSubType.values()) {
                toReturn.add(subType.getCode());
            }
            return toReturn;
        }

        private Set<String> getUsersMessagesSubTypesCode() {
            Set<String> toReturn = new HashSet<>();
            toReturn.add(SlackMessagePosted.MessageSubType.BOT_MESSAGE.getCode());
            return toReturn;
        }

    }

    SlackMessagePosted fetchMessageFromChannel(String channelId, String messageTimestamp);

    List<SlackMessagePosted> fetchHistoryOfChannel(String channelName);

    List<SlackMessagePosted> fetchHistoryOfChannel(String channelName, LocalDate day);

    List<SlackMessagePosted> fetchHistoryOfChannel(String channelName, int numberOfMessages);

    List<SlackMessagePosted> fetchHistoryOfChannel(String channelName, LocalDate day, int numberOfMessages);

    List<SlackMessagePosted> fetchHistoryOfChannel(String channelId, LocalDate day, int numberOfMessages, MessageSubTypeFilter filter);

    List<SlackMessagePosted> fetchHistoryOfChannel(String channelId, LocalDate day, int numberOfMessages, Set<String> allowedSubtypes);

    List<SlackMessagePosted> fetchUpdatingHistoryOfChannel(String channelId);
    
    List<SlackMessagePosted> fetchUpdatingHistoryOfChannel(String channelId, LocalDate day);

    List<SlackMessagePosted> fetchUpdatingHistoryOfChannel(String channelId, int numberOfMessages);

    List<SlackMessagePosted> fetchUpdatingHistoryOfChannel(String channelId, LocalDate day, int numberOfMessages);

    
}
