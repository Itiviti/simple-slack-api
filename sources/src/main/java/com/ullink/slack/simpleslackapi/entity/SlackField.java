package com.ullink.slack.simpleslackapi.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SlackField {
    private String title;
    private String value;
    private boolean isShort;
}
