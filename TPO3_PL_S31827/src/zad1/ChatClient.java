/**
 *
 *  @author Piwnik Łukasz s31827
 *
 */

package zad1;


import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ChatClient {
    private final String host;
    private final int port;
    private final String clientId;
    private Socket socket;
    private PrintWriter out;
    private final StringBuilder chatView = new StringBuilder();

    public ChatClient(String host, int port, String cid) {
        this.host = host;
        this.port = port;
        this.clientId = cid;
    }

    public void login() throws IOException{

        socket = new Socket(host, port);
        out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
        socket.setReuseAddress(true);

        Thread.ofVirtual().start(this::readMessages);
        send(clientId + " log in");
    }

    public void send(String msg) {
        if (out != null && !socket.isClosed()) {
            out.println(msg);
        }
    }

    public void logout(){
        send(clientId + " log out");
    }

    private void readMessages() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            boolean startRecording = false;

            while ((line = in.readLine()) != null) {
                if (!startRecording && line.contains(clientId + " log in")) {
                    startRecording = true;
                }

                if (startRecording) {
                    synchronized (chatView) {
                        chatView.append(line).append("\n");
                    }
                }

                if (line.contains("chat closed") || line.contains(clientId + " log out")) {
                    break;
                }
            }
        } catch (IOException e) {
            // Zamknięcie gniazda
        }
    }

    public String getId() {
        return clientId;
    }

    public String getChatView() {
            return chatView.toString();
    }
}
