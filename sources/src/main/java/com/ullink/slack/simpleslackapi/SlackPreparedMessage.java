package com.ullink.slack.simpleslackapi;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SlackPreparedMessage {
    private final String message;
    private final boolean unfurl;
    private final boolean linkNames;
    private final SlackAttachment[] attachments;
    private final String threadTimestamp;
    private final boolean replyBroadcast;

    private SlackPreparedMessage(String message, boolean unfurl, boolean linkNames, SlackAttachment[] attachments, String threadTimestamp, boolean replyBroadcast) {
        this.message = message;
        this.unfurl = unfurl;
        this.linkNames = linkNames;
        this.attachments = attachments;
        this.threadTimestamp = threadTimestamp;
        this.replyBroadcast = replyBroadcast;
    }

    public String getMessage() {
        return message;
    }

    public boolean isUnfurl() {
        return unfurl;
    }

    public boolean isLinkNames() {
        return linkNames;
    }

    public SlackAttachment[] getAttachments() {
        return attachments;
    }

    public String getThreadTimestamp() {
        return threadTimestamp;
    }

    public boolean isReplyBroadcast() {
        return replyBroadcast;
    }

    public static class Builder {
        String message;
        boolean unfurl;
        boolean linkNames;
        List<SlackAttachment> attachments;
        String threadTimestamp;
        boolean replyBroadcast;

        public Builder() {
            this.attachments = new ArrayList<>();
        }

        public Builder withMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder withUnfurl(boolean unfurl) {
            this.unfurl = unfurl;
            return this;
        }

        public Builder withLinkNames(boolean linkNames) {
            this.linkNames = linkNames;
            return this;
        }

        public Builder addAttachment(SlackAttachment attachment) {
            if (attachment != null)
            {
                this.attachments.add(attachment);
            }
            return this;
        }

        public Builder addAttachments(List<SlackAttachment> attachments) {
            if (attachments != null)
            {
                this.attachments.addAll(attachments);
            }
            return this;
        }

        public Builder withAttachments(List<SlackAttachment> attachments) {
            this.attachments = attachments;
            return this;
        }

        public Builder withThreadTimestamp(String threadTimestamp) {
            this.threadTimestamp = threadTimestamp;
            return this;
        }

        public Builder withReplyBroadcast(boolean replyBroadcast) {
            this.replyBroadcast = replyBroadcast;
            return this;
        }

        public SlackPreparedMessage build() {
            return new SlackPreparedMessage(
                    message,
                    unfurl,
                    linkNames,
                    attachments.toArray(new SlackAttachment[]{}),
                    threadTimestamp,
                    replyBroadcast);
        }
    }

    @Override
    public String toString() {
        return "SlackPreparedMessage{" +
                "message='" + message + '\'' +
                ", unfurl=" + unfurl +
                ", attachments=" + Arrays.toString(attachments) +
                '}';
    }
}
