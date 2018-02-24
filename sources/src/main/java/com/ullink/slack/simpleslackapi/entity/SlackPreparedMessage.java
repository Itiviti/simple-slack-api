package com.ullink.slack.simpleslackapi.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SlackPreparedMessage {
    private String message;
    private boolean unfurl;
    private boolean linkNames;
    private List<SlackAttachment> attachments;
    private String threadTimestamp;
    private boolean replyBroadcast;
}
