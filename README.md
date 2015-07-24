# Simple Slack API

This library allows an aplication to connect to [Slack](http://www.slack.com/) to receive and send messages from any channel as if it were a slack client. 

The main purpose of this library is to build slack bots able to react to channel events without having to configure any web hook.


## Short example

### Slack connection :

The connection is made through the SlackSessionFactory class :

#### Direct connection :

```java
SlackSession session = SlackSessionFactory.
  createWebSocketSlackSession("authenticationtoken",true);
session.connect();
```

#### Through a proxy :

```java
SlackSession session = SlackSessionFactory.
  createWebSocketSlackSession("authenticationtoken", Proxy.Type.HTTP, "myproxy", 1234,true);
session.connect();
```

### Listening to messages :

Slack messages can be retrieved by attaching a SlackMessageListener to the session provided by the factory :


```java
  session.addMessagePostedListener(new SlackMessagePostedListener()
  {
      @Override
      public void onEvent(SlackMessagePosted event, SlackSession session)
      {
          session.sendMessageOverWebSocket(session.findChannelByName("general"), "Message sent : " + event.getMessageContent(), null);
      }
  });
```

(Since v0.4.0, lambda friendly version)
```java
session.addMessagePostedListener((e, s) 
  -> s.sendMessageOverWebSocket(s.findChannelByName("general"), "Message sent : " + e.getMessageContent(), null));
```
### Message operation :

The SlackSession interface provides various methods to send/modify and delete messages on a given channel :
```java
SlackMessageHandle sendMessage(SlackChannel channel, String message, SlackAttachment attachment);
SlackMessageHandle sendMessage(SlackChannel channel, String message, SlackAttachment attachment, SlackChatConfiguration chatConfiguration);
SlackMessageHandle updateMessage(String timeStamp, SlackChannel channel, String message);
SlackMessageHandle deleteMessage(String timeStamp, SlackChannel channel)
```        
(Since v0.4.3, emoji reactions on channel messages)

The SlackSession interface provides a method to add an emoji reaction to a message on a given channel :
```java
SlackMessageHandle addReactionToMessage(SlackChannel channel, String messageTimeStamp, String emojiCode);
```        


### Supported events :

The following events are handled :

* message post on a channel / group / to the bot
* message update on a channel / group / to the bot
* message delete on a channel / group / to the bot
* channel creation
* channel deletion
* channel archiving
* channel unarchiving
* channel renaming
* private group joining
 
## Full example :

Here's a full example with an echoing bot using this library :
```java
public class Example
{

  public static void main(String[] args) throws Exception
  {

    final SlackSession session = SlackSessionFactory.
      createWebSocketSlackSession("authenticationtoken", Proxy.Type.HTTP, "myproxy", 1234, true);
    session.addMessagePostedListener(new SlackMessagePostedListener()
    {
        @Override
        public void onEvent(SlackMessagePosted event, SlackSession session)
        {
          //let's send a message
          SlackMessageHandle handle = session.sendMessage(slackMessage.getChannel(),
                              slackMessage.getMessageContent(), null);
          try
          {
              Thread.sleep(2000);
          } catch (InterruptedException e)
          {
              e.printStackTrace();
          }
          //2 secs later, let's update the message (I can only update my own messages)
          session.updateMessage(handle.getSlackReply().getTimestamp(),slackMessage.getChannel(),
                                slackMessage.getMessageContent()+" UPDATED");
          try
          {
              Thread.sleep(2000);
          } catch (InterruptedException e)
          {
              e.printStackTrace();
          }
          //2 secs later, let's now delete the message (I can only delete my own messages)
          session.deleteMessage(handle.getSlackReply().getTimestamp(),slackMessage.getChannel())
        }
      });
    session.connect();

    while (true)
    {
      Thread.sleep(1000);
    }
  }
}
```        

# License

This library is licensed under the [Creative Commons CC0 1.0 Universal](http://creativecommons.org/publicdomain/zero/1.0/) license with no warranty (expressed or implied) for any purpose.
