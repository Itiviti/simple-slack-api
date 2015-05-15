package com.ullink.slack.simpleslackapi.impl;

import com.ullink.slack.simpleslackapi.events.SlackReplyEvent;
import com.ullink.slack.simpleslackapi.listeners.SlackEventListener;

interface SlackReplyListener extends SlackEventListener<SlackReplyEvent>
{

}
