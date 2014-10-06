# Simple Slack API

This library allows an aplication to connect to [Slack](http://www.slack.com/) to receive and send messages from any channel as if it were a slack client. The main purpose of this library is to build slack bots able to react to channel events without having to configure any web hook. For now this library has very limited features and is quite experimental.


## Short example

### Slack connection :

The connection is made through the SlackSessionFactory class :
```
SlackSession session = SlackSessionFactory.
  createWebSocketSlackSession("authenticationtoken", Proxy.Type.HTTP, "myproxy", 1234);
session.connect();
```

### Listening to messages :

Slack messages can be retrieved by attaching a SlackMessageListener to the session provided by the factory :
```        
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
### Sending messages :

The SlackSession interface provides a method to send a message on a given channel :
```        
 session.sendMessage(SlackChannel channel, String messageContent, 
                    String publishersName, String pathToPublishersIcon);
```        
 
## Full example :

Here's a full example with an echoing bot using this library :
```        
public class Example
{

  public static void main(String[] args) throws Exception
  {

    final SlackSession session = SlackSessionFactory.
      createWebSocketSlackSession("authenticationtoken", Proxy.Type.HTTP, "myproxy", 1234);
      
    session.addMessageListener(new SlackMessageListener()
      {
        @Override
        public void onSessionLoad(SlackSession slackSession)
        {
        }

        @Override
        public void onMessage(SlackMessage slackMessage)
        {
          session.sendMessage(slackMessage.getChannel(),slackMessage.getMessageContent(),
                              slackMessage.getSender().getUserName(), 
                              "http://youriconurl.com/icon.jpg");
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
