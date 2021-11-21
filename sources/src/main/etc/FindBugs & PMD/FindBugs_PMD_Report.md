This is a Slack Member_Joined_Channel feature issue, where I had to contribute to multiple existing files and create a couple of new files to implement a new type of Listener.
So, I run the report for this feature's module including all the pre-exsiting files.
I used an Intellij plugin - QAPlug to run Findbugs and PMD report.

Although there are reports shown in the report file here, there are no new errors caused by my contribution.


Please note that my contributions affected only the following files:

  1. [SlackMemberJoined.java](https://github.com/rajanpatel4578/simple-slack-api/blob/MemberJoinedListener/sources/src/main/java/com/ullink/slack/simpleslackapi/events/SlackMemberJoined.java)
  2. [SlackMemberJoinedListener.java](https://github.com/rajanpatel4578/simple-slack-api/blob/MemberJoinedListener/sources/src/main/java/com/ullink/slack/simpleslackapi/listeners/SlackMemberJoinedListener.java)
  3. [AbstractSlackSessionImpl.java](https://github.com/rajanpatel4578/simple-slack-api/blob/MemberJoinedListener/sources/src/main/java/com/ullink/slack/simpleslackapi/impl/AbstractSlackSessionImpl.java)
  4. [SlackWebSocketSessionImpl.java](https://github.com/rajanpatel4578/simple-slack-api/blob/MemberJoinedListener/sources/src/main/java/com/ullink/slack/simpleslackapi/impl/SlackWebSocketSessionImpl.java)
  5. [SlackSession.java](https://github.com/rajanpatel4578/simple-slack-api/blob/MemberJoinedListener/sources/src/main/java/com/ullink/slack/simpleslackapi/SlackSession.java)
  6. [SlackSessionWrapper.java](https://github.com/rajanpatel4578/simple-slack-api/blob/MemberJoinedListener/sources/src/main/java/com/ullink/slack/simpleslackapi/SlackSessionWrapper.java)
  7. [SlackEventType.java](https://github.com/rajanpatel4578/simple-slack-api/blob/MemberJoinedListener/sources/src/main/java/com/ullink/slack/simpleslackapi/events/SlackEventType.java)
  8. [EventType.java](https://github.com/rajanpatel4578/simple-slack-api/blob/MemberJoinedListener/sources/src/main/java/com/ullink/slack/simpleslackapi/events/EventType.java)
  9. [SlackJSONMessageParser.java](https://github.com/rajanpatel4578/simple-slack-api/blob/MemberJoinedListener/sources/src/main/java/com/ullink/slack/simpleslackapi/impl/SlackJSONMessageParser.java)
