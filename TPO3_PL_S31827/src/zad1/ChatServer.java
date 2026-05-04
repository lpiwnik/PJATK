/**
 *
 *  @author Piwnik Łukasz s31827
 *
 */

package zad1;


import java.io.*;
import java.net.*;

import java.time.*;
import java.time.format.DateTimeFormatter;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ChatServer extends Thread {
    private final static String serverHost="localhost";
    private final int port;
    private ServerSocket serverSocket;

    private final List<String> logs = Collections.synchronizedList(new ArrayList<>());
    private final Map<Socket, PrintWriter> clients = new ConcurrentHashMap<>();
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss.SSSSSSSSS");
    private volatile boolean running=true;

    public ChatServer(int port) {
    this.port=port;
    }

    public void startServer() {
        try {
            serverSocket=new ServerSocket();
            serverSocket.setReuseAddress(true);
            serverSocket.bind(new InetSocketAddress(serverHost,port));
            this.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Server started");
    }

    @Override
    public void run() {
        while (running) {
            try {
                Socket client = serverSocket.accept();
                Thread.ofVirtual().start(() -> handleClient(client));
            } catch (IOException e) {
                if (!running) break;
                e.printStackTrace();
            }
        }
    }

    private void handleClient(Socket socket) {
        try (socket;
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {


            String line;
            clients.putIfAbsent(socket,out);
            while (running && (line = in.readLine()) != null) {
                if (line.contains("logged out")) {
                    broadcast(line);
                    break;
                }
                broadcast(line);
            }
        } catch (IOException e) {

        } finally {
            clients.remove(socket);
        }
    }

    public void stopServer() {
        running=false;
        broadcast("ChatServer: chat closed");
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        try {
            if(serverSocket != null && !serverSocket.isClosed()){
                serverSocket.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Server stopped");
    }

    public String getServerLog() {
        synchronized (logs){
            return String.join("\n", logs);
        }
    }

    private void broadcast(String response){
        synchronized (logs){
            logs.add(dtf.format(LocalDateTime.now()) + " " + response);
        }
        clients.forEach(((socket, printWriter) ->{
            if(!socket.isClosed()){
                printWriter.println(response);
            }
        }));
    }

}
