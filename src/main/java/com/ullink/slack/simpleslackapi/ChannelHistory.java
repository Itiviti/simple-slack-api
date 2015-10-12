/*
 * (c) Copyright by Goodgame Studios
 */
package com.ullink.slack.simpleslackapi;

import com.ullink.slack.simpleslackapi.events.ReactionAdded;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;
import java.util.List;

/**
 *
 * @author okozaczenko
 */
public interface ChannelHistory {
    
    
    public List<SlackMessagePosted> getPostedMessageEvents();
    
    public List<ReactionAdded> getReactionAddedEvents();
    
}
