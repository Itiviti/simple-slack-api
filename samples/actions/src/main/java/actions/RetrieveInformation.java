package actions;

import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackMessageHandle;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.replies.EmojiSlackReply;

import java.util.Map;

/**
 * Samples showing how to retrieve various bits of information from a slack group
 */
public class RetrieveInformation {

    /**
     * Demonstrates how to fetch glorious emoji.
     */
    public void fetchListOfEmoji(SlackSession session, SlackChannel slackChannel) {
        SlackMessageHandle<EmojiSlackReply> handle = session.listEmoji();
        Map<String, String> emojis = handle.getReply().getEmojis();

        for (String emojiName : emojis.keySet()) {
            String emojiUrl = emojis.get(emojiName);
            // you are now equipped to do glorious emoji things.
        }
    }

}
