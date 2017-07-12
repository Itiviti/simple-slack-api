package com.ullink.slack.simpleslackapi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SlackAttachment {

    private String              title;
    private String              titleLink;
    private String              fallback;
    private String              callback_id;
    private String              text;
    private String              pretext;
    private String              thumb_url;
    private String              author_name;
    private String              author_link;
    private String              author_icon;
    private String              footer;
    private String              footer_icon;
    private String              image_url;

    private String              color;
    private Long                timestamp;

    private Map<String, String> miscRootFields;

    private List<SlackField>    fields = new ArrayList<>();

    private List<SlackAction>   actions = new ArrayList<>();

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

    public void addAction(String name, String value, String text, String type) {
        actions.add(new SlackAction(name, text, type, value));
    }

    public void addAction(SlackAction action) {
        actions.add(action);
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

    public void setCallbackId(String callback_id) {
        this.callback_id = callback_id;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public void setPretext(String pretext)
    {
        this.pretext = pretext;
    }

    public void setThumbUrl(String thumb_url) { this.thumb_url = thumb_url; }

    public void setColor(String color)
    {
        this.color = color;
    }

    public void setImageUrl(String image_url) { this.image_url = image_url; }

    public void setAuthorName(String author_name) { this.author_name = author_name; }

    public void setAuthorLink(String author_link) { this.author_link = author_link; }

    public void setAuthorIcon(String author_icon) { this.author_icon = author_icon; }

    public void setFooter(String footer) { this.footer = footer; }

    public void setFooterIcon(String footer_icon) { this.footer_icon = footer_icon; }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
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

    public String getCallbackId() {
        return callback_id;
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

    public List<SlackAction> getActions() {
        return actions;
    }

    public List<String> getMarkdown_in() {
        return markdown_in;
    }

    public String getImageUrl() { return image_url; }

    public String getAuthorName() { return author_name; }

    public String getAuthorLink() { return author_link; }

    public String getAuthorIcon() { return author_icon; }

    public String getFooter() { return footer; }

    public String getFooterIcon() { return footer_icon; }

    public Long getTimestamp() {
        return timestamp;
    }
}
