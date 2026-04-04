/**
 *
 *  @author Piwnik Łukasz s31827
 *
 */

package zad1;


import java.util.List;
import java.util.concurrent.ExecutionException;

public class ChatClientTask implements Runnable {
    private final ChatClient client;
    private final List<String> messages;
    private final int wait;
    private boolean isFinished = false;
    private ChatClientTask(ChatClient client, List<String> messages, int wait){
        this.client=client;
        this.messages=messages;
        this.wait= wait;
    }
    public static ChatClientTask create(ChatClient client, List<String> messages, int wait){
        return new ChatClientTask(client,messages,wait);
    }

    @Override
    public void run() {
        try {
            client.login();
            if (wait > 0) Thread.sleep(wait);

            for (String msg : messages) {
                client.send(msg);
                if (wait > 0) Thread.sleep(wait);
            }

            client.logout();

            client.readMessages();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            synchronized (this) {
                isFinished = true;
                this.notifyAll();
            }
        }
    }
    public ChatClient get() throws InterruptedException, ExecutionException {
        synchronized (this) {
            while (!isFinished) {
                this.wait();
            }
        }
        return client;
    }

    public ChatClient getClient() {
        return client;
    }
}
