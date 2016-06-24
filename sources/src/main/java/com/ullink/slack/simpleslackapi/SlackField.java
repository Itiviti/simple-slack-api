package com.ullink.slack.simpleslackapi;

public class SlackField {
    private String  title;
    private String  value;
    private boolean isShort;

    public SlackField() {

    }

    public SlackField(String title, String value, boolean isShort) {
        this.title = title;
        this.value = value;
        this.isShort = isShort;
    }

    public String getTitle() {
        return title;
    }

    public String getValue() {
        return value;
    }

    public boolean isShort() {
        return isShort;
    }
}
