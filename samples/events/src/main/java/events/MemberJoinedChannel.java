package events;

import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.SlackUser;
import com.ullink.slack.simpleslackapi.events.*;
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory;
import com.ullink.slack.simpleslackapi.listeners.SlackChannelJoinedListener;
import com.ullink.slack.simpleslackapi.listeners.SlackChannelLeftListener;
import com.ullink.slack.simpleslackapi.listeners.SlackMemberJoinedListener;
import com.ullink.slack.simpleslackapi.listeners.SlackMessagePostedListener;

import java.io.IOException;

public class MemberJoinedChannel {
    public static void main(String[] args) throws IOException {
        SlackSession session = SlackSessionFactory.createWebSocketSlackSession(" xoxb..... - add bot auth token here");
        session.connect();
        SlackChannel channel = session.findChannelByName("general"); //make sure bot is a member of the channel.

        //optional item
        //send a message to the channel when connection is established
        session.sendMessage(channel, "hello world, i'm a bot and I just connected" );

        // Define the Member Joined listener
		SlackMemberJoinedListener slackMemberJoinedListener = new SlackMemberJoinedListener() {
           	public void onEvent(SlackMemberJoined event, SlackSession session) {
				SlackChannel slackChannel = event.getChannel();
                if(event.getChannel()==null)
                    System.out.println("Error: Getting slack channel as null");

                //optional item
                //send a message to the channel when a new member joins the channel
                session.sendMessage(event.getChannel(),"Welcome to the channel :) !");
				System.out.println(event.toString());
			}
		};

        //add it to the session
        session.addSlackMemberJoinedListener(slackMemberJoinedListener);
    }
}
