package com.ullink.slack.simpleslackapi;

public class SlackAction {
    public static final String TYPE_BUTTON = "button";

    private String name;
    private String text;
    private String style;
    private String type;
    private String value;
    private SlackConfirmation confirm;

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

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public SlackConfirmation getConfirm() {
        return confirm;
    }

    public void setConfirm(SlackConfirmation confirm) {
        this.confirm = confirm;
    }


    public static class SlackConfirmation {
        private String title;
        private String text;
        private String okText;
        private String dismissText;

        public SlackConfirmation(String title, String text, String okText, String dismissText) {
            this.title = title;
            this.text = text;
            this.okText = okText;
            this.dismissText = dismissText;
        }

        public SlackConfirmation() {

        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getOkText() {
            return okText;
        }

        public void setOkText(String okText) {
            this.okText = okText;
        }

        public String getDismissText() {
            return dismissText;
        }

        public void setDismissText(String dismissText) {
            this.dismissText = dismissText;
        }
    }
}
