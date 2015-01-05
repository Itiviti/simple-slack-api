package com.ullink.slack.simpleslackapi;

import java.util.ArrayList;
import java.util.List;

public class SlackAttachment
{
    public String           title;
    public String           fallback;
    public String           text;
    public String           pretext;

    public List<SlackField> fields;

    public List<String>     markdown_in;

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
}
