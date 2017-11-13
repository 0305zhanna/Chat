package lesson3;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.function.Consumer;

public class ChatSession {

    private final String name;
    private PrintWriter writer;
    private long delay;
    private Socket socket;
    private Scanner scanner;

    ChatSession(Socket socket, String name, long delay){
        this.socket = socket;
        this.name = name;
        this.delay = delay;
        try {
            scanner = new Scanner(socket.getInputStream());
            writer = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    void processConnection(Consumer<String> broadcaster,
                           Consumer<ChatSession> sessionRemover){
        try {

            send2Client("/name "+ name);

        while (scanner.hasNext()){
            String line = scanner.nextLine();
            System.out.println(line);
            broadcaster.accept(name + " > " + line);
        }

        System.out.println("connection closed");
        sessionRemover.accept(this);

        socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send2Client(String line) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        writer.println(line);
        writer.flush();
    }

    public String getName() {
    return name;
    }
}
