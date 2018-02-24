package com.ullink.slack.simpleslackapi.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SlackAction {
    public static final String TYPE_BUTTON = "button";

    private String name;
    private String text;
    private String style;
    private String type;
    private String value;
    private SlackConfirmation confirm;

    public SlackAction(String name, String text, String type, String value) {
        this.name = name;
        this.text = text;
        this.type = type;
        this.value = value;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SlackConfirmation {
        private String title;
        private String text;
        private String okText;
        private String dismissText;
    }
}
