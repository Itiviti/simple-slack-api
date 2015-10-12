/*
 * (c) Copyright by Goodgame Studios
 */
package com.ullink.slack.simpleslackapi.impl;

import com.ullink.slack.simpleslackapi.ChannelHistory;
import com.ullink.slack.simpleslackapi.events.ReactionAdded;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author okozaczenko
 */
public class ChannelHistoryImpl implements ChannelHistory {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChannelHistoryImpl.class);

    private List<SlackMessagePosted> messages = new ArrayList<>();
    private List<ReactionAdded> reactions = new ArrayList<>();

    @Override
    public List<SlackMessagePosted> getPostedMessageEvents() {
        return messages;
    }

    @Override
    public List<ReactionAdded> getReactionAddedEvents() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
