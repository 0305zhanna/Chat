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

                    ChatSession chatSession = new ChatSession(socket,name,delay);

                    sendBroadcastUserName(chatSession);

                    sessions.add(chatSession);
                    sendNameList2Client(chatSession);

                    System.out.println("Sessions size: "+sessions.size());

                    chatSession.processConnection(ChatServer::broadcast,
                            ChatServer::removeSessino);
                }).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void sendBroadcastUserName(ChatSession chatSession) {

        String command = "/add " + chatSession.getName();
        broadcast(command);

    }

    private static void sendNameList2Client(ChatSession chatSession) {
        String namelist = "/list";
        for (ChatSession s: sessions) {
            namelist+= " " + s.getName();
        }
        chatSession.send2Client(namelist);
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
        broadcast("/remove " + session.getName());
        System.out.println("removed "+session);
        System.out.println("Sessions size: "+sessions.size());
    }
}