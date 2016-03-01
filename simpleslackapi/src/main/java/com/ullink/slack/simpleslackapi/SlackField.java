package com.ullink.slack.simpleslackapi;

public class SlackField
{
    public String  title;
    public String  value;
    public boolean isShort;

    public SlackField()
    {
    }

    public SlackField(String title, String value, boolean isShort)
    {
        this.title = title;
        this.value = value;
        this.isShort = isShort;
    }

}
