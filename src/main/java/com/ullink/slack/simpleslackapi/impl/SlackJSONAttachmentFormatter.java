package com.ullink.slack.simpleslackapi.impl;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import com.ullink.slack.simpleslackapi.SlackAttachment;
import com.ullink.slack.simpleslackapi.SlackField;

class SlackJSONAttachmentFormatter
{
    public static List<JSONObject> encodeAttachments(SlackAttachment... attachments)
    {
        List<JSONObject> toReturn = new ArrayList<JSONObject>();
        for (int i = 0; i < attachments.length; i++)
        {
            JSONObject attachmentJSON = new JSONObject();
            toReturn.add(attachmentJSON);
            if (attachments[i].title != null)
            {
                attachmentJSON.put("title", attachments[i].title);
            }
            if (attachments[i].text != null)
            {
                attachmentJSON.put("text", attachments[i].text);
            }
            if (attachments[i].pretext != null)
            {
                attachmentJSON.put("pretext", attachments[i].pretext);
            }
            if (attachments[i].fallback != null)
            {
                attachmentJSON.put("fallback", attachments[i].fallback);
            }
            if (attachments[i].markdown_in != null && !attachments[i].markdown_in.isEmpty())
            {
                JSONArray array = new JSONArray();
                array.addAll(attachments[i].markdown_in);
                attachmentJSON.put("mrkdwn_in", array);
            }
            if (attachments[i].fields != null && !attachments[i].fields.isEmpty())
            {
                attachmentJSON.put("fields", encodeAttachmentFields(attachments[i].fields));
            }

        }
        return toReturn;
    }

    private static List<JSONObject> encodeAttachmentFields(List<SlackField> fields)
    {
        List<JSONObject> toReturn = new ArrayList<JSONObject>();
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
