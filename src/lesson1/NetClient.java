package lesson1;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class NetClient {

    public static void main(String[] args) {

        try {
            Socket socket = new Socket("localhost",10000);
            Scanner scanner = new Scanner(socket.getInputStream());

            while (scanner.hasNextLine()){
                String line = scanner.nextLine();
                System.out.println(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
