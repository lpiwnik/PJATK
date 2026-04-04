/**
 *
 *  @author Piwnik Łukasz s31827
 *
 */

package zad1;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class ChatClient {
    private final String host;
    private final int port;
    private final String id;
    private SocketChannel channel;
    private final StringBuffer chatView;

    public ChatClient(String host, int port, String id) {
        this.host = host;
        this.port = port;
        this.id = id;
        this.chatView = new StringBuffer("=== " + id + " chat view\n");
    }

    public void login() {
        try {
            channel = SocketChannel.open(new InetSocketAddress(host, port));
            channel.configureBlocking(false);
            sendRequest("login\t" + id);
        } catch (IOException e) {
            System.out.println("*** " + e);
        }
    }

    public void logout() {
        sendRequest("logout\t" + id);
    }

    public void send(String msg) {
        sendRequest("message\t" + msg);
    }

    private void sendRequest(String req) {
        try {
            channel.write(StandardCharsets.UTF_8.encode(req + "\n"));

            long startTime = System.currentTimeMillis();
            int n = 0;

            while ((n = channel.read(ByteBuffer.allocate(0))) == 0
                    && System.currentTimeMillis() - startTime < 10) {
                Thread.yield();
            }
            readMessages();
        } catch (Exception e) {
            System.out.println("*** " + e);
        }
    }

    public void readMessages() {
        if (channel == null || !channel.isOpen()) return;
        try {
            ByteBuffer buffer = ByteBuffer.allocate(2048);
            int n;
            while ((n = channel.read(buffer)) > 0) {
                buffer.flip();
                chatView.append(StandardCharsets.UTF_8.decode(buffer));
                buffer.clear();
            }
        } catch (IOException e) {
            System.out.println("*** " + e);
        }
    }

    public String getChatView() {
        readMessages();
        return chatView.toString();
    }
}
