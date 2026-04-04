/**
 *
 *  @author Piwnik Łukasz s31827
 *
 */

package zad1;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ChatServer extends Thread{
    private final String serverHost;
    private final int serverPort;
    private ServerSocketChannel serverChannel;
    private Selector selector;
    private final List<String> logs = new ArrayList<>();
    private final Map<SocketChannel, String> clients = new LinkedHashMap<>();
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    public ChatServer(String host, int port) {
        this.serverHost = host;
        this.serverPort = port;
    }

    public void startServer() {
        try {
            selector = Selector.open();
            serverChannel = ServerSocketChannel.open();
            serverChannel.bind(new InetSocketAddress(serverHost, serverPort));
            serverChannel.configureBlocking(false);
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("\nServer started");
        this.start();
    }

    @Override
    public void run() {
        serviceConnections();
    }

    private void serviceConnections() {
        while (!isInterrupted()) {
            try {
                selector.select();
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iter = keys.iterator();

                while (iter.hasNext()) {
                    SelectionKey key = iter.next();
                    iter.remove();

                    if (key.isAcceptable()) {
                        SocketChannel cc = serverChannel.accept();
                        cc.configureBlocking(false);
                        cc.register(selector, SelectionKey.OP_READ);
                    }
                    if (key.isReadable()) {
                        SocketChannel cc = (SocketChannel) key.channel();
                        serviceRequest(cc);
                    }
                }
            } catch (Exception e) {
                if (!isInterrupted()) break;
            }
        }
    }

    private void serviceRequest(SocketChannel sc) {
        if (!sc.isOpen()) return;
        try {
            String reqString = readSocket(sc);
            if (reqString == null || reqString.isEmpty()) return;

            String[] req = reqString.split("\t", 2);
            String cmd = req[0];
            String data = req.length > 1 ? req[1] : "";

            String logEvent = "";
            if (cmd.equals("login")) {
                clients.put(sc, data);
                logEvent = data + " logged in";

            } else if (cmd.equals("logout")) {
                logEvent = clients.get(sc) + " logged out";
                broadcast(logEvent);
                addLog(logEvent);
                clients.remove(sc);
                sc.close();
                return;
            } else {
                logEvent = clients.get(sc) + ": " + data;
            }

            addLog(logEvent);
            broadcast(logEvent);

        } catch (Exception e) {
            try { sc.close(); } catch (Exception ex) {}
        }
    }

    private String readSocket(SocketChannel sc) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        StringBuffer sb = new StringBuffer();
        int n = sc.read(buffer);
        if (n <= 0) return null;

        buffer.flip();
        CharBuffer cbuf = StandardCharsets.UTF_8.decode(buffer);
        while (cbuf.hasRemaining()) {
            char c = cbuf.get();
            if (c == '\n' || c == '\r') break;
            sb.append(c);
        }
        return sb.toString();
    }

    private void addLog(String msg) {
        logs.add(dtf.format(LocalTime.now()) + " " + msg);
    }

    private void broadcast(String response) throws IOException {
        ByteBuffer buf = StandardCharsets.UTF_8.encode(response + "\n");
        for (SocketChannel channel : clients.keySet()) {
            if (channel.isOpen()) {
                channel.write(buf.duplicate());
            }
        }
    }

    public void stopServer() {
        try {
            this.interrupt();
            selector.close();
            serverChannel.close();
            System.out.println("\nServer stopped");
        } catch (IOException e) { }
    }

    public String getServerLog() {
        return String.join("\n", logs);
    }

}
