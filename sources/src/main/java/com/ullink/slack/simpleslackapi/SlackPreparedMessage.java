package com.ullink.slack.simpleslackapi;


import com.google.gson.annotations.SerializedName;
import com.ullink.slack.simpleslackapi.blocks.Block;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Singular;

import java.util.List;

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

    @Override
    public String toString() {
        return "SlackPreparedMessage{" +
                "message='" + message + '\'' +
                ", unfurl=" + unfurl +
                ", attachments=" + attachments +
                ", blocks=" + blocks +
                '}';
    }

}
