package com.ullink.slack.simpleslackapi.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ullink.slack.simpleslackapi.SlackAction;
import com.ullink.slack.simpleslackapi.SlackAttachment;
import com.ullink.slack.simpleslackapi.SlackField;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class SlackJSONAttachmentFormatter
{
    public static List<JsonObject> encodeAttachments(SlackAttachment... attachments)
    {
        List<JsonObject> toReturn = new ArrayList<>();
        for (int i = 0; i < attachments.length; i++)
        {
            JsonObject attachmentJSON = new JsonObject();
            toReturn.add(attachmentJSON);
            if (attachments[i].getTitle() != null)
            {
                attachmentJSON.addProperty("title", attachments[i].getTitle());
            }
            if (attachments[i].getThumbUrl() != null)
            {
                attachmentJSON.addProperty("thumb_url", attachments[i].getThumbUrl());
            }
            if (attachments[i].getTitleLink() != null)
            {
                attachmentJSON.addProperty("title_link", attachments[i].getTitleLink());
            }
            if (attachments[i].getText() != null)
            {
                attachmentJSON.addProperty("text", attachments[i].getText());
            }
            if (attachments[i].getColor() != null)
            {
                attachmentJSON.addProperty("color", attachments[i].getColor());
            }
            if (attachments[i].getPretext() != null)
            {
                attachmentJSON.addProperty("pretext", attachments[i].getPretext());
            }
            if (attachments[i].getFallback() != null)
            {
                attachmentJSON.addProperty("fallback", attachments[i].getFallback());
            }
            if (attachments[i].getCallbackId() != null) {
                attachmentJSON.addProperty("callback_id", attachments[i].getCallbackId());
            }
            if (attachments[i].getAuthorName() != null) {
                attachmentJSON.addProperty("author_name", attachments[i].getAuthorName());
            }
            if (attachments[i].getAuthorLink() != null) {
                attachmentJSON.addProperty("author_link", attachments[i].getAuthorLink());
            }
            if (attachments[i].getAuthorIcon() != null) {
                attachmentJSON.addProperty("author_icon", attachments[i].getAuthorIcon());
            }
            if (attachments[i].getImageUrl() != null) {
                attachmentJSON.addProperty("image_url", attachments[i].getImageUrl());
            }
            if (attachments[i].getFooter() != null) {
                attachmentJSON.addProperty("footer", attachments[i].getFooter());
            }
            if (attachments[i].getFooterIcon() != null) {
                attachmentJSON.addProperty("footer_icon", attachments[i].getFooterIcon());
            }
            if (attachments[i].getTimestamp() != null) {
                attachmentJSON.addProperty("ts", attachments[i].getTimestamp());
            }
            if (attachments[i].getMiscRootFields() != null)
            {
                for (Map.Entry<String, String> entry : attachments[i].getMiscRootFields().entrySet())
                {
                    attachmentJSON.addProperty(entry.getKey(), entry.getValue());
                }
            }
            if (attachments[i].getMarkdown_in() != null && !attachments[i].getMarkdown_in().isEmpty())
            {
                JsonArray array = new JsonArray();
                for (String markdown : attachments[i].getMarkdown_in()) {
                    array.add(markdown);
                }
                attachmentJSON.add("mrkdwn_in", array);
            }
            if (attachments[i].getFields() != null && !attachments[i].getFields().isEmpty())
            {
                attachmentJSON.add("fields", encodeAttachmentFields(attachments[i].getFields()));
            }
            if (attachments[i].getActions() != null && !attachments[i].getActions().isEmpty())
            {
                attachmentJSON.add("actions", encodeAttachmentActions(attachments[i].getActions()));
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
