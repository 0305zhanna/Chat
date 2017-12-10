package lesson3;

import AES.Client;
import AES.EncryptedMessage;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Scanner;
import java.util.function.Consumer;

public class Communicator {

    private PrintWriter writer;
    private Client client = new Client();


    public void init(Consumer<String> consumer) {
        try {
            Socket socket = new Socket("localhost",10000);
            Scanner serverScanner = new Scanner(socket.getInputStream());
            writer = new PrintWriter(socket.getOutputStream());

            new Thread(()->{
                while (serverScanner.hasNextLine()){
                    String line = serverScanner.nextLine();
                    if(line.startsWith("/encrypted")){
                        System.out.println("Received encrypted message: "+line);
                        String[] s = line.split(" ");

                        System.out.println("Received encrypted message: "+s[1]+s[2]);
                        System.out.println(s[1] + " "+s[2]);

                        EncryptedMessage mes = new EncryptedMessage(new BigInteger(s[1], 16).toByteArray(),new BigInteger(s[2], 16).toByteArray());
                        try {
//                            line = client.decrypt(mes);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    consumer.accept(line);
                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendEncryptedTextServer(String text) {
        System.out.println("sending to server encrypted message "+text);
        EncryptedMessage message = null;
        try {
//            message = new EncryptedMessage(client.encrypt(text));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            writer.println("/encrypted " +message.getParamsString() +" "+ message.getMessageString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        writer.flush();
    }
    public void sendTextServer(String line) {
        System.out.println("sending to server "+line);

        writer.println(line);
        writer.flush();
    }
    public void sendPublicKey2Server() {
        try {
            System.out.println("Sending public key to server "+this.toString());
            client.generateKeyPair();
            client.init();
            System.out.println("Sending public key to server "+this.toString()+"| "+client.getMyPublicKey());
            writer.println("/publickey " + new BigInteger(1, client.getMyPublicKey()).toString(16));
            writer.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendPublicKey2ServerWithParams(byte[] pubKey){
        if(client.getMyPublicKey() == null){//если это не мой ключ, то создаю новый
            try {
                client.setPublicKey(pubKey);
                client.generateKeyPairFromParam();
                client.init();
                client.doPhase1();
                client.initSharedSecret();
                System.out.println("sending to server my public key with params "+client.getMyPublicKey());
                writer.println("/dophase1 " + new BigInteger(1, client.getMyPublicKey()).toString(16));
                writer.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void doPhase(byte[] pubKey){
        if(pubKey != client.getMyPublicKey()) {
            try {
                client.setPublicKey(pubKey);
                client.doPhase1();
            } catch (Exception e) {
                e.printStackTrace();
            }
            client.initSharedSecret();
        }
    }


}
