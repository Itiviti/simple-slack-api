package com.ullink.slack.simpleslackapi;

import lombok.Data;

@Data
public class SlackTeam {
    private final String id;
    private final String name;
    private final String domain;
}
