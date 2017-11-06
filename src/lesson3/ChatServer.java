package lesson3;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class ChatServer {

    private static final int DEFAULT_PORT = 10000;
    private static List<ChatSession> sessions;
    private static ExecutorService broadcastService;
    private static int userCount = 0;
    public static void main(String[] args) {

        System.out.println("Start");
        sessions = new ArrayList<>();

        broadcastService = Executors.newCachedThreadPool();
        try {
            ServerSocket serverSocket = new ServerSocket(DEFAULT_PORT);

            while(true){
                Socket socket = serverSocket.accept();
                System.out.println("Got connection "+ socket);
                new Thread(()->{
                    long delay = sessions.isEmpty()? 2000: 100;
                    String name = "User" + userCount++;
                    ChatSession chatSession = new ChatSession(name,delay);
                    sessions.add(chatSession);
                    System.out.println("Sessions size: "+sessions.size());
                    chatSession.processConnection(socket,ChatServer::broadcast,
                            ChatServer::removeSessino);
                }).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void broadcast(String s) {
        for (ChatSession session: sessions) {
            broadcastService.execute(()->{
                session.send2Client(s);
            });
        }
    }

    private static void removeSessino(ChatSession session){
        sessions.remove(session);
        System.out.println("removed "+session);
        System.out.println("Sessions size: "+sessions.size());
    }
}