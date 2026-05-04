/**
 *
 *  @author Piwnik Łukasz s31827
 *
 */

package zad1;

import java.util.List;
import java.util.concurrent.FutureTask;

public class ChatClientTask extends FutureTask<ChatClient> {
    private final ChatClient client;

    private ChatClientTask(ChatClient client, List<String> msgs, int wait) {
        super(() -> {
            client.login();
            if (wait > 0) Thread.sleep(wait);

            for (String msg : msgs) {
                client.send(client.getId()+": "+msg);
                if (wait > 0) Thread.sleep(wait);
            }

            client.logout();
            return client;
        });
        this.client = client;
    }

    public static ChatClientTask create(ChatClient c, List<String> msgs, int wait) {
        return new ChatClientTask(c, msgs, wait);
    }

    public ChatClient getClient() {
        return client;
    }

}