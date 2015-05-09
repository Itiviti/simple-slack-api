# Simple Slack API

This library allows an aplication to connect to [Slack](http://www.slack.com/) to receive and send messages from any channel as if it were a slack client. The main purpose of this library is to build slack bots able to react to channel events without having to configure any web hook. For now this library has very limited features and is quite experimental.


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
session.addMessageListener(new SlackMessageListener()
  {
    @Override
    public void onSessionLoad(SlackSession slackSession)
    {
      // is called when the session is connected
    }

    @Override
    public void onMessage(SlackMessage slackMessage)
    {
      // is called every time a message is sent on a subscribed channel
    }
  });
```        
### Message operation :

The SlackSession interface provides various methods to send/modify and delete messages on a given channel :
```java
SlackMessageHandle sendMessage(SlackChannel channel, String message, SlackAttachment attachment);
SlackMessageHandle sendMessage(SlackChannel channel, String message, SlackAttachment attachment, SlackChatConfiguration chatConfiguration);
SlackMessageHandle updateMessage(String timeStamp, SlackChannel channel, String message);
SlackMessageHandle deleteMessage(String timeStamp, SlackChannel channel)
```        

 
## Full example :

Here's a full example with an echoing bot using this library :
```java
public class Example
{

  public static void main(String[] args) throws Exception
  {

    final SlackSession session = SlackSessionFactory.
      createWebSocketSlackSession("authenticationtoken", Proxy.Type.HTTP, "myproxy", 1234, true);
      
    session.addMessageListener(new SlackMessageListener()
      {
        @Override
        public void onSessionLoad(SlackSession slackSession)
        {
        }

        @Override
        public void onMessage(SlackMessage slackMessage)
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
