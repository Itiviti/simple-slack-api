package com.ullink.slack.simpleslackapi.impl;

import com.google.common.io.CharStreams;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ullink.slack.simpleslackapi.*;
import com.ullink.slack.simpleslackapi.events.*;
import com.ullink.slack.simpleslackapi.impl.SlackChatConfiguration.Avatar;
import com.ullink.slack.simpleslackapi.listeners.SlackEventListener;
import com.ullink.slack.simpleslackapi.replies.*;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.ConnectException;
import java.net.Proxy;
import java.net.URI;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

class SlackWebSocketSessionImpl extends AbstractSlackSessionImpl implements SlackSession, MessageHandler.Whole<String> {
    private static final String SLACK_API_SCHEME = "https";

    private static final String SLACK_API_HOST = "slack.com";

    private static final String SLACK_API_PATH = "/api";

    private static final String SLACK_API_HTTPS_ROOT      = SLACK_API_SCHEME + "://" + SLACK_API_HOST + SLACK_API_PATH + "/";

    private static final String DIRECT_MESSAGE_OPEN_CHANNEL_COMMAND = "im.open";

    private static final String MULTIPARTY_DIRECT_MESSAGE_OPEN_CHANNEL_COMMAND = "mpim.open";

    private static final String CHANNELS_LEAVE_COMMAND    = "channels.leave";

    private static final String CHANNELS_JOIN_COMMAND     = "channels.join";

    private static final String CHANNELS_SET_TOPIC_COMMAND     = "channels.setTopic";
    
    private static final String CHANNELS_INVITE_COMMAND     = "channels.invite";
    
    private static final String CHANNELS_ARCHIVE_COMMAND     = "channels.archive";

    private static final String CHAT_POST_MESSAGE_COMMAND = "chat.postMessage";

    private static final String FILE_UPLOAD_COMMAND       = "files.upload";

    private static final String CHAT_DELETE_COMMAND       = "chat.delete";

    private static final String CHAT_UPDATE_COMMAND       = "chat.update";

    private static final String REACTIONS_ADD_COMMAND     = "reactions.add";
    
    private static final String INVITE_USER_COMMAND     = "users.admin.invite";

    private static final String SET_PERSONA_ACTIVE = "users.setPresence";

    private static final String LIST_EMOJI_COMMAND = "emoji.list";

    private static final String LIST_USERS = "users.list";


    private static final Logger               LOGGER                     = LoggerFactory.getLogger(SlackWebSocketSessionImpl.class);

    private static final String               SLACK_HTTPS_AUTH_URL       = "https://slack.com/api/rtm.start?token=";

    private  static final int                 DEFAULT_HEARTBEAT_IN_MILLIS = 30000;

    private Session                           websocketSession;
    private String                            authToken;
    private String                            proxyAddress;
    private int                               proxyPort                  = -1;
    HttpHost                                  proxyHost;
    private long                              lastPingSent;
    private volatile long                     lastPingAck;

    private AtomicLong                        messageId = new AtomicLong();

    private boolean                           reconnectOnDisconnection;
    private boolean                           wantDisconnect;

    private Thread                            connectionMonitoringThread;
    private EventDispatcher                   dispatcher                 = new EventDispatcher();
    private long                              heartbeat;
    private WebSocketContainerProvider        webSocketContainerProvider;


    @Override
    public SlackMessageHandle<SlackMessageReply> sendMessageToUser(SlackUser user, String message, SlackAttachment attachment) {
        SlackChannel iMChannel = getIMChannelForUser(user);
        return sendMessage(iMChannel, message, attachment, DEFAULT_CONFIGURATION);
    }

    @Override
    public SlackMessageHandle<SlackMessageReply> sendMessageToUser(String userName, String message, SlackAttachment attachment) {
        return sendMessageToUser(findUserByUserName(userName), message, attachment);
    }

    private List<SlackChannel> getAllIMChannels() {
        Collection<SlackChannel> allChannels = getChannels();
        List<SlackChannel> iMChannels = new ArrayList<>();
        for (SlackChannel channel : allChannels) {
            if (channel.isDirect()) {
                iMChannels.add(channel);
            }
        }
        return iMChannels;
    }

    private SlackChannel getIMChannelForUser(SlackUser user) {
        List<SlackChannel> imcs = getAllIMChannels();
        for (SlackChannel channel : imcs) {
            if (channel.getMembers().contains(user)) {
                return channel;
            }
        }
        SlackMessageHandle<SlackChannelReply> reply = openDirectMessageChannel(user);
        return reply.getReply().getSlackChannel();
    }

    public class EventDispatcher {

        void dispatch(SlackEvent event) {
            switch (event.getEventType()) {
                case SLACK_CHANNEL_ARCHIVED:
                    dispatchImpl((SlackChannelArchived) event, channelArchiveListener);
                    break;
                case SLACK_CHANNEL_CREATED:
                    dispatchImpl((SlackChannelCreated) event, channelCreateListener);
                    break;
                case SLACK_CHANNEL_DELETED:
                    dispatchImpl((SlackChannelDeleted) event, channelDeleteListener);
                    break;
                case SLACK_CHANNEL_RENAMED:
                    dispatchImpl((SlackChannelRenamed) event, channelRenamedListener);
                    break;
                case SLACK_CHANNEL_UNARCHIVED:
                    dispatchImpl((SlackChannelUnarchived) event, channelUnarchiveListener);
                    break;
                case SLACK_CHANNEL_JOINED:
                    dispatchImpl((SlackChannelJoined) event, channelJoinedListener);
                    break;
                case SLACK_CHANNEL_LEFT:
                    dispatchImpl((SlackChannelLeft) event, channelLeftListener);
                    break;
                case SLACK_GROUP_JOINED:
                    dispatchImpl((SlackGroupJoined) event, groupJoinedListener);
                    break;
                case SLACK_MESSAGE_DELETED:
                    dispatchImpl((SlackMessageDeleted) event, messageDeletedListener);
                    break;
                case SLACK_MESSAGE_POSTED:
                    dispatchImpl((SlackMessagePosted) event, messagePostedListener);
                    break;
                case SLACK_MESSAGE_UPDATED:
                    dispatchImpl((SlackMessageUpdated) event, messageUpdatedListener);
                    break;
                case SLACK_CONNECTED:
                    dispatchImpl((SlackConnected) event, slackConnectedListener);
                    break;
                case REACTION_ADDED:
                    dispatchImpl((ReactionAdded) event, reactionAddedListener);
                    break;
                case REACTION_REMOVED:
                    dispatchImpl((ReactionRemoved) event, reactionRemovedListener);
                    break;
                case SLACK_USER_CHANGE:
                    dispatchImpl((SlackUserChange) event, slackUserChangeListener);
                    break;
                case PIN_ADDED:
                    dispatchImpl((PinAdded) event, pinAddedListener);
                    break;
                case PIN_REMOVED:
                    dispatchImpl((PinRemoved) event, pinRemovedListener);
                    break;
                case PRESENCE_CHANGE:
                    dispatchImpl((PresenceChange) event, presenceChangeListener);
                    break;
                case SLACK_DISCONNECTED:
                    dispatchImpl((SlackDisconnected) event, slackDisconnectedListener);
                    break;
                case USER_TYPING:
                    dispatchImpl((UserTyping) event, userTypingListener);
                    break;
                case UNKNOWN:
                    throw new IllegalArgumentException("event of type " + event.getEventType() + " not handled: " + event);
            }
        }

        private <E extends SlackEvent, L extends SlackEventListener<E>> void dispatchImpl(E event, List<L> listeners) {
            for (L listener : listeners) {
                try {
                    listener.onEvent(event, SlackWebSocketSessionImpl.this);
                } catch (Throwable thr) {
                    LOGGER.error("caught exception in dispatchImpl", thr);
                }
            }
        }
    }

    SlackWebSocketSessionImpl(WebSocketContainerProvider webSocketContainerProvider, String authToken, boolean reconnectOnDisconnection, long heartbeat, TimeUnit unit) {
        this.authToken = authToken;
        this.reconnectOnDisconnection = reconnectOnDisconnection;
        this.heartbeat = heartbeat != 0 ? unit.toMillis(heartbeat) : 30000;
        this.webSocketContainerProvider = webSocketContainerProvider != null ? webSocketContainerProvider : new DefaultWebSocketContainerProvider(null,0);
    }

    SlackWebSocketSessionImpl(WebSocketContainerProvider webSocketContainerProvider, String authToken, Proxy.Type proxyType, String proxyAddress, int proxyPort, boolean reconnectOnDisconnection, long heartbeat, TimeUnit unit) {
        this.authToken = authToken;
        this.proxyAddress = proxyAddress;
        this.proxyPort = proxyPort;
        this.proxyHost = new HttpHost(proxyAddress, proxyPort);
        this.reconnectOnDisconnection = reconnectOnDisconnection;
        this.heartbeat = heartbeat != 0 ? unit.toMillis(heartbeat) : DEFAULT_HEARTBEAT_IN_MILLIS;
        this.webSocketContainerProvider = webSocketContainerProvider != null ? webSocketContainerProvider : new DefaultWebSocketContainerProvider(proxyAddress,proxyPort);
    }

    @Override
    public void connect() throws IOException {
        wantDisconnect = false;
        connectImpl();
        LOGGER.debug("starting actions monitoring");
        startConnectionMonitoring();
    }

    @Override
    public void disconnect() {
        wantDisconnect = true;
        LOGGER.debug("Disconnecting from the Slack server");
        disconnectImpl();
        stopConnectionMonitoring();
    }
    @Override
    public boolean isConnected()
    {
        return websocketSession != null && websocketSession.isOpen();
    }

    private void connectImpl() throws IOException
    {
        LOGGER.info("connecting to slack");
        lastPingSent = 0;
        lastPingAck = 0;
        HttpClient httpClient = getHttpClient();
        HttpGet request = new HttpGet(SLACK_HTTPS_AUTH_URL + authToken);
        HttpResponse response;
        response = httpClient.execute(request);
        LOGGER.debug(response.getStatusLine().toString());
        String jsonResponse = consumeToString(response.getEntity().getContent());
        SlackJSONSessionStatusParser sessionParser = new SlackJSONSessionStatusParser(jsonResponse);
        sessionParser.parse();
        if (sessionParser.getError() != null)
        {
            LOGGER.error("Error during authentication : " + sessionParser.getError());
            throw new ConnectException(sessionParser.getError());
        }

        users = sessionParser.getUsers();
        integrations = sessionParser.getIntegrations();
        channels = sessionParser.getChannels();
        sessionPersona = sessionParser.getSessionPersona();
        team = sessionParser.getTeam();
        LOGGER.info("Team " + team.getId() + " : " + team.getName());
        LOGGER.info("Self " + sessionPersona.getId() + " : " + sessionPersona.getUserName());
        LOGGER.info(users.size() + " users found on this session");
        LOGGER.info(channels.size() + " channels found on this session");
        String wssurl = sessionParser.getWebSocketURL();

        LOGGER.debug("retrieved websocket URL : " + wssurl);
        WebSocketContainer client = webSocketContainerProvider.getWebSocketContainer();
        final MessageHandler handler = this;
        LOGGER.debug("initiating actions to websocket");

        try {
            websocketSession = client.connectToServer(new Endpoint()
            {
                @Override
                public void onOpen(Session session, EndpointConfig config)
                {
                    session.addMessageHandler(handler);
                }

                @Override
                public void onError(Session session, Throwable thr) {
                    LOGGER.error("Endpoint#onError called", thr);
                }

            }, URI.create(wssurl));
        }
        catch (DeploymentException e) {
            LOGGER.error(e.toString());
        }

        if (websocketSession != null) {
            SlackConnectedImpl slackConnectedImpl = new SlackConnectedImpl(sessionPersona);
            dispatcher.dispatch(slackConnectedImpl);
            LOGGER.debug("websocket actions established");
            LOGGER.info("slack session ready");
        }
    }

    private String consumeToString(InputStream content) throws IOException
    {
        Reader reader = new InputStreamReader(content, "UTF-8");
        StringBuffer buf = new StringBuffer();
        char data[] = new char[16384];
        int numread;
        while (0 <= (numread = reader.read(data)))
            buf.append(data, 0, numread);
        return buf.toString();
    }


    private void disconnectImpl()
    {
        if (websocketSession != null)
        {
            try
            {
                websocketSession.close();
            }
            catch (IOException ex) {
                // ignored.
            }
            finally {
                SlackDisconnectedImpl slackDisconnected = new SlackDisconnectedImpl(sessionPersona);
                dispatcher.dispatch(slackDisconnected);
                websocketSession = null;
            }
        }
    }

    private void startConnectionMonitoring() {
        connectionMonitoringThread = new Thread() {
            @Override
            public void run() {
                LOGGER.debug("monitoring thread started");
                while (true) {
                    try {
                        // heart beat of 30s (should be configurable in the future)
                        Thread.sleep(heartbeat);

                        // disconnect() was called.
                        if (wantDisconnect) {
                            this.interrupt();
                        }

                        if (lastPingSent != lastPingAck || websocketSession == null) {
                            // disconnection happened
                            LOGGER.warn("Connection lost...");
                            try {
                                if (websocketSession != null)
                                {
                                    websocketSession.close();
                                }
                            }
                            catch (IOException e) {
                                LOGGER.error("exception while trying to close the websocket ", e);
                            }
                            websocketSession = null;
                            if (reconnectOnDisconnection) {
                                connectImpl();
                            }
                            else {
                                this.interrupt();
                            }
                        }
                        else {
                            lastPingSent = getNextMessageId();
                            LOGGER.debug("sending ping " + lastPingSent);
                            try {
                                if (websocketSession.isOpen()) {
                                    websocketSession.getBasicRemote().sendText("{\"type\":\"ping\",\"id\":" + lastPingSent + "}");
                                }
                                else if (reconnectOnDisconnection) {
                                    connectImpl();
                                }
                            }
                            catch (IllegalStateException e) {
                                // websocketSession might be closed in this case
                                if (reconnectOnDisconnection) {
                                    connectImpl();
                                }
                            }
                        }
                    }
                    catch (InterruptedException e) {
                        break;
                    }
                    catch (IOException e) {
                        LOGGER.error("unexpected exception on monitoring thread ", e);
                    }
                }
                LOGGER.debug("monitoring thread stopped");
            }
        };

        if (!wantDisconnect) {
            connectionMonitoringThread.start();
        }
    }

    private void stopConnectionMonitoring() {
        if (connectionMonitoringThread != null) {
            while (true) {
                try {
                    connectionMonitoringThread.interrupt();
                    connectionMonitoringThread.join();
                    break;
                }
                catch (InterruptedException ex) {
                    // ouch - let's try again!
                }
            }
        }
    }

    @Override
    public SlackMessageHandle<SlackMessageReply> sendMessage(SlackChannel channel, SlackPreparedMessage preparedMessage, SlackChatConfiguration chatConfiguration) {
        SlackMessageHandleImpl<SlackMessageReply> handle = new SlackMessageHandleImpl<>(getNextMessageId());
        Map<String, String> arguments = new HashMap<>();
        arguments.put("token", authToken);
        arguments.put("channel", channel.getId());
        arguments.put("text", preparedMessage.getMessage());
        if (chatConfiguration.asUser)
        {
            arguments.put("as_user", "true");
        }
        if (chatConfiguration.avatar == Avatar.ICON_URL)
        {
            arguments.put("icon_url", chatConfiguration.avatarDescription);
        }
        if (chatConfiguration.avatar == Avatar.EMOJI)
        {
            arguments.put("icon_emoji", chatConfiguration.avatarDescription);
        }
        if (chatConfiguration.userName != null)
        {
            arguments.put("username", chatConfiguration.userName);
        }
        if (preparedMessage.getAttachments() != null && preparedMessage.getAttachments().length > 0)
        {
            arguments.put("attachments", SlackJSONAttachmentFormatter
                    .encodeAttachments(preparedMessage.getAttachments()).toString());
        }
        if (!preparedMessage.isUnfurl())
        {
            arguments.put("unfurl_links", "false");
            arguments.put("unfurl_media", "false");
        }
        if (preparedMessage.isLinkNames())
        {
            arguments.put("link_names", "1");
        }

        postSlackCommand(arguments, CHAT_POST_MESSAGE_COMMAND, handle);
        return handle;
    }

    @Override
    public SlackMessageHandle<SlackMessageReply> sendFile(SlackChannel channel, byte[] data, String fileName) {
        SlackMessageHandleImpl<SlackMessageReply> handle = new SlackMessageHandleImpl<>(getNextMessageId());
        Map<String, String> arguments = new HashMap<>();
        arguments.put("token", authToken);
        arguments.put("channels", channel.getId());
        arguments.put("filename", fileName);
        postSlackCommandWithFile(arguments, data, fileName,FILE_UPLOAD_COMMAND, handle);
        return handle;
    }

    @Override
    public SlackMessageHandle<SlackMessageReply> deleteMessage(String timeStamp, SlackChannel channel) {
        SlackMessageHandleImpl<SlackMessageReply> handle = new SlackMessageHandleImpl<>(getNextMessageId());
        Map<String, String> arguments = new HashMap<>();
        arguments.put("token", authToken);
        arguments.put("channel", channel.getId());
        arguments.put("ts", timeStamp);
        postSlackCommand(arguments, CHAT_DELETE_COMMAND, handle);
        return handle;
    }

    @Override
    public SlackMessageHandle<SlackMessageReply> updateMessage(String timeStamp, SlackChannel channel, String message) {
        SlackMessageHandleImpl<SlackMessageReply> handle = new SlackMessageHandleImpl<>(getNextMessageId());
        Map<String, String> arguments = new HashMap<>();
        arguments.put("token", authToken);
        arguments.put("ts", timeStamp);
        arguments.put("channel", channel.getId());
        arguments.put("text", message);
        postSlackCommand(arguments, CHAT_UPDATE_COMMAND, handle);
        return handle;
    }

    @Override
    public SlackMessageHandle<SlackMessageReply> addReactionToMessage(SlackChannel channel, String messageTimeStamp, String emojiCode) {
        SlackMessageHandleImpl<SlackMessageReply> handle = new SlackMessageHandleImpl<>(getNextMessageId());
        Map<String, String> arguments = new HashMap<>();
        arguments.put("token", authToken);
        arguments.put("channel", channel.getId());
        arguments.put("timestamp", messageTimeStamp);
        arguments.put("name", emojiCode);
        postSlackCommand(arguments, REACTIONS_ADD_COMMAND, handle);
        return handle;
    }

    @Override
    public SlackMessageHandle<SlackChannelReply> joinChannel(String channelName) {
        SlackMessageHandleImpl<SlackChannelReply> handle = new SlackMessageHandleImpl<>(getNextMessageId());
        Map<String, String> arguments = new HashMap<>();
        arguments.put("token", authToken);
        arguments.put("name", channelName);
        postSlackCommand(arguments, CHANNELS_JOIN_COMMAND, handle);
        return handle;
    }

    @Override
    public SlackMessageHandle<SlackChannelReply> setChannelTopic(SlackChannel channel, String topic) {
        SlackMessageHandleImpl<SlackChannelReply> handle = new SlackMessageHandleImpl<>(getNextMessageId());
        Map<String, String> arguments = new HashMap<>();
        arguments.put("token", authToken);
        arguments.put("channel", channel.getId());
        arguments.put("topic", topic);
        postSlackCommand(arguments, CHANNELS_SET_TOPIC_COMMAND, handle);
        return handle;
    }

    @Override
    public SlackMessageHandle<SlackChannelReply> leaveChannel(SlackChannel channel) {
        SlackMessageHandleImpl<SlackChannelReply> handle = new SlackMessageHandleImpl<>(getNextMessageId());
        Map<String, String> arguments = new HashMap<>();
        arguments.put("token", authToken);
        arguments.put("channel", channel.getId());
        postSlackCommand(arguments, CHANNELS_LEAVE_COMMAND, handle);
        return handle;
    }

    @Override
    public SlackMessageHandle<SlackChannelReply> inviteToChannel(SlackChannel channel, SlackUser user) {
      SlackMessageHandleImpl<SlackChannelReply> handle = new SlackMessageHandleImpl<>(getNextMessageId());
      Map<String, String> arguments = new HashMap<>();
      arguments.put("token", authToken);
      arguments.put("channel", channel.getId());
      arguments.put("user", user.getId());
      postSlackCommand(arguments, CHANNELS_INVITE_COMMAND, handle);
      return handle;
    }
    
    @Override
    public SlackMessageHandle<ParsedSlackReply> archiveChannel(SlackChannel channel) {
      SlackMessageHandleImpl<ParsedSlackReply> handle = new SlackMessageHandleImpl<>(getNextMessageId());
      Map<String, String> arguments = new HashMap<>();
      arguments.put("token", authToken);
      arguments.put("channel", channel.getId());
      postSlackCommand(arguments, CHANNELS_ARCHIVE_COMMAND, handle);
      return handle;
    }
    
    @Override
    public SlackMessageHandle<SlackChannelReply> openDirectMessageChannel(SlackUser user) {
        SlackMessageHandleImpl<SlackChannelReply> handle = new SlackMessageHandleImpl<>(getNextMessageId());
        Map<String, String> arguments = new HashMap<>();
        arguments.put("token", authToken);
        arguments.put("user", user.getId());
        postSlackCommand(arguments, DIRECT_MESSAGE_OPEN_CHANNEL_COMMAND, handle);
        return handle;
    }

    @Override
    public SlackMessageHandle<SlackChannelReply> openMultipartyDirectMessageChannel(SlackUser... users) {
        SlackMessageHandleImpl<SlackChannelReply> handle = new SlackMessageHandleImpl<>(getNextMessageId());
        Map<String, String> arguments = new HashMap<>();
        arguments.put("token", authToken);
        StringBuilder strBuilder = new StringBuilder();
        for (int i = 0 ; i < users.length ; i++) {
            if (i != 0) {
                strBuilder.append(',');
            }
            strBuilder.append(users[i].getId());
        }
        arguments.put("users", strBuilder.toString());
        postSlackCommand(arguments, MULTIPARTY_DIRECT_MESSAGE_OPEN_CHANNEL_COMMAND, handle);
        if (!handle.getReply().isOk()) {
            LOGGER.debug("Error occurred while performing command: '" + handle.getReply().getErrorMessage() + "'");
            return null;
        }
        return handle;
    }

    public SlackMessageHandle<EmojiSlackReply> listEmoji() {
        SlackMessageHandleImpl<EmojiSlackReply> handle = new SlackMessageHandleImpl<>(getNextMessageId());
        Map<String, String> arguments = new HashMap<>();
        arguments.put("token", authToken);
        postSlackCommand(arguments, LIST_EMOJI_COMMAND, handle);
        return handle;
    }

    @Override
    public void refetchUsers() {
        Map<String, String> params = new HashMap<>();
        params.put("presence", "1");
        SlackMessageHandle<GenericSlackReply> handle = postGenericSlackCommand(params, LIST_USERS);
        GenericSlackReply replyEv = handle.getReply();
        String answer = replyEv.getPlainAnswer();
        JsonParser parser = new JsonParser();
        JsonObject answerJson = parser.parse(answer).getAsJsonObject();
        JsonArray membersjson = answerJson.get("members").getAsJsonArray();
        Map<String, SlackUser> members = new HashMap<>();
        if (membersjson != null) {
            for (JsonElement member : membersjson) {
                SlackUser user = SlackJSONParsingUtils.buildSlackUser(member.getAsJsonObject());
                members.put(user.getId(), user);
            }
        }

        //blindly replace cache
        users = members;
    }

    private void postSlackCommand(Map<String, String> params, String command, SlackMessageHandleImpl handle) {
        HttpClient client = getHttpClient();
        HttpPost request = new HttpPost(SLACK_API_HTTPS_ROOT + command);
        List<NameValuePair> nameValuePairList = new ArrayList<>();
        for (Map.Entry<String, String> arg : params.entrySet())
        {
            nameValuePairList.add(new BasicNameValuePair(arg.getKey(), arg.getValue()));
        }
        try
        {
            request.setEntity(new UrlEncodedFormEntity(nameValuePairList, "UTF-8"));
            HttpResponse response = client.execute(request);
            String jsonResponse = consumeToString(response.getEntity().getContent());
            LOGGER.debug("PostMessage return: " + jsonResponse);
            ParsedSlackReply reply = SlackJSONReplyParser.decode(parseObject(jsonResponse),this);
            handle.setReply(reply);
        }
        catch (Exception e)
        {
            // TODO : improve exception handling
            e.printStackTrace();
        }
    }

    private void postSlackCommandWithFile(Map<String, String> params, byte [] fileContent, String fileName, String command, SlackMessageHandleImpl handle) {
        URIBuilder uriBuilder = new URIBuilder();
        uriBuilder.setScheme(SLACK_API_SCHEME).setHost(SLACK_API_HOST).setPath(SLACK_API_PATH+"/"+command);
        for (Map.Entry<String, String> arg : params.entrySet())
        {
            uriBuilder.setParameter(arg.getKey(),arg.getValue());
        }
        HttpPost request = new HttpPost(uriBuilder.toString());
        HttpClient client = getHttpClient();
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        try
        {
            builder.addBinaryBody("file",fileContent, ContentType.DEFAULT_BINARY,fileName);
            request.setEntity(builder.build());
            HttpResponse response = client.execute(request);
            String jsonResponse = CharStreams.toString(new InputStreamReader(response.getEntity().getContent()));
            LOGGER.debug("PostMessage return: " + jsonResponse);
            ParsedSlackReply reply = SlackJSONReplyParser.decode(parseObject(jsonResponse),this);
            handle.setReply(reply);
        }
        catch (Exception e)
        {
            // TODO : improve exception handling
            e.printStackTrace();
        }
    }

    @Override
    public SlackMessageHandle<GenericSlackReply> postGenericSlackCommand(Map<String, String> params, String command) {
        HttpClient client = getHttpClient();
        HttpPost request = new HttpPost(SLACK_API_HTTPS_ROOT + command);
        List<NameValuePair> nameValuePairList = new ArrayList<>();
        for (Map.Entry<String, String> arg : params.entrySet())
        {
            if (!"token".equals(arg.getKey())) {
                nameValuePairList.add(new BasicNameValuePair(arg.getKey(), arg.getValue()));
            }
        }
        nameValuePairList.add(new BasicNameValuePair("token", authToken));
        try
        {
            SlackMessageHandleImpl<GenericSlackReply> handle = new SlackMessageHandleImpl<>(getNextMessageId());
            request.setEntity(new UrlEncodedFormEntity(nameValuePairList, "UTF-8"));
            HttpResponse response = client.execute(request);
            String jsonResponse = consumeToString(response.getEntity().getContent());
            LOGGER.debug("PostMessage return: " + jsonResponse);
            GenericSlackReplyImpl reply = new GenericSlackReplyImpl(jsonResponse);
            handle.setReply(reply);
            return handle;
        }
        catch (Exception e)
        {
            // TODO : improve exception handling
            e.printStackTrace();
        }
        return null;
    }

    private HttpClient getHttpClient() {
        HttpClient client;
        if (proxyHost != null)
        {
            client = HttpClientBuilder.create().setRoutePlanner(new DefaultProxyRoutePlanner(proxyHost)).build();
        }
        else
        {
            client = HttpClientBuilder.create().build();
        }
        return client;
    }

    @Override
    public SlackMessageHandle<SlackMessageReply> sendMessageOverWebSocket(SlackChannel channel, String message) {
        SlackMessageHandleImpl<SlackMessageReply> handle = new SlackMessageHandleImpl<>(getNextMessageId());
        try
        {
            JsonObject messageJSON = new JsonObject();
            messageJSON.addProperty("type", "message");
            messageJSON.addProperty("channel", channel.getId());
            messageJSON.addProperty("text", message);

            websocketSession.getBasicRemote().sendText(messageJSON.toString());
        }
        catch (Exception e)
        {
            // TODO : improve exception handling
            e.printStackTrace();
        }
        return handle;
    }

    @Override
    public SlackMessageHandle<SlackMessageReply> sendTyping(SlackChannel channel) {
        SlackMessageHandleImpl<SlackMessageReply> handle = new SlackMessageHandleImpl<>(getNextMessageId());
        try
        {
            JsonObject messageJSON = new JsonObject();
            messageJSON.addProperty("type", "typing");
            messageJSON.addProperty("channel", channel.getId());
            websocketSession.getBasicRemote().sendText(messageJSON.toString());
        }
        catch (Exception e)
        {
            // TODO : improve exception handling
            e.printStackTrace();
        }
        return handle;
    }

    @Override
    public SlackPersona.SlackPresence getPresence(SlackPersona persona) {
        HttpClient client = getHttpClient();
        HttpPost request = new HttpPost("https://slack.com/api/users.getPresence");
        List<NameValuePair> nameValuePairList = new ArrayList<>();
        nameValuePairList.add(new BasicNameValuePair("token", authToken));
        nameValuePairList.add(new BasicNameValuePair("user", persona.getId()));
        try
        {
            request.setEntity(new UrlEncodedFormEntity(nameValuePairList, "UTF-8"));
            HttpResponse response = client.execute(request);
            String jsonResponse = consumeToString(response.getEntity().getContent());
            LOGGER.debug("PostMessage return: " + jsonResponse);
            JsonObject resultObject = parseObject(jsonResponse);
            //quite hacky need to refactor this
            SlackUserPresenceReply reply = (SlackUserPresenceReply)SlackJSONReplyParser.decode(resultObject,this);
            if (!reply.isOk())
            {
                return SlackPersona.SlackPresence.UNKNOWN;
            }
            String presence = resultObject.get("presence") != null ? resultObject.get("presence").getAsString() : null;

            if ("active".equals(presence))
            {
                return SlackPersona.SlackPresence.ACTIVE;
            }
            if ("away".equals(presence))
            {
                return SlackPersona.SlackPresence.AWAY;
            }
        }
        catch (Exception e)
        {
            // TODO : improve exception handling
            e.printStackTrace();
        }
        return SlackPersona.SlackPresence.UNKNOWN;
    }

    public void setPresence(SlackPersona.SlackPresence presence) {
        if(presence == SlackPersona.SlackPresence.UNKNOWN || presence == SlackPersona.SlackPresence.ACTIVE) {
            throw new IllegalArgumentException("Presence must be either AWAY or AUTO");
        }
        HttpClient client = getHttpClient();
        HttpPost request = new HttpPost(SLACK_API_HTTPS_ROOT + SET_PERSONA_ACTIVE);
        List<NameValuePair> nameValuePairList = new ArrayList<>();
        nameValuePairList.add(new BasicNameValuePair("token", authToken));
        nameValuePairList.add(new BasicNameValuePair("presence", presence.toString().toLowerCase()));
        try {
            request.setEntity(new UrlEncodedFormEntity(nameValuePairList, "UTF-8"));
            HttpResponse response = client.execute(request);
            String JSONResponse = consumeToString(response.getEntity().getContent());
            LOGGER.debug("JSON Response=" + JSONResponse);
        }catch(IOException e) {
            e.printStackTrace();
        }

    }

    private long getNextMessageId() {
        return messageId.getAndIncrement();
    }

    @Override
    public void onMessage(String message) {
        LOGGER.debug("receiving from websocket " + message);
        if (message.contains("{\"type\":\"pong\",\"reply_to\"")) {
            int rightBracketIdx = message.indexOf('}');
            String toParse = message.substring(26, rightBracketIdx);
            lastPingAck = Integer.parseInt(toParse);
            LOGGER.debug("pong received " + lastPingAck);
        }
        else
        {
            JsonObject object = parseObject(message);
            SlackEvent slackEvent = SlackJSONMessageParser.decode(this, object);
            if (slackEvent instanceof SlackChannelCreated)
            {
                SlackChannelCreated slackChannelCreated = (SlackChannelCreated) slackEvent;
                channels.put(slackChannelCreated.getSlackChannel().getId(), slackChannelCreated.getSlackChannel());
            }
            if (slackEvent instanceof SlackGroupJoined)
            {
                SlackGroupJoined slackGroupJoined = (SlackGroupJoined) slackEvent;
                channels.put(slackGroupJoined.getSlackChannel().getId(), slackGroupJoined.getSlackChannel());
            }
            if (slackEvent instanceof SlackUserChange)
            {
                SlackUserChange slackUserChange = (SlackUserChange) slackEvent;
                users.put(slackUserChange.getUser().getId(), slackUserChange.getUser());
            }
            dispatcher.dispatch(slackEvent);
        }
    }

    private JsonObject parseObject(String json)
    {
        JsonParser parser = new JsonParser();
        return parser.parse(json).getAsJsonObject();
    }

    @Override
    public SlackMessageHandle<ParsedSlackReply> inviteUser(String email, String firstName, boolean setActive) {

        SlackMessageHandleImpl<ParsedSlackReply> handle = new SlackMessageHandleImpl<>(getNextMessageId());
        Map<String, String> arguments = new HashMap<>();
        arguments.put("token", authToken);
        arguments.put("email", email);
        arguments.put("first_name", firstName);
        arguments.put("set_active", ""+setActive);
        postSlackCommand(arguments, INVITE_USER_COMMAND, handle);
        return handle;
    }

    public long getHeartbeat() {
        return TimeUnit.MILLISECONDS.toSeconds(heartbeat);
    }

    public void setHeartbeat(long heartbeat, TimeUnit unit) {
        this.heartbeat = unit.toMillis(heartbeat);
    }
}
