package ChatEncrypt;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.Key;
import java.util.Scanner;
import java.util.function.Consumer;

public class Communicator {

    private PrintWriter writer;
    private static final byte[] keyValue =
            new byte[]{'T', 'h', 'e', 'B', 'e', 's', 't', 'S', 'e', 'c', 'r', 'e', 't', 'K', 'e', 'y'};

    public String encrypt(String data) throws Exception {
        Key key = new SecretKeySpec(keyValue, "AES");
        Cipher c = Cipher.getInstance("AES");
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(data.getBytes());
        return new BASE64Encoder().encode(encVal);
    }
    public String decrypt(String encryptedData) throws Exception {
        Key key = new SecretKeySpec(keyValue, "AES");
        Cipher c = Cipher.getInstance("AES");
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedData);
        byte[] decValue = c.doFinal(decordedValue);
        return new String(decValue);
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

    public void sendEncryptedTextServer(String text) throws Exception {
        Cipher aesCipher = Cipher.getInstance("AES");
        // Initialize the cipher for encryption
        System.out.println("Encrypting...");

        System.out.println("Sending to server... "+ encrypt(text));
        writer.println("/encrypted " + encrypt(text));
        writer.flush();
    }

    public void sendTextServer(String line) {
        System.out.println("sending to server "+line);
        writer.println(line);
        writer.flush();
    }

}
