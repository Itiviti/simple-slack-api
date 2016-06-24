package com.ullink.slack.simpleslackapi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SlackAttachment {

    private String              title;
    private String              titleLink;
    private String              fallback;
    private String              text;
    private String              pretext;
    private String              thumb_url;

    private String              color;

    private Map<String, String> miscRootFields;

    private List<SlackField>    fields = new ArrayList<>();

    private List<String>        markdown_in;

    public SlackAttachment() {

    }

    public SlackAttachment(String title, String fallback, String text, String pretext) {
        this.title = title;
        this.fallback = fallback;
        this.text = text;
        this.pretext = pretext;
    }

    @Override
    public String toString() {
        return "SlackAttachment [title=" + title + ", fallback=" + fallback + ", text=" + text + ", pretext=" + pretext + ", fields=" + fields + "]";
    }

    public void addField(String title, String value, boolean isShort) {
        fields.add(new SlackField(title, value, isShort));
    }

    public void addMarkdownIn(String value) {
        if (markdown_in == null) {
            markdown_in = new ArrayList<>();
        }
        markdown_in.add(value);
    }

    public void addMiscField(String key, String value) {
        if (miscRootFields == null) {
            miscRootFields = new HashMap<>();
        }
        miscRootFields.put(key, value);
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public void setTitleLink(String titleLink)
    {
        this.titleLink = titleLink;
    }

    public void setFallback(String fallback)
    {
        this.fallback = fallback;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public void setPretext(String pretext)
    {
        this.pretext = pretext;
    }

    public void setColor(String color)
    {
        this.color = color;
    }

    public String getTitle() {
        return title;
    }

    public String getTitleLink() {
        return titleLink;
    }

    public String getFallback() {
        return fallback;
    }

    public String getText() {
        return text;
    }

    public String getPretext() {
        return pretext;
    }

    public String getThumbUrl() {
        return thumb_url;
    }

    public String getColor() {
        return color;
    }

    public Map<String, String> getMiscRootFields() {
        return miscRootFields;
    }

    public List<SlackField> getFields() {
        return fields;
    }

    public List<String> getMarkdown_in() {
        return markdown_in;
    }
}
