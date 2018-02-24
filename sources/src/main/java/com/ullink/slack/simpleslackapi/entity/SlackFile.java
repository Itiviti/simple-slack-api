package com.ullink.slack.simpleslackapi.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SlackFile {
    private String id;
    private String name;
    private String title;
    private String mimetype;
    private String filetype;
    @Deprecated
    private String url;
    @Deprecated
    private String urlDownload;
    private String urlPrivate;
    private String urlPrivateDownload;
    private String thumb64;
    private String thumb80;
    private String thumb160;
    private String thumb360;
    private String thumb480;
    private String thumb720;
    private Long imageExifRotation;
    private Long originalW;
    private Long originalH;
    private String permalink;
    private String permalinkPublic;
    private String comment;
}
