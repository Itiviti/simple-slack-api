package com.ullink.slack.simpleslackapi.impl;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import com.ullink.slack.simpleslackapi.SlackAttachment;
import com.ullink.slack.simpleslackapi.SlackField;

class SlackJSONAttachmentFormatter {
    public static List<JSONObject> encodeAttachments(SlackAttachment... attachments) {
        List<JSONObject> toReturn = new ArrayList<>();
        for (SlackAttachment attachment : attachments) {
            JSONObject attachmentJSON = new JSONObject();
            toReturn.add(attachmentJSON);
            if (attachment.getTitle() != null) {
                attachmentJSON.put("title", attachment.getTitle());
            }
            if (attachment.getThumbUrl() != null) {
                attachmentJSON.put("thumb_url", attachment.getThumbUrl());
            }
            if (attachment.getTitleLink() != null) {
                attachmentJSON.put("title_link", attachment.getTitleLink());
            }
            if (attachment.getText() != null) {
                attachmentJSON.put("text", attachment.getText());
            }
            if (attachment.getColor() != null) {
                attachmentJSON.put("color", attachment.getColor());
            }
            if (attachment.getPretext() != null) {
                attachmentJSON.put("pretext", attachment.getPretext());
            }
            if (attachment.getFallback() != null) {
                attachmentJSON.put("fallback", attachment.getFallback());
            }
            if (attachment.getAuthorName() != null) {
                attachmentJSON.put("author_name", attachment.getAuthorName());
            }
            if (attachment.getAuthorLink() != null) {
                attachmentJSON.put("author_link", attachment.getAuthorLink());
            }
            if (attachment.getAuthorIcon() != null) {
                attachmentJSON.put("author_icon", attachment.getAuthorIcon());
            }
            if (attachment.getImageUrl() != null) {
                attachmentJSON.put("image_url", attachment.getImageUrl());
            }
            if (attachment.getFooter() != null) {
                attachmentJSON.put("footer", attachment.getFooter());
            }
            if (attachment.getFooterIcon() != null) {
                attachmentJSON.put("footer_icon", attachment.getFooterIcon());
            }
            if (attachment.getMiscRootFields() != null) {
                for (Map.Entry<String, String> entry : attachment.getMiscRootFields().entrySet()) {
                    attachmentJSON.put(entry.getKey(), entry.getValue());
                }
            }
            if (attachment.getMarkdown_in() != null && !attachment.getMarkdown_in().isEmpty()) {
                JSONArray array = new JSONArray();
                array.addAll(attachment.getMarkdown_in());
                attachmentJSON.put("mrkdwn_in", array);
            }
            if (attachment.getFields() != null && !attachment.getFields().isEmpty()) {
                attachmentJSON.put("fields", encodeAttachmentFields(attachment.getFields()));
            }

        }
        return toReturn;
    }

    private static List<JSONObject> encodeAttachmentFields(List<SlackField> fields) {
        List<JSONObject> toReturn = new ArrayList<>();
        for (SlackField field : fields)
        {
            JSONObject fieldJSON = new JSONObject();
            toReturn.add(fieldJSON);
            if (field.getTitle() != null)
            {
                fieldJSON.put("title", field.getTitle());
            }
            if (field.getValue() != null)
            {
                fieldJSON.put("value", field.getValue());
            }
            fieldJSON.put("short", field.isShort());
        }
        return toReturn;
    }
}
