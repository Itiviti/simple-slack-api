package com.ullink.slack.simpleslackapi.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SlackAttachment {
    private String title;
    private String titleLink;
    private String fallback;
    private String callbackId;
    private String text;
    private String pretext;
    private String thumbUrl;
    private String authorName;
    private String authorLink;
    private String authorIcon;
    private String footer;
    private String footerIcon;
    private String imageUrl;
    private String color;
    private Long timestamp;

    private Map<String, String> miscRootFields;
    private List<SlackField> fields = new ArrayList<>();
    private List<SlackAction> actions = new ArrayList<>();
    private List<String> markdownIn;

    public SlackAttachment(String title, String fallback, String text, String pretext) {
        this.title = title;
        this.fallback = fallback;
        this.text = text;
        this.pretext = pretext;
    }

    public void addField(String title, String value, boolean isShort) {
        fields.add(new SlackField(title, value, isShort));
    }

    public void addAction(String name, String value, String text, String type) {
        actions.add(new SlackAction(name, text, type, value));
    }

    public void addAction(SlackAction action) {
        actions.add(action);
    }

    public void addMarkdownIn(String value) {
        if (markdownIn == null) {
            markdownIn = new ArrayList<>();
        }
        markdownIn.add(value);
    }

    public void addMiscField(String key, String value) {
        if (miscRootFields == null) {
            miscRootFields = new HashMap<>();
        }
        miscRootFields.put(key, value);
    }
}
