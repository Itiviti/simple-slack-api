package com.ullink.slack.simpleslackapi.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ullink.slack.simpleslackapi.SlackAction;
import com.ullink.slack.simpleslackapi.SlackAttachment;
import com.ullink.slack.simpleslackapi.SlackField;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

class SlackJSONAttachmentFormatter
{
    public static List<JsonObject> encodeAttachments(SlackAttachment... attachments)
    {
        return encodeAttachments(Arrays.asList(attachments));
    }
    public static List<JsonObject> encodeAttachments(List<SlackAttachment> attachments)
    {
        List<JsonObject> toReturn = new ArrayList<>();
        for (SlackAttachment attachment : attachments)
        {
            JsonObject attachmentJSON = new JsonObject();
            toReturn.add(attachmentJSON);
            if (attachment.getTitle() != null)
            {
                attachmentJSON.addProperty("title", attachment.getTitle());
            }
            if (attachment.getThumbUrl() != null)
            {
                attachmentJSON.addProperty("thumb_url", attachment.getThumbUrl());
            }
            if (attachment.getTitleLink() != null)
            {
                attachmentJSON.addProperty("title_link", attachment.getTitleLink());
            }
            if (attachment.getText() != null)
            {
                attachmentJSON.addProperty("text", attachment.getText());
            }
            if (attachment.getColor() != null)
            {
                attachmentJSON.addProperty("color", attachment.getColor());
            }
            if (attachment.getPretext() != null)
            {
                attachmentJSON.addProperty("pretext", attachment.getPretext());
            }
            if (attachment.getFallback() != null)
            {
                attachmentJSON.addProperty("fallback", attachment.getFallback());
            }
            if (attachment.getCallbackId() != null)
            {
                attachmentJSON.addProperty("callback_id", attachment.getCallbackId());
            }
            if (attachment.getAuthorName() != null)
            {
                attachmentJSON.addProperty("author_name", attachment.getAuthorName());
            }
            if (attachment.getAuthorLink() != null)
            {
                attachmentJSON.addProperty("author_link", attachment.getAuthorLink());
            }
            if (attachment.getAuthorIcon() != null)
            {
                attachmentJSON.addProperty("author_icon", attachment.getAuthorIcon());
            }
            if (attachment.getImageUrl() != null)
            {
                attachmentJSON.addProperty("image_url", attachment.getImageUrl());
            }
            if (attachment.getFooter() != null)
            {
                attachmentJSON.addProperty("footer", attachment.getFooter());
            }
            if (attachment.getFooterIcon() != null)
            {
                attachmentJSON.addProperty("footer_icon", attachment.getFooterIcon());
            }
            if (attachment.getTimestamp() != null)
            {
                attachmentJSON.addProperty("ts", attachment.getTimestamp());
            }
            if (attachment.getMiscRootFields() != null)
            {
                for (Map.Entry<String, String> entry : attachment.getMiscRootFields().entrySet())
                {
                    attachmentJSON.addProperty(entry.getKey(), entry.getValue());
                }
            }
            if (attachment.getMarkdown_in() != null && !attachment.getMarkdown_in().isEmpty())
            {
                JsonArray array = new JsonArray();
                for (String markdown : attachment.getMarkdown_in())
                {
                    array.add(markdown);
                }
                attachmentJSON.add("mrkdwn_in", array);
            }
            if (attachment.getFields() != null && !attachment.getFields().isEmpty())
            {
                attachmentJSON.add("fields", encodeAttachmentFields(attachment.getFields()));
            }
            if (attachment.getActions() != null && !attachment.getActions().isEmpty())
            {
                attachmentJSON.add("actions", encodeAttachmentActions(attachment.getActions()));
            }
        }
        return toReturn;
    }

    private static JsonArray encodeAttachmentFields(List<SlackField> fields)
    {
        JsonArray toReturn = new JsonArray();
        for (SlackField field : fields)
        {
            JsonObject fieldJSON = new JsonObject();
            if (field.getTitle() != null)
            {
                fieldJSON.addProperty("title", field.getTitle());
            }
            if (field.getValue() != null)
            {
                fieldJSON.addProperty("value", field.getValue());
            }
            fieldJSON.addProperty("short", field.isShort());
            toReturn.add(fieldJSON);
        }
        return toReturn;
    }

    private static JsonArray encodeAttachmentActions(List<SlackAction> actions) {
        JsonArray toReturn = new JsonArray();
        for (SlackAction action : actions) {
            JsonObject actionJSON = new JsonObject();
            toReturn.add(actionJSON);
            if (action.getName() != null)
            {
                actionJSON.addProperty("name", action.getName());
            }
            if (action.getText() != null)
            {
                actionJSON.addProperty("text", action.getText());
            }
            if (action.getType() != null)
            {
                actionJSON.addProperty("type", action.getType());
            }
            if (action.getValue() != null)
            {
                actionJSON.addProperty("value", action.getValue());
            }
            if (action.getStyle() != null)
            {
                actionJSON.addProperty("style", action.getStyle());
            }
            if (action.getConfirm() != null)
            {
                actionJSON.add("confirm", encodeAttachmentActionsConfirmation(action.getConfirm()));
            }
        }
        return toReturn;
    }

    private static JsonObject encodeAttachmentActionsConfirmation(SlackAction.SlackConfirmation confirmation) {
        JsonObject toReturn = new JsonObject();
        if (confirmation.getTitle() != null)
        {
            toReturn.addProperty("title", confirmation.getTitle());
        }
        if(confirmation.getText() != null)
        {
            toReturn.addProperty("text", confirmation.getText());
        }
        if(confirmation.getOkText() != null)
        {
            toReturn.addProperty("ok_text", confirmation.getOkText());
        }
        if(confirmation.getDismissText() != null)
        {
            toReturn.addProperty("dismiss_text", confirmation.getDismissText());
        }
        return toReturn;
    }
}
