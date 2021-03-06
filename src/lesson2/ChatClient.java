package lesson2;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.function.Consumer;

public class ChatClient {

    private PrintWriter writer;

    public static void main(String[] args) {
        Scanner keyboardScanner = new Scanner(System.in);
        ChatClient chat = new ChatClient();

        new Thread(() -> {
            while (keyboardScanner.hasNextLine()){
                String line = keyboardScanner.nextLine();
                chat.sendTextServer(line);
            }
        }).start();

//        chat.init(new Consumer<String>() {
//
//            @Override
//            public void accept(String line) {
//                System.out.println(line);
//            }
//        });

        chat.init(System.out::println);

    }

    public void init(Consumer<String> consumer) {
        try {
            Socket socket = new Socket("localhost",10000);
            Scanner serverScanner = new Scanner(socket.getInputStream());
            writer = new PrintWriter(socket.getOutputStream());

            new Thread(()->{
                while (serverScanner.hasNextLine()){
                    String line = serverScanner.nextLine();
                    consumer.accept(line);
                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendTextServer(String line) {
        System.out.println("sending to server "+line);
        writer.println(line);
        writer.flush();
    }

}
