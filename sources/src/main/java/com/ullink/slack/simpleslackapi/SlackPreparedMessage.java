package com.ullink.slack.simpleslackapi;


import com.google.gson.annotations.SerializedName;
import com.ullink.slack.simpleslackapi.blocks.Block;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Singular;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Builder
@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class SlackPreparedMessage {

    @SerializedName("text")
    private String message;
    private boolean unfurl;
    private boolean linkNames;
    @Singular
    private List<SlackAttachment> attachments;
    @Singular
    private List<Block> blocks;
    private String threadTimestamp;
    private boolean replyBroadcast;

    @Deprecated
    private SlackPreparedMessage(String message, boolean unfurl, boolean linkNames, SlackAttachment[] attachments, String threadTimestamp, boolean replyBroadcast) {
        this.message = message;
        this.unfurl = unfurl;
        this.linkNames = linkNames;
        this.attachments = Arrays.asList(attachments);
        this.threadTimestamp = threadTimestamp;
        this.replyBroadcast = replyBroadcast;
        this.blocks = new ArrayList<>();
    }

    public SlackPreparedMessage(){
        this.attachments = new ArrayList<>();
        this.blocks = new ArrayList<>();
    }

    @Deprecated
    public SlackAttachment[] getAttachments() {
        return attachments.toArray(new SlackAttachment[]{});
    }

    @Override
    public String toString() {
        return "SlackPreparedMessage{" +
                "message='" + message + '\'' +
                ", unfurl=" + unfurl +
                ", attachments=" + attachments +
                ", blocks=" + blocks +
                '}';
    }

    @Deprecated
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

}
