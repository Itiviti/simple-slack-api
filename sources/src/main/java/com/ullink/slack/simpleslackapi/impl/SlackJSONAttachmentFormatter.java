package com.ullink.slack.simpleslackapi.impl;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import com.ullink.slack.simpleslackapi.SlackAttachment;
import com.ullink.slack.simpleslackapi.SlackField;

class SlackJSONAttachmentFormatter
{
    public static List<JSONObject> encodeAttachments(SlackAttachment... attachments)
    {
        List<JSONObject> toReturn = new ArrayList<>();
        for (SlackAttachment attachment : attachments) {
            JSONObject attachmentJSON = new JSONObject();
            toReturn.add(attachmentJSON);
            if (attachment.title != null) {
                attachmentJSON.put("title", attachment.title);
            }
            if (attachment.thumb_url != null) {
                attachmentJSON.put("thumb_url", attachment.thumb_url);
            }
            if (attachment.titleLink != null) {
                attachmentJSON.put("title_link", attachment.titleLink);
            }
            if (attachment.text != null) {
                attachmentJSON.put("text", attachment.text);
            }
            if (attachment.color != null) {
                attachmentJSON.put("color", attachment.color);
            }
            if (attachment.pretext != null) {
                attachmentJSON.put("pretext", attachment.pretext);
            }
            if (attachment.fallback != null) {
                attachmentJSON.put("fallback", attachment.fallback);
            }
            if (attachment.miscRootFields != null) {
                for (Map.Entry<String, String> entry : attachment.miscRootFields.entrySet()) {
                    attachmentJSON.put(entry.getKey(), entry.getValue());
                }
            }
            if (attachment.markdown_in != null && !attachment.markdown_in.isEmpty()) {
                JSONArray array = new JSONArray();
                array.addAll(attachment.markdown_in);
                attachmentJSON.put("mrkdwn_in", array);
            }
            if (attachment.fields != null && !attachment.fields.isEmpty()) {
                attachmentJSON.put("fields", encodeAttachmentFields(attachment.fields));
            }

        }
        return toReturn;
    }

    private static List<JSONObject> encodeAttachmentFields(List<SlackField> fields)
    {
        List<JSONObject> toReturn = new ArrayList<>();
        for (SlackField field : fields)
        {
            JSONObject fieldJSON = new JSONObject();
            toReturn.add(fieldJSON);
            if (field.title != null)
            {
                fieldJSON.put("title", field.title);
            }
            if (field.value != null)
            {
                fieldJSON.put("value", field.value);
            }
            fieldJSON.put("short", field.isShort);
        }
        return toReturn;
    }

    public static void main(String[] args) throws UnsupportedEncodingException
    {
        System.out.println(new String("Loïc Herve".getBytes(), "UTF-8"));
    }
}
