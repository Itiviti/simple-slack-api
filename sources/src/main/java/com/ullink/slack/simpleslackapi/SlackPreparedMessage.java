package com.ullink.slack.simpleslackapi;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class SlackPreparedMessage {

    private String message;
    private boolean unfurl;
    private boolean linkNames;
    private List<SlackAttachment> attachments;
    private String threadTimestamp;
    private boolean replyBroadcast;


    private SlackPreparedMessage(String message, boolean unfurl, boolean linkNames, SlackAttachment[] attachments, String threadTimestamp, boolean replyBroadcast) {
        this.message = message;
        this.unfurl = unfurl;
        this.linkNames = linkNames;
        this.attachments = Arrays.asList(attachments);
        this.threadTimestamp = threadTimestamp;
        this.replyBroadcast = replyBroadcast;
    }

    public SlackPreparedMessage(){

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
        return attachments.toArray(new SlackAttachment[]{});
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
                ", attachments=" + attachments +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SlackPreparedMessage that = (SlackPreparedMessage) o;
        return isUnfurl() == that.isUnfurl() &&
                isLinkNames() == that.isLinkNames() &&
                isReplyBroadcast() == that.isReplyBroadcast() &&
                Objects.equals(getMessage(), that.getMessage()) &&
                Arrays.equals(getAttachments(), that.getAttachments()) &&
                Objects.equals(getThreadTimestamp(), that.getThreadTimestamp());
    }

    @Override
    public int hashCode() {

        int result = Objects.hash(getMessage(), isUnfurl(), isLinkNames(), getThreadTimestamp(), isReplyBroadcast());
        result = 31 * result + Arrays.hashCode(getAttachments());
        return result;
    }
}
