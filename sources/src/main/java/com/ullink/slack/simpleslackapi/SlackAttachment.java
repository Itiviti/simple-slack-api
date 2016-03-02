package com.ullink.slack.simpleslackapi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SlackAttachment
{
    public String              title;
    public String              titleLink;
    public String              fallback;
    public String              text;
    public String              pretext;
    public String              thumb_url;

    public String              color;

    public Map<String, String> miscRootFields;

    public List<SlackField>    fields;

    public List<String>        markdown_in;

    public SlackAttachment()
    {
    }

    public SlackAttachment(String title, String fallback, String text, String pretext)
    {
        this.title = title;
        this.fallback = fallback;
        this.text = text;
        this.pretext = pretext;
    }

    @Override
    public String toString()
    {
        return "SlackAttachment [title=" + title + ", fallback=" + fallback + ", text=" + text + ", pretext=" + pretext + ", fields=" + fields + "]";
    }

    public void addField(String title, String value, boolean isShort)
    {
        if (fields == null)
        {
            fields = new ArrayList<>();
        }
        fields.add(new SlackField(title, value, isShort));
    }

    public void addMarkdownIn(String value)
    {
        if (markdown_in == null)
        {
            markdown_in = new ArrayList<>();
        }
        markdown_in.add(value);
    }

    public void addMiscField(String key, String value)
    {
        if (miscRootFields == null)
        {
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
}
