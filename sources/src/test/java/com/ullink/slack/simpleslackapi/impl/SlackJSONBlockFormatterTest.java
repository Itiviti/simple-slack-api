package com.ullink.slack.simpleslackapi.impl;

import com.ullink.slack.simpleslackapi.SlackPreparedMessage;
import com.ullink.slack.simpleslackapi.blocks.Actions;
import com.ullink.slack.simpleslackapi.blocks.Block;
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
import java.util.List;

import static com.ullink.slack.simpleslackapi.TestUtils.getFile;
import static org.junit.Assert.*;

public class SlackJSONBlockFormatterTest {

  @Test
  public void encodeBlocks() throws IOException {
    SlackPreparedMessage message = SlackPreparedMessage.builder()
        .block(Context.builder().element(PlainText.builder().text("test context").build()).build())
        .block(Context.builder().element(Markdown.builder().text("test markdown context").build()).build())
        .block(Divider.builder().build())
        .block(Image.builder().title(PlainText.builder().text("gold image").build()).altText("alternative").imageUrl("https://picsum.photos/200/300").build())
        .block(Divider.builder().build())
        .block(Section.builder().field(PlainText.builder().text("test section plain").build()).accessory(Button.builder().text(PlainText.builder().text("GO").build()).style(Button.Style.DANGER.getLabel()).build()).build())
        .block(Section.builder().field(Markdown.builder().text("test section markdown").build()).build())
        .block(Actions.builder().element(DatePicker.builder().placeholder(PlainText.builder().text("datepicker").build()).confirm(ConfirmationDialog.builder().build()).build()).build())
        .build();
    List<Block> blocks = message.getBlocks();
    String json = SlackJSONBlockFormatter.encodeBlocks(blocks);
    assertEquals(getFile("/blocks.json"), json);

  }
}