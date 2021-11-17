//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.ullink.slack.simpleslackapi.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ullink.slack.simpleslackapi.SlackAttachment;
import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackChatConfiguration;
import com.ullink.slack.simpleslackapi.SlackMessageHandle;
import com.ullink.slack.simpleslackapi.SlackPersona;
import com.ullink.slack.simpleslackapi.SlackPreparedMessage;
import com.ullink.slack.simpleslackapi.SlackPresence;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.SlackUser;
import com.ullink.slack.simpleslackapi.WebSocketContainerProvider;
import com.ullink.slack.simpleslackapi.SlackChatConfiguration.Avatar;
import com.ullink.slack.simpleslackapi.blocks.Block;
import com.ullink.slack.simpleslackapi.events.*;
import com.ullink.slack.simpleslackapi.events.userchange.SlackTeamJoin;
import com.ullink.slack.simpleslackapi.events.userchange.SlackUserChange;
import com.ullink.slack.simpleslackapi.events.userchange.SlackUserChangeEvent;
import com.ullink.slack.simpleslackapi.listeners.*;
import com.ullink.slack.simpleslackapi.replies.EmojiSlackReply;
import com.ullink.slack.simpleslackapi.replies.GenericSlackReply;
import com.ullink.slack.simpleslackapi.replies.ParsedSlackReply;
import com.ullink.slack.simpleslackapi.replies.SlackChannelReply;
import com.ullink.slack.simpleslackapi.replies.SlackMessageReply;
import com.ullink.slack.simpleslackapi.replies.SlackReply;
import com.ullink.slack.simpleslackapi.replies.SlackReplyImpl;
import com.ullink.slack.simpleslackapi.replies.SlackUserPresenceReply;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.ConnectException;
import java.net.URI;
import java.net.Proxy.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import javax.websocket.DeploymentException;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import javax.websocket.MessageHandler.Whole;
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

class SlackWebSocketSessionImpl extends AbstractSlackSessionImpl implements SlackSession, Whole<String> {
    private static final String DEFAULT_SLACK_API_SCHEME = "https";
    private static final String DEFAULT_SLACK_API_HOST = "slack.com";
    private static final String DEFAULT_SLACK_API_PATH = "/api";
    private static final String DEFAULT_SLACK_API_HTTPS_ROOT = "https://slack.com/api/";
    private static final String MULTIPARTY_DIRECT_MESSAGE_OPEN_CHANNEL_COMMAND = "mpim.open";
    private static final String FILE_UPLOAD_COMMAND = "files.upload";
    private static final String INVITE_USER_COMMAND = "users.admin.invite";
    private static final String LIST_EMOJI_COMMAND = "emoji.list";
    private static final Logger LOGGER = LoggerFactory.getLogger(SlackWebSocketSessionImpl.class);
    private static final int DEFAULT_HEARTBEAT_IN_MILLIS = 30000;
    private volatile Session websocketSession;
    private final String authToken;
    private String slackApiBase;
    private String proxyAddress;
    private int proxyPort;
    HttpHost proxyHost;
    private String proxyUser;
    private String proxyPassword;
    private volatile long lastPingSent;
    private volatile long lastPingAck;
    private final AtomicLong messageId;
    private final boolean reconnectOnDisconnection;
    private final boolean isRateLimitSupported;
    private volatile boolean wantDisconnect;
    private Thread connectionMonitoringThread;
    private final SlackWebSocketSessionImpl.EventDispatcher dispatcher;
    private final long heartbeat;
    private final WebSocketContainerProvider webSocketContainerProvider;
    private volatile String webSocketConnectionURL;
    private final PresenceChangeListener INTERNAL_PRESENCE_CHANGE_LISTENER;
    private final SlackChannelArchivedListener INTERNAL_CHANNEL_ARCHIVE_LISTENER;
    private final SlackChannelCreatedListener INTERNAL_CHANNEL_CREATED_LISTENER;
    private final SlackChannelDeletedListener INTERNAL_CHANNEL_DELETED_LISTENER;
    private final SlackChannelRenamedListener INTERNAL_CHANNEL_RENAMED_LISTENER;
    private final SlackChannelUnarchivedListener INTERNAL_CHANNEL_UNARCHIVED_LISTENER;
    private final SlackTeamJoinListener INTERNAL_TEAM_JOIN_LISTENER;
    private final SlackUserChangeListener INTERNAL_USER_CHANGE_LISTENER;

    public SlackMessageHandle<SlackMessageReply> sendMessageToUser(SlackUser user, SlackPreparedMessage message) {
        SlackChannel iMChannel = this.getIMChannelForUser(user);
        return this.sendMessage((SlackChannel)iMChannel, (SlackPreparedMessage)message);
    }

    public SlackMessageHandle<SlackMessageReply> sendMessageToUser(String userName, SlackPreparedMessage preparedMessage) {
        return this.sendMessageToUser(this.findUserByUserName(userName), preparedMessage);
    }

    public SlackMessageHandle<SlackMessageReply> sendMessageToUser(SlackUser user, String message, SlackAttachment attachment) {
        SlackChannel iMChannel = this.getIMChannelForUser(user);
        return this.sendMessage(iMChannel, message, attachment, DEFAULT_CONFIGURATION);
    }

    public SlackMessageHandle<SlackMessageReply> sendMessageToUser(String userName, String message, SlackAttachment attachment) {
        return this.sendMessageToUser(this.findUserByUserName(userName), message, attachment);
    }

    private List<SlackChannel> getAllIMChannels() {
        Collection<SlackChannel> allChannels = this.getChannels();
        List<SlackChannel> iMChannels = new ArrayList();
        Iterator var3 = allChannels.iterator();

        while(var3.hasNext()) {
            SlackChannel channel = (SlackChannel)var3.next();
            if (channel.isDirect()) {
                iMChannels.add(channel);
            }
        }

        return iMChannels;
    }

    private SlackChannel getIMChannelForUser(SlackUser user) {
        List<SlackChannel> imcs = this.getAllIMChannels();
        Iterator var3 = imcs.iterator();

        SlackChannel channel;
        do {
            if (!var3.hasNext()) {
                SlackMessageHandle<SlackChannelReply> reply = this.openDirectMessageChannel(user);
                return ((SlackChannelReply)reply.getReply()).getSlackChannel();
            }

            channel = (SlackChannel)var3.next();
        } while(!channel.getMembers().contains(user));

        return channel;
    }

    SlackWebSocketSessionImpl(WebSocketContainerProvider webSocketContainerProvider, String authToken, String slackApiBase, boolean reconnectOnDisconnection, boolean isRateLimitSupported, long heartbeat, TimeUnit unit) {
        this(webSocketContainerProvider, authToken, slackApiBase, (Type)null, (String)null, -1, (String)null, (String)null, reconnectOnDisconnection, isRateLimitSupported, heartbeat, unit);
    }

    SlackWebSocketSessionImpl(WebSocketContainerProvider webSocketContainerProvider, String authToken, String slackApiBase, Type proxyType, String proxyAddress, int proxyPort, String proxyUser, String proxyPassword, boolean reconnectOnDisconnection, boolean isRateLimitSupported, long heartbeat, TimeUnit unit) {
        this.slackApiBase = "https://slack.com/api/";
        this.proxyPort = -1;
        this.messageId = new AtomicLong();
        this.dispatcher = new SlackWebSocketSessionImpl.EventDispatcher();
        this.INTERNAL_PRESENCE_CHANGE_LISTENER = new PresenceChangeListener() {
            public void onEvent(PresenceChange event, SlackSession session) {
                SlackUser user = (SlackUser)SlackWebSocketSessionImpl.this.users.get(event.getUserId());
                SlackPersonaImpl persona = (SlackPersonaImpl)user;
                SlackProfileImpl profile = persona.getProfile().toBuilder().presence(event.getPresence()).build();
                SlackUser newUser = ((SlackPersonaImpl)user).toBuilder().profile(profile).build();
                SlackWebSocketSessionImpl.this.users.put(event.getUserId(), newUser);
            }
        };
        this.INTERNAL_CHANNEL_ARCHIVE_LISTENER = new SlackChannelArchivedListener() {
            public void onEvent(SlackChannelArchived event, SlackSession session) {
                SlackChannel channel = (SlackChannel)SlackWebSocketSessionImpl.this.channels.get(event.getSlackChannel().getId());
                SlackChannel newChannel = channel.toBuilder().isArchived(true).build();
                SlackWebSocketSessionImpl.this.channels.put(newChannel.getId(), newChannel);
            }
        };
        this.INTERNAL_CHANNEL_CREATED_LISTENER = new SlackChannelCreatedListener() {
            public void onEvent(SlackChannelCreated event, SlackSession session) {
                SlackWebSocketSessionImpl.this.channels.put(event.getSlackChannel().getId(), event.getSlackChannel());
            }
        };
        this.INTERNAL_CHANNEL_DELETED_LISTENER = new SlackChannelDeletedListener() {
            public void onEvent(SlackChannelDeleted event, SlackSession session) {
                SlackWebSocketSessionImpl.this.channels.remove(event.getSlackChannel().getId());
            }
        };
        this.INTERNAL_CHANNEL_RENAMED_LISTENER = new SlackChannelRenamedListener() {
            public void onEvent(SlackChannelRenamed event, SlackSession session) {
                SlackChannel channel = (SlackChannel)SlackWebSocketSessionImpl.this.channels.get(event.getSlackChannel().getId());
                SlackChannel newChannel = channel.toBuilder().name(event.getNewName()).build();
                SlackWebSocketSessionImpl.this.channels.put(newChannel.getId(), newChannel);
            }
        };
        this.INTERNAL_CHANNEL_UNARCHIVED_LISTENER = new SlackChannelUnarchivedListener() {
            public void onEvent(SlackChannelUnarchived event, SlackSession session) {
                SlackChannel channel = (SlackChannel)SlackWebSocketSessionImpl.this.channels.get(event.getSlackChannel().getId());
                SlackChannel newChannel = channel.toBuilder().isArchived(false).build();
                SlackWebSocketSessionImpl.this.channels.put(newChannel.getId(), newChannel);
            }
        };
        this.INTERNAL_TEAM_JOIN_LISTENER = new SlackTeamJoinListener() {
            public void onEvent(SlackTeamJoin event, SlackSession session) {
                SlackWebSocketSessionImpl.this.users.put(event.getUser().getId(), event.getUser());
            }
        };
        this.INTERNAL_USER_CHANGE_LISTENER = new SlackUserChangeListener() {
            public void onEvent(SlackUserChange event, SlackSession session) {
                SlackWebSocketSessionImpl.this.users.put(event.getUser().getId(), event.getUser());
            }
        };
        this.authToken = authToken;
        if (slackApiBase != null) {
            this.slackApiBase = slackApiBase;
        }

        if (proxyType != null && proxyType != Type.DIRECT) {
            this.proxyAddress = proxyAddress;
            this.proxyPort = proxyPort;
            this.proxyHost = new HttpHost(proxyAddress, proxyPort);
            this.proxyUser = proxyUser;
            this.proxyPassword = proxyPassword;
        }

        this.reconnectOnDisconnection = reconnectOnDisconnection;
        this.isRateLimitSupported = isRateLimitSupported;
        this.heartbeat = heartbeat != 0L ? unit.toMillis(heartbeat) : 30000L;
        this.webSocketContainerProvider = (WebSocketContainerProvider)(webSocketContainerProvider != null ? webSocketContainerProvider : new DefaultWebSocketContainerProvider(this.proxyAddress, this.proxyPort, this.proxyUser, this.proxyPassword));
        this.addInternalListeners();
    }

    private void addInternalListeners() {
        this.addPresenceChangeListener(this.INTERNAL_PRESENCE_CHANGE_LISTENER);
        this.addChannelArchivedListener(this.INTERNAL_CHANNEL_ARCHIVE_LISTENER);
        this.addChannelCreatedListener(this.INTERNAL_CHANNEL_CREATED_LISTENER);
        this.addChannelDeletedListener(this.INTERNAL_CHANNEL_DELETED_LISTENER);
        this.addChannelRenamedListener(this.INTERNAL_CHANNEL_RENAMED_LISTENER);
        this.addChannelUnarchivedListener(this.INTERNAL_CHANNEL_UNARCHIVED_LISTENER);
        this.addSlackTeamJoinListener(this.INTERNAL_TEAM_JOIN_LISTENER);
        this.addSlackUserChangeListener(this.INTERNAL_USER_CHANGE_LISTENER);
    }

    public void connect() throws IOException {
        this.wantDisconnect = false;
        this.connectImpl();
        LOGGER.debug("starting actions monitoring");
        this.startConnectionMonitoring();
    }

    public void disconnect() {
        this.wantDisconnect = true;
        LOGGER.debug("Disconnecting from the Slack server");
        this.disconnectImpl();
        this.stopConnectionMonitoring();
    }

    public void reconnect() throws IOException {
        while(this.isConnected()) {
            this.disconnectImpl();
        }

        this.connectImpl();
    }

    public boolean isConnected() {
        return this.websocketSession != null && this.websocketSession.isOpen();
    }

    private void connectImpl() throws IOException {
        LOGGER.info("connecting to slack");
        HttpClient httpClient = this.getHttpClient();
        HttpGet request = new HttpGet(this.slackApiBase + "rtm.start?token=" + this.authToken);
        HttpResponse response = httpClient.execute(request);
        LOGGER.debug(response.getStatusLine().toString());
        String jsonResponse = this.consumeToString(response.getEntity().getContent());
        SlackJSONSessionStatusParser sessionParser = new SlackJSONSessionStatusParser(jsonResponse);
        sessionParser.parse();
        if (sessionParser.getError() != null) {
            LOGGER.error("Error during authentication : " + sessionParser.getError());
            throw new ConnectException(sessionParser.getError());
        } else {
            this.users = sessionParser.getUsers();
            this.integrations = sessionParser.getIntegrations();
            this.channels = sessionParser.getChannels();
            this.sessionPersona = sessionParser.getSessionPersona();
            this.team = sessionParser.getTeam();
            LOGGER.info("Team " + this.team.getId() + " : " + this.team.getName());
            LOGGER.info("Self " + this.sessionPersona.getId() + " : " + this.sessionPersona.getUserName());
            LOGGER.info(this.users.size() + " users found on this session");
            LOGGER.info(this.channels.size() + " channels found on this session");
            this.webSocketConnectionURL = sessionParser.getWebSocketURL();
            LOGGER.debug("retrieved websocket URL : " + this.webSocketConnectionURL);
            this.establishWebsocketConnection();
        }
    }

    private void establishWebsocketConnection() throws IOException {
        this.lastPingSent = 0L;
        this.lastPingAck = 0L;
        WebSocketContainer client = this.webSocketContainerProvider.getWebSocketContainer();
        final MessageHandler handler = this;
        LOGGER.debug("initiating actions to websocket");

        try {
            this.websocketSession = client.connectToServer(new Endpoint() {
                public void onOpen(Session session, EndpointConfig config) {
                    session.addMessageHandler(handler);
                }

                public void onError(Session session, Throwable thr) {
                    SlackWebSocketSessionImpl.LOGGER.error("Endpoint#onError called", thr);
                }
            }, URI.create(this.webSocketConnectionURL));
        } catch (DeploymentException var4) {
            LOGGER.error(var4.toString());
            throw new IOException(var4);
        }

        if (this.websocketSession != null) {
            SlackConnected slackConnected = new SlackConnected(this.sessionPersona);
            this.dispatcher.dispatch(slackConnected);
            LOGGER.debug("websocket actions established");
            LOGGER.info("slack session ready");
        } else {
            throw new IOException("Unable to establish a connection to this websocket URL " + this.webSocketConnectionURL);
        }
    }

    private String consumeToString(InputStream content) throws IOException {
        Reader reader = new InputStreamReader(content, StandardCharsets.UTF_8);
        StringBuilder buf = new StringBuilder();
        char[] data = new char[16384];

        int numread;
        while(0 <= (numread = reader.read(data))) {
            buf.append(data, 0, numread);
        }

        return buf.toString();
    }

    private void disconnectImpl() {
        if (this.websocketSession != null) {
            boolean var6 = false;

            SlackDisconnected slackDisconnected;
            label48: {
                try {
                    var6 = true;
                    this.websocketSession.close();
                    var6 = false;
                    break label48;
                } catch (IOException var7) {
                    var6 = false;
                } finally {
                    if (var6) {
                        slackDisconnected = new SlackDisconnected(this.sessionPersona);
                        this.dispatcher.dispatch(slackDisconnected);
                        this.websocketSession = null;
                    }
                }

                slackDisconnected = new SlackDisconnected(this.sessionPersona);
                this.dispatcher.dispatch(slackDisconnected);
                this.websocketSession = null;
                return;
            }

            slackDisconnected = new SlackDisconnected(this.sessionPersona);
            this.dispatcher.dispatch(slackDisconnected);
            this.websocketSession = null;
        }

    }

    private void startConnectionMonitoring() {
        this.connectionMonitoringThread = new Thread() {
            public void run() {
                SlackWebSocketSessionImpl.LOGGER.debug("monitoring thread started");

                while(true) {
                    while(true) {
                        try {
                            Thread.sleep(SlackWebSocketSessionImpl.this.heartbeat);
                            if (SlackWebSocketSessionImpl.this.wantDisconnect) {
                                this.interrupt();
                            }

                            if (SlackWebSocketSessionImpl.this.lastPingSent == SlackWebSocketSessionImpl.this.lastPingAck && SlackWebSocketSessionImpl.this.websocketSession != null) {
                                SlackWebSocketSessionImpl.this.lastPingSent = SlackWebSocketSessionImpl.this.getNextMessageId();
                                SlackWebSocketSessionImpl.LOGGER.debug("sending ping " + SlackWebSocketSessionImpl.this.lastPingSent);

                                try {
                                    if (SlackWebSocketSessionImpl.this.websocketSession.isOpen()) {
                                        SlackWebSocketSessionImpl.this.websocketSession.getBasicRemote().sendText("{\"type\":\"ping\",\"id\":" + SlackWebSocketSessionImpl.this.lastPingSent + "}");
                                    } else if (SlackWebSocketSessionImpl.this.reconnectOnDisconnection) {
                                        SlackWebSocketSessionImpl.this.reconnect();
                                    }
                                } catch (IllegalStateException var3) {
                                    SlackWebSocketSessionImpl.LOGGER.warn("exception caught while using websocket ", var3);
                                    if (SlackWebSocketSessionImpl.this.reconnectOnDisconnection) {
                                        SlackWebSocketSessionImpl.this.reconnect();
                                    }
                                }
                            } else {
                                SlackWebSocketSessionImpl.LOGGER.warn("Connection lost...");

                                try {
                                    if (SlackWebSocketSessionImpl.this.websocketSession != null) {
                                        SlackWebSocketSessionImpl.this.websocketSession.close();
                                    }
                                } catch (IOException var2) {
                                    SlackWebSocketSessionImpl.LOGGER.error("exception while trying to close the websocket ", var2);
                                }

                                SlackWebSocketSessionImpl.this.websocketSession = null;
                                if (SlackWebSocketSessionImpl.this.reconnectOnDisconnection) {
                                    SlackWebSocketSessionImpl.this.reconnect();
                                } else {
                                    this.interrupt();
                                }
                            }
                        } catch (InterruptedException var4) {
                            SlackWebSocketSessionImpl.LOGGER.info("monitoring thread interrupted");
                            SlackWebSocketSessionImpl.LOGGER.debug("monitoring thread stopped");
                            return;
                        } catch (IOException var5) {
                            SlackWebSocketSessionImpl.LOGGER.error("unexpected exception on monitoring thread ", var5);
                        }
                    }
                }
            }
        };
        if (!this.wantDisconnect) {
            this.connectionMonitoringThread.start();
        }

    }

    private void stopConnectionMonitoring() {
        if (this.connectionMonitoringThread != null) {
            while(true) {
                try {
                    this.connectionMonitoringThread.interrupt();
                    this.connectionMonitoringThread.join();
                    break;
                } catch (InterruptedException var2) {
                }
            }
        }

    }

    public SlackMessageHandle<SlackMessageReply> sendMessage(SlackChannel channel, SlackPreparedMessage preparedMessage, SlackChatConfiguration chatConfiguration) {
        if (channel == null) {
            throw new IllegalArgumentException("Channel can't be null");
        } else {
            SlackMessageHandle<SlackMessageReply> handle = new SlackMessageHandle(this.getNextMessageId());
            Map<String, String> arguments = new HashMap();
            arguments.put("token", this.authToken);
            arguments.put("channel", channel.getId());
            arguments.put("text", preparedMessage.getMessage());
            if (chatConfiguration.isAsUser()) {
                arguments.put("as_user", "true");
            }

            if (chatConfiguration.getAvatar() == Avatar.ICON_URL) {
                arguments.put("icon_url", chatConfiguration.getAvatarDescription());
            }

            if (chatConfiguration.getAvatar() == Avatar.EMOJI) {
                arguments.put("icon_emoji", chatConfiguration.getAvatarDescription());
            }

            if (chatConfiguration.getUserName() != null) {
                arguments.put("username", chatConfiguration.getUserName());
            }

            if (preparedMessage.getAttachments() != null && preparedMessage.getAttachments().size() > 0) {
                arguments.put("attachments", SlackJSONAttachmentFormatter.encodeAttachments(preparedMessage.getAttachments()).toString());
            }

            if (preparedMessage.getBlocks() != null && preparedMessage.getBlocks().size() > 0) {
                arguments.put("blocks", SlackJSONBlockFormatter.encodeBlocks(preparedMessage.getBlocks()));
            }

            if (!preparedMessage.isUnfurl()) {
                arguments.put("unfurl_links", "false");
                arguments.put("unfurl_media", "false");
            }

            if (preparedMessage.isLinkNames()) {
                arguments.put("link_names", "1");
            }

            if (preparedMessage.getThreadTimestamp() != null) {
                arguments.put("thread_ts", preparedMessage.getThreadTimestamp());
                if (preparedMessage.isReplyBroadcast()) {
                    arguments.put("reply_broadcast", "true");
                }
            }

            this.postSlackCommand(arguments, "chat.postMessage", handle, SlackMessageReply.class);
            return handle;
        }
    }

    public SlackMessageHandle<SlackMessageReply> deleteMessage(String timeStamp, String channelId) {
        return this.deleteMessage(timeStamp, this.findChannelById(channelId));
    }

    public SlackMessageHandle<SlackMessageReply> sendMessage(String channelId, SlackPreparedMessage preparedMessage, SlackChatConfiguration chatConfiguration) {
        return this.sendMessage(this.findChannelById(channelId), preparedMessage, chatConfiguration);
    }

    public SlackMessageHandle<SlackMessageReply> sendMessage(String channelId, SlackPreparedMessage preparedMessage) {
        return this.sendMessage((SlackChannel)this.findChannelById(channelId), (SlackPreparedMessage)preparedMessage);
    }

    public SlackMessageHandle<SlackMessageReply> sendMessage(String channelId, String message, SlackAttachment attachment, SlackChatConfiguration chatConfiguration, boolean unfurl) {
        return this.sendMessage(this.findChannelById(channelId), message, attachment, chatConfiguration, unfurl);
    }

    public SlackMessageHandle<SlackMessageReply> sendMessage(String channelId, String message, SlackAttachment attachment, SlackChatConfiguration chatConfiguration) {
        return this.sendMessage(this.findChannelById(channelId), message, attachment, chatConfiguration);
    }

    public SlackMessageHandle<SlackMessageReply> sendMessage(String channelId, String message, SlackAttachment attachment, boolean unfurl) {
        return this.sendMessage(this.findChannelById(channelId), message, attachment, unfurl);
    }

    public SlackMessageHandle<SlackMessageReply> sendMessage(String channelId, String message, SlackAttachment attachment) {
        return this.sendMessage((SlackChannel)this.findChannelById(channelId), (String)message, (SlackAttachment)attachment);
    }

    public SlackMessageHandle<SlackMessageReply> sendMessage(String channelId, String message, boolean unfurl) {
        return this.sendMessage(this.findChannelById(channelId), message, unfurl);
    }

    public SlackMessageHandle<SlackMessageReply> sendMessage(String channelId, String message) {
        return this.sendMessage((SlackChannel)this.findChannelById(channelId), (String)message);
    }

    public SlackMessageHandle<SlackMessageReply> sendEphemeralMessage(SlackChannel channel, SlackUser user, SlackPreparedMessage preparedMessage, SlackChatConfiguration chatConfiguration) {
        SlackMessageHandle<SlackMessageReply> handle = new SlackMessageHandle(this.getNextMessageId());
        Map<String, String> arguments = new HashMap();
        arguments.put("token", this.authToken);
        arguments.put("channel", channel.getId());
        arguments.put("text", preparedMessage.getMessage());
        arguments.put("user", user.getId());
        if (chatConfiguration.isAsUser()) {
            arguments.put("as_user", "true");
        }

        if (chatConfiguration.getAvatar() == Avatar.ICON_URL) {
            arguments.put("icon_url", chatConfiguration.getAvatarDescription());
        }

        if (chatConfiguration.getAvatar() == Avatar.EMOJI) {
            arguments.put("icon_emoji", chatConfiguration.getAvatarDescription());
        }

        if (chatConfiguration.getUserName() != null) {
            arguments.put("username", chatConfiguration.getUserName());
        }

        if (preparedMessage.getAttachments() != null && preparedMessage.getAttachments().size() > 0) {
            arguments.put("attachments", SlackJSONAttachmentFormatter.encodeAttachments(preparedMessage.getAttachments()).toString());
        }

        if (preparedMessage.getBlocks() != null && preparedMessage.getBlocks().size() > 0) {
            arguments.put("blocks", SlackJSONBlockFormatter.encodeBlocks(preparedMessage.getBlocks()));
        }

        if (!preparedMessage.isUnfurl()) {
            arguments.put("unfurl_links", "false");
            arguments.put("unfurl_media", "false");
        }

        if (preparedMessage.isLinkNames()) {
            arguments.put("link_names", "1");
        }

        if (preparedMessage.getThreadTimestamp() != null) {
            arguments.put("thread_ts", preparedMessage.getThreadTimestamp());
            if (preparedMessage.isReplyBroadcast()) {
                arguments.put("reply_broadcast", "true");
            }
        }

        this.postSlackCommand(arguments, "chat.postEphemeral", handle, SlackMessageReply.class);
        return handle;
    }

    public SlackMessageHandle<SlackMessageReply> sendFileToUser(String userName, InputStream data, String fileName) {
        return this.sendFileToUser(this.findUserByUserName(userName), data, fileName);
    }

    public SlackMessageHandle<SlackMessageReply> sendFileToUser(SlackUser user, InputStream data, String fileName) {
        SlackChannel iMChannel = this.getIMChannelForUser(user);
        return this.sendFile(iMChannel, data, fileName);
    }

    public SlackMessageHandle<SlackMessageReply> sendFile(SlackChannel channel, InputStream data, String fileName) {
        SlackMessageHandle<SlackMessageReply> handle = new SlackMessageHandle(this.getNextMessageId());
        Map<String, String> arguments = new HashMap();
        arguments.put("token", this.authToken);
        arguments.put("channels", channel.getId());
        arguments.put("filename", fileName);
        this.postSlackCommandWithFile(arguments, data, fileName, handle);
        return handle;
    }

    public SlackMessageHandle<SlackMessageReply> sendFile(SlackChannel channel, InputStream data, String fileName, String title, String initialComment) {
        SlackMessageHandle<SlackMessageReply> handle = new SlackMessageHandle(this.getNextMessageId());
        Map<String, String> arguments = new HashMap();
        arguments.put("token", this.authToken);
        arguments.put("channels", channel.getId());
        arguments.put("filename", fileName);
        arguments.put("title", title);
        arguments.put("initial_comment", initialComment);
        this.postSlackCommandWithFile(arguments, data, fileName, handle);
        return handle;
    }

    public SlackMessageHandle<SlackMessageReply> sendEphemeralMessage(String channelId, String user, SlackPreparedMessage preparedMessage, SlackChatConfiguration chatConfiguration) {
        return this.sendEphemeralMessage(this.findChannelById(channelId), this.findUserByUserName(user), preparedMessage, chatConfiguration);
    }

    public SlackMessageHandle<SlackMessageReply> sendEphemeralMessage(String channelId, String user, SlackPreparedMessage preparedMessage) {
        return this.sendEphemeralMessage((SlackChannel)this.findChannelById(channelId), (SlackUser)this.findUserByUserName(user), (SlackPreparedMessage)preparedMessage);
    }

    public SlackMessageHandle<SlackMessageReply> sendEphemeralMessage(String channelId, String user, String message, SlackAttachment attachment, SlackChatConfiguration chatConfiguration, boolean unfurl) {
        return this.sendEphemeralMessage(this.findChannelById(channelId), this.findUserByUserName(user), message, attachment, chatConfiguration, unfurl);
    }

    public SlackMessageHandle<SlackMessageReply> sendEphemeralMessage(String channelId, String user, String message, SlackAttachment attachment, SlackChatConfiguration chatConfiguration) {
        return this.sendEphemeralMessage(this.findChannelById(channelId), this.findUserByUserName(user), message, attachment, chatConfiguration);
    }

    public SlackMessageHandle<SlackMessageReply> sendEphemeralMessage(String channelId, String user, String message, SlackAttachment attachment, boolean unfurl) {
        return this.sendEphemeralMessage(this.findChannelById(channelId), this.findUserByUserName(user), message, attachment, unfurl);
    }

    public SlackMessageHandle<SlackMessageReply> sendEphemeralMessage(String channelId, String user, String message, SlackAttachment attachment) {
        return this.sendEphemeralMessage((SlackChannel)this.findChannelById(channelId), (SlackUser)this.findUserByUserName(user), (String)message, (SlackAttachment)attachment);
    }

    public SlackMessageHandle<SlackMessageReply> sendEphemeralMessage(String channelId, String user, String message, boolean unfurl) {
        return this.sendEphemeralMessage(this.findChannelById(channelId), this.findUserByUserName(user), message, unfurl);
    }

    public SlackMessageHandle<SlackMessageReply> sendEphemeralMessage(String channelId, String user, String message) {
        return this.sendEphemeralMessage((SlackChannel)this.findChannelById(channelId), (SlackUser)this.findUserByUserName(user), (String)message);
    }

    public SlackMessageHandle<SlackMessageReply> sendFile(String channelId, InputStream data, String fileName) {
        return this.sendFile(this.findChannelById(channelId), data, fileName);
    }

    public SlackMessageHandle<SlackMessageReply> sendFile(String channelId, InputStream data, String fileName, String title, String initialComment) {
        return this.sendFile(this.findChannelById(channelId), data, fileName, title, initialComment);
    }

    public SlackMessageHandle<SlackMessageReply> deleteMessage(String timeStamp, SlackChannel channel) {
        SlackMessageHandle<SlackMessageReply> handle = new SlackMessageHandle(this.getNextMessageId());
        Map<String, String> arguments = new HashMap();
        arguments.put("token", this.authToken);
        arguments.put("channel", channel.getId());
        arguments.put("ts", timeStamp);
        this.postSlackCommand(arguments, "chat.delete", handle, SlackMessageReply.class);
        return handle;
    }

    public SlackMessageHandle<SlackMessageReply> updateMessage(String timeStamp, SlackChannel channel, String message) {
        SlackMessageHandle<SlackMessageReply> handle = new SlackMessageHandle(this.getNextMessageId());
        Map<String, String> arguments = new HashMap();
        arguments.put("token", this.authToken);
        arguments.put("ts", timeStamp);
        arguments.put("channel", channel.getId());
        arguments.put("text", message);
        this.postSlackCommand(arguments, "chat.update", handle, SlackMessageReply.class);
        return handle;
    }

    public SlackMessageHandle<SlackMessageReply> updateMessage(String timeStamp, SlackChannel channel, String message, SlackAttachment[] attachments) {
        return this.updateMessage(timeStamp, (SlackChannel)channel, message, attachments, new ArrayList());
    }

    public SlackMessageHandle<SlackMessageReply> updateMessage(String timeStamp, SlackChannel channel, String message, SlackAttachment[] attachments, List<Block> blocks) {
        SlackMessageHandle<SlackMessageReply> handle = new SlackMessageHandle(this.getNextMessageId());
        Map<String, String> arguments = new HashMap();
        arguments.put("token", this.authToken);
        arguments.put("ts", timeStamp);
        arguments.put("channel", channel.getId());
        arguments.put("text", message);
        arguments.put("attachments", SlackJSONAttachmentFormatter.encodeAttachments(attachments).toString());
        arguments.put("blocks", SlackJSONBlockFormatter.encodeBlocks(blocks));
        this.postSlackCommand(arguments, "chat.update", handle, SlackMessageReply.class);
        return handle;
    }

    public SlackMessageHandle<SlackMessageReply> addReactionToMessage(SlackChannel channel, String messageTimeStamp, String emojiCode) {
        SlackMessageHandle<SlackMessageReply> handle = new SlackMessageHandle(this.getNextMessageId());
        Map<String, String> arguments = new HashMap();
        arguments.put("token", this.authToken);
        arguments.put("channel", channel.getId());
        arguments.put("timestamp", messageTimeStamp);
        arguments.put("name", emojiCode);
        this.postSlackCommand(arguments, "reactions.add", handle, SlackMessageReply.class);
        return handle;
    }

    public SlackMessageHandle<SlackMessageReply> removeReactionFromMessage(SlackChannel channel, String messageTimeStamp, String emojiCode) {
        SlackMessageHandle<SlackMessageReply> handle = new SlackMessageHandle(this.getNextMessageId());
        Map<String, String> arguments = new HashMap();
        arguments.put("token", this.authToken);
        arguments.put("channel", channel.getId());
        arguments.put("timestamp", messageTimeStamp);
        arguments.put("name", emojiCode);
        this.postSlackCommand(arguments, "reactions.remove", handle, SlackMessageReply.class);
        return handle;
    }

    public SlackMessageHandle<SlackChannelReply> joinChannel(String channelName) {
        SlackMessageHandle<SlackChannelReply> handle = new SlackMessageHandle(this.getNextMessageId());
        Map<String, String> arguments = new HashMap();
        arguments.put("token", this.authToken);
        arguments.put("name", channelName);
        this.postSlackCommand(arguments, "conversations.join", handle, SlackChannelReply.class);
        return handle;
    }

    public SlackMessageHandle<SlackChannelReply> setChannelTopic(SlackChannel channel, String topic) {
        SlackMessageHandle<SlackChannelReply> handle = new SlackMessageHandle(this.getNextMessageId());
        Map<String, String> arguments = new HashMap();
        arguments.put("token", this.authToken);
        arguments.put("channel", channel.getId());
        arguments.put("topic", topic);
        this.postSlackCommand(arguments, "conversations.setTopic", handle, SlackChannelReply.class);
        return handle;
    }

    public SlackMessageHandle<SlackChannelReply> leaveChannel(SlackChannel channel) {
        SlackMessageHandle<SlackChannelReply> handle = new SlackMessageHandle(this.getNextMessageId());
        Map<String, String> arguments = new HashMap();
        arguments.put("token", this.authToken);
        arguments.put("channel", channel.getId());
        this.postSlackCommand(arguments, "conversations.leave", handle, SlackChannelReply.class);
        return handle;
    }

    public SlackMessageHandle<SlackChannelReply> inviteToChannel(SlackChannel channel, SlackUser user) {
        SlackMessageHandle<SlackChannelReply> handle = new SlackMessageHandle(this.getNextMessageId());
        Map<String, String> arguments = new HashMap();
        arguments.put("token", this.authToken);
        arguments.put("channel", channel.getId());
        arguments.put("user", user.getId());
        this.postSlackCommand(arguments, "conversations.invite", handle, SlackChannelReply.class);
        return handle;
    }

    public SlackMessageHandle<ParsedSlackReply> archiveChannel(SlackChannel channel) {
        SlackMessageHandle<ParsedSlackReply> handle = new SlackMessageHandle(this.getNextMessageId());
        Map<String, String> arguments = new HashMap();
        arguments.put("token", this.authToken);
        arguments.put("channel", channel.getId());
        this.postSlackCommand(arguments, "conversations.archive", handle, SlackReplyImpl.class);
        return handle;
    }

    public SlackMessageHandle<ParsedSlackReply> unarchiveChannel(SlackChannel channel) {
        SlackMessageHandle<ParsedSlackReply> handle = new SlackMessageHandle(this.getNextMessageId());
        Map<String, String> arguments = new HashMap();
        arguments.put("token", this.authToken);
        arguments.put("channel", channel.getId());
        this.postSlackCommand(arguments, "conversations.unarchive", handle, SlackReplyImpl.class);
        return handle;
    }

    public SlackMessageHandle<SlackMessageReply> updateMessage(String timeStamp, String channelId, String message) {
        return this.updateMessage(timeStamp, this.findChannelById(channelId), message);
    }

    public SlackMessageHandle<SlackMessageReply> updateMessage(String timeStamp, String channelId, String message, SlackAttachment[] attachments) {
        return this.updateMessage(timeStamp, this.findChannelById(channelId), message, attachments);
    }

    public SlackMessageHandle<SlackMessageReply> updateMessage(String timeStamp, String channelId, String message, SlackAttachment[] attachments, List<Block> blocks) {
        return this.updateMessage(timeStamp, this.findChannelById(channelId), message, attachments, blocks);
    }

    public SlackMessageHandle<SlackMessageReply> sendMessageOverWebSocket(String channelId, String message) {
        return this.sendMessageOverWebSocket(this.findChannelById(channelId), message);
    }

    public SlackMessageHandle<SlackMessageReply> addReactionToMessage(String channelId, String messageTimeStamp, String emojiCode) {
        return this.addReactionToMessage(this.findChannelById(channelId), messageTimeStamp, emojiCode);
    }

    public SlackMessageHandle<SlackMessageReply> removeReactionFromMessage(String channelId, String messageTimeStamp, String emojiCode) {
        return this.removeReactionFromMessage(this.findChannelById(channelId), messageTimeStamp, emojiCode);
    }

    public SlackMessageHandle<SlackChannelReply> setChannelTopic(String channelId, String topic) {
        return this.setChannelTopic(this.findChannelById(channelId), topic);
    }

    public SlackMessageHandle<SlackChannelReply> leaveChannel(String channelId) {
        return this.leaveChannel(this.findChannelById(channelId));
    }

    public SlackMessageHandle<SlackChannelReply> inviteToChannel(String channelId, SlackUser user) {
        return this.inviteToChannel(this.findChannelById(channelId), user);
    }

    public SlackMessageHandle<SlackChannelReply> inviteToChannel(String channelId, String userName) {
        return this.inviteToChannel(this.findChannelById(channelId), this.findUserByUserName(userName));
    }

    public SlackMessageHandle<ParsedSlackReply> archiveChannel(String channelId) {
        return this.archiveChannel(this.findChannelById(channelId));
    }

    public SlackMessageHandle<ParsedSlackReply> unarchiveChannel(String channelId) {
        return this.unarchiveChannel(this.findChannelById(channelId));
    }

    public SlackMessageHandle<SlackChannelReply> openDirectMessageChannel(SlackUser user) {
        SlackMessageHandle<SlackChannelReply> handle = new SlackMessageHandle(this.getNextMessageId());
        Map<String, String> arguments = new HashMap();
        arguments.put("token", this.authToken);
        arguments.put("user", user.getId());
        this.postSlackCommand(arguments, "conversations.open", handle, SlackChannelReply.class);
        return handle;
    }

    public SlackMessageHandle<SlackChannelReply> openMultipartyDirectMessageChannel(SlackUser... users) {
        SlackMessageHandle<SlackChannelReply> handle = new SlackMessageHandle(this.getNextMessageId());
        Map<String, String> arguments = new HashMap();
        arguments.put("token", this.authToken);
        StringBuilder strBuilder = new StringBuilder();

        for(int i = 0; i < users.length; ++i) {
            if (i != 0) {
                strBuilder.append(',');
            }

            strBuilder.append(users[i].getId());
        }

        arguments.put("users", strBuilder.toString());
        this.postSlackCommand(arguments, "mpim.open", handle, SlackChannelReply.class);
        if (!((SlackChannelReply)handle.getReply()).isOk()) {
            LOGGER.debug("Error occurred while performing command: '" + ((SlackChannelReply)handle.getReply()).getErrorMessage() + "'");
            return null;
        } else {
            return handle;
        }
    }

    public SlackMessageHandle<EmojiSlackReply> listEmoji() {
        SlackMessageHandle<EmojiSlackReply> handle = new SlackMessageHandle(this.getNextMessageId());
        Map<String, String> arguments = new HashMap();
        arguments.put("token", this.authToken);
        this.postSlackCommand(arguments, "emoji.list", handle, EmojiSlackReply.class);
        return handle;
    }

    public void refetchUsers() {
        Map<String, String> params = new HashMap();
        params.put("presence", "1");
        SlackMessageHandle<GenericSlackReply> handle = this.postGenericSlackCommand(params, "users.list");
        GenericSlackReply replyEv = (GenericSlackReply)handle.getReply();
        String answer = replyEv.getPlainAnswer();
        JsonParser parser = new JsonParser();
        JsonObject answerJson = parser.parse(answer).getAsJsonObject();
        JsonArray membersjson = answerJson.get("members").getAsJsonArray();
        Map<String, SlackUser> members = new HashMap();
        if (membersjson != null) {
            Iterator var9 = membersjson.iterator();

            while(var9.hasNext()) {
                JsonElement member = (JsonElement)var9.next();
                SlackUser user = SlackJSONParsingUtils.buildSlackUser(member.getAsJsonObject());
                members.put(user.getId(), user);
            }
        }

        this.users = members;
    }

    private <T extends SlackReply> void postSlackCommand(Map<String, String> params, String command, SlackMessageHandle handle, Class<T> replyType) {
        HttpClient client = this.getHttpClient();
        HttpPost request = new HttpPost(this.slackApiBase + command);
        List<NameValuePair> nameValuePairList = new ArrayList();
        Iterator var8 = params.entrySet().iterator();

        while(var8.hasNext()) {
            Entry<String, String> arg = (Entry)var8.next();
            nameValuePairList.add(new BasicNameValuePair((String)arg.getKey(), (String)arg.getValue()));
        }

        try {
            request.setEntity(new UrlEncodedFormEntity(nameValuePairList, "UTF-8"));
            T reply = (T) client.execute(request, new SlackResponseHandler(replyType));
            handle.setReply(reply);
        } catch (Exception var10) {
            var10.printStackTrace();
        }

    }

    private <T extends SlackReply> void postSlackCommandWithFile(Map<String, String> params, InputStream fileContent, String fileName, SlackMessageHandle handle) {
        try {
            URIBuilder uriBuilder = new URIBuilder(this.slackApiBase + "files.upload");
            Iterator var6 = params.entrySet().iterator();

            while(var6.hasNext()) {
                Entry<String, String> arg = (Entry)var6.next();
                uriBuilder.setParameter((String)arg.getKey(), (String)arg.getValue());
            }

            HttpPost request = new HttpPost(uriBuilder.toString());
            HttpClient client = this.getHttpClient();
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.addBinaryBody("file", fileContent, ContentType.DEFAULT_BINARY, fileName);
            request.setEntity(builder.build());
            T reply = (T) client.execute(request, new SlackResponseHandler(SlackMessageReply.class));
            handle.setReply(reply);
        } catch (Exception var10) {
            var10.printStackTrace();
        }

    }

    public SlackMessageHandle<GenericSlackReply> postGenericSlackCommand(Map<String, String> params, String command) {
        HttpClient client = this.getHttpClient();
        HttpPost request = new HttpPost(this.slackApiBase + command);
        List<NameValuePair> nameValuePairList = new ArrayList();
        Iterator var6 = params.entrySet().iterator();

        while(var6.hasNext()) {
            Entry<String, String> arg = (Entry)var6.next();
            if (!"token".equals(arg.getKey())) {
                nameValuePairList.add(new BasicNameValuePair((String)arg.getKey(), (String)arg.getValue()));
            }
        }

        nameValuePairList.add(new BasicNameValuePair("token", this.authToken));

        try {
            SlackMessageHandle<GenericSlackReply> handle = new SlackMessageHandle(this.getNextMessageId());
            request.setEntity(new UrlEncodedFormEntity(nameValuePairList, "UTF-8"));
            HttpResponse response = client.execute(request);
            String jsonResponse = this.consumeToString(response.getEntity().getContent());
            LOGGER.debug("PostMessage return: " + jsonResponse);
            GenericSlackReply reply = new GenericSlackReply(jsonResponse);
            handle.setReply(reply);
            return handle;
        } catch (Exception var10) {
            var10.printStackTrace();
            return null;
        }
    }

    private HttpClient getHttpClient() {
        HttpClientBuilder builder = HttpClientBuilder.create();
        if (this.proxyHost != null) {
            if (null == this.proxyUser) {
                builder.setRoutePlanner(new DefaultProxyRoutePlanner(this.proxyHost));
            } else {
                RequestConfig config = RequestConfig.custom().setProxy(this.proxyHost).build();
                CredentialsProvider credsProvider = new BasicCredentialsProvider();
                credsProvider.setCredentials(new AuthScope(this.proxyHost), new UsernamePasswordCredentials(this.proxyUser, this.proxyPassword));
                builder.setDefaultCredentialsProvider(credsProvider).setDefaultRequestConfig(config);
            }
        }

        if (this.isRateLimitSupported) {
            builder.setServiceUnavailableRetryStrategy(new SlackRateLimitRetryStrategy());
        }

        return builder.build();
    }

    public SlackMessageHandle<SlackMessageReply> sendMessageOverWebSocket(SlackChannel channel, String message) {
        SlackMessageHandle handle = new SlackMessageHandle(this.getNextMessageId());

        try {
            JsonObject messageJSON = new JsonObject();
            messageJSON.addProperty("id", handle.getMessageId());
            messageJSON.addProperty("type", "message");
            messageJSON.addProperty("channel", channel.getId());
            messageJSON.addProperty("text", message);
            this.websocketSession.getBasicRemote().sendText(messageJSON.toString());
        } catch (Exception var5) {
            var5.printStackTrace();
        }

        return handle;
    }

    public SlackMessageHandle<SlackMessageReply> sendTyping(SlackChannel channel) {
        SlackMessageHandle handle = new SlackMessageHandle(this.getNextMessageId());

        try {
            JsonObject messageJSON = new JsonObject();
            messageJSON.addProperty("id", handle.getMessageId());
            messageJSON.addProperty("type", "typing");
            messageJSON.addProperty("channel", channel.getId());
            this.websocketSession.getBasicRemote().sendText(messageJSON.toString());
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        return handle;
    }

    public SlackMessageHandle<SlackMessageReply> sendTyping(String channelId) {
        return this.sendTyping(channelId);
    }

    public SlackPresence getPresence(SlackPersona persona) {
        HttpClient client = this.getHttpClient();
        HttpPost request = new HttpPost(this.slackApiBase + "users.getPresence");
        List<NameValuePair> nameValuePairList = new ArrayList();
        nameValuePairList.add(new BasicNameValuePair("token", this.authToken));
        nameValuePairList.add(new BasicNameValuePair("user", persona.getId()));

        try {
            request.setEntity(new UrlEncodedFormEntity(nameValuePairList, "UTF-8"));
            HttpResponse response = client.execute(request);
            String jsonResponse = this.consumeToString(response.getEntity().getContent());
            LOGGER.debug("PostMessage return: " + jsonResponse);
            JsonObject resultObject = this.parseObject(jsonResponse);
            SlackUserPresenceReply reply = (SlackUserPresenceReply)SlackJSONReplyParser.decode(resultObject, this);
            if (!reply.isOk()) {
                return SlackPresence.UNKNOWN;
            }

            String presence = resultObject.get("presence") != null ? resultObject.get("presence").getAsString() : null;
            if ("active".equals(presence)) {
                return SlackPresence.ACTIVE;
            }

            if ("away".equals(presence)) {
                return SlackPresence.AWAY;
            }
        } catch (Exception var10) {
            var10.printStackTrace();
        }

        return SlackPresence.UNKNOWN;
    }

    public void setPresence(SlackPresence presence) {
        if (presence != SlackPresence.UNKNOWN && presence != SlackPresence.ACTIVE) {
            HttpClient client = this.getHttpClient();
            HttpPost request = new HttpPost(this.slackApiBase + "users.setPresence");
            List<NameValuePair> nameValuePairList = new ArrayList();
            nameValuePairList.add(new BasicNameValuePair("token", this.authToken));
            nameValuePairList.add(new BasicNameValuePair("presence", presence.toString().toLowerCase()));

            try {
                request.setEntity(new UrlEncodedFormEntity(nameValuePairList, "UTF-8"));
                HttpResponse response = client.execute(request);
                String JSONResponse = this.consumeToString(response.getEntity().getContent());
                LOGGER.debug("JSON Response=" + JSONResponse);
            } catch (IOException var7) {
                var7.printStackTrace();
            }

        } else {
            throw new IllegalArgumentException("Presence must be either AWAY or AUTO");
        }
    }

    private long getNextMessageId() {
        return this.messageId.getAndIncrement();
    }

    public void onMessage(String message) {
        JsonObject object = this.parseObject(message);
        LOGGER.debug("receiving from websocket " + message);
        if (object.get("type") == null) {
            LOGGER.info("unable to parse message, missing 'type' attribute: " + message);
        } else {
            if ("pong".equals(object.get("type").getAsString())) {
                this.lastPingAck = (long)object.get("reply_to").getAsInt();
                LOGGER.debug("pong received " + this.lastPingAck);
            } else if ("reconnect_url".equals(object.get("type").getAsString())) {
                String newWebSocketConnectionURL = object.get("url").getAsString();
                LOGGER.debug("new websocket connection received " + newWebSocketConnectionURL);
            } else {
                SlackEvent slackEvent = SlackJSONMessageParser.decode(this, object);
                if (slackEvent instanceof SlackChannelCreated) {
                    SlackChannelCreated slackChannelCreated = (SlackChannelCreated)slackEvent;
                    this.channels.put(slackChannelCreated.getSlackChannel().getId(), slackChannelCreated.getSlackChannel());
                }

                if (slackEvent instanceof SlackGroupJoined) {
                    SlackGroupJoined slackGroupJoined = (SlackGroupJoined)slackEvent;
                    this.channels.put(slackGroupJoined.getSlackChannel().getId(), slackGroupJoined.getSlackChannel());
                }

                if (slackEvent instanceof SlackUserChangeEvent) {
                    SlackUserChangeEvent slackUserChangeEvent = (SlackUserChangeEvent)slackEvent;
                    this.users.put(slackUserChangeEvent.getUser().getId(), slackUserChangeEvent.getUser());
                }

                this.dispatcher.dispatch(slackEvent);
            }

        }
    }

    private JsonObject parseObject(String json) {
        JsonParser parser = new JsonParser();
        return parser.parse(json).getAsJsonObject();
    }

    public SlackMessageHandle<ParsedSlackReply> inviteUser(String email, String firstName, boolean setActive) {
        SlackMessageHandle<ParsedSlackReply> handle = new SlackMessageHandle(this.getNextMessageId());
        Map<String, String> arguments = new HashMap();
        arguments.put("token", this.authToken);
        arguments.put("email", email);
        arguments.put("first_name", firstName);
        arguments.put("set_active", "" + setActive);
        this.postSlackCommand(arguments, "users.admin.invite", handle, SlackReplyImpl.class);
        return handle;
    }

    public long getHeartbeat() {
        return TimeUnit.MILLISECONDS.toSeconds(this.heartbeat);
    }

    public class EventDispatcher {
        public EventDispatcher() {
        }

        void dispatch(SlackEvent event) {
            switch(event.getEventType()) {
                case SLACK_CHANNEL_ARCHIVED:
                    this.dispatchImpl((SlackChannelArchived)event, SlackWebSocketSessionImpl.this.channelArchiveListener);
                    break;
                case SLACK_CHANNEL_CREATED:
                    this.dispatchImpl((SlackChannelCreated)event, SlackWebSocketSessionImpl.this.channelCreateListener);
                    break;
                case SLACK_CHANNEL_DELETED:
                    this.dispatchImpl((SlackChannelDeleted)event, SlackWebSocketSessionImpl.this.channelDeleteListener);
                    break;
                case SLACK_CHANNEL_RENAMED:
                    this.dispatchImpl((SlackChannelRenamed)event, SlackWebSocketSessionImpl.this.channelRenamedListener);
                    break;
                case SLACK_CHANNEL_UNARCHIVED:
                    this.dispatchImpl((SlackChannelUnarchived)event, SlackWebSocketSessionImpl.this.channelUnarchiveListener);
                    break;
                case SLACK_CHANNEL_JOINED:
                    this.dispatchImpl((SlackChannelJoined)event, SlackWebSocketSessionImpl.this.channelJoinedListener);
                    break;
                case SLACK_CHANNEL_LEFT:
                    this.dispatchImpl((SlackChannelLeft)event, SlackWebSocketSessionImpl.this.channelLeftListener);
                    break;
                case SLACK_GROUP_JOINED:
                    this.dispatchImpl((SlackGroupJoined)event, SlackWebSocketSessionImpl.this.groupJoinedListener);
                    break;
                case SLACK_MESSAGE_DELETED:
                    this.dispatchImpl((SlackMessageDeleted)event, SlackWebSocketSessionImpl.this.messageDeletedListener);
                    break;
                case SLACK_MESSAGE_POSTED:
                    this.dispatchImpl((SlackMessagePosted)event, SlackWebSocketSessionImpl.this.messagePostedListener);
                    break;
                case SLACK_MESSAGE_UPDATED:
                    this.dispatchImpl((SlackMessageUpdated)event, SlackWebSocketSessionImpl.this.messageUpdatedListener);
                    break;
                case SLACK_CONNECTED:
                    this.dispatchImpl((SlackConnected)event, SlackWebSocketSessionImpl.this.slackConnectedListener);
                    break;
                case REACTION_ADDED:
                    this.dispatchImpl((ReactionAdded)event, SlackWebSocketSessionImpl.this.reactionAddedListener);
                    break;
                case REACTION_REMOVED:
                    this.dispatchImpl((ReactionRemoved)event, SlackWebSocketSessionImpl.this.reactionRemovedListener);
                    break;
                case SLACK_USER_CHANGE:
                    this.dispatchImpl((SlackUserChange)event, SlackWebSocketSessionImpl.this.slackUserChangeListener);
                    break;
                case SLACK_TEAM_JOIN:
                    this.dispatchImpl((SlackTeamJoin)event, SlackWebSocketSessionImpl.this.slackTeamJoinListener);
                    break;
                case PIN_ADDED:
                    this.dispatchImpl((PinAdded)event, SlackWebSocketSessionImpl.this.pinAddedListener);
                    break;
                case PIN_REMOVED:
                    this.dispatchImpl((PinRemoved)event, SlackWebSocketSessionImpl.this.pinRemovedListener);
                    break;
                case PRESENCE_CHANGE:
                    this.dispatchImpl((PresenceChange)event, SlackWebSocketSessionImpl.this.presenceChangeListener);
                    break;
                case SLACK_DISCONNECTED:
                    this.dispatchImpl((SlackDisconnected)event, SlackWebSocketSessionImpl.this.slackDisconnectedListener);
                    break;
                case USER_TYPING:
                    this.dispatchImpl((UserTyping)event, SlackWebSocketSessionImpl.this.userTypingListener);
                    break;
                case SLACK_MEMBER_JOINED:
                    this.dispatchImpl((SlackMemberJoined)event, SlackWebSocketSessionImpl.this.slackMemberJoinedListener);
                    break;
                default:
                    if (event instanceof UnknownEvent) {
                        SlackWebSocketSessionImpl.LOGGER.debug("event of type " + event.getEventType() + " not handled: " + ((UnknownEvent)event).getJsonPayload());
                    } else {
                        SlackWebSocketSessionImpl.LOGGER.warn("event of type " + event.getEventType() + " not handled: ");
                    }
            }

        }

        private <E extends SlackEvent, L extends SlackEventListener<E>> void dispatchImpl(E event, List<L> listeners) {
            Iterator var3 = listeners.iterator();

            while(var3.hasNext()) {
                SlackEventListener listener = (SlackEventListener)var3.next();

                try {
                    listener.onEvent(event, SlackWebSocketSessionImpl.this);
                } catch (Throwable var6) {
                    SlackWebSocketSessionImpl.LOGGER.error("caught exception in dispatchImpl", var6);
                }
            }

        }
    }

    private interface REACTIONS {
        String ADD_COMMAND = "reactions.add";
        String REMOVE_COMMAND = "reactions.remove";
    }

    private interface USERS {
        String SET_PRESENCE_COMMAND = "users.setPresence";
        String GET_PRESENCE_COMMAND = "users.getPresence";
        String LIST_COMMAND = "users.list";
    }

    private interface CHAT {
        String POST_MESSAGE_COMMAND = "chat.postMessage";
        String POST_EPHEMERAL_COMMAND = "chat.postEphemeral";
        String DELETE_COMMAND = "chat.delete";
        String UPDATE_COMMAND = "chat.update";
    }

    private interface CONVERSATION {
        String OPEN_COMMAND = "conversations.open";
        String LEAVE_COMMAND = "conversations.leave";
        String JOIN_COMMAND = "conversations.join";
        String SET_TOPIC_COMMAND = "conversations.setTopic";
        String INVITE_COMMAND = "conversations.invite";
        String ARCHIVE_COMMAND = "conversations.archive";
        String UNARCHIVE_COMMAND = "conversations.unarchive";
    }
}
