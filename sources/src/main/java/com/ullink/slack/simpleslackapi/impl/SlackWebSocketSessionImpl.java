package com.ullink.slack.simpleslackapi.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ullink.slack.simpleslackapi.*;
import com.ullink.slack.simpleslackapi.events.*;
import com.ullink.slack.simpleslackapi.SlackChatConfiguration.Avatar;
import com.ullink.slack.simpleslackapi.events.userchange.SlackTeamJoin;
import com.ullink.slack.simpleslackapi.events.userchange.SlackUserChange;
import com.ullink.slack.simpleslackapi.events.userchange.SlackUserChangeEvent;
import com.ullink.slack.simpleslackapi.listeners.PresenceChangeListener;
import com.ullink.slack.simpleslackapi.listeners.SlackChannelArchivedListener;
import com.ullink.slack.simpleslackapi.listeners.SlackChannelCreatedListener;
import com.ullink.slack.simpleslackapi.listeners.SlackChannelDeletedListener;
import com.ullink.slack.simpleslackapi.listeners.SlackChannelRenamedListener;
import com.ullink.slack.simpleslackapi.listeners.SlackChannelUnarchivedListener;
import com.ullink.slack.simpleslackapi.listeners.SlackEventListener;
import com.ullink.slack.simpleslackapi.listeners.SlackTeamJoinListener;
import com.ullink.slack.simpleslackapi.listeners.SlackUserChangeListener;
import com.ullink.slack.simpleslackapi.replies.*;
import com.ullink.slack.simpleslackapi.utils.ReaderUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicCredentialsProvider;
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

    private static final String CHANNELS_UNARCHIVE_COMMAND     = "channels.unarchive";

    private static final String CHAT_POST_MESSAGE_COMMAND = "chat.postMessage";

    private static final String CHAT_POST_EPHEMERAL_COMMAND = "chat.postEphemeral";

    private static final String FILE_UPLOAD_COMMAND       = "files.upload";

    private static final String CHAT_DELETE_COMMAND       = "chat.delete";

    private static final String CHAT_UPDATE_COMMAND       = "chat.update";

    private static final String REACTIONS_ADD_COMMAND     = "reactions.add";

    private static final String REACTIONS_REMOVE_COMMAND     = "reactions.remove";

    private static final String INVITE_USER_COMMAND     = "users.admin.invite";

    private static final String SET_PERSONA_ACTIVE = "users.setPresence";

    private static final String LIST_EMOJI_COMMAND = "emoji.list";

    private static final String LIST_USERS = "users.list";


    private static final Logger               LOGGER                     = LoggerFactory.getLogger(SlackWebSocketSessionImpl.class);

    private static final String               SLACK_HTTPS_AUTH_URL       = "https://slack.com/api/rtm.start?token=";

    private  static final int                 DEFAULT_HEARTBEAT_IN_MILLIS = 30000;

    private volatile Session                  websocketSession;
    private String                            authToken;
    private String                            proxyAddress;
    private int                               proxyPort                  = -1;
    HttpHost                                  proxyHost;
    private String                            proxyUser;
    private String                            proxyPassword;
    private volatile long                     lastPingSent;
    private volatile long                     lastPingAck;

    private AtomicLong                        messageId = new AtomicLong();

    private final boolean                     reconnectOnDisconnection;
    private volatile boolean                  wantDisconnect;

    private Thread                            connectionMonitoringThread; //TODO: replace this with a scheduled executor
    private EventDispatcher                   dispatcher                 = new EventDispatcher();
    private final long                        heartbeat;
    private WebSocketContainerProvider        webSocketContainerProvider;
    private volatile String                   webSocketConnectionURL;

    @Override
    public SlackMessageHandle<SlackMessageReply> sendMessageToUser(SlackUser user, SlackPreparedMessage message) {
        SlackChannel iMChannel = getIMChannelForUser(user);
        return sendMessage(iMChannel, message);
    }

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
                case SLACK_TEAM_JOIN:
                    dispatchImpl((SlackTeamJoin) event, slackTeamJoinListener);
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
                    LOGGER.warn("event of type " + event.getEventType() + " not handled: " + ((UnknownEvent)event).getJsonPayload());
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
        this.webSocketContainerProvider = webSocketContainerProvider != null ? webSocketContainerProvider : new DefaultWebSocketContainerProvider(null, -1, null, null);
        addInternalListeners();
    }

    SlackWebSocketSessionImpl(WebSocketContainerProvider webSocketContainerProvider, String authToken, Proxy.Type proxyType, String proxyAddress, int proxyPort, String proxyUser, String proxyPassword, boolean reconnectOnDisconnection, long heartbeat, TimeUnit unit) {
        this.authToken = authToken;
        if (proxyType != null && proxyType != Proxy.Type.DIRECT) {
            this.proxyAddress = proxyAddress;
            this.proxyPort = proxyPort;
            this.proxyHost = new HttpHost(proxyAddress, proxyPort);
            this.proxyUser = proxyUser;
            this.proxyPassword = proxyPassword;
        }
        this.reconnectOnDisconnection = reconnectOnDisconnection;
        this.heartbeat = heartbeat != 0 ? unit.toMillis(heartbeat) : DEFAULT_HEARTBEAT_IN_MILLIS;
        this.webSocketContainerProvider = webSocketContainerProvider != null ? webSocketContainerProvider : new DefaultWebSocketContainerProvider(this.proxyAddress, this.proxyPort, this.proxyUser, this.proxyPassword);
        addInternalListeners();
    }

    private void addInternalListeners()
    {
        addPresenceChangeListener(INTERNAL_PRESENCE_CHANGE_LISTENER);
        addChannelArchivedListener(INTERNAL_CHANNEL_ARCHIVE_LISTENER);
        addChannelCreatedListener(INTERNAL_CHANNEL_CREATED_LISTENER);
        addChannelDeletedListener(INTERNAL_CHANNEL_DELETED_LISTENER);
        addChannelRenamedListener(INTERNAL_CHANNEL_RENAMED_LISTENER);
        addChannelUnarchivedListener(INTERNAL_CHANNEL_UNARCHIVED_LISTENER);
        addSlackTeamJoinListener(INTERNAL_TEAM_JOIN_LISTENER);
        addSlackUserChangeListener(INTERNAL_USER_CHANGE_LISTENER);
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

    public void reconnect() throws IOException{
        while(true) {
            if (!this.isConnected()) {
                connectImpl();
                break;
            } else {
                disconnectImpl();
            }

        }
    }


    @Override
    public boolean isConnected()
    {
        return websocketSession != null && websocketSession.isOpen();
    }

    private void connectImpl() throws IOException
    {
        LOGGER.info("connecting to slack");
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
        webSocketConnectionURL = sessionParser.getWebSocketURL();
        LOGGER.debug("retrieved websocket URL : " + webSocketConnectionURL);
        establishWebsocketConnection();
    }

    private void establishWebsocketConnection() throws IOException
    {
        lastPingSent = 0;
        lastPingAck = 0;
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

            }, URI.create(webSocketConnectionURL));
        }
        catch (DeploymentException e) {
            LOGGER.error(e.toString());
            throw new IOException(e);
        }
        if (websocketSession != null) {
            SlackConnected slackConnected = new SlackConnected(sessionPersona);
            dispatcher.dispatch(slackConnected);
            LOGGER.debug("websocket actions established");
            LOGGER.info("slack session ready");
        } else {
            throw new IOException("Unable to establish a connection to this websocket URL " + webSocketConnectionURL);
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
                SlackDisconnected slackDisconnected = new SlackDisconnected(sessionPersona);
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
                                reconnect();
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
                                    reconnect();
                                }
                            }
                            catch (IllegalStateException e) {
                                LOGGER.warn("exception caught while using websocket ", e);
                                // websocketSession might be closed in this case
                                if (reconnectOnDisconnection) {
                                    reconnect();
                                }
                            }
                        }
                    }
                    catch (InterruptedException e) {
                        LOGGER.info("monitoring thread interrupted");
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
        if (channel == null) {
            throw new IllegalArgumentException("Channel can't be null");
        }
        SlackMessageHandle<SlackMessageReply> handle = new SlackMessageHandle<>(getNextMessageId());
        Map<String, String> arguments = new HashMap<>();
        arguments.put("token", authToken);
        arguments.put("channel", channel.getId());
        arguments.put("text", preparedMessage.getMessage());
        if (chatConfiguration.isAsUser())
        {
            arguments.put("as_user", "true");
        }
        if (chatConfiguration.getAvatar() == Avatar.ICON_URL)
        {
            arguments.put("icon_url", chatConfiguration.getAvatarDescription());
        }
        if (chatConfiguration.getAvatar() == Avatar.EMOJI)
        {
            arguments.put("icon_emoji", chatConfiguration.getAvatarDescription());
        }
        if (chatConfiguration.getUserName() != null)
        {
            arguments.put("username", chatConfiguration.getUserName());
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
        if(preparedMessage.getThreadTimestamp() != null) {
            arguments.put("thread_ts", preparedMessage.getThreadTimestamp());

            if(preparedMessage.isReplyBroadcast()) {
                arguments.put("reply_broadcast", "true");
            }
        }

        postSlackCommand(arguments, CHAT_POST_MESSAGE_COMMAND, handle);
        return handle;
    }

    @Override
    public SlackMessageHandle<SlackMessageReply> sendEphemeralMessage(SlackChannel channel, SlackUser user, SlackPreparedMessage preparedMessage, SlackChatConfiguration chatConfiguration)
    {
        SlackMessageHandle<SlackMessageReply> handle = new SlackMessageHandle<>(getNextMessageId());
        Map<String, String> arguments = new HashMap<>();
        arguments.put("token", authToken);
        arguments.put("channel", channel.getId());
        arguments.put("text", preparedMessage.getMessage());
        arguments.put("user", user.getId());
        if (chatConfiguration.isAsUser())
        {
            arguments.put("as_user", "true");
        }
        if (chatConfiguration.getAvatar() == Avatar.ICON_URL)
        {
            arguments.put("icon_url", chatConfiguration.getAvatarDescription());
        }
        if (chatConfiguration.getAvatar() == Avatar.EMOJI)
        {
            arguments.put("icon_emoji", chatConfiguration.getAvatarDescription());
        }
        if (chatConfiguration.getUserName() != null)
        {
            arguments.put("username", chatConfiguration.getUserName());
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
        if(preparedMessage.getThreadTimestamp() != null) {
            arguments.put("thread_ts", preparedMessage.getThreadTimestamp());

            if(preparedMessage.isReplyBroadcast()) {
                arguments.put("reply_broadcast", "true");
            }
        }

        postSlackCommand(arguments, CHAT_POST_EPHEMERAL_COMMAND, handle);
        return handle;
    }

    @Override
    public SlackMessageHandle<SlackMessageReply> sendFileToUser(String userName, byte[] data, String fileName) {
        return sendFileToUser(findUserByUserName(userName), data, fileName);
    }

    @Override
    public SlackMessageHandle<SlackMessageReply> sendFileToUser(SlackUser user, byte[] data, String fileName) {
        SlackChannel iMChannel = getIMChannelForUser(user);
        return sendFile(iMChannel, data, fileName);
    }

    @Override
    public SlackMessageHandle<SlackMessageReply> sendFile(SlackChannel channel, byte[] data, String fileName) {
        SlackMessageHandle<SlackMessageReply> handle = new SlackMessageHandle<>(getNextMessageId());
        Map<String, String> arguments = new HashMap<>();
        arguments.put("token", authToken);
        arguments.put("channels", channel.getId());
        arguments.put("filename", fileName);
        postSlackCommandWithFile(arguments, data, fileName,FILE_UPLOAD_COMMAND, handle);
        return handle;
    }

    @Override
    public SlackMessageHandle<SlackMessageReply> sendFile(SlackChannel channel, byte[] data, String fileName, String title, String initialComment) {
        SlackMessageHandle<SlackMessageReply> handle = new SlackMessageHandle<>(getNextMessageId());
        Map<String, String> arguments = new HashMap<>();
        arguments.put("token", authToken);
        arguments.put("channels", channel.getId());
        arguments.put("filename", fileName);
        arguments.put("title", title);
        arguments.put("initial_comment", initialComment);
        postSlackCommandWithFile(arguments, data, fileName,FILE_UPLOAD_COMMAND, handle);
        return handle;
    }

    @Override
    public SlackMessageHandle<SlackMessageReply> deleteMessage(String timeStamp, SlackChannel channel) {
        SlackMessageHandle<SlackMessageReply> handle = new SlackMessageHandle<>(getNextMessageId());
        Map<String, String> arguments = new HashMap<>();
        arguments.put("token", authToken);
        arguments.put("channel", channel.getId());
        arguments.put("ts", timeStamp);
        postSlackCommand(arguments, CHAT_DELETE_COMMAND, handle);
        return handle;
    }

    @Override
    public SlackMessageHandle<SlackMessageReply> updateMessage(String timeStamp, SlackChannel channel, String message) {
        SlackMessageHandle<SlackMessageReply> handle = new SlackMessageHandle<>(getNextMessageId());
        Map<String, String> arguments = new HashMap<>();
        arguments.put("token", authToken);
        arguments.put("ts", timeStamp);
        arguments.put("channel", channel.getId());
        arguments.put("text", message);
        postSlackCommand(arguments, CHAT_UPDATE_COMMAND, handle);
        return handle;
    }

    @Override
    public SlackMessageHandle<SlackMessageReply> updateMessage(String timeStamp, SlackChannel channel, String message, SlackAttachment[] attachments) {
        SlackMessageHandle<SlackMessageReply> handle = new SlackMessageHandle<>(getNextMessageId());
        Map<String, String> arguments = new HashMap<>();
        arguments.put("token", authToken);
        arguments.put("ts", timeStamp);
        arguments.put("channel", channel.getId());
        arguments.put("text", message);
        arguments.put("attachments", SlackJSONAttachmentFormatter.encodeAttachments(attachments).toString());
        postSlackCommand(arguments, CHAT_UPDATE_COMMAND, handle);
        return handle;
    }

    @Override
    public SlackMessageHandle<SlackMessageReply> addReactionToMessage(SlackChannel channel, String messageTimeStamp, String emojiCode) {
        SlackMessageHandle<SlackMessageReply> handle = new SlackMessageHandle<>(getNextMessageId());
        Map<String, String> arguments = new HashMap<>();
        arguments.put("token", authToken);
        arguments.put("channel", channel.getId());
        arguments.put("timestamp", messageTimeStamp);
        arguments.put("name", emojiCode);
        postSlackCommand(arguments, REACTIONS_ADD_COMMAND, handle);
        return handle;
    }

    @Override
    public SlackMessageHandle<SlackMessageReply> removeReactionFromMessage(SlackChannel channel, String messageTimeStamp, String emojiCode) {
        SlackMessageHandle<SlackMessageReply> handle = new SlackMessageHandle<>(getNextMessageId());
        Map<String, String> arguments = new HashMap<>();
        arguments.put("token", authToken);
        arguments.put("channel", channel.getId());
        arguments.put("timestamp", messageTimeStamp);
        arguments.put("name", emojiCode);
        postSlackCommand(arguments, REACTIONS_REMOVE_COMMAND, handle);
        return handle;
    }

    @Override
    public SlackMessageHandle<SlackChannelReply> joinChannel(String channelName) {
        SlackMessageHandle<SlackChannelReply> handle = new SlackMessageHandle<>(getNextMessageId());
        Map<String, String> arguments = new HashMap<>();
        arguments.put("token", authToken);
        arguments.put("name", channelName);
        postSlackCommand(arguments, CHANNELS_JOIN_COMMAND, handle);
        return handle;
    }

    @Override
    public SlackMessageHandle<SlackChannelReply> setChannelTopic(SlackChannel channel, String topic) {
        SlackMessageHandle<SlackChannelReply> handle = new SlackMessageHandle<>(getNextMessageId());
        Map<String, String> arguments = new HashMap<>();
        arguments.put("token", authToken);
        arguments.put("channel", channel.getId());
        arguments.put("topic", topic);
        postSlackCommand(arguments, CHANNELS_SET_TOPIC_COMMAND, handle);
        return handle;
    }

    @Override
    public SlackMessageHandle<SlackChannelReply> leaveChannel(SlackChannel channel) {
        SlackMessageHandle<SlackChannelReply> handle = new SlackMessageHandle<>(getNextMessageId());
        Map<String, String> arguments = new HashMap<>();
        arguments.put("token", authToken);
        arguments.put("channel", channel.getId());
        postSlackCommand(arguments, CHANNELS_LEAVE_COMMAND, handle);
        return handle;
    }

    @Override
    public SlackMessageHandle<SlackChannelReply> inviteToChannel(SlackChannel channel, SlackUser user) {
      SlackMessageHandle<SlackChannelReply> handle = new SlackMessageHandle<>(getNextMessageId());
      Map<String, String> arguments = new HashMap<>();
      arguments.put("token", authToken);
      arguments.put("channel", channel.getId());
      arguments.put("user", user.getId());
      postSlackCommand(arguments, CHANNELS_INVITE_COMMAND, handle);
      return handle;
    }
    
    @Override
    public SlackMessageHandle<ParsedSlackReply> archiveChannel(SlackChannel channel) {
      SlackMessageHandle<ParsedSlackReply> handle = new SlackMessageHandle<>(getNextMessageId());
      Map<String, String> arguments = new HashMap<>();
      arguments.put("token", authToken);
      arguments.put("channel", channel.getId());
      postSlackCommand(arguments, CHANNELS_ARCHIVE_COMMAND, handle);
      return handle;
    }

    @Override public SlackMessageHandle<ParsedSlackReply> unarchiveChannel(SlackChannel channel)
    {
        SlackMessageHandle<ParsedSlackReply> handle = new SlackMessageHandle<>(getNextMessageId());
        Map<String, String> arguments = new HashMap<>();
        arguments.put("token", authToken);
        arguments.put("channel", channel.getId());
        postSlackCommand(arguments, CHANNELS_UNARCHIVE_COMMAND, handle);
        return handle;
    }

    @Override
    public SlackMessageHandle<SlackChannelReply> openDirectMessageChannel(SlackUser user) {
        SlackMessageHandle<SlackChannelReply> handle = new SlackMessageHandle<>(getNextMessageId());
        Map<String, String> arguments = new HashMap<>();
        arguments.put("token", authToken);
        arguments.put("user", user.getId());
        postSlackCommand(arguments, DIRECT_MESSAGE_OPEN_CHANNEL_COMMAND, handle);
        return handle;
    }

    @Override
    public SlackMessageHandle<SlackChannelReply> openMultipartyDirectMessageChannel(SlackUser... users) {
        SlackMessageHandle<SlackChannelReply> handle = new SlackMessageHandle<>(getNextMessageId());
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
        SlackMessageHandle<EmojiSlackReply> handle = new SlackMessageHandle<>(getNextMessageId());
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

    private void postSlackCommand(Map<String, String> params, String command, SlackMessageHandle handle) {
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

    private void postSlackCommandWithFile(Map<String, String> params, byte [] fileContent, String fileName, String command, SlackMessageHandle handle) {
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
            String jsonResponse = ReaderUtils.readAll(new InputStreamReader(response.getEntity().getContent()));
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
            SlackMessageHandle<GenericSlackReply> handle = new SlackMessageHandle<>(getNextMessageId());
            request.setEntity(new UrlEncodedFormEntity(nameValuePairList, "UTF-8"));
            HttpResponse response = client.execute(request);
            String jsonResponse = consumeToString(response.getEntity().getContent());
            LOGGER.debug("PostMessage return: " + jsonResponse);
            GenericSlackReply reply = new GenericSlackReply(jsonResponse);
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
            if(null == this.proxyUser)
            {
                client = HttpClientBuilder.create().setRoutePlanner(new DefaultProxyRoutePlanner(proxyHost)).build();
            }
            else
            {
                RequestConfig config = RequestConfig.custom().setProxy(this.proxyHost).build();
                CredentialsProvider credsProvider = new BasicCredentialsProvider();
                credsProvider.setCredentials(new AuthScope(this.proxyHost), new UsernamePasswordCredentials(this.proxyUser, this.proxyPassword));
                client = HttpClientBuilder.create().setDefaultCredentialsProvider(credsProvider).setDefaultRequestConfig(config).build();
            }
        }
        else
        {
            client = HttpClientBuilder.create().build();
        }
        return client;
    }

    @Override
    public SlackMessageHandle<SlackMessageReply> sendMessageOverWebSocket(SlackChannel channel, String message) {
        SlackMessageHandle<SlackMessageReply> handle = new SlackMessageHandle<>(getNextMessageId());
        try
        {
            JsonObject messageJSON = new JsonObject();
            messageJSON.addProperty("id", handle.getMessageId());
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
        SlackMessageHandle<SlackMessageReply> handle = new SlackMessageHandle<>(getNextMessageId());
        try
        {
            JsonObject messageJSON = new JsonObject();
            messageJSON.addProperty("id", handle.getMessageId());
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
        final JsonObject object = parseObject(message);

        LOGGER.debug("receiving from websocket " + message);
        if (object.get("type") == null) {
            LOGGER.info("unable to parse message, missing 'type' attribute: " + message);
            return;
        }
        if ("pong".equals(object.get("type").getAsString())) {
            lastPingAck = object.get("reply_to").getAsInt();
            LOGGER.debug("pong received " + lastPingAck);
        }
        else if ("reconnect_url".equals(object.get("type").getAsString())) {
            String newWebSocketConnectionURL = object.get("url").getAsString();
            LOGGER.debug("new websocket connection received " + newWebSocketConnectionURL);
        }
        else
        {
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
            if (slackEvent instanceof SlackUserChangeEvent)
            {
                SlackUserChangeEvent slackUserChangeEvent = (SlackUserChangeEvent) slackEvent;
                users.put(slackUserChangeEvent.getUser().getId(), slackUserChangeEvent.getUser());
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

        return inviteUser(email, null, firstName, null, false, false, setActive);
    }

    @Override
    public SlackMessageHandle<ParsedSlackReply> inviteUser(String email, List<SlackChannel> channels,
                                                           String firstName, String lastName, boolean resend,
                                                           boolean restricted, boolean setActive) {

        SlackMessageHandle<ParsedSlackReply> handle = new SlackMessageHandle<>(getNextMessageId());
        if (channels == null) {
            channels = Collections.emptyList();
        }
        List<String> channelsIds = new ArrayList<>(channels.size());
        for (SlackChannel channel : channels) {
            channelsIds.add(channel.getId());
        }
        Map<String, String> arguments = new HashMap<>();
        arguments.put("token", authToken);
        arguments.put("email", email);
        arguments.put("channels", StringUtils.join(channelsIds, ","));
        arguments.put("first_name", firstName);
        arguments.put("last_name", lastName);
        arguments.put("resend", "" + resend);
        arguments.put("restricted", "" + restricted);
        arguments.put("set_active", "" + setActive);
        postSlackCommand(arguments, INVITE_USER_COMMAND, handle);
        return handle;
    }

    public long getHeartbeat() {
        return TimeUnit.MILLISECONDS.toSeconds(heartbeat);
    }

    private final PresenceChangeListener INTERNAL_PRESENCE_CHANGE_LISTENER = new PresenceChangeListener()
    {
        @Override public void onEvent(PresenceChange event, SlackSession session)
        {
            SlackUser user = users.get(event.getUserId());
            SlackUserImpl newUser = new SlackUserImpl(user.getId(), user.getUserName(), user.getRealName(), user.getUserMail(), user.getUserSkype(), user.getUserTitle(), user.getUserPhone(),
                user.isDeleted(), user.isAdmin(), user.isOwner(), user.isPrimaryOwner(), user.isRestricted(),
                user.isUltraRestricted(), user.isBot(), user.getTimeZone(), user.getTimeZoneLabel(), user.getTimeZoneOffset(),
                event.getPresence());
            users.put(event.getUserId(), newUser);
        }
    };

    private final SlackChannelArchivedListener INTERNAL_CHANNEL_ARCHIVE_LISTENER = new SlackChannelArchivedListener()
    {
        @Override public void onEvent(SlackChannelArchived event, SlackSession session)
        {
            SlackChannel channel = channels.get(event.getSlackChannel().getId());
            SlackChannel newChannel = new SlackChannel(channel.getId(), channel.getName(), channel.getTopic(), channel.getPurpose(), channel.isDirect(), channel.isMember(), true);
            channels.put(newChannel.getId(), newChannel);
        }
    };

    private final SlackChannelCreatedListener INTERNAL_CHANNEL_CREATED_LISTENER = new SlackChannelCreatedListener()
    {
        @Override public void onEvent(SlackChannelCreated event, SlackSession session)
        {
            channels.put(event.getSlackChannel().getId(), event.getSlackChannel());
        }
    };

    private final SlackChannelDeletedListener INTERNAL_CHANNEL_DELETED_LISTENER = new SlackChannelDeletedListener()
    {
        @Override public void onEvent(SlackChannelDeleted event, SlackSession session)
        {
            channels.remove(event.getSlackChannel().getId());
        }
    };

    private final SlackChannelRenamedListener INTERNAL_CHANNEL_RENAMED_LISTENER = new SlackChannelRenamedListener()
    {
        @Override public void onEvent(SlackChannelRenamed event, SlackSession session)
        {
            SlackChannel channel = channels.get(event.getSlackChannel().getId());
            SlackChannel newChannel = new SlackChannel(channel.getId(), event.getNewName(), channel.getTopic(), channel.getPurpose(), channel.isDirect(), channel.isMember(), channel.isArchived());
            channels.put(newChannel.getId(), newChannel);
        }
    };

    private final SlackChannelUnarchivedListener INTERNAL_CHANNEL_UNARCHIVED_LISTENER = new SlackChannelUnarchivedListener()
    {
        @Override public void onEvent(SlackChannelUnarchived event, SlackSession session)
        {
            SlackChannel channel = channels.get(event.getSlackChannel().getId());
            SlackChannel newChannel = new SlackChannel(channel.getId(), channel.getName(), channel.getTopic(), channel.getPurpose(), channel.isDirect(), channel.isMember(), false);
            channels.put(newChannel.getId(), newChannel);
        }
    };

    private final SlackTeamJoinListener INTERNAL_TEAM_JOIN_LISTENER = new SlackTeamJoinListener()
    {
        @Override public void onEvent(SlackTeamJoin event, SlackSession session)
        {
            users.put(event.getUser().getId(), event.getUser());
        }
    };

    private final SlackUserChangeListener INTERNAL_USER_CHANGE_LISTENER = new SlackUserChangeListener()
    {
        @Override public void onEvent(SlackUserChange event, SlackSession session)
        {
            users.put(event.getUser().getId(), event.getUser());
        }
    };

}
