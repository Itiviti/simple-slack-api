package com.ullink.slack.simpleslackapi;

public class SlackAction {
    public static final String TYPE_BUTTON = "button";

    private String name;
    private String text;
    private String type;
    private String value;

    public SlackAction() {
    }

    public SlackAction(String name, String text, String type, String value) {
        this.name = name;
        this.text = text;
        this.type = type;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
