package com.ullink.slack.simpleslackapi;

import com.ullink.slack.simpleslackapi.blocks.Actions;
import com.ullink.slack.simpleslackapi.blocks.BlockType;
import com.ullink.slack.simpleslackapi.blocks.Context;
import com.ullink.slack.simpleslackapi.blocks.Divider;
import com.ullink.slack.simpleslackapi.blocks.Image;
import com.ullink.slack.simpleslackapi.blocks.Section;
import com.ullink.slack.simpleslackapi.blocks.actions.Button;
import com.ullink.slack.simpleslackapi.blocks.actions.DatePicker;
import com.ullink.slack.simpleslackapi.blocks.compositions.ConfirmationDialog;
import com.ullink.slack.simpleslackapi.blocks.compositions.Markdown;
import com.ullink.slack.simpleslackapi.blocks.compositions.PlainText;
import org.junit.Test;

import java.io.IOException;

import static com.ullink.slack.simpleslackapi.TestUtils.getFile;
import static com.ullink.slack.simpleslackapi.TestUtils.gson;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class SlackPreparedMessageTest {

  @Test
  public void testJsonSerialize() throws IOException {
    SlackPreparedMessage message = SlackPreparedMessage.builder()
        .linkNames(true)
        .message("test message")
        .unfurl(true)
        .replyBroadcast(true)
        .threadTimestamp("123.456")
        .block(Context.builder().element(PlainText.builder().text("test context").build()).build())
        .block(Context.builder().element(Markdown.builder().text("test markdown context").build()).build())
        .block(Divider.builder().build())
        .block(Image.builder().title(PlainText.builder().text("gold image").build()).altText("alternative").imageUrl("https://picsum.photos/200/300").build())
        .block(Divider.builder().build())
        .block(Section.builder().field(PlainText.builder().text("test section plain").build()).accessory(Button.builder().text(PlainText.builder().text("GO").build()).style(Button.Style.DANGER.getLabel()).build()).build())
        .block(Section.builder().field(Markdown.builder().text("test section markdown").build()).build())
        .block(Actions.builder().element(DatePicker.builder().placeholder(PlainText.builder().text("datepicker").build()).confirm(ConfirmationDialog.builder().build()).build()).build())
        .attachment(SlackAttachment.builder().text("test attachment").action(new SlackAction("attachment action", "action", "test", "test value")).build())
        .build();
    String json = gson().toJson(message);
    assertNotNull(json);
    assertEquals(getFile("/message.json"), json);
  }

  @Test
  public void testJsonDeserialize() throws IOException {
    String json = getFile("/message.json");
    SlackPreparedMessage message = gson().fromJson(json, SlackPreparedMessage.class);
    assertEquals("test message", message.getMessage());
    assertEquals("123.456", message.getThreadTimestamp());
    assertTrue(message.isUnfurl());
    assertTrue(message.isLinkNames());
    assertTrue(message.isReplyBroadcast());
    assertNotNull(message.getBlocks());
    assertEquals(8, message.getBlocks().size());
    assertEquals(BlockType.CONTEXT.getType(), message.getBlocks().get(0).getType());
    assertEquals(1, message.getAttachments().size());
    assertEquals("test attachment", message.getAttachments().get(0).getText());
    assertEquals("attachment action", message.getAttachments().get(0).getActions().get(0).getName());


  }

}